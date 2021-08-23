package me.ratsiel.auth.model.mojang;

import android.os.Build;

import androidx.annotation.RequiresApi;

import me.ratsiel.auth.abstracts.Authenticator;
import me.ratsiel.auth.abstracts.exception.AuthenticationException;
import me.ratsiel.auth.model.microsoft.MicrosoftAuthenticator;
import me.ratsiel.auth.model.microsoft.XboxToken;
import me.ratsiel.auth.model.mojang.profile.MinecraftCape;
import me.ratsiel.auth.model.mojang.profile.MinecraftProfile;
import me.ratsiel.auth.model.mojang.profile.MinecraftSkin;
import me.ratsiel.json.model.JsonArray;
import me.ratsiel.json.model.JsonBoolean;
import me.ratsiel.json.model.JsonNumber;
import me.ratsiel.json.model.JsonObject;
import me.ratsiel.json.model.JsonString;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The class Minecraft authenticator is used to log in with normal minecraft details or with microsoft
 */
public class MinecraftAuthenticator extends Authenticator<MinecraftToken> {

    /**
     * The Microsoft authenticator is used for {@link #loginWithXbox(String, String)}
     */
    protected final MicrosoftAuthenticator microsoftAuthenticator = new MicrosoftAuthenticator();
    private LoginInterface loginInterface;
    public MinecraftAuthenticator(LoginInterface loginInterface){
        this.loginInterface = loginInterface;
    }

    @Override
    public MinecraftToken login(String email, String password) {
        JsonObject jsonObject= new JsonObject ();
        jsonObject.add ("username",new JsonString (email));
        jsonObject.add ("password",new JsonString (password));

        JsonObject objectJ = new JsonObject ();
        objectJ.add ("name",new JsonString ("Minecraft"));
        objectJ.add ("version",new JsonNumber ("1"));
        jsonObject.add ("agent",objectJ);
        jsonObject.add ("requestUser",new JsonBoolean (true));
        jsonObject.add ("clientToken",new JsonString ("hamcl"));


        byte[] bytes = jsonObject.toString ().getBytes ();
        OutputStream outputStream;
        try {
            URL url = new URL ("https://authserver.mojang.com/authenticate");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection ();
            httpURLConnection.setDoOutput (true);
            httpURLConnection.setDoInput (true);
            httpURLConnection.setRequestMethod ("POST");
            httpURLConnection.setRequestProperty ("Connection","Keep-Alive");
            httpURLConnection.setRequestProperty ("Charset","UTF-8");
            httpURLConnection.setRequestProperty ("Content-Length",String.valueOf (bytes.length));
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect ();
            outputStream = new DataOutputStream (httpURLConnection.getOutputStream ());
            outputStream.write (bytes);
            outputStream.flush ();
            outputStream.close ();

            if (httpURLConnection.getResponseCode () == HttpURLConnection.HTTP_OK){
                String backData = new String (read (httpURLConnection.getInputStream ()));
                JsonObject objectBack = parseResponseData (httpURLConnection);//new JsonObject (backData);
                JsonObject proFile = objectBack.get ("selectedProfile",JsonObject.class);
                MinecraftToken token = new MinecraftToken ((objectBack.get ("accessToken",JsonString.class)).getValue (),proFile.get ("name",JsonString.class).getValue ());
                loginInterface.onConnectSuccess ();
                return token;
            }else {
                loginInterface.onConnectFail ();
                return new MinecraftToken ("","");
            }

        }catch (IOException exception){
            exception.printStackTrace ();
            loginInterface.onConnectFail ();

        }
        loginInterface.onConnectFail ();
        return new MinecraftToken ("","");
       // throw new RuntimeException("Not implemented yet.");
    }

