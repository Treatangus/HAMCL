package com.launcher.hamcl.uis.homepage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.launcher.hamcl.MainActivity;
import com.launcher.hamcl.R;
import com.launcher.hamcl.version.LocalVersionListAdapter;
import com.launcher.hamcl.version.LocalVersionListBean;
import com.launcher.hamcl.setting.SettingManager;
import com.launcher.hamcl.setting.model.ConfigModel;
import com.launcher.hamcl.setting.model.SettingModel;
import com.launcher.hamcl.uis.UICallbacks;
import com.launcher.hamcl.views.PullListView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class VersionListFragment extends Fragment implements View.OnClickListener , UICallbacks {

    private SettingManager settingManager;
    private SettingModel settingModel;
    private ConfigModel configModel;

    private PullListView versionFileListView;

    private FrameLayout game_list_framelayout;

    private MainActivity parents;
    private FragmentTransaction HomepageTransaction;

    private List<LocalVersionListBean> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_version_list,container,false); //  此处的布局文件是普通的线性布局（此博客忽略）

        versionFileListView = (PullListView) view.findViewById(R.id.versionFileListView);

        game_list_framelayout = (FrameLayout)view.findViewById(R.id.game_list_framelayout);

        gameslist();
        return view;
    }

    @Override
    public void initSetting() {
        settingManager = new SettingManager(getActivity());
        settingModel = new SettingModel();
        configModel = new ConfigModel();
        /*settingManager = MainActivity.settingManager;
        settingModel = MainActivity.settingModel;
        configModel = MainActivity.configModel;*/
    }
    @Override
    public void getSettingFile() {
        configModel = settingManager.getConfigFromFile(configModel);
    }

    @Override
    public void saveSetting() {
        settingManager.saveConfigToFile(configModel);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            parents= (MainActivity) activity;

        }
    }

    private void gameslist() {
        setdata();

        versionFileListView.setOnPullListener(new PullListView.OnPullListener() {
            @Override
            public void onPullDown(View p) {

            }

            @Override
            public void onPullUp(View p) {

            }
        });

        LocalVersionListAdapter adapter = new LocalVersionListAdapter(getActivity(), data); //传入数据-同上
        adapter.setOnItemDepartment(new LocalVersionListAdapter.OnItemDepartment() {

            @Override
            public void OnItemDepartmentItem(String plot_id, ImageView department_select) {

            }

            @Override
            public void OnItemDepartmentItem(String id) {
                Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
            }
        });
        versionFileListView.setAdapter(adapter);
    }

    private void setHomepageClick(int type) {
        HomepageTransaction = ((MainActivity)this.getActivity()).getSupportFragmentManager().beginTransaction();
        ((MainActivity)this.getActivity()).Homepageline=type;
        ((MainActivity)this.getActivity()).hideHomepageFragment(HomepageTransaction);
        ((MainActivity)this.getActivity()).setHomepageClick(type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    private void setdata() {
        try {
            String[] f =new File(dirs().toString() + "/versions/").list();

            data = new ArrayList<LocalVersionListBean>();
            if (new File(dirs().toString() +"/versions/").exists()) {
               for (String a : f) {
                   LocalVersionListBean gameVersionData = new LocalVersionListBean();
                   gameVersionData.setVersionId ("id显示在这-"+a);
                   gameVersionData.setVersionName (a);
                    data.add(gameVersionData);
                }
            } else {
               // data.add("null");
            }
        } catch (Exception e) {
            System.out.println(e);
            //Toast.makeText(getActivity(),e,0).show();
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
            json.put("currentVersion", dirs().toString() + "/versions/" + version.trim());
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

    @SuppressLint("SdCardPath")
    public String dirs() {
        /*try {
            FileInputStream in=new FileInputStream("/sdcard/games/com.explore.launcher/config.txt");
            byte[] b=new byte[in.available()];
            in.read(b);
            in.close();
            String str=new String(b);
            JSONObject json=new JSONObject(str);
            return json.getString("game_directory");
        } catch (Exception e) {
            System.out.println(e);
        }*/
        return "/sdcard/games/com.explore.launcher/";
    }
}