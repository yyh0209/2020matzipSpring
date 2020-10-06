package com.koreait.matzip.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koreait.matzip.Const;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.rest.RestMapper;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;
import com.koreait.matzip.rest.model.RestVO;
import com.koreait.matzip.user.model.UserDMI;
import com.koreait.matzip.user.model.UserPARAM;
import com.koreait.matzip.user.model.UserVO;

@Service
public class UserService {

	@Autowired
	private UserMapper mapper;//mapper를 쓸수있는 어노테이션
//	주소값이 안들어있는데  Autowired 라는 annotation을 적어주면 주소값을 담을수있다.
	// 1번 로그인 성공, 2번 아이디 없음, 3번 비번 틀림.
	@Autowired
	private RestMapper restMapper;
	public int login(UserPARAM param) {
		// id,비번이 들어있다.
		if (param.getUser_id().equals("")) {return Const.NO_ID;}
		// Usermapper의 selUser의 param.을 UserDML에 보낸다.
		UserDMI dbUser = mapper.selUser(param); // select문을 넣는다.
		// 암호화하기 dbuser가 null일때는 id없음
		if (dbUser == null) {return Const.NO_ID;}
		// 암호화 단계
		String cryptPw = SecurityUtils.getEncrypt(param.getUser_pw(), dbUser.getSalt());
		if (!cryptPw.equals(dbUser.getUser_pw())) {return Const.NO_PW;}
		param.setI_user(dbUser.getI_user());
		param.setUser_pw(null); // pw를 null값을 저장하고
		param.setNm(dbUser.getNm());
		param.setProfile_img(dbUser.getProfile_img());
		return Const.SUCCESS;
	}

	public int join(UserVO param) {
		// user_id의 값을 대입
		String pw = param.getUser_pw(); //회원가입에 필요한건 암호다.
		String salt = SecurityUtils.generateSalt(); // 도메인사이트의 키와 value를 임의숫자로 변형/
		String cryptPw = SecurityUtils.getEncrypt(pw, salt);
		
		param.setSalt(salt); //
		param.setUser_pw(cryptPw);
		
		return mapper.insUser(param); // 로그인 창으로 이등.

	}
	public int ajaxToggleFavorite(UserPARAM param) { //i_rest,i_user,proc_type
		System.out.println("진입!");
		switch(param.getProc_type()) {
		case "ins":
			return mapper.insFavorite(param);
		case "del":
			return mapper.delFavorite(param);
		}
		return 0;
	}
	//userDMI
	public List<UserDMI> selFavoriteList(UserPARAM param){
		List<UserDMI> list = mapper.selFavoriteList(param); //문자열을 null지정 안하면 쓰레기값으로 지정됨.
		//어떤 주소값을 참조하지않는 상태.
		for(UserDMI vo: list) {
			RestPARAM param2 = new RestPARAM();
			param2.setI_rest(vo.getI_rest());
			
			List<RestRecMenuVO> eachRecMenuList = restMapper.selRestRecMenus(param2);
			vo.setMenuList(eachRecMenuList);
		}
		//로그인한 사람의 정보를 받아옴. i_user
		return list;
	}
}
