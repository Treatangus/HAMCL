package com.launcher.hamcl.uis.homepage;

import android.content.Context;
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
import com.launcher.hamcl.login.MinecraftLogin;
import com.launcher.hamcl.setting.SettingManager;
import com.launcher.hamcl.setting.model.ConfigModel;
import com.launcher.hamcl.setting.model.SettingModel;
import com.launcher.hamcl.uis.UICallbacks;
import com.launcher.hamcl.user.UserListAdapter;
import com.launcher.hamcl.user.UserListBean;
import com.launcher.hamcl.utils.ScreenUtils;
import com.launcher.hamcl.views.PullListView;
import com.launcher.hamcl.widget.MaterialDesignToast;
import com.zuowu.utils.UserDataBaseBox;

import java.util.ArrayList;
import java.util.List;

import me.ratsiel.auth.model.mojang.LoginInterface;

public class UserFragment extends Fragment implements View.OnClickListener , UICallbacks, LoginInterface {

   static UserListAdapter userListAdapter;

    private SettingManager settingManager;
    private SettingModel settingModel;
    private ConfigModel configModel;

    private FloatingActionButton adduser_fabtn;

    private TextInputEditText user_mojang_edit;
    private TextInputEditText password_mojang_edit;

    private TextInputEditText user_microsoft_edit;
    private TextInputEditText password_microsoft_edit;

    private static PullListView users_lv;
    public static Context context;

    private static List<UserListBean> beans;

    public static void fruse(){
        UserDataBaseBox dataBaseBox = UserDataBaseBox.getInstance ();
        beans = dataBaseBox.getUsers ("/sdcard/games/com.explore.launcher/users");

        userListAdapter = getUserListAdapter();  // new U serListAdapter (getContext (),beans);
        users_lv.setAdapter (userListAdapter);


    }
    public static UserListAdapter getUserListAdapter (){
        return new UserListAdapter (context,beans);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,container,false);

        context = getContext ();
        adduser_fabtn = (FloatingActionButton)view.findViewById(R.id.adduser_fabtn);
        users_lv = (PullListView) view.findViewById(R.id.users_lv);

        UserDataBaseBox dataBaseBox = UserDataBaseBox.getInstance ();
        beans = dataBaseBox.getUsers ("/sdcard/games/com.explore.launcher/users");

       /* UserListBean bean = new UserListBean ();
        bean.setUserPassword ("");
        bean.setUserType (0);
        bean.setUserNmae ("osjcos");

        UserListBean bean1 = new UserListBean ();
        bean1.setUserPassword ("");
        bean1.setUserType (1);
        bean1.setUserNmae ("oos");

        beans = new ArrayList<UserListBean> ();*/
      //  beans.add (bean);
      //  beans.add (bean1);


        userListAdapter = new UserListAdapter (getContext (),beans);
        users_lv.setAdapter (userListAdapter);

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
        user_mojang_edit = view.findViewById(R.id.user_mojang_edit);
        password_mojang_edit = view .findViewById(R.id.password_mojang_edit);

        final LinearLayoutCompat microsoft_user = view.findViewById(R.id.microsoft_user);
        final LinearLayoutCompat microsoft_password = view.findViewById(R.id.microsoft_password);
        user_microsoft_edit = view.findViewById(R.id.user_microsoft_edit);
        password_microsoft_edit = view .findViewById(R.id.password_microsoft_edit);

