package com.launcher.hamcl.version;

import com.launcher.hamcl.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalVersionListAdapter extends BaseAdapter {

    private List<LocalVersionListBean> coll;
    private Context ctx;

    List<Boolean> mChecked;

    List<Integer> listItemID;

    public OnItemDepartment listener = null;

    @SuppressLint("UseSparseArrays")
    private Map<Integer, View> viewMap = new HashMap<Integer, View>();

    private LayoutInflater mInflater;

    public LocalVersionListAdapter(Context context, List<LocalVersionListBean> coll ) {

        this.ctx = context;
        this.coll = coll;

        mChecked = new ArrayList<Boolean>();
        for (int i = 0; i < coll.size(); i++) {
            mChecked.add(false);
        }

        listItemID = new ArrayList<Integer>();

        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {

        return coll.size();
    }

    public Object getItem(int arg0) {

        return coll.get(arg0);
    }

    public long getItemId(int arg0) {

        return arg0;
    }

    public int getItemViewType(int arg0) {

        return arg0;
    }

    public View getView(final int arg0, View arg1, ViewGroup arg2) {
        View madapter = (View) this.viewMap.get(arg0);
        final ViewHolder viewh;
        if (madapter == null) {
            madapter = (View) mInflater.inflate(R.layout.adapter_version, null);
            viewh = new ViewHolder();
            viewh.item_version_ll = (LinearLayout)madapter.findViewById(R.id.item_version_ll);
            viewh.rb_choose_version = (RadioButton)madapter.findViewById(R.id.rb_choose_version);
            viewh.iv_version_icon = (ImageView)madapter.findViewById(R.id.iv_version_icon);
            viewh.iv_test_games = (ImageButton) madapter.findViewById(R.id.iv_test_games);
            viewh.menu_setting = (Toolbar)madapter.findViewById(R.id.menu_setting);
            viewh.menu_setting.setId(arg0);
            viewh.tv_version_title = (TextView)madapter.findViewById(R.id.tv_version_title);
            viewh.tv_about_version = (TextView)madapter.findViewById(R.id.tv_about_version);

            viewh.rb_choose_version.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(listItemID.size()>0){
                            mChecked.set(listItemID.get(0),false);//归零
                        }
                        mChecked.set(arg0, true);//选中
                        ClickResult(ctx);
                        notifyDataSetInvalidated();//数据刷新

                    } catch (Exception e) {
                        //Toast(e.toString());
                    }
                }
            });

            viewh.iv_test_games.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
						/*if(!viewh.checkbox.isChecked()){
							setSelectIndex(index);
						}
					}
				});
			if (position == selectedIndex) {
				viewh.checkbox.setChecked(true);
			} else {
				viewh.checkbox.setChecked(false);
			}*/

			/*viewh.layout.setOnClickListener(new LinearLayout.OnClickListener(){
					@Override
					public void onClick(View p1)
					{
						try {
							if(listItemID.size()>0){
								 mChecked.set(listItemID.get(0),false);//归零
							}
							mChecked.set(arg0, true);//选中
							ClickResult(ctx);
							notifyDataSetInvalidated();//数据刷新

						} catch (Exception e) {
							//Toast(e.toString());
						}
					}
				});*/
            if(viewh.menu_setting.getMenu().size()==0){

                viewh.menu_setting.inflateMenu(R.menu.menu_adapter_version);

                Menu menu=viewh.menu_setting.getMenu();
                if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                    try{
                        Method m = menu.getClass().getDeclaredMethod(
                                "setOptionalIconsVisible", Boolean.TYPE);
                        m.setAccessible(true);
                        m.invoke(menu, true);
                    }
                    catch(NoSuchMethodException e){
                        //Log.e(TAG, "onMenuOpened", e);
                    }
                    catch(Exception e){
                        throw new RuntimeException(e);
                    }
                }

                viewh.menu_setting.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_test_the_game://上文ID
                                try{
                                    listener.OnItemDepartmentItem("这是数列:"+viewh.menu_setting.getId()
                                            +"项:");
                                }catch(Exception e){
                                    //过滤错误
                                }
                                //回调接口
                                break;
                            case R.id.item_game_manage://上文ID
                                try{
                                    listener.OnItemDepartmentItem("这是数列:"+viewh.menu_setting.getId()
                                            +"项:");
                                }catch(Exception e){
                                    //过滤错误
                                }
                                //回调接口
                                break;
                            case R.id.item_rename_this_version://上文ID
                                try{
                                    listener.OnItemDepartmentItem("这是数列:"+viewh.menu_setting.getId()
                                            +"项:");
                                }catch(Exception e){
                                    //过滤错误
                                }
                                //回调接口
                                break;
                            case R.id.item_delete_version://上文ID
                                try{
                                    listener.OnItemDepartmentItem("这是数列:"+viewh.menu_setting.getId()
                                            +"项:");
                                }catch(Exception e){
                                    //过滤错误
                                }
                                //回调接口
                                break;
                            case R.id.item_derive_package://上文ID
                                try{
                                    listener.OnItemDepartmentItem("这是数列:"+viewh.menu_setting.getId()
                                            +"项:");
                                }catch(Exception e){
                                    //过滤错误
                                }
                                //回调接口
                                break;
                            case R.id.item_game_folder://上文ID
                                try{
                                    listener.OnItemDepartmentItem("这是数列:"+viewh.menu_setting.getId()
                                            +"项:");
                                }catch(Exception e){
                                    //过滤错误
                                }
                                //回调接口
                                break;
                            //处理逻辑
                        }
                        return true;
                    }
                });
            }

            madapter.setTag(viewh);
        } else {
            viewh = (ViewHolder) madapter.getTag();

        }

        if(mChecked.get(arg0)){

            viewh.rb_choose_version.setChecked(true);

        }else{
            viewh.rb_choose_version.setChecked(false);
        }

        viewh.iv_version_icon.setImageResource(R.drawable.grass);
        viewh.iv_test_games.setImageResource(R.drawable.ic_send_black);
        viewh.tv_version_title.setText(coll.get(arg0).getVersionName ());
        viewh.tv_about_version.setText(coll.get(arg0).getVersionId ());
        //to strong

        viewMap.put(arg0, madapter);
        return madapter;
    }

    class ViewHolder
    {
        public LinearLayout item_version_ll;
        public RadioButton rb_choose_version;
        public ImageView iv_version_icon;
        public ImageButton iv_test_games;
        public Toolbar menu_setting;
        public TextView tv_version_title;
        public TextView tv_about_version;

    }
    public int getViewTypeCount() {

        return coll.size();
    }

    public boolean hasStableIds() {

        return false;
    }

    public boolean isEmpty() {

        return false;
    }

    public boolean areAllItemsEnabled() {

        return false;
    }

    public boolean isEnabled(int arg0) {

        return true;
    }

    public interface OnItemDepartment{
        public abstract void OnItemDepartmentItem(String plot_id,ImageView department_select);

        public abstract void OnItemDepartmentItem(String id);

    }

    public void setOnItemDepartment(OnItemDepartment onItemDepartment) {
        this.listener = onItemDepartment;
    }

    public void ClickResult(Context ctx)
    {
        listItemID.clear();

        for (int i = 0; i < mChecked.size(); i++) {
            if (mChecked.get(i)) {
                listItemID.add(i);
            }
        }
/*** 检测
 if (listItemID.size() == 0) {

 //无
 } else {


 StringBuilder sb = new StringBuilder();

 for (int i = 0; i < listItemID.size(); i++) {
 sb.append(listItemID.get(i) + ". "+coll.get(listItemID.get(i)).get("version").toString()+"\n");

 }
 AlertDialog.Builder builder2 = new AlertDialog.Builder(ctx);
 builder2.setMessage(sb.toString());

 builder2.show();


 }
 */
    }
    public int getChecked(){
        //第多少项选中----未选中却获取，只能是null
        return listItemID.get(0);
    }

}
