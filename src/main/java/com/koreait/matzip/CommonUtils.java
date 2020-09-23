package com.koreait.matzip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

public class CommonUtils {
	public static int parseStringToInt(String str) {
		try {
			return Integer.parseInt(str);
		}catch(Exception e) {}
		return 0;
		//문자열을 정수로.
	}
	public static double parseStringToDouble(String str) {
		try {
			return Double.parseDouble(str);
		}catch(Exception e) {}
		return 0;
		//문자열을 실수형으로
	}
	public static int getIntParameter(String key,HttpServletRequest request) {
		return parseStringToInt(request.getParameter(key));
		//문자열로 되어있는 키값을 정수형으로 변환
	}
	
	public static int getIntParameter(String key,MultipartRequest request) {
		return parseStringToInt(request.getParameter(key));
		//파일 업로드
	}
	
	public static double getDoubleParameter(String key,HttpServletRequest request) {
		return parseStringToDouble(request.getParameter(key));
	}
}