        user_mojang_edit.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
            }

            @Override
            public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
            }

            @Override
            public void afterTextChanged(Editable p1) {
               // configModel.setauth_player_name(p1.toString());
            }
        });
        //user_name_edit.setText(configModel.getauth_player_name());

        String[] mItems = getResources().getStringArray(R.array.login_mode);
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
                        if(genuine_login.getVisibility() == View.VISIBLE)
                        {
                            genuine_login.setVisibility(View.GONE);
                            offline_mode.setVisibility( View.VISIBLE);
                            microsoft_user.setVisibility(View.GONE);
                            microsoft_password.setVisibility(View.GONE);
                        }
                        else if(genuine_login.getVisibility() == View.GONE)
                        {
                            genuine_login.setVisibility(View.GONE);
                            offline_mode.setVisibility( View.VISIBLE);
                            microsoft_user.setVisibility(View.GONE);
                            microsoft_password.setVisibility(View.GONE);
                        }
                      //  MaterialDesignToast.makeText(getActivity(), "离线模式", Toast.LENGTH_SHORT,MaterialDesignToast.TYPE_INFO).show();
                        break;
                    case 1:
                        if(offline_mode.getVisibility() == View.VISIBLE)
                        {
                            offline_mode.setVisibility(View.VISIBLE);
                            genuine_login.setVisibility( View.VISIBLE);
                            microsoft_user.setVisibility(View.GONE);
                            microsoft_password.setVisibility(View.GONE);
                        }
                        else if(offline_mode.getVisibility() == View.GONE)
                        {
                            offline_mode.setVisibility(View.VISIBLE);
                            genuine_login.setVisibility( View.VISIBLE);
                            microsoft_user.setVisibility(View.GONE);
                            microsoft_password.setVisibility(View.GONE);
                        }
                       // MaterialDesignToast.makeText(getActivity(), "正版登录", Toast.LENGTH_SHORT,MaterialDesignToast.TYPE_INFO).show();
                        break;
                    case 2:
                        microsoft_user.setVisibility(View.VISIBLE);
                        microsoft_password.setVisibility(View.VISIBLE);
                        offline_mode.setVisibility(View.GONE);
                        genuine_login.setVisibility( View.GONE);
                        /*if(offline_mode.getVisibility() == View.VISIBLE||offline_mode.getVisibility() == View.VISIBLE||
                                microsoft_account_ll.getVisibility() == View.VISIBLE) {
                            microsoft_account_ll.setVisibility(View.VISIBLE);
                            offline_mode.setVisibility(View.GONE);
                            genuine_login.setVisibility( View.GONE);
                        }
                        else if(offline_mode.getVisibility() == View.GONE||genuine_login.getVisibility() == View.GONE||
                                microsoft_account_ll.getVisibility() == View.GONE)
                        {
                            microsoft_account_ll.setVisibility(View.VISIBLE);
                            offline_mode.setVisibility(View.GONE);
                            genuine_login.setVisibility( View.GONE);
                        }*/
                       // MaterialDesignToast.makeText(getActivity(), "微软登录", Toast.LENGTH_SHORT,MaterialDesignToast.TYPE_INFO).show();
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
                if(sp_login_mode.getSelectedItemId () == 0/*offline_mode.getVisibility() == View.VISIBLE||genuine_login.getVisibility() == View.GONE*/) {
                    //settingManager.saveConfigToFile(configModel);
                    if (!user_mojang_edit.getText ().toString ().equals ("")) {

                      if(UserDataBaseBox.getInstance ().saveUserData (user_mojang_edit.getText ().toString (),0,"non"))
                        {
                            dialog.dismiss ();
                            MaterialDesignToast.makeText (getActivity (), "创建成功", Toast.LENGTH_SHORT, MaterialDesignToast.TYPE_SUCCESS).show ();
                           // userListAdapter.notifyDataSetChanged ();
                            fruse ();
                        }else {
                          MaterialDesignToast.makeText (getActivity (),"用户已存在！",Toast.LENGTH_LONG,MaterialDesignToast.TYPE_WARNING).show ();
                      }
                       // beans = UserDataBaseBox.getInstance ().getUsers ("/sdcard/games/com.explore.launcher/users");

                      //  users_lv.setAdapter (new UserListAdapter (getActivity (),beans));
                      //  users_lv.deferNotifyDataSetChanged ();
                    }else {
                        MaterialDesignToast.makeText (getContext (),"请输入用户名！",Toast.LENGTH_LONG,MaterialDesignToast.TYPE_WARNING).show ();
                    }

                } else if(sp_login_mode.getSelectedItemId () == 1/*offline_mode.getVisibility() == View.VISIBLE||genuine_login.getVisibility() == View.VISIBLE*/){

                    if (!user_mojang_edit.getText ().toString ().equals ("")&&!password_mojang_edit.getText ().toString ().equals ("")) {
                        dialog.dismiss ();
                        MaterialDesignToast.makeText (getActivity (), "登录Mojang中", Toast.LENGTH_SHORT, MaterialDesignToast.TYPE_INFO).show ();
                        MinecraftLogin.loginWithMojang (user_mojang_edit.getText ().toString (),password_mojang_edit.getText ().toString (),UserFragment.this);
                    }else {
                        MaterialDesignToast.makeText (getContext (),"请将信息输入完整！！",Toast.LENGTH_LONG,MaterialDesignToast.TYPE_WARNING).show ();
                    }

                } else if(sp_login_mode.getSelectedItemId () == 2/*microsoft_user.getVisibility() == View.VISIBLE||microsoft_password.getVisibility() == View.VISIBLE*/){
                    if (!user_microsoft_edit.getText ().toString ().equals ("")&&!password_microsoft_edit.getText ().toString ().equals ("")) {
                        dialog.dismiss ();
                        MaterialDesignToast.makeText (getActivity (), "登录Microsoft中", Toast.LENGTH_SHORT, MaterialDesignToast.TYPE_INFO).show ();
                        MinecraftLogin.loginWithMicrosoft (user_microsoft_edit.getText ().toString (),password_microsoft_edit.getText ().toString (),UserFragment.this);

                    }else {
                        MaterialDesignToast.makeText (getContext (),"请将信息输入完整！！",Toast.LENGTH_LONG,MaterialDesignToast.TYPE_WARNING).show ();
                    }
                }
            }
        });

        dialog.show();
        //此处设置位置窗体大小，我这里设置为了手机屏幕宽度的3/4  注意一定要在show方法调用后再写设置窗口大小的代码，否则不起效果会
        dialog.getWindow().setLayout((ScreenUtils.getScreenWidth(getActivity())/6*4),/*ScreenUtils.getScreenHeight(getActivity())/4*3);*/ LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onConnectSuccess () {
        //登录连接成功
        getActivity ().runOnUiThread (new Runnable () {
            @Override
            public void run () {
                MaterialDesignToast.makeText (getContext (),"登录成功！",Toast.LENGTH_LONG,MaterialDesignToast.TYPE_SUCCESS).show ();
            }
        });

    }

    @Override
    public void onConnectFail () {
    //登录/连接失败
        getActivity ().runOnUiThread (new Runnable () {
            @Override
            public void run () {
                MaterialDesignToast.makeText (getContext (),"登录失败！请检查账号密码或网络连接",Toast.LENGTH_LONG,MaterialDesignToast.TYPE_ERROR).show ();
            }
        });

    }
}
