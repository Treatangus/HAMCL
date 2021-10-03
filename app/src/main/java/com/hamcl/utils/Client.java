package com.hamcl.utils;

import com.google.gson.annotations.SerializedName;

public class Client {
    @SerializedName (value = "sha1")
    public String sha1;
    @SerializedName (value = "url")
    public String url;
    @SerializedName (value = "size")
    public int size;

    public String name;
}
