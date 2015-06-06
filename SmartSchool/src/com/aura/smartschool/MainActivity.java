package com.aura.smartschool;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.aura.smartschool.Interface.LoginListener;
import com.aura.smartschool.dialog.LoadingDialog;
import com.aura.smartschool.dialog.LoginDialog;
import com.aura.smartschool.dialog.LoginDialogFragment;
import com.aura.smartschool.dialog.RegisterDialog;
import com.aura.smartschool.utils.PreferenceUtil;
import com.aura.smartschool.utils.Util;
import com.aura.smartschool.vo.MemberVO;

public class MainActivity extends Activity {

	private TextView tvTitle;
	private ImageView ivHome;
	
	private LoginDialog mLoginDialog;
	private RegisterDialog mRegisterDialog;
	
	private AQuery mAq;
	
	private FragmentManager mFm;
	private Fragment mFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//액션바 처리
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		View mCustomView = View.inflate(this, R.layout.actionbar, null);
		tvTitle = (TextView) mCustomView.findViewById(R.id.tvTitle);
		ivHome = (ImageView) mCustomView.findViewById(R.id.logo);
		ivHome.setVisibility(View.GONE);
		ivHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		final View actionbar_fl_more = mCustomView.findViewById(R.id.fl_more);
		mCustomView.findViewById(R.id.fl_more).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PopupMenu popupMenu = new PopupMenu(MainActivity.this, actionbar_fl_more);
				if (PreferenceUtil.getInstance(MainActivity.this).isParent()) {
					//popupMenu.getMenu().findItem(R.id.action_usermanage).setVisible(false);
					popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
				} else {
					popupMenu.getMenuInflater().inflate(R.menu.menu_child, popupMenu.getMenu());
				}
			    //popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
			    popupMenu.setOnMenuItemClickListener(mMoreMenuItemClickListener);
			    popupMenu.show();
			}
		});
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
		
		checkLogin();
	}
	
	@Override
	public void onBackPressed() {
		if(mFragment == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Smart School")
				.setMessage("종료하시겠습니까?")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
						if(mLoginDialog != null) {
							mLoginDialog.dismiss();
						}
						if(mRegisterDialog != null) {
							mRegisterDialog.dismiss();
						}
						finish();
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.show();
		} else {
			mFm.beginTransaction().remove(mFragment).commit();
			mFragment = null;
			//hideSubmenu();
		}
	}
	
	private void checkLogin() {
		String id = PreferenceUtil.getInstance(this).getHomeId();
		String mdn = Util.getMdn(this);
		Log.d("LDK", id + "," + mdn);
		
		//usim없는 태블릿은 사용불가
		if(TextUtils.isEmpty(id) || TextUtils.isEmpty(mdn)) {
			showLoginDialog();
			return;
		}
		
		getLogin(new MemberVO(id, mdn));
 	}
	
	private void showLoginDialog(){
		if(mLoginDialog == null){
			mLoginDialog = new LoginDialog(this, mLoginListener);
		}
		
		if(mLoginDialog.isShowing() == false){
			mLoginDialog.show();
		}
	}
	
	private void getLogin(MemberVO member) {
		LoadingDialog.showLoading(this);
		try {
			String url = Constant.HOST + Constant.API_SIGNIN;

			JSONObject json = new JSONObject();
			json.put("home_id", member.home_id);
			json.put("mdn", member.mdn);
			
			Log.d("LDK", "url:" + url);
			Log.d("LDK", "input parameter:" + json.toString(1));
	
			mAq.post(url, json, JSONObject.class, new AjaxCallback<JSONObject>(){
				@Override
				public void callback(String url, JSONObject object, AjaxStatus status) {
					LoadingDialog.hideLoading();
					try {
						Log.d("LDK", "result:" + object.toString(1));
						
						if("0".equals(object.getString("RESULT"))) {
							//
						} else {

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void getRegister(MemberVO member) {
		LoadingDialog.showLoading(this);
		try {
			String url = Constant.HOST + Constant.API_SIGNUP;

			JSONObject json = new JSONObject();
			json.put("home_id", member.home_id);
			json.put("mdn", member.mdn);
			json.put("is_parent", member.is_parent);
			json.put("name", member.name);
			json.put("photo", member.photo);
			json.put("school_name", member.school_name);
			json.put("school_grade", member.school_grade);
			json.put("school_ban", member.school_ban);
			
			Log.d("LDK", "url:" + url);
			Log.d("LDK", "input parameter:" + json.toString(1));
	
			mAq.post(url, json, JSONObject.class, new AjaxCallback<JSONObject>(){
				@Override
				public void callback(String url, JSONObject object, AjaxStatus status) {
					LoadingDialog.hideLoading();
					try {
						Log.d("LDK", "result:" + object.toString(1));
						
						if("0".equals(object.getString("RESULT"))) {
							//
						} else {

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	//more menu
	PopupMenu.OnMenuItemClickListener mMoreMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_settings:

				break;
			}
			
			return true;
		}
	};
	
	LoginListener mLoginListener = new LoginListener() {
		@Override
		public void onLogin(MemberVO member) {
			getLogin(member);
		}

		@Override
		public void gotoRegister() {
			mLoginDialog.dismiss();
			mRegisterDialog = new RegisterDialog(MainActivity.this, mLoginListener);
			mRegisterDialog.show();
		}

		@Override
		public void onRegister(MemberVO member) {
			getRegister(member);
		}
	};
}
