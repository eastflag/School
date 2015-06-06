package com.aura.smartschool.dialog;

import com.aura.smartschool.R;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

public class LoadingDialog {
	private static Dialog mLoadingDialog = null;
	public static void showLoading(Context context){
		if(mLoadingDialog == null){
			mLoadingDialog = new Dialog(context, R.style.TransDialog);
			ProgressBar pb = new ProgressBar(context);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			mLoadingDialog.addContentView(pb, params);
			mLoadingDialog.setCancelable(false);
		}
		if(!mLoadingDialog.isShowing()) {
			mLoadingDialog.show();
		}
	}
	
	public static void hideLoading(){
		if(mLoadingDialog != null){
			mLoadingDialog.dismiss();
			mLoadingDialog = null;
		}
	}
}
