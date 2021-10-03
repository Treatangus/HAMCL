package com.launcher.hamcl.uis.functionbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.launcher.hamcl.R;
import com.launcher.hamcl.setting.SettingManager;
import com.launcher.hamcl.setting.model.ConfigModel;
import com.launcher.hamcl.setting.model.SettingModel;
import com.launcher.hamcl.uis.UICallbacks;

public class UserFunctionbarFragment extends Fragment implements View.OnClickListener, UICallbacks {

    private SettingManager settingManager;
    private SettingModel settingModel;
    private ConfigModel configModel;

    private LinearLayoutCompat user_offline_ll;
    private LinearLayoutCompat user_mojang_ll;
    private LinearLayoutCompat user_microsoft_ll;
    private LinearLayoutCompat user_external_login_ll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_functionbar,container,false);

        user_offline_ll = view.findViewById(R.id.user_offline_ll);
        user_mojang_ll = view.findViewById(R.id.user_mojang_ll);
        user_microsoft_ll = view.findViewById(R.id.user_microsoft_ll);
        user_external_login_ll = view.findViewById(R.id.user_external_login_ll);

        user_offline_ll.setOnClickListener(this);
        user_mojang_ll.setOnClickListener(this);
        user_microsoft_ll.setOnClickListener(this);
        user_external_login_ll.setOnClickListener(this);
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
        //configModel = settingManager.getConfigFromFile();
    }

    @Override
    public void saveSetting() {
        settingManager.saveConfigToFile(configModel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_offline_ll:

                break;
            case R.id.user_mojang_ll:

                break;
            case R.id.user_microsoft_ll:

                break;
            case R.id.user_external_login_ll:

                break;
        }
    }
}