    /**
     * Login with microsoft email and microsoft password
     *
     * @param email    the email
     * @param password the password
     * @return the minecraft token
     */
    public MinecraftToken loginWithXbox(String email, String password) {
        XboxToken xboxToken = microsoftAuthenticator.login(email, password);

        try {
            URL url = new URL("https://api.minecraftservices.com/authentication/login_with_xbox");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            JsonObject request = new JsonObject();
            request.add("identityToken", new JsonString("XBL3.0 x=" + xboxToken.getUhs() + ";" + xboxToken.getToken()));

            String requestBody = request.toString();

            httpURLConnection.setFixedLengthStreamingMode(requestBody.length());
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Host", "api.minecraftservices.com");
            httpURLConnection.connect();

            try (OutputStream outputStream = httpURLConnection.getOutputStream()) {
                outputStream.write(requestBody.getBytes(StandardCharsets.US_ASCII));
            }

            JsonObject jsonObject = microsoftAuthenticator.parseResponseData(httpURLConnection);
            return new MinecraftToken(jsonObject.get("access_token", JsonString.class).getValue(), jsonObject.get("username", JsonString.class).getValue());
        } catch (IOException exception) {
            throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", exception.getMessage()));
        }
    }

    /**
     * Check ownership from {@link MinecraftToken} and generate {@link MinecraftProfile}
     *
     * @param minecraftToken the minecraft token
     * @return the minecraft profile
     */
    public MinecraftProfile checkOwnership(MinecraftToken minecraftToken) {
        try {
            URL url = new URL("https://api.minecraftservices.com/minecraft/profile");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setRequestMethod("GET");

            httpURLConnection.setRequestProperty("Authorization", "Bearer " + minecraftToken.getAccessToken());
            httpURLConnection.setRequestProperty("Host", "api.minecraftservices.com");
            httpURLConnection.connect();

            JsonObject jsonObject = parseResponseData(httpURLConnection);

            UUID uuid = generateUUID(jsonObject.get("id", JsonString.class).getValue());
            String name = jsonObject.get("name", JsonString.class).getValue();
            List<MinecraftSkin> minecraftSkins = json.fromJson(jsonObject.get("skins", JsonArray.class), List.class, MinecraftSkin.class);
            List<MinecraftCape> minecraftCapes = json.fromJson(jsonObject.get("skins", JsonArray.class), List.class, MinecraftCape.class);

            return new MinecraftProfile(uuid, name, minecraftSkins, minecraftCapes);
        } catch (IOException exception) {
            throw new AuthenticationException(String.format("Authentication error. Request could not be made! Cause: '%s'", exception.getMessage()));
        }
    }

    /**
     * Parse response data to {@link JsonObject}
     *
     * @param httpURLConnection the http url connection
     * @return the json object
     * @throws IOException the io exception
     */
   // @RequiresApi(api = Build.VERSION_CODES.N)
    public JsonObject parseResponseData(HttpURLConnection httpURLConnection) throws IOException {
        //BufferedReader bufferedReader;
        String lines;
        if (httpURLConnection.getResponseCode() != 200) {
            lines = new String (read (httpURLConnection.getErrorStream ()));
          //  bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
        } else {
            lines = new String (read (httpURLConnection.getInputStream ()));
          //  bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        }
      //  String lines = bufferedReader.lines().collect(Collectors.joining());

        JsonObject jsonObject = json.fromJsonString(lines, JsonObject.class);
        if (jsonObject.has("error")) {
            throw new AuthenticationException(String.format("Could not find profile!. Error: '%s'", jsonObject.get("errorMessage", JsonString.class).getValue()));
        }
        return jsonObject;
    }


    /**
     * Generate uuid from trimmedUUID
     *
     * @param trimmedUUID the trimmed uuid
     * @return the uuid
     * @throws IllegalArgumentException the illegal argument exception
     */
    public UUID generateUUID(String trimmedUUID) throws IllegalArgumentException {
        if (trimmedUUID == null) throw new IllegalArgumentException();
        StringBuilder builder = new StringBuilder(trimmedUUID.trim());
        try {
            builder.insert(20, "-");
            builder.insert(16, "-");
            builder.insert(12, "-");
            builder.insert(8, "-");
            return UUID.fromString(builder.toString());
        } catch (StringIndexOutOfBoundsException e) {
            return null;
        }
    }
    public static byte[] read(InputStream inputStream) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int num = inputStream.read(buffer);
            while (num != -1) {
                baos.write(buffer, 0, num);
                num = inputStream.read(buffer);
            }
            baos.flush();
            return baos.toByteArray();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

}
