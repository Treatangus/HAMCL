package com.launcher.hamcl.uis.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;
import com.launcher.hamcl.MainActivity;
import com.launcher.hamcl.R;
import com.launcher.hamcl.setting.SettingManager;
import com.launcher.hamcl.setting.model.ConfigModel;
import com.launcher.hamcl.setting.model.SettingModel;
import com.launcher.hamcl.uis.UICallbacks;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import cosine.boat.LauncherActivity;

public class StartFragment extends Fragment implements View.OnClickListener , UICallbacks {

    private SettingManager settingManager;
    private SettingModel settingModel;
    private ConfigModel configModel;

    private LinearLayoutCompat total_ll;
    private LinearLayoutCompat start_games_ll;
    private MaterialTextView versions_tv;
    private AppCompatImageView choice_version_iv;

    private List<String> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start,container,false);

        total_ll = view.findViewById(R.id.total_ll);
        start_games_ll = view.findViewById(R.id.start_games_ll);
        versions_tv = view.findViewById(R.id.versions_tv);
        choice_version_iv = view.findViewById(R.id.choice_version_iv);

        start_games_ll.setOnClickListener(this);
        choice_version_iv.setOnClickListener(this);

        versions_tv.setText(gamesVersions());

        return view;
    }

    @Override
    public void initSetting() {
        settingManager = new SettingManager(getActivity());
        settingModel = new SettingModel();
        configModel = new ConfigModel();
        /*settingManager = MainActivity.settingManager;
        settingModel = MainActivity.settingModel;
        configModel = MainActivity.configModel = new ConfigModel();*/
    }

    @Override
    public void getSettingFile() {
        //configModel = settingManager.getConfigFromFile();
    }

    @Override
    public void saveSetting() {
        //settingManager.saveConfigToFile(configModel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_games_ll:
                start();
                break;
            case R.id.choice_version_iv:
                showListPopulWindow();
                break;
        }
    }

    public void start(){
        Intent intent = new Intent(getActivity(), LauncherActivity.class);
        startActivity(intent);
    }

    private void showListPopulWindow() {
        setdata();
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(getActivity());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_start_games_adapter, R.id.spn_versions_tv, data);
        listPopupWindow.setAdapter(adapter);//用android内置布局，或设计自己的样式)
        listPopupWindow.setAnchorView(total_ll);//以哪个控件为基准
        listPopupWindow.setVerticalOffset(0);//相对控件竖轴偏移量
        listPopupWindow.setWidth(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setHeight(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setModal(true);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!((String)adapterView.getItemAtPosition(i)).equals("null")) {
                    setVersion((String)adapterView.getItemAtPosition(i));
                    versions_tv.setText(((String)adapterView.getItemAtPosition(i)));
                    listPopupWindow.dismiss();
                }
            }
        });
        listPopupWindow.setSelection(adapter.getPosition(gamesVersions()));
        listPopupWindow.show();//把ListPopWindow展示出来
    }

    private void setdata() {
        String[] f =new File(configModel.getcurrentVersion()).list();
        data = new ArrayList<String>();
        if (new File(configModel.getcurrentVersion()).exists()) {
            for (String a : f) {
                data.add(a);
            }
        } else {
            data.add("null");
        }
    }

    private void setVersion(String version) {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.explore.launcher/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            json.remove("currentVersion");
            json.put("currentVersion", configModel.getcurrentVersion() + version.trim());
            FileWriter fr=new FileWriter(new File("/sdcard/games/com.explore.launcher/config.txt"));
            fr.write(json.toString());
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String gamesVersions() {
        try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.explore.launcher/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            if (str.equals("")) {
                return "null";
            }
            JSONObject json=new JSONObject(str);
            String cnt=json.getString("currentVersion");
            return cnt.substring(cnt.indexOf("versions") + 9);
        } catch (Exception e) {
            return e.toString();
        }
    }
}
