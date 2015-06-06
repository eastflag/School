package com.aura.smartschool.Interface;

import com.aura.smartschool.vo.MemberVO;

public interface LoginListener {
	void onLogin(MemberVO member);
	void gotoRegister();
	void onRegister(MemberVO member);
}
