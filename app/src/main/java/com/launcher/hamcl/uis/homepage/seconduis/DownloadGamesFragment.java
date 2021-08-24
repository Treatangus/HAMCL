package com.launcher.hamcl.uis.homepage.seconduis;

import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.launcher.hamcl.R;
import com.launcher.hamcl.download.VersionLibraryAdapter;
import com.launcher.hamcl.download.downloader.HttpUtil;
import com.launcher.hamcl.download.util.VersionUtil;
import com.launcher.hamcl.views.PullListView;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

public class DownloadGamesFragment extends Fragment {

    private AppCompatSpinner sp_download_source_mode;
    private String download_source;

    private RadioGroup rgSelect;
    private AppCompatRadioButton rbRelease;
    private AppCompatRadioButton rbSnapshot;
    private AppCompatRadioButton rbOldbeta;

    private PullListView version_list;
    private VersionLibraryAdapter adapter_version_library;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download_games,container,false); //  此处的布局文件是普通的线性布局（此博客忽略）

        sp_download_source_mode = (AppCompatSpinner)view.findViewById(R.id.sp_download_source_mode);

        rgSelect = (RadioGroup)view.findViewById(R.id.rg_select);

        rbRelease = (AppCompatRadioButton)view.findViewById(R.id.rb_release);
        rbSnapshot = (AppCompatRadioButton)view.findViewById(R.id.rb_snapshot);
        rbOldbeta = (AppCompatRadioButton)view.findViewById(R.id.rb_old_beta);

        version_list = (PullListView)view.findViewById(R.id.version_list);

        get("https://launchermeta.mojang.com/mc/game/version_manifest.json");

        initView();
        return view;
    }

    private void initView() {
        rgSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
					/*if(adapter==null){
					 get("https://download.mcbbs.net/mc/game/version_manifest.json");
					 }{*/
                switch (checkedId){
                    case R.id.rb_release:
                        adapter_version_library.setType(1);
                        //Toast.makeText(MainActivity.this, checkedId , Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_snapshot:
                        adapter_version_library.setType(0);
                        break;
                    case R.id.rb_old_beta:
                        adapter_version_library.setType(2);
                        break;
                    default:
                        break;
                }
            }
        });
        String[] mItems = getResources().getStringArray(R.array.download_source);
        ArrayAdapter adapter_source = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_download_games_source, mItems);
        sp_download_source_mode.setAdapter(adapter_source);
        sp_download_source_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /*
             * parent:指示spinner
             * view：显示item的空间，这里指TextView
             * position：被选中的item的位置
             * id：选中项的id
             * */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        version_list.setAdapter(null);
                        rbRelease.setChecked(true);
                        get("https://launchermeta.mojang.com/mc/game/version_manifest.json");
                        download_source = "https://launchermeta.mojang.com";
                        Toast.makeText(getActivity(), download_source , Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        version_list.setAdapter(null);
                        rbRelease.setChecked(true);
                        get("https://download.mcbbs.net/mc/game/version_manifest.json");
                        download_source = "https://bmclapi2.bangbang93.com";
                        Toast.makeText(getActivity(), download_source , Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        version_list.setAdapter(null);
                        rbRelease.setChecked(true);
                        get("https://bmclapi2.bangbang93.com/mc/game/version_manifest.json");
                        download_source = "https://download.mcbbs.net";
                        Toast.makeText(getActivity(), download_source , Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void set(String x){

        Message msg = new Message();
        msg.what = 1;
        msg.obj = x;

        mHandler.sendMessage(msg);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg)
        {

            switch (msg.what)
            {
                case 1:
                    getlist((String)msg.obj);
                    break;
                default:
                    break;
            }
        }
    }

    private MyHandler mHandler=new MyHandler();

    private void getlist(String json)
    {

        ArrayList<VersionUtil> listItems=Online_Version_List(json);
        adapter_version_library = new VersionLibraryAdapter(getActivity(), listItems,1);//初始化type1
        adapter_version_library.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onInvalidated() {

            }
        });

        version_list.setAdapter(adapter_version_library);
        adapter_version_library.setOnItemDepartment(new VersionLibraryAdapter.OnItemDepartment(){

            @Override
            public void OnItemDepartmentItem(String type)
            {
                // TODO: Implement this method
            }

            @Override
            public void OnItemDepartmentItem(String id,String url)
            {
                /*
                DownloadFragment showDialog = new DownloadFragment();
                Bundle bundle = new Bundle();
                bundle.putString("version",id);
                bundle.putString("game","/sdcard/boat/gamedir");
                bundle.putString("address",download_source);
                showDialog.setArguments(bundle);
                showDialog.show(getSupportFragmentManager(),"show");

                 */

					/*
					if(DownloadService.isStarted)return;
					Intent i=new Intent(MainActivity.this,DownloadService.class);
					Bundle bundle=new Bundle();
					bundle.putString("version",id);
					bundle.putString("game","/sdcard/boat/gamedir");
					bundle.putString("address",download_source);
					i.putExtras(bundle);
					startService(i);*/

					/*
					Intent i=new Intent(MainActivity.this,DownloadService.class);
					Bundle bundle=new Bundle();
					bundle.putString("version",id);
					bundle.putString("game","/sdcard/boat/gamedir");
					bundle.putString("address",download_source);
					i.putExtras(bundle);
					startService(i);*/

            }
        });
    }

    private void get(String url){
        HttpUtil.sendOkHttpRequest(url, new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, final IOException e) {

            }
            @Override
            public void onResponse(Call p1, Response p2) throws IOException
            {
                final String url=p2.body().string();
                new Thread(){
                    public void run(){
                        set(url);
                    }
                }.start();
            }
        });
    }

    public ArrayList<VersionUtil> Online_Version_List(String json) throws JSONException {
        ArrayList<VersionUtil> data_list=new ArrayList<VersionUtil>();
        if(json!=null){

            JSONObject root = JSON.parseObject(json);
            JSONArray branch = root.getJSONArray("versions");
            for (int i = 0 ; i < branch.size();i++){
                VersionUtil util=new VersionUtil();
                JSONObject value = (JSONObject)branch.get(i);
                String brach_id = (String)value.get("id");
                String brach_type = (String)value.get("type");
                String brach_url=((String)value.get("url"));

                util.id(brach_id);
                util.type(brach_type);
                util.url(brach_url);
                data_list.add(util);
            }

        }else{

            return null;
        }
        return data_list;
    }
}