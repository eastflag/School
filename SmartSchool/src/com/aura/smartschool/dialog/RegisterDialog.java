package com.aura.smartschool.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.aura.smartschool.MainActivity;
import com.aura.smartschool.R;
import com.aura.smartschool.Interface.LoginListener;
import com.aura.smartschool.utils.PreferenceUtil;
import com.aura.smartschool.utils.Util;
import com.aura.smartschool.vo.MemberVO;

public class RegisterDialog extends Dialog {
	
private Context mContext;
	
	LoginListener mListener;
	
	EditText et_id, et_name, et_mdn;
	Button btn_register;

	public RegisterDialog(Context context, LoginListener listener) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		mContext = context;
		mListener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.7f;
		getWindow().setAttributes(lpWindow);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		setContentView(R.layout.dialog_register);

		et_id = (EditText) findViewById(R.id.et_id);
		et_name = (EditText) findViewById(R.id.et_name);
		et_mdn = (EditText) findViewById(R.id.et_mdn);
		btn_register = (Button) findViewById(R.id.btn_register);
		
		et_mdn.setText(Util.getMdn(mContext));
		
		btn_register.setOnClickListener(mClick);
	}
	
	@Override
	public void onBackPressed() {
		this.dismiss();
		((MainActivity)mContext).onBackPressed();;
		return;
	}
	
	View.OnClickListener mClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_register:
				String id = et_id.getText().toString();
				if(TextUtils.isEmpty(id)){
					Util.showToast(mContext, "입력되지 않은 항목이 있습니다.");
					return;
				}
				
				PreferenceUtil.getInstance(mContext).putHomeId(id);
				
				MemberVO member = new MemberVO();
				member.home_id = et_id.getText().toString();
				member.name = et_name.getText().toString();
				member.mdn = et_id.getText().toString();
				member.is_parent = 1;
				
				mListener.onRegister(member);
				
				break;
				
			default:
				break;
			}
		}
	};
}
