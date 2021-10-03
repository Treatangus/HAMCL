package com.hamcl.utils;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class Downloads 
{
	@Expose
	@SerializedName(value="artifact")
	public Artifact artifact;
	
	//@SerializedName(value="name")
	//public String name;
	
}
