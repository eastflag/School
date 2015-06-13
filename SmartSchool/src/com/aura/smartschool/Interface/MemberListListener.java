package com.aura.smartschool.Interface;

import com.aura.smartschool.vo.MemberVO;

public interface MemberListListener {
	public void onSelected(int position);
	public void onUpdateClicked(int position);
	public void onAddClicked(MemberVO member);
}
