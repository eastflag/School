package com.aura.smartschool;

import java.lang.reflect.Member;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.aura.smartschool.Interface.LoginListener;
import com.aura.smartschool.Interface.MemberListListener;
import com.aura.smartschool.adapter.MemberListAdapter;
import com.aura.smartschool.dialog.LoadingDialog;
import com.aura.smartschool.dialog.LoginDialog;
import com.aura.smartschool.dialog.MemberSaveDialogActivity;
import com.aura.smartschool.dialog.RegisterDialogActivity;
import com.aura.smartschool.fragment.MainFragment;
import com.aura.smartschool.utils.PreferenceUtil;
import com.aura.smartschool.utils.Util;
import com.aura.smartschool.vo.MemberVO;

public class MainActivity extends Activity {
	public static final int REQ_DIALOG_SIGNUP = 100;
	public static final int REQ_DIALOG_MEMBER_UPDATE = 101;
	public static final int REQ_DIALOG_MEMBER_ADD = 102;
	public static final int REQ_CODE_PICK_IMAGE = 200;
	public static final String TEMP_PHOTO_FILE = "temp.jpg";       // 임시 저장파일
	public static final int MOD_ADD = 0;
	public static final int MOD_UPDATE = 1;
	
	private TextView tvTitle;
	private ImageView ivHome;
	private LinearLayout mainMenu;
	private TextView tvAddMember;
	
	private LoginDialog mLoginDialog;
	
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
		
		mAq = new AQuery(this);
		mFm = getFragmentManager();
		mListView = (ListView) findViewById(R.id.listview);
		mAdapter = new MemberListAdapter(this, mMemberList, mMemberListListener);
		mListView.setAdapter(mAdapter);
		mainMenu = (LinearLayout) findViewById(R.id.main);
		tvAddMember = (TextView) findViewById(R.id.tvAddMember);
		
		tvAddMember.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MemberVO member = new MemberVO();
				member.home_id = PreferenceUtil.getInstance(MainActivity.this).getHomeId();
				
				Intent intent = new Intent(MainActivity.this, MemberSaveDialogActivity.class);
				intent.putExtra("mode", MOD_ADD);
				intent.putExtra("member", member);
				startActivityForResult(intent, REQ_DIALOG_MEMBER_ADD);
			}
		});
		
		//액션바 처리
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		View mCustomView = View.inflate(this, R.layout.actionbar, null);
		tvTitle = (TextView) mCustomView.findViewById(R.id.tvTitle);
		ivHome = (ImageView) mCustomView.findViewById(R.id.logo);
		ivHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mFragment != null) {
					showMainMenu();
				}
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
			showMainMenu();
		}
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case REQ_DIALOG_SIGNUP:
        case REQ_DIALOG_MEMBER_UPDATE:
        case REQ_DIALOG_MEMBER_ADD:
            if (resultCode == RESULT_OK) {
            	getMemberList();
            }
            break;
        }
    }
    
    private void showSubMenu() {
    	ivHome.setImageResource(R.drawable.btn_pre);
    	tvTitle.setText(PreferenceUtil.getInstance(this).getName());
    	mainMenu.setVisibility(View.GONE);
    }
    
    private void showSubMenu(String name) {
    	tvTitle.setText(name);
    	showSubMenu();
    }
    
    private void showMainMenu() {
    	ivHome.setImageResource(R.drawable.home);
    	tvTitle.setText(PreferenceUtil.getInstance(this).getHomeId());
    	mainMenu.setVisibility(View.VISIBLE);
    	
    	if(mFragment != null) {
	    	mFm.beginTransaction().remove(mFragment).commit();
			mFragment = null;
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
	
	private void hideLoginDialog() {
		if(mLoginDialog != null) {
			mLoginDialog.dismiss();
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
						if(status.getCode() != 200) {
							
							return;
						}
						
						Log.d("LDK", "result:" + object.toString(1));
						
						if("0".equals(object.getString("result"))) {
							hideLoginDialog();
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
		mMemberList.clear();
		
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
			//자기 정보 저장
			if(Util.getMdn(this).equals(member.mdn)) {
				PreferenceUtil.getInstance(this).putHomeId(member.home_id);
				PreferenceUtil.getInstance(this).putParent(member.is_parent==1 ? true:false );
				PreferenceUtil.getInstance(this).putName(member.name);
			}
		}
		
		mAdapter.setData(mMemberList);
		mAdapter.notifyDataSetChanged();
		showMainMenu();
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
	
	MemberListListener mMemberListListener = new MemberListListener() {
		@Override
		public void onSelected(int position) {
			mFragment = new MainFragment(mMemberList.get(position));
			mFm.beginTransaction().replace(R.id.container, mFragment).commit();
			showSubMenu(mMemberList.get(position).name);
		}

		@Override
		public void onUpdateClicked(int position) {
			Intent intent = new Intent(MainActivity.this, MemberSaveDialogActivity.class);
			intent.putExtra("mode", MOD_UPDATE);
			intent.putExtra("member", mMemberList.get(position));
			startActivityForResult(intent, REQ_DIALOG_MEMBER_UPDATE);
		}

		@Override
		public void onAddClicked(MemberVO member) {
			
		}
	};
}
