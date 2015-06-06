package com.aura.smartschool.dialog;

import com.aura.smartschool.R;
import com.aura.smartschool.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginDialogFragment extends DialogFragment {
	
	private View mView;

	public LoginDialogFragment() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mView = View.inflate(getActivity(), R.layout.dialog_login, null);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
        	.setView(mView);

		return builder.create();
	}

}
