package com.hamcl.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssetIndex 
{
	@Expose
	@SerializedName(value="id")
	public String id;
	@Expose
	@SerializedName(value="size")
	public int size;
	@Expose
	@SerializedName(value="totalSize")
	public int totalSize;
	@Expose
	@SerializedName(value="sha1")
	public String sha1;
	@Expose
	@SerializedName(value="url")
	public String url;
}
