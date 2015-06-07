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
import android.widget.TextView;

import com.aura.smartschool.MainActivity;
import com.aura.smartschool.R;
import com.aura.smartschool.Interface.LoginListener;
import com.aura.smartschool.utils.PreferenceUtil;
import com.aura.smartschool.utils.Util;
import com.aura.smartschool.vo.MemberVO;

public class LoginDialog extends Dialog {
	
	private Button btn_login;
	private TextView tv_register;
	private TextView tv_guest;
	private EditText et_id;
	
	private Context mContext;
	LoginListener mListener;
	
	public LoginDialog(Context context, LoginListener listener) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		mContext = context;
		mListener = listener;
	}
	
	@Override
	public void onBackPressed() {
		((MainActivity)mContext).onBackPressed();;
		return;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.6f;
		getWindow().setAttributes(lpWindow);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		
		setContentView(R.layout.dialog_login);
		
		btn_login = (Button)findViewById(R.id.btn_login);
		tv_register = (TextView)findViewById(R.id.tv_register);
		tv_guest = (TextView)findViewById(R.id.tv_guest);
		
		btn_login.setOnClickListener(mClick);
		tv_register.setOnClickListener(mClick);
		tv_guest.setOnClickListener(mClick);
		
		et_id = (EditText)findViewById(R.id.et_id);

	}

	View.OnClickListener mClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_login:
				
				String id = et_id.getText().toString();
				if(TextUtils.isEmpty(id)){
					Util.showToast(mContext, "입력되지 않은 항목이 있습니다.");
					return;
				}
				
				//로그인 버튼 클릭시 id는 항상 저장
				PreferenceUtil.getInstance(mContext).putHomeId(id);
				
				MemberVO member = new MemberVO();
				member.home_id = et_id.getText().toString();
				member.mdn = et_id.getText().toString();
				
				//로그인 처리
				mListener.onLogin(member);
				break;

			case R.id.tv_register:
				mListener.gotoRegister();
				break;
				
			case R.id.tv_guest:
				
				break;
				
			default:
				break;
			}
		}
	};

}