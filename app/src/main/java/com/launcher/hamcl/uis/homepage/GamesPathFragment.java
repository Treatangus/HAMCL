package com.launcher.hamcl.uis.homepage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.launcher.hamcl.R;
import com.launcher.hamcl.setting.SettingManager;
import com.launcher.hamcl.setting.model.ConfigModel;
import com.launcher.hamcl.setting.model.SettingModel;
import com.launcher.hamcl.uis.UICallbacks;
import com.launcher.hamcl.user.UserListAdapter;
import com.launcher.hamcl.views.PullListView;
import com.launcher.hamcl.widget.MaterialDesignToast;
import com.zuowu.utils.UserDataBaseBox;

public class GamesPathFragment extends Fragment implements View.OnClickListener , UICallbacks {

    private SettingManager settingManager;
    private SettingModel settingModel;
    private ConfigModel configModel;

    private FloatingActionButton add_game_path;

    private static PullListView game_path_lv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games_path,container,false);

        add_game_path = (FloatingActionButton)view.findViewById(R.id.add_game_path);
        game_path_lv = (PullListView) view.findViewById(R.id.game_list_tpis_tv);

        add_game_path.setOnClickListener(this);

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_game_path:
                MaterialDesignToast.makeText(getActivity(), "添加游戏目录", Toast.LENGTH_SHORT, MaterialDesignToast.TYPE_INFO).show();
                break;
        }
    }
}