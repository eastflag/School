package com.aura.smartschool;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.aura.smartschool.Interface.LoginListener;
import com.aura.smartschool.adapter.MemberListAdapter;
import com.aura.smartschool.dialog.LoadingDialog;
import com.aura.smartschool.dialog.LoginDialog;
import com.aura.smartschool.dialog.RegisterDialogActivity;
import com.aura.smartschool.utils.PreferenceUtil;
import com.aura.smartschool.utils.Util;
import com.aura.smartschool.vo.MemberVO;

public class MainActivity extends Activity {
	private static final int REQ_DIALOG_SIGNUP = 0;
	
	private TextView tvTitle;
	private ImageView ivHome;
	
	private LoginDialog mLoginDialog;
	//private RegisterDialog mRegisterDialog;
	
	private AQuery mAq;
	
	private FragmentManager mFm;
	private Fragment mFragment;
	
	private ArrayList<MemberVO> mMemberList = new ArrayList<MemberVO>();
	private ListView mListView;
	private MemberListAdapter mAdapter;

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
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case REQ_DIALOG_SIGNUP:
            if (resultCode == RESULT_OK) {
            	getMemberList();
            }
            break;
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
						
						if(status.getCode() != 200) {
							
							return;
						}
						
						if("0".equals(object.getString("result"))) {
							mLoginDialog.dismiss();
							JSONArray array = object.getJSONArray("data");
							displayMemberList(array);
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
	
	private void getMemberList() {
		LoadingDialog.showLoading(this);
		try {
			String url = Constant.HOST + Constant.API_GET_MEMBERLIST;

			JSONObject json = new JSONObject();
			json.put("home_id", PreferenceUtil.getInstance(MainActivity.this).getHomeId());
			
			Log.d("LDK", "url:" + url);
			Log.d("LDK", "input parameter:" + json.toString(1));
	
			mAq.post(url, json, JSONObject.class, new AjaxCallback<JSONObject>(){
				@Override
				public void callback(String url, JSONObject object, AjaxStatus status) {
					LoadingDialog.hideLoading();
					try {
						Log.d("LDK", "result:" + object.toString(1));
						
						if(status.getCode() != 200) {
							
							return;
						}
						
						if("0".equals(object.getString("result"))) {
							JSONArray array = object.getJSONArray("data");
							displayMemberList(array);
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
	
	private void displayMemberList(JSONArray array) throws JSONException {
		for(int i=0; i < array.length(); ++i) {
			JSONObject json = array.getJSONObject(i);
			MemberVO member = new MemberVO();
			member.home_id = json.getString("home_id");
			member.member_id = json.getInt("member_id");
			member.mdn = json.getString("mdn");
			member.is_parent = json.getInt("is_parent");
			member.name = json.getString("name");
			member.relation = json.getString("relation");
			member.photo = json.getString("photo");
			member.school_name = json.getString("school_name");
			member.school_grade = json.getString("school_grade");
			member.school_ban = json.getString("school_ban");
			mMemberList.add(member);
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
			//mLoginDialog.dismiss();
			//mRegisterDialog = new RegisterDialog(MainActivity.this, mLoginListener);
			//mRegisterDialog.show();
			Intent intent = new Intent(MainActivity.this, RegisterDialogActivity.class);
			startActivityForResult(intent, REQ_DIALOG_SIGNUP);
		}

		@Override
		public void onRegister(MemberVO member) {
			//getRegister(member);
		}
	};
}
