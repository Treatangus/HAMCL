package com.launcher.hamcl.adapter;

import com.launcher.hamcl.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.launcher.hamcl.download.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterListVersionLibrary extends BaseAdapter {

	protected final Context mContext;
	protected final LayoutInflater mInflater;
	private List<com.launcher.hamcl.download.util.VersionUtil> overall;
	private List<VersionUtil>release;
	private List<VersionUtil>snapshot;
	private List<VersionUtil>old_alpha;
	private List<VersionUtil> display;
	private Map<Integer, RelativeLayout> viewMap = new HashMap<Integer, RelativeLayout>();
	public OnItemDepartment listener = null;
	public AdapterListVersionLibrary(Context context,List<VersionUtil> overall ,int type){
		mContext = context;

		this.overall = overall;
		display=new ArrayList<VersionUtil>();

		release=new ArrayList<>();
		old_alpha=new ArrayList<>();
		snapshot=new ArrayList<>();

		for(VersionUtil util:overall){
			if(util.type().equals("release")){
				release.add(util);
			}else if(util.type().equals("snapshot")){
				snapshot.add(util);
			}else if(util.type().equals("old_alpha")){
				old_alpha.add(util);
			}
		}
		if(type==0){
			display= snapshot;
		}else if(type==1){
			display= release;
		}else if(type==2){
			display= old_alpha;
		}
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public void setType(int type){
		if(type==0){
			display= snapshot;
		}else if(type==1){
			display= release;
		}else if(type==2){
			display= old_alpha;
		}

		notifyDataSetInvalidated();

	}
	public int getCount() {
		return getList().size();
	}

	public Object getItem(int position) {
		return getList().get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	public boolean hasStableIds() {

		return false;
	}
	public boolean isEmpty() {

		return false;
	}
	public int getItemViewType(int arg0) {

		return arg0;
	}
	public boolean areAllItemsEnabled() {

		return false;
	}
	public boolean isEnabled(int arg0) {

		return true;
	}
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		final VersionUtil file = getList().get(arg0);
		String id=file.id().toLowerCase();
		String type=file.type().toLowerCase();
		RelativeLayout madapter = this.viewMap.get(arg0);
		final ViewHolder viewh;
		if (madapter == null) {
			madapter = (RelativeLayout) mInflater.inflate(R.layout.adapter_version_library, null);
			viewh = new ViewHolder();
			//viewh.item=(Toolbar)madapter.findViewById(R.id.versionitemToolbar1);
			viewh.rlAll=(RelativeLayout)madapter.findViewById(R.id.rl_all);
			viewh.tvId=(TextView)madapter.findViewById(R.id.tv_id);
			viewh.tvType=(TextView)madapter.findViewById(R.id.tv_type);
			viewh.ivOther=(ImageView)madapter.findViewById(R.id.iv_other);
			viewh.rlAll.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View p1)
					{
						listener.OnItemDepartmentItem(
							(display.get(arg0)).id()
							,( display.get(arg0)).url());
					}
				});
			viewh.ivOther.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View p1)
					{
						/*Intent i=new Intent(mContext ,DownloadService.class);
						mContext.startService(i);*/
						Toast.makeText(mContext, arg0 + "" , Toast.LENGTH_SHORT).show();
					}
				});
			madapter.setTag(viewh);
		} else {
			viewh = (ViewHolder) madapter.getTag();
		}
		//viewh.item.setTitle(id);
		//viewh.item.setSubtitle(type);
		viewh.tvId.setText(id);
		viewh.tvType.setText(type);
		viewMap.put(arg0, madapter);
		return madapter;
	}
	class ViewHolder
	{
		//public Toolbar item;
		public RelativeLayout rlAll;
		public TextView tvId;
		public TextView tvType;
		public ImageView ivOther;

	}
	protected List<VersionUtil> getList() {
		return display;
	}
	public interface OnItemDepartment{
		public abstract void OnItemDepartmentItem(String type);
		public abstract void OnItemDepartmentItem(String id,String url);


	}

	public void setOnItemDepartment(OnItemDepartment onItemDepartment) {
		this.listener = onItemDepartment;
	}

}

