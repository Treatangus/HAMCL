package com.launcher.hamcl.login;

import me.ratsiel.auth.model.mojang.LoginInterface;
import me.ratsiel.auth.model.mojang.MinecraftAuthenticator;
import me.ratsiel.auth.model.mojang.MinecraftToken;
import me.ratsiel.auth.model.mojang.profile.MinecraftProfile;

public class MinecraftLogin {
    private static MinecraftAuthenticator minecraftAuthenticator;
    public static void loginWithMicrosoft(String email, String password,LoginInterface loginInterface){
        if (minecraftAuthenticator == null){
            minecraftAuthenticator = new MinecraftAuthenticator (loginInterface);
        }
        final MinecraftToken[] minecraftToken = new MinecraftToken[1];
        new Thread (new Runnable () {
            @Override
            public void run () {
                minecraftToken[0] = minecraftAuthenticator.loginWithXbox (email, password);
                MinecraftProfile profile = minecraftAuthenticator.checkOwnership (minecraftToken[0]);
                System.out.println (profile.getUuid ().toString ());
            }
        }).start ();
    }
    public static void loginWithMojang(String username, String password, LoginInterface loginInterface){
        if (minecraftAuthenticator == null){
            minecraftAuthenticator = new MinecraftAuthenticator (loginInterface);
        }
        final MinecraftToken[] minecraftToken = new MinecraftToken[1];
        new Thread (new Runnable () {
            @Override
            public void run () {
                minecraftToken[0] = minecraftAuthenticator.login (username, password);
                MinecraftProfile profile = minecraftAuthenticator.checkOwnership (minecraftToken[0]);
                System.out.println (profile.getUuid ().toString ());
            }
        }).start ();
       // return minecraftAuthenticator.checkOwnership (minecraftToken[0]);
    }
}
