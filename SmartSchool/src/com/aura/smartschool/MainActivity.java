package com.aura.smartschool;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.aura.smartschool.utils.PreferenceUtil;

public class MainActivity extends Activity {

	private TextView tvTitle;
	private ImageView ivHome;

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
}
