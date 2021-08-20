package com.launcher.hamcl.setting.model;

import android.os.Build;
import java.util.UUID;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import android.widget.Toast;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import android.app.Activity;

public class SettingModel {
	
	private  String downloadType; //下载源："office"官方 "bmclapi"国内BMCLAPI "mcbbs"国内MCBBS
    private MinecraftParameter minecraftParameter; //Config配置
    
    public SettingModel(){
        //默认模板初始化
        super();
		
        downloadType = "official";
	
        minecraftParameter = new MinecraftParameter()
        .setauth_uuid("00000000-0000-0000-0000-000000000000")
        .setextraMinecraftFlags("")
        .setauth_player_name("Steve")
        .setauth_session("0")
        .setextraJavaFlags("-server -Xms850M -Xmx850M")
        .sethome("/sdcard/games/com.explore.launcher")
        .setruntimePath("/data/user/0/cosine.boat/app_runtime/")
        .setauth_access_token("0")
        .setcurrentVersion("/sdcard/games/com.explore.launcher/gamedir/versions/")
        .setgame_assets("/sdcard/games/com.explore.launcher/gamedir/assets/virtual/legacy")
        .setuser_type("mojang")
        .setgame_directory("/sdcard/games/com.explore.launcher/gamedir")
		.setuser_properties("{}")
		.setassets_root("/sdcard/games/com.explore.launcher/gamedir/assets");
	
    }
    
    //Config配置
    public class MinecraftParameter {

		private String auth_uuid;
		private String extraMinecraftFlags;
		private String auth_player_name;
		private String auth_session;
		private String extraJavaFlags;
		private String home;
		private String runtimePath;
		private String auth_access_token;
		private String currentVersion;
		private String game_assets;
		private String user_type;
		private String game_directory;
		private String user_properties;
		private String assets_root;
		
        public MinecraftParameter(){
            super();
        }

        //Getter and Setter
		public String getauth_uuid() { return auth_uuid; }
		public MinecraftParameter setauth_uuid(String auth_uuid) { this.auth_uuid = auth_uuid; return this; } 
		public String getextraMinecraftFlags() { return extraMinecraftFlags; }
		public MinecraftParameter setextraMinecraftFlags(String extraMinecraftFlags) { this.extraMinecraftFlags = extraMinecraftFlags; return this; } 
		public String getauth_player_name() { return auth_player_name; }
		public MinecraftParameter setauth_player_name(String auth_player_name) { this.auth_player_name = auth_player_name; return this; } 
		public String getauth_session() { return auth_session; }
		public MinecraftParameter setauth_session(String auth_session) { this.auth_session = auth_session; return this; } 
		public String getextraJavaFlags() { return extraJavaFlags; }
		public MinecraftParameter setextraJavaFlags(String extraJavaFlags) { this.extraJavaFlags = extraJavaFlags; return this;} 
		public String gethome() { return home; }
		public MinecraftParameter sethome(String home) { this.home = home; return this; } 
		public String getruntimePath() { return runtimePath; }
		public MinecraftParameter setruntimePath(String runtimePath) { this.runtimePath = runtimePath; return this; } 
		public String getauth_access_token() { return auth_access_token; }
		public MinecraftParameter setauth_access_token(String auth_access_token) { this.auth_access_token = auth_access_token; return this; } 
		public String getcurrentVersion() { return currentVersion; }
		public MinecraftParameter setcurrentVersion(String currentVersion) { this.currentVersion = currentVersion; return this; } 
		public String getgame_assets() { return game_assets; }
		public MinecraftParameter setgame_assets(String game_assets) { this.game_assets = game_assets; return this; } 
		public String getuser_type() { return user_type; }
		public MinecraftParameter setuser_type(String user_type) { this.user_type = user_type; return this; } 
		public String getgame_directory() { return game_directory; }
		public MinecraftParameter setgame_directory(String game_directory) { this.game_directory = game_directory; return this; } 
		public String getuser_properties() { return user_properties; }
		public MinecraftParameter setuser_properties(String user_properties) { this.user_properties = user_properties; return this; } 
		public String getassets_root() { return assets_root; }
		public MinecraftParameter setassets_root(String assets_root) { this.assets_root = assets_root; return this; } 
		
    }

    //Getter and Setter
    public String getDownloadType() { return downloadType; }
    public void setDownloadType(String downloadType) { this.downloadType = downloadType; }
	
    public MinecraftParameter getMinecraftParameter() {
        return minecraftParameter;
    }
    public SettingModel setMinecraftParameter(MinecraftParameter minecraftParameter) {
        this.minecraftParameter = minecraftParameter;
        return this;
    }
}
