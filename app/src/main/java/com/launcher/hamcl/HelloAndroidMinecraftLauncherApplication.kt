package com.launcher.hamcl

import android.app.Application

class HelloAndroidMinecraftLauncherApplication : Application() ,Thread.UncaughtExceptionHandler{
    override fun onCreate() {
        super.onCreate()
        CrashHandler.init(this)
       // Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(p0: Thread, p1: Throwable) {

    }
}