package com.launcher.hamcl.download.util;

public interface DLLCALLBack {
    void onFinish();
    void onProgress(int m,int n);
    void onStart(int m,String name);
}
