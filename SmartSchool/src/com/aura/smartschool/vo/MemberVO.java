package com.aura.smartschool.vo;

public class MemberVO {
	public int member_id;
	public String home_id;
	public String mdn;
	public String name;
	public String relation;
	public int is_parent = 1; //0:학생, 1:부모
	public String photo;
	public String school_name;
	public String school_grade;
	public String school_ban;
	
	public MemberVO () {
		
	}
	
	public MemberVO(String home_id, String mdn) {
		this.home_id = home_id;
		this.mdn = mdn;
	}
}
