package com.launcher.hamcl.setting;

import android.content.Context;
import android.util.Log;

import com.launcher.hamcl.MainActivity;
import com.launcher.hamcl.setting.model.ConfigModel;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class SettingManager {

    private Context mContext;
    private File settingFile;
	private File configFile;
    
    private final static String TAG = "SettingManager";

    public SettingManager(Context context){
        this.mContext = context;
        settingFile = new File("/sdcard/games/com.explore.launcher/setting.txt");
		configFile = new File("/storage/emulated/0/games/com.explore.launcher/config.txt");
    }

    /**【读入config.txt】**/
    public ConfigModel getConfigFromFile(ConfigModel config){
        ConfigModel configModel;

        if (!configFile.exists()) {
            configModel = config;
        } else {
            try {
                InputStream inputStream = new FileInputStream(configFile);
                Reader reader = new InputStreamReader(inputStream);
                Gson gson = new Gson();
                //使用Gson将ConfigModel实例化
                configModel = gson.fromJson(reader, ConfigModel.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG,"get failed");
                configModel = null;
            }
        }

        if(configModel == null){
            configModel = config;
        }

        return configModel;
    }

    /**【保存config.txt文件】**/
    public void saveConfigToFile(ConfigModel config){
        Gson gson = new Gson();
        String jsonString = gson.toJson(config);
        try {
            FileWriter jsonWriter = new FileWriter(configFile);
            BufferedWriter out = new BufferedWriter(jsonWriter);
            out.write(jsonString);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"save failed.");
        }
    }

    /**【读入config.txt】**/
    /*public ConfigModel getConfigFromFile(){
        ConfigModel configModel;

        if (!configFile.exists()) {
            configModel = new ConfigModel();
        } else {
            try {
                InputStream inputStream = new FileInputStream(configFile);
                Reader reader = new InputStreamReader(inputStream);
                Gson gson = new Gson();
                //使用Gson将ConfigModel实例化
                configModel = gson.fromJson(reader, ConfigModel.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG,"get failed");
                configModel = null;
            }
        }

        if(configModel == null){
            configModel = new ConfigModel();
        }

        return configModel;
    }*/

    /**【保存config.txt文件】**/
    /*public void saveConfigToFile(ConfigModel configModel){
        Gson gson = new Gson();
        String jsonString = gson.toJson(MainActivity.configModel = configModel);

        try {
            FileWriter jsonWriter = new FileWriter(configFile);
            BufferedWriter out = new BufferedWriter(jsonWriter);
            out.write(jsonString);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"save failed.");
        }
    }*/

	/**【读入mcinabox.json】**/
   /* public ConfigModel getSettingFromFile(){
        JsonModel settingModel;

        if (!settingFile.exists()) {
            settingModel = new JsonModel();
        } else {
            try {
                InputStream inputStream = new FileInputStream(settingFile);
                Reader reader = new InputStreamReader(inputStream);
                Gson gson = new Gson();
                //使用Gson将ListVersionManifestJson实例化
                settingModel = gson.fromJson(reader, JsonModel.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                settingModel = null;
            }
        }

        if(settingModel == null){
            settingModel = new JsonModel();
        }

        return settingModel;
    }*/
	
	/**【保存mcinabox.json文件】**/
    /*public void saveSettingToFile(){
        Gson gson = new Gson();
        String jsonString = gson.toJson(MainActivity.configurations);
        try {
            FileWriter jsonWriter = new FileWriter(settingFile);
            BufferedWriter out = new BufferedWriter(jsonWriter);
            out.write(jsonString);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"save failed.");
        }
    }*/

}

