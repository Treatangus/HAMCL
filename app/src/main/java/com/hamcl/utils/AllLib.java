package com.hamcl.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AllLib 
{
	@Expose
	@SerializedName(value="libraries")
	public List<Libraries> all;
}
