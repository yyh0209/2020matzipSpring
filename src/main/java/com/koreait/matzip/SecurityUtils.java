package com.koreait.matzip;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import com.koreait.matzip.user.model.UserVO;

public class SecurityUtils {

	// boolean타입엔 반드시 is를 붙인다.
	public static int getLoginUserPk(HttpServletRequest request) {
		return getLoginUserPk (request.getSession()); //바로밑의 함수한테 가져감.
		//로그인한 사람의 pk값.
	}
	public static int getLoginUserPk(HttpSession hs) {
		UserVO loginUser = (UserVO)hs.getAttribute(Const.LOGIN_USER);
		return loginUser == null ? 0 : loginUser.getI_user();
		//로그인한 사람의 pk값.
	}

	public static UserVO getLoginUser(HttpServletRequest request) {
		HttpSession hs = request.getSession(); //세션이 로그인한사람에게 접근 권한을 준다.
		return (UserVO) hs.getAttribute(Const.LOGIN_USER);
		//로그인해서 들어온 사람들이 할수있는 모든것을 지원.
	}

	public static boolean isLogout(HttpServletRequest request) {
		return getLoginUser(request) == null; //getLoginUser 함수에서 세션이 들어왔을때. 반환값에 참/거짓을 결정
		//로그아웃여부를 확인.
	}

	public static String generateSalt() {
		return BCrypt.gensalt();
		//주소를 암호화시킴.
	}

	public static String getEncrypt(String pw, String salt) {
		return BCrypt.hashpw(pw, salt);
		//비밀번호와 도메인의 값을 암호화 시킴.
	}

}
