package com.launcher.hamcl;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.launcher.hamcl.setting.SettingManager;
import com.launcher.hamcl.setting.model.ConfigModel;
import com.launcher.hamcl.setting.model.SettingModel;
import com.launcher.hamcl.uis.functionbar.FunctionbarFragment;
import com.launcher.hamcl.uis.homepage.GamesListFragment;
import com.launcher.hamcl.uis.homepage.LibraryFragment;
import com.launcher.hamcl.uis.homepage.StartFragment;
import com.launcher.hamcl.uis.homepage.UserFragment;
import com.launcher.hamcl.widget.MaterialDesignToast;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.HashMap;
import java.util.Objects;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RxPermissions rxPermissions;

    public SettingManager settingManager;
    public SettingModel settingModel;
    public ConfigModel configModel;

    private ImageButton toolbar_button_backspace;
    private TextView main_text_showstate;
    private ImageButton toolbar_button_backhome;
    private ImageButton toolbar_button_refresh;

    private FrameLayout functionbar_fl;
    private FunctionbarFragment functionbar_fragment;

    private FragmentTransaction FunctionbarTransaction;//fragment事务
    private FragmentManager FunctionbarManager;//fragment管理者

    private FrameLayout homepage_fl;
    private StartFragment start_fragment;
    private UserFragment user_fragment;
    private GamesListFragment games_list_fragment;
    private LibraryFragment library_fragment;

    private FragmentTransaction HomepageTransaction;//fragment事务
    private FragmentManager HomepagerManager;//fragment管理者

    private HashMap<Integer, Integer> HomepageUIMap;
    public int Homepageline;
    private HashMap<Integer, String> ToolbarStringMap;//fragment管理者

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar();

        rxPermissions = new RxPermissions (this);

        requestPermission();
        initSetting();
        getSettingFile();
        saveSetting();
        initView();
        setFunctionbarClick(0);
        HomepageMap();
    }

    private void initView(){
        toolbar_button_backspace = (ImageButton)findViewById(R.id.toolbar_button_backspace);
        main_text_showstate = (TextView)findViewById(R.id.main_text_showstate);
        toolbar_button_backhome = (ImageButton)findViewById(R.id.toolbar_button_backhome);
        toolbar_button_refresh = (ImageButton)findViewById(R.id.toolbar_button_refresh);

        toolbar_button_backspace.setOnClickListener(this);
        toolbar_button_backhome.setOnClickListener(this);
        toolbar_button_refresh.setOnClickListener(this);

        functionbar_fl = (FrameLayout)findViewById(R.id.functionbar_fl);
        homepage_fl = (FrameLayout)findViewById(R.id.homepage_fl);
    }

    private void initSetting() {
        settingManager = new SettingManager(this);
        settingModel = new SettingModel();
        configModel = new ConfigModel();
    }

    private void getSettingFile() {
        configModel = settingManager.getConfigFromFile(configModel);
    }

    private void saveSetting() {
        settingManager.saveConfigToFile(configModel);
    }

    private void HomepageMap() {
        HomepageUIMap = new HashMap<Integer,Integer>();
        HomepageUIMap.put(0,0);
        HomepageUIMap.put(1,0);
        HomepageUIMap.put(2,0);
        HomepageUIMap.put(3,0);
        HomepageUIMap.put(4,0);
        HomepageUIMap.put(5,0);
        HomepageUIMap.put(6,0);
        //map.put(目前值,上一级值);
        HomepageUIMap.put(203,2);

        ToolbarStringMap = new HashMap<Integer,String>();
        ToolbarStringMap.put(0,getString(R.string.home_title_homepage));
        ToolbarStringMap.put(1,getString(R.string.fb_user_manage));
        ToolbarStringMap.put(2,getString(R.string.fb_game_manage));
        ToolbarStringMap.put(3,getString(R.string.fb_game_list));
        ToolbarStringMap.put(4,getString(R.string.fb_minecraft_log));
        ToolbarStringMap.put(5,getString(R.string.fb_library_manage));
        ToolbarStringMap.put(4,getString(R.string.fb_launcher_settings));
        ToolbarStringMap.put(203,getString(R.string.home_title_game_setting));

        Homepageline=0;
        setHomepageClick(Homepageline);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_button_backspace:
                setHomepageClick(HomepageUIMap.get(Homepageline));
                Homepageline=HomepageUIMap.get(Homepageline);
                break;
            case R.id.toolbar_button_backhome:
                setHomepageClick(0);
                break;
            case R.id.toolbar_button_refresh:
                refresh();
                break;
        }
    }

    public void refresh() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void setFunctionbarClick(int type) {
        //开启事务
        FunctionbarManager = getSupportFragmentManager();
        //获取到fragment的管理对象
        FunctionbarTransaction = FunctionbarManager.beginTransaction();
        //加载切换动画
        FunctionbarTransaction.setCustomAnimations(R.anim.layout_show,R.anim.layout_hide);
        //显示之前将所有的fragment都隐藏起来,在去显示我们想要显示的fragment
        hideFunctionbarFragment(FunctionbarTransaction);
        switch (type) {
            case 0://左侧功能栏
                //如果左侧功能栏的fragment是null的话,就创建一个
                if (functionbar_fragment == null) {
                    functionbar_fragment = new FunctionbarFragment();
                    //加入事务
                    FunctionbarTransaction.add(R.id.functionbar_fl, functionbar_fragment);
                } else {
                    //如果左侧功能栏fragment不为空就显示出来
                    FunctionbarTransaction.show(
                            functionbar_fragment);
                }
                break;
        }
        //提交事务
        FunctionbarTransaction.commit();
    }

    /**
     * 用来隐藏functionbar_fragment的方法
     *
     * @param；FunctionbarTransaction
     */
    public void hideFunctionbarFragment(FragmentTransaction fragmentTransaction) {
        //如果此fragment不为空的话就隐藏起来
        if (functionbar_fragment != null) {
            fragmentTransaction.hide(functionbar_fragment);
        }
    }

    public void setHomepageClick(int type) {
        //开启事务
        HomepagerManager = getSupportFragmentManager();
        //获取到fragment的管理对象
        HomepageTransaction = HomepagerManager.beginTransaction();
        //加载切换动画
        HomepageTransaction.setCustomAnimations(R.anim.layout_show,R.anim.layout_hide);
        //显示之前将所有的fragment都隐藏起来,在去显示我们想要显示的fragment
        hideHomepageFragment(HomepageTransaction);
        setToolbarTitle(ToolbarStringMap.get(type));
        switch (type) {
            case 0://主页
                //如果主页的fragment是null的话,就创建一个
                if (start_fragment == null) {
                    start_fragment = new StartFragment();
                    //加入事务
                    HomepageTransaction.add(R.id.homepage_fl, start_fragment);
                } else {
                    //如果主页fragment不为空就显示出来
                    HomepageTransaction.show(start_fragment);
                }
                break;
            case 1:
                if (user_fragment == null) {
                    user_fragment = new UserFragment();
                    //加入事务
                    HomepageTransaction.add(R.id.homepage_fl, user_fragment);
                } else {
                    //如果用户fragment不为空就显示出来
                    HomepageTransaction.show(user_fragment);
                }
                break;
            case 3:
                if (games_list_fragment == null) {
                    games_list_fragment = new GamesListFragment();
                    //加入事务
                    HomepageTransaction.add(R.id.homepage_fl, games_list_fragment);
                } else {
                    //如果用户fragment不为空就显示出来
                    HomepageTransaction.show(games_list_fragment);
                }
                break;
			/*case 2:
                if (games_fragment == null) {
                    games_fragment = new GamesFragment();
                    //加入事务
                    mFragmentTransaction.add(R.id.framelayout, games_fragment);
                } else {
                    //如果用户fragment不为空就显示出来
                    mFragmentTransaction.show(games_fragment);
                }
                break;*/
            case 5:
                if (library_fragment == null) {
                    library_fragment = new LibraryFragment();
                    //加入事务
                    HomepageTransaction.add(R.id.homepage_fl, library_fragment);
                } else {
                    //如果核心安装fragment不为空就显示出来
                    HomepageTransaction.show(library_fragment);
                }
                break;
			/*case 4:
                if (gameslog_fragment == null) {
                    gameslog_fragment = new GamesLogFragment();
                    //加入事务
                    mFragmentTransaction.add(R.id.framelayout, gameslog_fragment);
                } else {
                    //如果用户fragment不为空就显示出来
                    mFragmentTransaction.show(gameslog_fragment);
                }
                break;
			case 5:
                if (setting_fragment == null) {
                    setting_fragment = new SettingFragmnet();
                    //加入事务
                    mFragmentTransaction.add(R.id.framelayout, setting_fragment);
                } else {
                    //如果用户fragment不为空就显示出来
                    mFragmentTransaction.show(setting_fragment);
                }
                break;
			case 201:
				if (download_games_fragment == null) {
                    download_games_fragment = new DownloadGamesFragment();
                    //加入事务
                    mFragmentTransaction.add(R.id.framelayout, download_games_fragment);
                } else {
                    //如果主页fragment不为空就显示出来
                    mFragmentTransaction.show(download_games_fragment);
                }
                break;
			case 202:
				if (install_package_fragment == null) {
                    install_package_fragment = new InstallPackageFragment();
                    //加入事务
                    mFragmentTransaction.add(R.id.framelayout, install_package_fragment);
                } else {
                    //如果主页fragment不为空就显示出来
                    mFragmentTransaction.show(install_package_fragment);
                }
                break;
			case 203:
				if (games_setting_fragment == null) {
                    games_setting_fragment = new GamesSettingFragment();
                    //加入事务
                    mFragmentTransaction.add(R.id.framelayout, games_setting_fragment);
                } else {
                    //如果主页fragment不为空就显示出来
                    mFragmentTransaction.show(games_setting_fragment);
                }
                break;*/

        }
        //提交事务
        HomepageTransaction.commit();
    }

    /**
     * 用来隐藏fragment的方法
     *
     * @param fragmentTransaction
     */
    public void hideHomepageFragment(FragmentTransaction fragmentTransaction) {
        //如果此fragment不为空的话就隐藏起来
        if (start_fragment != null) {
            fragmentTransaction.hide(start_fragment);
        }
        if (user_fragment != null) {
            fragmentTransaction.hide(user_fragment);
        }
        if (games_list_fragment != null) {
            fragmentTransaction.hide(games_list_fragment);
        }
		/*if (games_fragment != null) {
            fragmentTransaction.hide(games_fragment);
        }
		if (download_games_fragment != null) {
            fragmentTransaction.hide(download_games_fragment);
        }
		if (install_package_fragment != null) {
            fragmentTransaction.hide(install_package_fragment);
        }
		if (games_setting_fragment != null) {
            fragmentTransaction.hide(games_setting_fragment);
        }*/
        if (library_fragment != null) {
            fragmentTransaction.hide(library_fragment);
        }
		/*if (gameslog_fragment != null) {
            fragmentTransaction.hide(gameslog_fragment);
        }
		if (setting_fragment != null) {
            fragmentTransaction.hide(setting_fragment);
        }*/
    }

    public void setToolbarTitle(String s)
    {
        main_text_showstate.setText(s);
    }

    /**【当Activity销毁时】**/
    @Override
    public void onDestroy() {
        super.onDestroy();
        getSettingFile();
        saveSetting();
    }



    public void requestPermission() {

        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET};

        rxPermissions.requestEach (permissions).subscribe (new Consumer<Permission> () {
            @SuppressLint("CheckResult")
            @Override
            public void accept (Permission permission) throws Exception {
                if (permission.granted){

                }else if (permission.shouldShowRequestPermissionRationale){
                    MaterialDesignToast.makeText ( MainActivity.this,"没有必要权限无法运行，请给予HAMCL权限",Toast.LENGTH_LONG,MaterialDesignToast.TYPE_WARNING).show ();
                    requestPermission ();
                }else {
                    MaterialDesignToast.makeText ( MainActivity.this,"没有必要权限无法运行，请在设置中手动给予HAMCL权限",Toast.LENGTH_LONG,MaterialDesignToast.TYPE_WARNING).show ();
                }
            }
        });

 /*       if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "申请权限", Toast.LENGTH_SHORT).show();

            // 申请 相机 麦克风权限
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,

                    Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }*/
    }
}
