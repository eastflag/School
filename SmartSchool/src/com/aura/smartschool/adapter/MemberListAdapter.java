package com.aura.smartschool.adapter;

import java.util.ArrayList;

import com.aura.smartschool.R;
import com.aura.smartschool.Interface.MemberListListener;
import com.aura.smartschool.vo.MemberVO;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MemberListAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<MemberVO> mMemberList;
	private MemberListListener mMemberListListener;
	
	public MemberListAdapter(Context context, ArrayList<MemberVO> memberList, MemberListListener memberListListener) {
		mContext = context;
		mMemberList = memberList;
		mMemberListListener = memberListListener;
	}
	
	public void setData(ArrayList<MemberVO> memberList) {
		mMemberList = memberList;
	}

	@Override
	public int getCount() {
		return mMemberList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.adapter_member_list, null);
			holder.iv_user_image = (ImageView) convertView.findViewById(R.id.iv_user_image);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvRelation = (TextView) convertView.findViewById(R.id.tvRelation);
			holder.tvMdn = (TextView) convertView.findViewById(R.id.tvMdn);
			holder.btnModify = (Button) convertView.findViewById(R.id.btnModify);
			holder.btnView = (Button) convertView.findViewById(R.id.btnView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvName.setText(mMemberList.get(position).name);
		holder.tvRelation.setText(mMemberList.get(position).relation);
		holder.tvMdn.setText(mMemberList.get(position).mdn);
		
		String userImage = mMemberList.get(position).photo;
		if(!TextUtils.isEmpty(userImage)){
			holder.iv_user_image.setImageBitmap(decodeEncodeString(userImage));
		}else{
			holder.iv_user_image.setImageBitmap(null);
		}
		
		holder.btnModify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		
		holder.btnView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mMemberListListener.onSelected(position);
			}
		});
		
		return convertView;
	}
	
	public Bitmap decodeEncodeString(String encodedImage){
		byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
		
		return decodedByte;
	}

	class ViewHolder {
		ImageView iv_user_image;
		TextView tvName;
		TextView tvRelation;
		TextView tvMdn;
		Button btnModify;
		Button btnView;
	}
}
