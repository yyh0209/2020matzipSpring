package com.koreait.matzip;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	@RequestMapping("/")
	public String index(HttpServletRequest req) {
		if(Const.realPath == null) {
			Const.realPath = req.getServletContext().getRealPath("");//절대주소값이 들어감.
		}
		System.out.println("Root!"); //서버를 내렸다 올리때 실행.
		return "redirect:/rest/map";
	}
}
