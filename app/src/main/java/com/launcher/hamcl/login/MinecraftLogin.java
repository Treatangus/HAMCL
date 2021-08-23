package com.launcher.hamcl.login;

import me.ratsiel.auth.model.mojang.LoginInterface;
import me.ratsiel.auth.model.mojang.MinecraftAuthenticator;
import me.ratsiel.auth.model.mojang.MinecraftToken;
import me.ratsiel.auth.model.mojang.profile.MinecraftProfile;

public class MinecraftLogin {
    private static MinecraftAuthenticator minecraftAuthenticator;
    public static MinecraftProfile loginWithMicrosoft(String email, String password,LoginInterface loginInterface){
        if (minecraftAuthenticator == null){
            minecraftAuthenticator = new MinecraftAuthenticator (loginInterface);
        }
        MinecraftToken minecraftToken = minecraftAuthenticator.loginWithXbox (email, password);
        return minecraftAuthenticator.checkOwnership (minecraftToken);
    }
    public static MinecraftProfile loginWithMojang(String username, String password, LoginInterface loginInterface){
        if (minecraftAuthenticator == null){
            minecraftAuthenticator = new MinecraftAuthenticator (loginInterface);
        }
        MinecraftToken minecraftToken = minecraftAuthenticator.login (username, password);
        return minecraftAuthenticator.checkOwnership (minecraftToken);
    }
}
