package com.hamcl.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AssetsIndexBean 
{
	@Expose
	@SerializedName(value="assetIndex")
	public AssetIndex assetIndex;
}
