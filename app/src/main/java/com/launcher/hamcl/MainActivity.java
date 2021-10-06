package com.launcher.hamcl;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.launcher.hamcl.setting.SettingManager;
import com.launcher.hamcl.setting.model.ConfigModel;
import com.launcher.hamcl.setting.model.SettingModel;
import com.launcher.hamcl.uis.functionbar.FunctionbarFragment;
import com.launcher.hamcl.uis.functionbar.UserFunctionbarFragment;
import com.launcher.hamcl.uis.functionbar.VersionListFunctionbarFragment;
import com.launcher.hamcl.uis.homepage.GamesPathFragment;
import com.launcher.hamcl.uis.homepage.LauncherSettingFragment;
import com.launcher.hamcl.uis.homepage.LibraryFragment;
import com.launcher.hamcl.uis.homepage.StartFragment;
import com.launcher.hamcl.uis.homepage.UserFragment;
import com.launcher.hamcl.uis.homepage.VersionListFragment;
import com.launcher.hamcl.uis.homepage.seconduis.DownloadGamesFragment;
import com.launcher.hamcl.widget.MaterialDesignToast;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.HashMap;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Thread.UncaughtExceptionHandler {

    private static final int REQUEST_PERMISSION_CODE = 0;
    private RxPermissions rxPermissions;

    public SettingManager settingManager;
    public SettingModel settingModel;
    public ConfigModel configModel;

    private ImageButton toolbar_button_backspace;
    private TextView main_text_showstate;
    private ImageButton toolbar_button_backhome;
    private ImageButton toolbar_button_refresh;

    private FrameLayout functionbar_fl;
    private FunctionbarFragment fb_fragment;
    private UserFunctionbarFragment fb_user_fragment;
    private VersionListFunctionbarFragment fb_version_list_fragment;

    private FragmentTransaction FunctionbarTransaction;//fragment事务
    private FragmentManager FunctionbarManager;//fragment管理者

    private HashMap<Integer, Integer> FunctionbarUIMap;
    public int Functionbarline;

    private FrameLayout homepage_fl;
    private StartFragment start_fragment;
    private UserFragment user_fragment;
    private VersionListFragment games_list_fragment;
    private DownloadGamesFragment download_games_fragment;
    private GamesPathFragment games_path_fragment;
    private LibraryFragment library_fragment;
    private LauncherSettingFragment launcher_setting_fragment;

    private FragmentTransaction HomepageTransaction;//fragment事务
    private FragmentManager HomepagerManager;//fragment管理者

    private HashMap<Integer, Integer> HomepageUIMap;
    public int Homepageline;
    private HashMap<Integer, String> ToolbarStringMap;//fragment管理者

    private static MainActivity INSTANCE;

    public static MainActivity getInstance () {
        if (INSTANCE == null) {
            INSTANCE = new MainActivity ();
        }
        return INSTANCE;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById (R.id.toolbar);
        getSupportActionBar ();

        rxPermissions = new RxPermissions (this);

        requestPermission ();
        initSetting ();
        getSettingFile ();
        saveSetting ();
        initView ();
        //setFunctionbarClick(0);
        FunctionbarMap ();
        HomepageMap ();
    }

    private void initView () {
        toolbar_button_backspace = (ImageButton) findViewById (R.id.toolbar_button_backspace);
        main_text_showstate = (TextView) findViewById (R.id.main_text_showstate);
        toolbar_button_backhome = (ImageButton) findViewById (R.id.toolbar_button_backhome);
        toolbar_button_refresh = (ImageButton) findViewById (R.id.toolbar_button_refresh);

        toolbar_button_backspace.setOnClickListener (this);
        toolbar_button_backhome.setOnClickListener (this);
        toolbar_button_refresh.setOnClickListener (this);

        functionbar_fl = (FrameLayout) findViewById (R.id.functionbar_fl);
        homepage_fl = (FrameLayout) findViewById (R.id.homepage_fl);
    }

    private void initSetting () {
        settingManager = new SettingManager (this);
        settingModel = new SettingModel ();
        configModel = new ConfigModel ();
    }

    private void getSettingFile () {
        configModel = settingManager.getConfigFromFile (configModel);
    }

    private void saveSetting () {
        settingManager.saveConfigToFile (configModel);
    }

    private void FunctionbarMap () {
        FunctionbarUIMap = new HashMap<Integer, Integer> ();
        FunctionbarUIMap.put (0, 0);
        FunctionbarUIMap.put (1, 0);
        FunctionbarUIMap.put (3, 0);

        Functionbarline = 0;
        setFunctionbarClick (Homepageline);
    }

    private void HomepageMap () {
        HomepageUIMap = new HashMap<Integer, Integer> ();
        HomepageUIMap.put (0, 0);
        HomepageUIMap.put (1, 0);
        HomepageUIMap.put (2, 0);
        HomepageUIMap.put (3, 0);
        HomepageUIMap.put (4, 0);
        HomepageUIMap.put (5, 0);
        HomepageUIMap.put (6, 0);
        //map.put(目前值,上一级值);
        HomepageUIMap.put (201, 3);
        HomepageUIMap.put (203, 2);

        ToolbarStringMap = new HashMap<Integer, String> ();
        ToolbarStringMap.put (0, getString (R.string.home_title_homepage));
        ToolbarStringMap.put (1, getString (R.string.fb_user_manage));
        ToolbarStringMap.put (2, getString (R.string.fb_game_manage));
        ToolbarStringMap.put (3, getString (R.string.fb_version_list));
        ToolbarStringMap.put (4, getString (R.string.fb_download));
        ToolbarStringMap.put (5, getString (R.string.fb_library_manage));
        ToolbarStringMap.put (6, getString (R.string.fb_launcher_settings));
        ToolbarStringMap.put (201, getString (R.string.home_title_download_games));
        ToolbarStringMap.put (203, getString (R.string.home_title_game_setting));

        Homepageline = 0;
        setHomepageClick (Homepageline);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.toolbar_button_backspace:
                setFunctionbarClick (FunctionbarUIMap.get (Functionbarline));
                Functionbarline = FunctionbarUIMap.get (Functionbarline);

                setHomepageClick (HomepageUIMap.get (Homepageline));
                Homepageline = HomepageUIMap.get (Homepageline);
                break;
            case R.id.toolbar_button_backhome:
                setFunctionbarClick (0);
                setHomepageClick (0);
                break;
            case R.id.toolbar_button_refresh:
                refresh ();
                break;
        }
    }

    public void refresh () {
        finish ();
        Intent intent = new Intent (this, MainActivity.class);
        startActivity (intent);
    }

    public void setFunctionbarClick (int type) {
        //开启事务
        FunctionbarManager = getSupportFragmentManager ();
        //获取到fragment的管理对象
        FunctionbarTransaction = FunctionbarManager.beginTransaction ();
        //加载切换动画
        FunctionbarTransaction.setCustomAnimations (R.anim.layout_show, R.anim.layout_hide);
        //显示之前将所有的fragment都隐藏起来,在去显示我们想要显示的fragment
        hideFunctionbarFragment (FunctionbarTransaction);
        switch (type) {
            case 0://左侧功能栏
                //如果左侧功能栏的fragment是null的话,就创建一个
                if (fb_fragment == null) {
                    fb_fragment = new FunctionbarFragment ();
                    //加入事务
                    FunctionbarTransaction.add (R.id.functionbar_fl, fb_fragment);
                } else {
                    //如果左侧功能栏fragment不为空就显示出来
                    FunctionbarTransaction.show (
                            fb_fragment);
                }
                break;
            case 1://左侧功能栏
                //如果左侧功能栏的fragment是null的话,就创建一个
                if (fb_user_fragment == null) {
                    fb_user_fragment = new UserFunctionbarFragment ();
                    //加入事务
                    FunctionbarTransaction.add (R.id.functionbar_fl, fb_user_fragment);
                } else {
                    //如果左侧功能栏fragment不为空就显示出来
                    FunctionbarTransaction.show (
                            fb_user_fragment);
                }
                break;
            case 3://左侧功能栏
                //如果左侧功能栏的fragment是null的话,就创建一个
                if (fb_version_list_fragment == null) {
                    fb_version_list_fragment = new VersionListFunctionbarFragment ();
                    //加入事务
                    FunctionbarTransaction.add (R.id.functionbar_fl, fb_version_list_fragment);
                } else {
                    //如果左侧功能栏fragment不为空就显示出来
                    FunctionbarTransaction.show (
                            fb_version_list_fragment);
                }
                break;
        }
        //提交事务
        FunctionbarTransaction.commit ();
    }

    /**
     * 用来隐藏functionbar_fragment的方法
     *
     * @param；FunctionbarTransaction
     */
    public void hideFunctionbarFragment (FragmentTransaction fragmentTransaction) {
        //如果此fragment不为空的话就隐藏起来
        if (fb_fragment != null) {
            fragmentTransaction.hide (fb_fragment);
        }
        if (fb_user_fragment != null) {
            fragmentTransaction.hide (fb_user_fragment);
        }
        if (fb_version_list_fragment != null) {
            fragmentTransaction.hide (fb_version_list_fragment);
        }
    }

    public void setHomepageClick (int type) {
        //开启事务
        HomepagerManager = getSupportFragmentManager ();
        //获取到fragment的管理对象
        HomepageTransaction = HomepagerManager.beginTransaction ();
        //加载切换动画
        HomepageTransaction.setCustomAnimations (R.anim.layout_show, R.anim.layout_hide);
        //显示之前将所有的fragment都隐藏起来,在去显示我们想要显示的fragment
        hideHomepageFragment (HomepageTransaction);
        setToolbarTitle (ToolbarStringMap.get (type));
        switch (type) {
            case 0://主页
                //如果主页的fragment是null的话,就创建一个
                if (start_fragment == null) {
                    start_fragment = new StartFragment ();
                    //加入事务
                    HomepageTransaction.add (R.id.homepage_fl, start_fragment);
                } else {
                    //如果主页fragment不为空就显示出来
                    HomepageTransaction.show (start_fragment);
                }
                break;
            case 1:
                if (user_fragment == null) {
                    user_fragment = new UserFragment ();
                    //加入事务
                    HomepageTransaction.add (R.id.homepage_fl, user_fragment);
                } else {
                    //如果用户fragment不为空就显示出来
                    HomepageTransaction.show (user_fragment);
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
            case 3:
                if (games_list_fragment == null) {
                    games_list_fragment = new VersionListFragment ();
                    //加入事务
                    HomepageTransaction.add (R.id.homepage_fl, games_list_fragment);
                } else {
                    //如果用户fragment不为空就显示出来
                    HomepageTransaction.show (games_list_fragment);
                }
                break;
            case 4:
                if (games_path_fragment == null) {
                    games_path_fragment = new GamesPathFragment ();
                    //加入事务
                    HomepageTransaction.add (R.id.homepage_fl, games_path_fragment);
                } else {
                    //如果用户fragment不为空就显示出来
                    HomepageTransaction.show (games_path_fragment);
                }
                break;
            case 5:
                if (library_fragment == null) {
                    library_fragment = new LibraryFragment ();
                    //加入事务
                    HomepageTransaction.add (R.id.homepage_fl, library_fragment);
                } else {
                    //如果核心安装fragment不为空就显示出来
                    HomepageTransaction.show (library_fragment);
                }
                break;
            case 6:
                if (launcher_setting_fragment == null) {
                    launcher_setting_fragment = new LauncherSettingFragment ();
                    //加入事务
                    HomepageTransaction.add (R.id.homepage_fl, launcher_setting_fragment);
                } else {
                    //如果用户fragment不为空就显示出来
                    HomepageTransaction.show (launcher_setting_fragment);
                }
                break;
            case 201:
                if (download_games_fragment == null) {
                    download_games_fragment = new DownloadGamesFragment ();
                    //加入事务
                    HomepageTransaction.add (R.id.homepage_fl, download_games_fragment);
                } else {
                    //如果主页fragment不为空就显示出来
                    HomepageTransaction.show (download_games_fragment);
                }
                break;
			/*case 202:
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
        HomepageTransaction.commit ();
    }

    /**
     * 用来隐藏fragment的方法
     *
     * @param fragmentTransaction
     */
    public void hideHomepageFragment (FragmentTransaction fragmentTransaction) {
        //如果此fragment不为空的话就隐藏起来
        if (start_fragment != null) {
            fragmentTransaction.hide (start_fragment);
        }
        if (user_fragment != null) {
            fragmentTransaction.hide (user_fragment);
        }
        if (games_list_fragment != null) {
            fragmentTransaction.hide (games_list_fragment);
        }
		/*if (games_fragment != null) {
            fragmentTransaction.hide(games_fragment);
        }*/
        if (download_games_fragment != null) {
            fragmentTransaction.hide (download_games_fragment);
        }
		/*if (install_package_fragment != null) {
            fragmentTransaction.hide(install_package_fragment);
        }
		if (games_setting_fragment != null) {
            fragmentTransaction.hide(games_setting_fragment);
        }*/
        if (library_fragment != null) {
            fragmentTransaction.hide (library_fragment);
        }
        if (games_path_fragment != null) {
            fragmentTransaction.hide (games_path_fragment);
        }
        if (launcher_setting_fragment != null) {
            fragmentTransaction.hide (launcher_setting_fragment);
        }
    }

    public void setToolbarTitle (String s) {
        main_text_showstate.setText (s);
    }

    /**
     * 【当Activity销毁时】
     **/
    @Override
    public void onDestroy () {
        super.onDestroy ();
        getSettingFile ();
        saveSetting ();
    }

    public void requestPermission () {

int i = 444;
int[] g = {5};
int hh = g[i];
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET};

        rxPermissions.requestEach (permissions).subscribe (new Consumer<Permission> () {
            @SuppressLint("CheckResult")
            @Override
            public void accept (Permission permission) throws Exception {
                if (permission.granted) {

                } else if (permission.shouldShowRequestPermissionRationale) {
                    MaterialDesignToast.makeText (MainActivity.this, "没有必要权限无法运行，请给予HAMCL权限", Toast.LENGTH_LONG, MaterialDesignToast.TYPE_WARNING).show ();
                    requestPermission ();
                } else {
                    MaterialDesignToast.makeText (MainActivity.this, "没有必要权限无法运行，请在设置中手动给予HAMCL权限", Toast.LENGTH_LONG, MaterialDesignToast.TYPE_WARNING).show ();
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 适配android11读写权限
            if (Environment.isExternalStorageManager ()) {
                //已获取android读写权限
            } else {
                MaterialDesignToast.makeText (this, "请给予管理全部文件的权限，否则无法运行", Toast.LENGTH_LONG, MaterialDesignToast.TYPE_INFO).show ();
                Intent intent = new Intent (Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData (Uri.parse ("package:" + getPackageName ()));
                startActivityForResult (intent, REQUEST_PERMISSION_CODE);
            }
            return;
        }


 /*       if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "申请权限", Toast.LENGTH_SHORT).show();

            // 申请 相机 麦克风权限
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,

                    Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }*/
    }

    @Override
    public void uncaughtException (@NonNull Thread thread, @NonNull Throwable throwable) {

    }

    public static void jump (Intent i) {

    }
}
