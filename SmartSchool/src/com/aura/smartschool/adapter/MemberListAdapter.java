package com.aura.smartschool.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MemberListAdapter extends BaseAdapter {

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/*final ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.more_lifestyle_fragment_list, null);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkBox);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.btnExecute = (Button) convertView.findViewById(R.id.btnExecute);
			holder.btnModify = (Button) convertView.findViewById(R.id.btnModify);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvName.setText(mModeList.get(position).name);
		
		holder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mModeList.get(position).isChecked = isChecked;
			}
		});
		
		holder.btnModify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onModify(position);
			}
		});
		
		holder.btnExecute.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(mContext).setTitle(mModeList.get(position).name)
				.setMessage("실행하시겠습니까?")
				.setNegativeButton("취소", null)
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mListener.onExecute(position);
					}
				})
				.create().show();
				
			}
		});*/
		return null;
	}

}
