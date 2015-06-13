package com.aura.smartschool.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aura.smartschool.R;
import com.aura.smartschool.vo.MemberVO;

public class MainFragment extends Fragment {
	private View mView;
	private MemberVO mMember;

	public MainFragment() {
		// Required empty public constructor
	}
	
	public MainFragment(MemberVO member) {
		mMember = member;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = View.inflate(getActivity(), R.layout.fragment_main, null);
		
		return mView;
	}

}
