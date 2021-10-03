package com.launcher.hamcl.download;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hamcl.utils.AllLib;
import com.hamcl.utils.AssetDownBean;
import com.hamcl.utils.Base;
import com.hamcl.utils.Downloads;
import com.hamcl.utils.Libraries;
import com.hamcl.utils.MinecraftJsonParser;
import com.launcher.hamcl.R;
import com.launcher.hamcl.download.util.DLLCALLBack;
import com.launcher.hamcl.utils.SDCardUtils;
import com.launcher.hamcl.utils.Source;
import com.launcher.hamcl.widget.MaterialDesignToast;
import com.tamic.rx.fastdown.callback.IDLCallback;
import com.tamic.rx.fastdown.content.DownLoadInfo;
import com.tamic.rx.fastdown.core.DownLoadBuilder;
import com.tamic.rx.fastdown.core.Download;
import com.tamic.rx.fastdown.http.callback.Callback;
import com.tamic.rx.fastdown.http.exception.RxException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class MinecraftDownloader implements DLLCALLBack{

    Base base;
    int mode = 0;

    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    LinearLayout parent1;
    TextView mainText;
    ProgressBar progressBar1;

    LinearLayout parent2;
    TextView mainText2;
    ProgressBar progressBar2;

    LinearLayout parent3;
    TextView mainText3;
    ProgressBar progressBar3;

    MaterialButton cancelBtn;
    Context context;
    AppCompatActivity activity;
    String tmp;
    public void download(Context ctx,String tmp){
        context = ctx;
        this.tmp = tmp;
        activity = (AppCompatActivity) ctx;
        CardView linearLayout = (CardView) LayoutInflater.from(context).inflate(R.layout.dialog_download_minecraft,null,false);
        alertDialogBuilder = new AlertDialog.Builder (context);
        alertDialogBuilder.setView (linearLayout);
        parent1 = linearLayout.findViewById (R.id.parent_mainProgress);
        parent2 = linearLayout.findViewById (R.id.parent_secondProgress);
        parent3 = linearLayout.findViewById (R.id.parent_thirdProgress);

        mainText = linearLayout.findViewById (R.id.textView);
        mainText2 = linearLayout.findViewById (R.id.downloading_object);
        mainText3 = linearLayout.findViewById (R.id.downloading_object_progress);

        progressBar1 = linearLayout.findViewById (R.id.progressBar1);
        progressBar2 = linearLayout.findViewById (R.id.progressBar2);
        progressBar3 = linearLayout.findViewById (R.id.progressBar3);

        progressBar1.setMax (3);

        parent2.setVisibility (View.GONE);
        parent3.setVisibility (View.GONE);

        alertDialogBuilder.setCancelable (false);
        alertDialog = alertDialogBuilder.create ();
        alertDialog.show ();
        try {
            downloadLibraries (tmp);
        } catch (IOException exception) {
            exception.printStackTrace ();
        }

    }
    int nowDownloading = -1;

    AllLib lib;
    List<Boolean> maxLib = new ArrayList<Boolean> ();
    void downloadLibraries(String jsonp) throws IOException {
        mode=0;
        MinecraftJsonParser minecraftJsonParser = MinecraftJsonParser.parseFromFile (jsonp,MinecraftJsonParser.TYPE_LIBRARIES);
        lib = minecraftJsonParser.getLibrary ();
        mainText.setText ("下载依赖库...");
        progressBar1.setProgress (1);
        for (Libraries libraries:lib.all){
            if (libraries.all.artifact!=null){
                maxLib.add (true);
            }
        }
        next (jsonp);
    }
    void next(String s){
        nowDownloading+=1;
        if (nowDownloading<lib.all.size ()) {
            Libraries downloads = lib.all.get (nowDownloading);
            if (downloads.all.artifact==null){
                next (s);
                return;
            }
            String path = SDCardUtils.getBaseMinecraftPath ()+"/libraries/"+ downloads.all.artifact.path;
            String url = downloads.all.artifact.url;
            d (path,url);
            activity.runOnUiThread (new Runnable () {
                @Override
                public void run () {
                    progressBar2.setMax (maxLib.size ());
                    progressBar2.setProgress (progressBar2.getProgress ()+1);
                }
            });
        }else {
            try {
                downloadAssets (s);
            } catch (IOException exception) {
                exception.printStackTrace ();
            }


        }
    }
    void d(String p,String u){
        new DLM ().d (p,u,this);
        // new Download.Builder<> ().url (u).isImplicit (false).savepath (p).setCallback (this).build (context).start ();
    }


    /**
     * @param a
     * @param b
     * @return
     */
    public int bs(int a ,int b){
        return (int)((new BigDecimal ((float) a / b).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())*100);
    }

    @Override
    public void onFinish () {
        if (mode==0)
            next (tmp);
        else if (mode==1)
            nextAss ();
        else {
            activity.runOnUiThread (new Runnable () {
                @Override
                public void run () {
                    MaterialDesignToast.makeText (activity,"下载完成！",Toast.LENGTH_LONG,MaterialDesignToast.TYPE_SUCCESS).show ();
                }
            });
        }

    }

    @Override
    public void onProgress (int m, int n) {

        activity.runOnUiThread (new Runnable () {
            @SuppressLint("SetTextI18n")
            @Override
            public void run () {

                progressBar3.setMax (m);
                progressBar3.setProgress ((int) n);
                mainText3.setText (bs ((int) n, (int) m) + "%");

            }
        });
    }

    @Override
    public void onStart (int m, String name) {

        activity.runOnUiThread (new Runnable () {
            @Override
            public void run () {

                parent2.setVisibility (View.VISIBLE);
                parent3.setVisibility (View.VISIBLE);
                progressBar3.setMax ((int) m);
                mainText2.setText (name);

            }
        });
    }
    Map<String, AssetDownBean> assetDownBeanMap;
    List<String> keys;
    void downloadAssets(String jsop) throws IOException {
        mode=1;nowDownloading = -1;
        maxLib = new ArrayList<> ();
        keys= new ArrayList<> ();
        MinecraftJsonParser minecraftJsonParser = MinecraftJsonParser.parseFromFile (jsop,MinecraftJsonParser.TYPE_ASSETINDEX);
        base = minecraftJsonParser.base;
        MinecraftJsonParser minecraftJsonParser1 = MinecraftJsonParser.parseFromFile (SDCardUtils.getBaseMinecraftPath ()+"/assets/indexes/"+minecraftJsonParser.getAssetIndex ().assetIndex.id+".json",MinecraftJsonParser.TYPE_ASSETS);
        assetDownBeanMap = minecraftJsonParser1.getAsset ();
        for (String s:assetDownBeanMap.keySet ()){
            keys.add (s);
        }
        nextAss ();
        progressBar2.setMax (keys.size ());
        activity.runOnUiThread (new Runnable () {
            @Override
            public void run () {
                mainText.setText ("下载资源文件...");
                progressBar1.setProgress (2);
            }
        });
    }
    void nextAss(){
        nowDownloading+=1;
        progressBar2.setProgress (nowDownloading);
        if (nowDownloading<keys.size ()){
            AssetDownBean bean = assetDownBeanMap.get (keys.get (nowDownloading));
            String p = bean.getUrl (Source.source);
            String sp = SDCardUtils.getBaseMinecraftPath ()+ bean.getpath ();
            d (sp,p);
        }else{

            activity.runOnUiThread (new Runnable () {
                @Override
                public void run () {
                    mainText.setText ("下载客户端中...");
                }
            });



            progressBar1.setProgress (3);
            String name = base.downloadsSmall.client.name;
            String sp = SDCardUtils.getBaseMinecraftPath ()+"/versions/"+name+"/"+name+".jar";
            d (sp,base.downloadsSmall.client.url);
            //  base.downloadsSmall.client.
        }
    }
}
