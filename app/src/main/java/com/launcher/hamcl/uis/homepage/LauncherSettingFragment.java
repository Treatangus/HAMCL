package com.launcher.hamcl.uis.homepage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.launcher.hamcl.R;

public class LauncherSettingFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launcher_setting,container,false); //  此处的布局文件是普通的线性布局（此博客忽略）

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}