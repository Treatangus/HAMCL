package com.hamcl.utils;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.launcher.hamcl.download.downloader.HttpUtil;
import com.launcher.hamcl.utils.SDCardUtils;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MinecraftJsonParser
{
	public static final int TYPE_LIBRARIES = 0;
	public static final int TYPE_ASSETINDEX = 1;
	public static final int TYPE_ASSETS = 2;
	
	
	private static int type;
	Gson g;
	AssetsIndexBean indexBean;
	AllLib libraries;
	Map<String,AssetDownBean> assets;
	private MinecraftJsonParser(String json){
		g = new Gson();
		switch(type){
			case TYPE_ASSETINDEX:
				assetIndex(json);
				break;
				case TYPE_LIBRARIES:
					libraries(json);
					break;
					case TYPE_ASSETS:
						assets(json);
						break;
		}
	}
	public static MinecraftJsonParser parse(String json,int TYPE){
		type=TYPE;
		return new MinecraftJsonParser(json);
	}
	public static MinecraftJsonParser parseFromFile(String path,int TYPE) throws IOException{
		type=TYPE;
		String js = new String(readFromInput(new FileInputStream(path)));
		return new MinecraftJsonParser(js);
	}
	public static MinecraftJsonParser parseFromFileClass(File path,int TYPE) throws IOException{
		type=TYPE;
		String js = new String(readFromInput(new FileInputStream(path)));
		return new MinecraftJsonParser(js);
	}
	public Base base;
	public String path;
	private void assetIndex(String json){
		indexBean = g.fromJson(json,AssetsIndexBean.class);
		 base = g.fromJson (json,Base.class);
		 base.downloadsSmall.client.name = indexBean.assetIndex.id;
		HttpUtil.sendOkHttpRequest (indexBean.assetIndex.url, new Callback () {
			@Override
			public void onFailure (@NonNull Call call, @NonNull IOException e) {
				e.printStackTrace ();
			}
			@Override
			public void onResponse (@NonNull Call call, @NonNull Response response) throws IOException {
				//path = com.explore.launcher.utils.SDCardUtils.getBaseMinecraftPath ()+"/assets/indexes/"+indexBean.assetIndex.id+".json";
				FileUtils
						.writeByteArrayToFile (new File (SDCardUtils.getBaseMinecraftPath ()+"/assets/indexes/"+indexBean.assetIndex.id+".json"),response.body ().string ().getBytes ());
			}
		});
	} 
	private AssetsIndexBean getAssetsIndex() throws Throwable{
		if(type == TYPE_ASSETINDEX)
		return indexBean;
		else{
			throw new Throwable("Minecraft Json Parser ：error type.");
		}
	}
	private void libraries(String json){
		libraries = g.fromJson(json,AllLib.class);
	}
	private AllLib getAllLibraries() throws Throwable{
		if(type == TYPE_LIBRARIES){
			return libraries;
		}else{
			throw new Throwable("Minecraft Json Parser ：error type.");
		}
	}
	
	private void assets(String json){
		JsonObject obj = g.fromJson(json,JsonObject.class);
		String child = obj.get("objects").toString();
		assets = g.fromJson(child,new TypeToken<Map<String,AssetDownBean>>(){}.getType());
	}
	private Map<String,AssetDownBean> getAssets() throws Throwable{
		if(type == TYPE_ASSETS)
			return assets;
		else{
			throw new Throwable("Minecraft Json Parser ：error type.");
		}
	}
	public AssetsIndexBean getAssetIndex(){
		try {
			return getAssetsIndex();
		} catch (Throwable e) {e.printStackTrace();}
		return null;
	}
	public AllLib getLibrary(){
		try {
			return getAllLibraries();
		} catch (Throwable e) {e.printStackTrace();}
		return null;
	}
	public Map<String,AssetDownBean> getAsset(){
		try {
			return getAssets();
		} catch (Throwable e) {e.printStackTrace();}
		return null;
	}
	private static byte[] readFromInput(InputStream inputStream) throws IOException {
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
