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
import com.launcher.hamcl.uis.homepage.LauncherSettingFragment;
import com.launcher.hamcl.views.PullListView;

public class VersionListFunctionbarFragment extends Fragment implements View.OnClickListener{

    private PullListView fb_version_list_path;
    private LinearLayoutCompat fb_add_minecraft_path;

    private LinearLayoutCompat fb_install_pack;
    private LinearLayoutCompat fb_refresh;
    private LinearLayoutCompat fb_global_game_settings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_version_list_functionbar,container,false);

        fb_version_list_path = view.findViewById(R.id.fb_version_list_path);
        fb_add_minecraft_path = view.findViewById(R.id.fb_add_minecraft_path);

        fb_install_pack = view.findViewById(R.id.fb_install_pack);
        fb_refresh = view.findViewById(R.id.fb_refresh);
        fb_global_game_settings = view.findViewById(R.id.fb_global_game_settings);

        fb_add_minecraft_path.setOnClickListener(this);
        fb_install_pack.setOnClickListener(this);
        fb_refresh.setOnClickListener(this);
        fb_global_game_settings.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb_add_minecraft_path:

                break;
            case R.id.fb_install_pack:

                break;
            case R.id.fb_refresh:

                break;
            case R.id.fb_global_game_settings:

                break;
        }
    }
}
