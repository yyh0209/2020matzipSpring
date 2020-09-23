package com.koreait.matzip.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koreait.matzip.Const;
import com.koreait.matzip.ViewRef;
import com.koreait.matzip.user.model.UserPARAM;
import com.koreait.matzip.user.model.UserVO;

//앱개발할때는 레스트 컨트롤러.
@Controller
@RequestMapping("/user") // 1차주소값
public class UserController {
	@Autowired
	private UserService service;
	// 자동 선연결.빈이등록된 애들 스프링 컨테이너가 객체화 시킨 애들 new로 객체화 한적이 없다.
	// 한개만 빈등록이 되어있어야한다. 두개있으면 서버가 켜지지않는다.
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession hs) {
		//로그아웃할때는 HttpSession을 매개변수로 가지고 가야 로그아웃을 날릴수있다.
		hs.invalidate(); //invalidate는 무효화의 뜻을 가지고있음. 
		//세션삭제면 로그아웃.
		return "redirect:/"; // 2차주소로 넘긴다.
		//로그아웃 적으면 무한루프
	}
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		// 로그인페이지에는
		model.addAttribute(Const.TITLE, "로그인"); // 서블릿
		model.addAttribute(Const.VIEW, "user/login"); // 값을 jsp로 보내줘야될때는 model로 보여줌.
		return ViewRef.TEMP_DEFAULT; // 2차주소로 넘긴다.
	}

	// 자동으로 jsp로 넘어간다.
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(UserPARAM param, HttpSession hs, RedirectAttributes ra) {
		// VO를 받을때는 post! 처리할때는 받는게 중요하니 post redirect가 필요하다.
		int result = service.login(param);
		//
		System.out.println("result : "+result);
		if (result == Const.SUCCESS) {
			// 1이 넘어왔을때 jsp의 속성값에 저장함. userDTO를 인자로 받음.
			hs.setAttribute(Const.LOGIN_USER, param);
			// 주소값을 저장해서 접근함.
			return "redirect:/rest/map";
			//로그인에 성공했으면 로그인 정보를 속성값에 설정하고 해당 주소값으로 전달한다.
		}
		String msg = null;
		if (result == Const.NO_ID) {
			msg = "아이디를 확인해주세요";
		} else if (result == Const.NO_PW) {
			msg = "비밀번호를 확인해주세요";
		}
		param.setMsg(msg);
		ra.addFlashAttribute("data", param);
		return "redirect:/user/login";
		// dispatcher쓰면 포스트로 날아감 값이 유지되고 있는건 세션때문이다.
		// 세션에서 받고 세션에서 지운다 포스트"처럼"쓸수있는것.
		// 응답할때 지운다.
		// 결과를 쿼리스트링으로 보내거나
	}

	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public String join(Model model, @RequestParam(defaultValue = "0") int err) {
		
		//0보다 클때는
		if (err > 0) {
			model.addAttribute("msg", "에러가 발생했습니다.");
			//jsp내에 msg라는 el식을 찾아서 텍스트를 추가함.
			//클래스를 추적하지 않고 태그내의 택스트에서 msg라는 이름을 가진 el식을 찾아서 
			//텍스트를 넣는다.
		}
		model.addAttribute(Const.TITLE, "회원가입"); // 서블릿
		model.addAttribute(Const.VIEW, "user/join"); // 값을 jsp로 보내줘야될때는 model로 보여줌.
		return ViewRef.TEMP_DEFAULT; // 2차주소로 넘긴다.
	}
	//Controller를 선택할 때 대표적으로 사용하는 애노테이션이다.
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	//파일여는것
	public String join(UserVO param, RedirectAttributes ra) {
		
		int result = service.join(param);

		if (result == 1) {
			return "redirect:/user/login";
		}
		ra.addAttribute("err" + result);
		return "redirect:/user/join";
	}
		//addAttribute 쿼리스트링
		//addFlashAttribute는 세션에 받았다가 응답할때 지운다.
	@RequestMapping(value="/ajaxIdChk",method=RequestMethod.POST)
	@ResponseBody public String ajaxIdChk(@RequestBody UserPARAM param) {
		int result = service.login(param);
		return String.valueOf(result);
		//파일명을 안쓰고 이거 자체를 반환. 파일자체를 응답.
		//redirect가 안붙여있으니 문자열, jsp파일
		//아이디 없음:2번이 넘어와야 한다.
		

		//객체리턴을 하려면 ResponseBody 가 필요하다. 
		//결과물 Response의 몸. jsp파일을 찾는게 아니라 그 자체가 응답임.
		//vue.js,react.js는 전부 ResponseBody를 쓴다
		//첫페이지는 html을 요구하고 그 다음엔 데이터를 요구함.
	}
}
