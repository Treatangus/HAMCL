package com.launcher.hamcl.download;

import android.os.Environment;
import android.os.Message;

import androidx.annotation.NonNull;

import com.launcher.hamcl.download.util.DLLCALLBack;
import com.tamic.rx.fastdown.callback.IDLCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DLM {
    public void d (String sp, String u, DLLCALLBack callback){
        OkHttpClient client = new OkHttpClient ();
        Request request = new Request .Builder ().url (u).build ();
        client.newCall (request).enqueue (new Callback () {
            @Override
            public void onFailure (@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace ();
            }

            @Override
            public void onResponse (@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        int i = sp.lastIndexOf ("/")+1;

                        callback.onStart ((int) body.contentLength (),sp.substring (i));
                        writeFile(body,sp,callback);
                    }
                }
            }
        });
    }


    private void writeFile(ResponseBody body, String sp, DLLCALLBack callback) {
        InputStream is = null;  //网络输入流
        FileOutputStream fos = null;  //文件输出流

        is = body.byteStream ();

        String filePath = sp;//Environment.getExternalStorageDirectory ().getAbsolutePath () + File.separator + fileName;
        File file = new File (filePath);
        if (!file.getParentFile ().exists ()){
            file.getParentFile ().mkdirs ();
        }
        try {
            fos = new FileOutputStream (file);
            byte[] buffer = new byte[1024];
            int len = 0;
            long totalSize = body.contentLength ();  //文件总大小
            long sum = 0;
            while ((len = is.read (buffer)) != -1) {
                fos.write (buffer, 0, len);
                sum += len;
                int progress = (int) (sum * 1.0f / totalSize * 100);
                callback.onProgress ((int) body.contentLength (),(int) sum);
            }

        } catch (FileNotFoundException e) {
            callback.onFinish ();
            e.printStackTrace ();
        } catch (IOException e) {
            callback.onFinish ();
            e.printStackTrace ();
        } finally {
            callback.onFinish ();
            if (is != null) {
                try {
                    is.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
            if (fos != null) {
                try {
                    fos.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }

    }

}
