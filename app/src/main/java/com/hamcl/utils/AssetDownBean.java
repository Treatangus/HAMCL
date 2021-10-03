package com.hamcl.utils;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class AssetDownBean
{
	@Expose
	@SerializedName(value="hash")
	public String hash;
	
	@Expose
	@SerializedName(value="size")
	public int size;
	
	public String getUrl(String source){
		return source+"/assets/"+hash.substring(0,2)+"/"+hash;
	}
	public String getpath(){
		return "/assets/objects/"+hash.substring (0,2)+"/"+hash;
	}
}
