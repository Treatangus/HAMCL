package com.launcher.hamcl.setting.model;

import java.io.Serializable;

public class ConfigModel implements Serializable {
    
    private String auth_uuid = "00000000-0000-0000-0000-000000000000";
	private String extraMinecraftFlags = "";
	private String auth_player_name = "Steve";
	private String auth_session = "0";
	private String extraJavaFlags = "-client -Xmx1024M";
	private String home = "/sdcard/games/com.explore.launcher";
	private String runtimePath = "/data/user/0/com.explore.launcher/app_runtime";
	private String auth_access_token = "0";
	private String currentVersion = "/sdcard/games/com.explore.launcher/gamedir/versions/";
	private String game_assets = "/sdcard/games/com.explore.launcher/gamedir/assets/virtual/legacy";
	private String user_type = "mojang";
	private String game_directory = "/sdcard/games/com.explore.launcher/gamedir";
	private String user_properties = "{}";
	private String assets_root ="/sdcard/games/com.explore.launcher/gamedir/assets";
	
	public ConfigModel() {
		super();
	}
	
	public ConfigModel(String auth_uuid, String extraMinecraftFlags, String auth_player_name, String auth_session,
	String extraJavaFlags, String home, String runtimePath, String auth_access_token, String currentVersion, 
	String game_assets, String user_type, String game_directory, String user_properties, String assets_root) {
		
        this.auth_uuid = auth_uuid;
        this.extraMinecraftFlags = extraMinecraftFlags;
        this.auth_player_name = auth_player_name;
        this.auth_session = auth_session;
        this.extraJavaFlags = extraJavaFlags;
        this.home = home;
		this.runtimePath = runtimePath;
		this.auth_access_token = auth_access_token;
		this.currentVersion = currentVersion;
		this.game_assets = game_assets;
		this.user_type = user_type;
		this.game_directory = game_directory;
		this.user_properties = user_properties;
		this.assets_root = assets_root;
    }

	//Getter and Setter
	public String getauth_uuid() { return auth_uuid; }
	public void setauth_uuid(String auth_uuid) { this.auth_uuid = auth_uuid; } 
	public String getextraMinecraftFlags() { return extraMinecraftFlags; }
	public void setextraMinecraftFlags(String extraMinecraftFlags) { this.extraMinecraftFlags = extraMinecraftFlags; } 
	public String getauth_player_name() { return auth_player_name; }
	public void setauth_player_name(String auth_player_name) { this.auth_player_name = auth_player_name; } 
	public String getauth_session() { return auth_session; }
	public void setauth_session(String auth_session) { this.auth_session = auth_session; } 
	public String getextraJavaFlags() { return extraJavaFlags; }
	public void setextraJavaFlags(String extraJavaFlags) { this.extraJavaFlags = extraJavaFlags; } 
	public String gethome() { return home; }
	public void sethome(String home) { this.home = home; } 
	public String getruntimePath() { return runtimePath; }
	public void setruntimePath(String runtimePath) { this.runtimePath = runtimePath; } 
	public String getauth_access_token() { return auth_access_token; }
	public void setauth_access_token(String auth_access_token) { this.auth_access_token = auth_access_token; } 
	public String getcurrentVersion() { return currentVersion; }
	public void setcurrentVersion(String currentVersion) { this.currentVersion = currentVersion; } 
	public String getgame_assets() { return game_assets; }
	public void setgame_assets(String game_assets) { this.game_assets = game_assets; } 
	public String getuser_type() { return user_type; }
	public void getuser_type(String user_type) { this.user_type = user_type; } 
	public String getgame_directory() { return game_directory; }
	public void setgame_directory(String game_directory) { this.game_directory = game_directory; } 
	public String getuser_properties() { return user_properties; }
	public void setuser_properties(String user_properties) { this.user_properties = user_properties; } 
	public String getassets_root() { return assets_root; }
	public void setassets_root(String assets_root) { this.assets_root = assets_root; } 
	
}
