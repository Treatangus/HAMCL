package com.launcher.hamcl.uis.functionbar;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.material.textview.MaterialTextView;
import com.launcher.hamcl.MainActivity;
import com.launcher.hamcl.R;
import com.launcher.hamcl.setting.SettingManager;
import com.launcher.hamcl.setting.model.ConfigModel;
import com.launcher.hamcl.setting.model.SettingModel;
import com.launcher.hamcl.uis.UICallbacks;

public class FunctionbarFragment extends Fragment implements View.OnClickListener , UICallbacks {

    private SettingManager settingManager;
    private SettingModel settingModel;
    private ConfigModel configModel;

    private RelativeLayout user_management_rl;
    private RelativeLayout game_management_rl;
    private RelativeLayout game_list_rl;
    private RelativeLayout minecraft_log_rl;
    private RelativeLayout library_management_rl;
    private RelativeLayout starter_settings_rl;

    private MaterialTextView user_name_tv;
    private MaterialTextView user_mode_tv;

    private MainActivity parents;
    private FragmentTransaction HomepageTransaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_functionbar,container,false);
        user_management_rl = view.findViewById(R.id.user_management_rl);
        game_management_rl = view.findViewById(R.id.game_management_rl);
        game_list_rl = view.findViewById(R.id.game_list_rl);
        minecraft_log_rl = view.findViewById(R.id.minecraft_log_rl);
        library_management_rl = view.findViewById(R.id.library_management_rl);
        starter_settings_rl = view.findViewById(R.id.starter_settings_rl);

        user_name_tv = view.findViewById(R.id.user_name_tv);
        user_mode_tv = view.findViewById(R.id.user_mode_tv);

        user_management_rl.setOnClickListener(this);
        game_management_rl.setOnClickListener(this);
        game_list_rl.setOnClickListener(this);
        minecraft_log_rl.setOnClickListener(this);
        library_management_rl.setOnClickListener(this);
        starter_settings_rl.setOnClickListener(this);
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

        user_name_tv.setText(configModel.getauth_player_name());
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

    private void setHomepageClick(int type) {
        /*HomepageTransaction = ((MainActivity)this.getActivity()).getSupportFragmentManager().beginTransaction();
        ((MainActivity)this.getActivity()).Homepageline=type;
        ((MainActivity)this.getActivity()).hideHomepageFragment(HomepageTransaction);
        ((MainActivity)this.getActivity()).setHomepageClick(type);*/

        parents.Homepageline=parents.AddArray(parents.Homepageline,type);
        parents.setHomepageClick(type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_management_rl:
                //Toast.makeText(MainActivity.this, "用户管理" , Toast.LENGTH_SHORT).show();
                setHomepageClick(1);
                //line=AddArray(line,1);
                break;
            case R.id.game_management_rl:

                break;
            case R.id.game_list_rl:

                break;
            case R.id.minecraft_log_rl:

                break;
            case R.id.library_management_rl:
                setHomepageClick(5);
                break;
            case R.id.starter_settings_rl:

                break;
        }
    }
}