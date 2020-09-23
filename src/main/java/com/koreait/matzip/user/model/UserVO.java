package com.koreait.matzip.user.model;

public class UserVO {
	private int i_user;
	private String user_id;
	private String user_pw;
	private String nm;
	private String profile_img;
	private String r_dt;
	private String m_dt;
	private String salt;
	
	
	public String getSalt() {
		return salt;
		//기존의 encrypt의 기능은 암호를 단순히 암호화 시키는것에 불과하고
		//이 기능은 암호화를 하는 방식이 같은 암호문자열을 할당하기때문에 
		//암호화를 역추적을 할수있는 단점을 가지고 있다.
		//salt의 기능은 난수를 발생시키는 기능을 가지고있는데
		//encrypt의 기능을 보강하는 기능을 가지고있다 암호화+난수의 조합으로 추적을 어렵게 만든다.
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public int getI_user() {
		return i_user;
	}
	public void setI_user(int i_user) {
		this.i_user = i_user;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_pw() {
		return user_pw;
	}
	public void setUser_pw(String user_pw) {
		this.user_pw = user_pw;
	}
	public String getNm() {
		return nm;
	}
	public void setNm(String nm) {
		this.nm = nm;
	}
	public String getProfile_img() {
		return profile_img;
	}
	public void setProfile_img(String profile_img) {
		this.profile_img = profile_img;
	}
	public String getR_dt() {
		return r_dt;
	}
	public void setR_dt(String r_dt) {
		this.r_dt = r_dt;
	}
	public String getM_dt() {
		return m_dt;
	}
	public void setM_dt(String m_dt) {
		this.m_dt = m_dt;
	}
	
	
}
