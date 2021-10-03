package com.hamcl.utils;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class Artifact 
{
	@Expose
	@SerializedName(value="path")
	public String path;
	@Expose
	@SerializedName(value="sha1")
	public String sha1;
	@Expose
	@SerializedName(value="size")
	public int size;
	@Expose
	@SerializedName(value="url")
	public String url;
}
