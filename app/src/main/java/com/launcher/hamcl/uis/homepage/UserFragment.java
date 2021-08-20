package com.launcher.hamcl.uis.homepage;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.launcher.hamcl.MainActivity;
import com.launcher.hamcl.R;
import com.launcher.hamcl.setting.SettingManager;
import com.launcher.hamcl.setting.model.ConfigModel;
import com.launcher.hamcl.setting.model.SettingModel;
import com.launcher.hamcl.uis.UICallbacks;
import com.launcher.hamcl.utils.ScreenUtils;

public class UserFragment extends Fragment implements View.OnClickListener , UICallbacks {

    private SettingManager settingManager;
    private SettingModel settingModel;
    private ConfigModel configModel;

    private FloatingActionButton adduser_fabtn;

    private TextInputEditText user_name_edit;
    private TextInputEditText password_edit;

    private ListView users_lv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,container,false);

        adduser_fabtn = (FloatingActionButton)view.findViewById(R.id.adduser_fabtn);
        users_lv = (ListView)view.findViewById(R.id.users_lv);

        adduser_fabtn.setOnClickListener(this);

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
            case R.id.adduser_fabtn:
                CreateUserDialog();
                break;
        }
    }

    private void CreateUserDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_create_user,null,false);
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(view).create();

        AppCompatSpinner sp_login_mode = view.findViewById(R.id.sp_login_mode);
        MaterialButton btn_cancel_high_opion = view.findViewById(R.id.btn_cancel_high_opion);
        MaterialButton btn_agree_high_opion = view.findViewById(R.id.btn_agree_high_opion);

        final LinearLayoutCompat offline_mode = view.findViewById(R.id.offline_mode);
        final LinearLayoutCompat genuine_login = view.findViewById(R.id.genuine_login);
        user_name_edit = view.findViewById(R.id.user_name_edit);
        password_edit = view .findViewById(R.id.password_edit);
        user_name_edit.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
            }

            @Override
            public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
            }

            @Override
            public void afterTextChanged(Editable p1) {
                configModel.setauth_player_name(p1.toString());
            }
        });
        //user_name_edit.setText(configModel.getauth_player_name());

        String[] mItems = getResources().getStringArray(R.array.arrays);
        ArrayAdapter adapter= new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_dialog_create_user, mItems);
        sp_login_mode.setAdapter(adapter);
        sp_login_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /*
             * parent:指示spinner
             * view：显示item的空间，这里指TextView
             * position：被选中的item的位置
             * id：选中项的id
             *
             **/
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        if(genuine_login.getVisibility()== View.VISIBLE)
                        {
                            genuine_login.setVisibility(View.GONE);
                            offline_mode.setVisibility( View.VISIBLE);
                        }
                        Toast.makeText(getActivity(), "离线模式", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        if(offline_mode.getVisibility()== View.VISIBLE)
                        {
                            offline_mode.setVisibility(View.VISIBLE);
                            genuine_login.setVisibility( View.VISIBLE);
                        }
                        Toast.makeText(getActivity(), "正版登录", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //sp_login_mode.setSelection(adapter.getPosition(mItems));

        btn_cancel_high_opion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_agree_high_opion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(offline_mode.getVisibility() == View.VISIBLE) {
                    settingManager.saveConfigToFile(configModel);
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "创建成功", Toast.LENGTH_SHORT).show();
                } else if(genuine_login.getVisibility() == View.VISIBLE){
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "登录中", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
        //此处设置位置窗体大小，我这里设置为了手机屏幕宽度的3/4  注意一定要在show方法调用后再写设置窗口大小的代码，否则不起效果会
        dialog.getWindow().setLayout((ScreenUtils.getScreenWidth(getActivity())/6*4),/*ScreenUtils.getScreenHeight(getActivity())/4*3);*/ LinearLayout.LayoutParams.WRAP_CONTENT);
    }
}
