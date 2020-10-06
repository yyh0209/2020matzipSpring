package com.koreait.matzip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.koreait.matzip.rest.RestMapper;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;

public class RestInterceptor extends HandlerInterceptorAdapter{
	
	@Autowired
	private RestMapper mapper;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("rest - interceptor");
		
		String uri =request.getRequestURI();
		System.out.println("uri:"+uri);
		String[] uriArr = uri.split("/"); //1,2차 주소값을 자름.
		
		String[] checkKeywords = {"del","Del","upd","Upd"}; //1차주소값에 포함된 주소값을체크함.
		//i_rest를 얻어왔다. 레코드의 pk값이 로그인 한사람이 맞는지 체크한다. 남이 쓴글에 삭제와 수정을 시도하는걸 방지함
		//세션에있는건 조작을 못한다.
		for(String keyword: checkKeywords) {
			if(uriArr[2].toLowerCase().contains(keyword)) {
				int i_rest = CommonUtils.getIntParameter("i_rest", request);
				if(i_rest == 0) {
					return false;
				}
				int i_user = SecurityUtils.getLoginUserPk(request);
				//현재 시도하는 로그인한 사람의 i_user값을 담음.
				boolean result = _authSuccess(i_rest, i_user);
				System.out.println("=== auth result:"+result);
				return result;
			}
		}
		System.out.println("auth result:true");
		return true;
	}
	private boolean _authSuccess(int i_rest,int i_user) {
		
		return i_user == mapper.selRestChkUser(i_rest);
	}
}
