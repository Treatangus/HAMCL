package com.launcher.hamcl;

import android.app.Application;

import com.tamic.fastdownsimple.down.DownloadInit;
import com.tamic.rx.fastdown.content.DownLoadInfo;
import com.tamic.rx.fastdown.core.DownLoadBuilder;
import com.tamic.rx.fastdown.core.Download;
import com.tamic.rx.fastdown.core.RxDownloadManager;

public class HAMCL extends Application {
    @Override
    public void onCreate () {
        super.onCreate ();
       // RxDownloadManager manager = RxDownloadManager.getInstance ();
       // manager.init (getApplicationContext (),new DownloadA);
       // DownloadInit.init (getApplicationContext ());
    }
}
