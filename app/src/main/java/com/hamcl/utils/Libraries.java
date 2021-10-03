package com.hamcl.utils;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Libraries 
{
	@Expose
	@SerializedName(value="downloads")
	public Downloads all;
	@Expose
	@SerializedName(value="name")
	public String name;
}
