package com.launcher.hamcl.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.launcher.hamcl.R;
import com.zuowu.utils.UserDataBaseBox;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListAdapter extends BaseAdapter {
    private List<UserListBean> beanList;
    private Context context;

    private List<Boolean> allChecked;
    private Map<Integer, View> viewMap = new HashMap<Integer, View> ();

    public UserListAdapter(Context context,List<UserListBean> beans){
        this.context = context;
        this.beanList = beans;
        allChecked = new ArrayList<Boolean> ();
        for (UserListBean bean : beanList){
            allChecked.add (false);
        }
    }


    @Override
    public int getCount () {
        return beanList.size ();
    }

    @Override
    public Object getItem (int i) {
        return  beanList.get (i);
    }

    @Override
    public long getItemId (int i) {
        return i;
    }

    @Override
    public View getView (int i, View view, ViewGroup viewGroup) {
        View v = viewMap.get (i);
        ViewHolder viewHolder;
        if (v == null){
            v  = LayoutInflater.from (context).inflate (R.layout.adapter_user,null);
            viewHolder = new ViewHolder ();
            viewHolder.chooseUser = v.findViewById (R.id.rb_choose_user);
            viewHolder.userName = v.findViewById (R.id.tv_username_title);
            viewHolder.userType = v.findViewById (R.id.tv_usertype);
            viewHolder.deleteButton = v.findViewById (R.id.delete_user_btn);


            viewHolder.chooseUser.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    allChecked.set (i,true);
                    for (int fi = 0;fi < allChecked.size ();fi++){
                        if (fi!=i){
                            allChecked.set (fi,false);
                        }
                    }
                    notifyDataSetChanged ();
                }
            });
            v.setTag (viewHolder);
        }else {
            viewHolder = (ViewHolder) v.getTag ();
        }
        viewHolder.userName.setText (beanList.get (i).getUserNmae ());
        int type = beanList.get (i).getUserType ();
        String showType;
        switch (type){
            case 0:
                showType = "离线模式";
                break;
            case 1:
                showType = "Mojang登录";
                break;
            case 2:
                showType = "微软登录";
                break;
            default:
                showType = "离线";
                // throw new IllegalStateException ("Unexpected value: " + type);
        }
        viewHolder.userType.setText (showType);

        viewHolder.deleteButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                File file = new File ("/sdcard/games/com.explore.launcher/users/"+beanList.get (i).getUserNmae ()+".msu");
                file.delete ();
                notifyDataSetChanged ();
            }
        });



        if (allChecked.get (i)){
            viewHolder.chooseUser.setChecked (true);
        }else {
            viewHolder.chooseUser.setChecked (false);
        }
        viewMap.put (i,v);
       // View v  = LayoutInflater.from (context).inflate (R.layout.adapter_user,null);

        return v;
    }
    class ViewHolder{
        RadioButton chooseUser;
        TextView userName;
        TextView userType;
        MaterialButton deleteButton;
    }

    @Override
    public void notifyDataSetChanged () {
        beanList  = UserDataBaseBox.getInstance ().getUsers ("/sdcard/games/com.explore.launcher/users/");
        allChecked = new ArrayList<Boolean> ();
        for (UserListBean bean : beanList){
            allChecked.add (false);
        }
        super.notifyDataSetChanged ();
    }
}
