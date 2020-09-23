package com.koreait.matzip.model;

public class CodeVO {
	//select는 vo
	//domain은 insert용
	private int i_m;
	private int cd;
	private String val;
	
	public int getI_m() {
		return i_m;
	}
	public void setI_m(int i_m) {
		this.i_m = i_m;
	}
	public int getCd() {
		return cd;
	}
	public void setCd(int cd) {
		this.cd = cd;
	}
	public String getVal() {
		return val; //대입한 매개변수를 함수로 담아서 외부로 보냄
	}
	public void setVal(String val) {
		this.val = val; //먼저 private변수를 대신할 매개변수를 대입
	}
	
}
