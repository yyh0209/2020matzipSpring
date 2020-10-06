package com.koreait.matzip.user.model;

import java.util.List;

import com.koreait.matzip.rest.model.RestRecMenuVO;

public class UserDMI extends UserVO{
	//도메인 DoMaIn
	private int i_rest; //키값을 받아 식당마다 메뉴를 가져옴.
	private String rest_nm;
	private String rest_addr;
	private List<RestRecMenuVO> menuList; //식당의 메뉴리스트를 담음.
	
	
	public String getRest_nm() {
		return rest_nm;
	}
	public void setRest_nm(String rest_nm) {
		this.rest_nm = rest_nm;
	}
	public String getRest_addr() {
		return rest_addr;
	}
	public void setRest_addr(String rest_addr) {
		this.rest_addr = rest_addr;
	}
	
	public int getI_rest() {
		return i_rest;
	}
	public void setI_rest(int i_rest) {
		this.i_rest = i_rest;
	}
	public List<RestRecMenuVO> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<RestRecMenuVO> menuList) {
		this.menuList = menuList;
	}

}
