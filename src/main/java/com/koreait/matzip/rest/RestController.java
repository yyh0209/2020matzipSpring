package com.koreait.matzip.rest;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koreait.matzip.Const;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.ViewRef;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestFile;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;

@Controller //외부에서 전달받은걸 연결. 자동으로 소문자로 바꾼다
@RequestMapping("/rest") // 1차주소값 1차에선 로그인에 성공했을때
public class RestController {
	@Autowired
	private RestService service;

	@RequestMapping("/map")
	public String restMap(Model model) {
		model.addAttribute(Const.TITLE, "지도보기");
		// model 써야 addAttribute를 사용할수있다. 뷰에서만 사용할수있다.
		// request에 담으면 어디든 사용가능.
		model.addAttribute(Const.VIEW, "rest/restMap"); // 문자열 'view'를 저장한 상수.에다 파일경로를 집어넣는다.
		return ViewRef.TEMP_MENU_TEMP;
	}

	// 식당 좌표와 식당명을 보여줌
	@RequestMapping(value = "/ajaxGetList", produces = "application/json; charset=utf-8")
	@ResponseBody
	public List<RestDMI> ajaxGetList(RestPARAM param, HttpSession hs) {
		System.out.println("sw_lat :" + param.getSw_lat());
		System.out.println("sw_lng :" + param.getSw_lng());
		System.out.println("ne_lat :" + param.getNe_lat());
		System.out.println("ne_lng :" + param.getNe_lng());
		int i_user = SecurityUtils.getLoginUserPk(hs);
		param.setI_user(i_user);
		return service.selRestList(param);
	}
	//
	// 매장등록을 뷰로 보여줌.
	@RequestMapping("/reg")
	public String restReg(Model model) {
		// 매장등록
		model.addAttribute("categoryList", service.selCategoryList()); // 타이틀값
		model.addAttribute(Const.TITLE, "가게등록"); // 타이틀값
		model.addAttribute(Const.VIEW, "rest/restReg"); // jsp파일
		return ViewRef.TEMP_MENU_TEMP; // get방식은 뷰라는 단어가 들어가있다.
	}

	// 매장등록
	@RequestMapping(value = "/reg", method = RequestMethod.POST)
	public String restReg(RestPARAM param, HttpSession hs) {
		param.setI_user(SecurityUtils.getLoginUserPk(hs));
		int result = service.insRest(param);

		return "redirect:/";
	}
	// 인터셉터 로그인체크 유무 로그인 은 디테일 비로그인은 로그인페이지로.
	@RequestMapping("/detail")
	public String detail(RestPARAM param, Model model,HttpServletRequest req) {
		service.addHits(param, req);
		int i_user = SecurityUtils.getLoginUserPk(req);
		param.setI_user(i_user);
		
		RestDMI data = service.selRest(param);
		
		model.addAttribute("menuList",service.selRestMenus(param));
		model.addAttribute("recMenuList", service.selRestRecMenus(param));
		model.addAttribute("data", data);
		model.addAttribute(Const.CSS, new String[] {"restDetail","swiper-bundle.min"}); //공백없앤 css
		model.addAttribute(Const.TITLE, data.getNm()); // 타이틀값
		model.addAttribute(Const.VIEW, "rest/restDetail"); // jsp파일
		return ViewRef.TEMP_MENU_TEMP;
	}
	@RequestMapping("/ajaxSelMenuList")
	@ResponseBody //json형태로 넘겨준다.
	public List<RestRecMenuVO> ajaxSelMenuList(RestPARAM param) {
	//식당추천메뉴를 보여줌. 모델이 매개변수면 jsp파일을 씀 
		return service.selRestMenus(param);
	}
	
	// 매장삭제
	@RequestMapping("/del")
	public String del(RestPARAM param, HttpSession hs) {
		int loginI_user = SecurityUtils.getLoginUserPk(hs); //삭제권한은 로그인한 사람에게준다.
		param.setI_user(loginI_user); // 키값을 받아서 데이터를 삭제함.
		int result = 1;
		//예외가 발생되면 쿼리문이 노출된다.
		try {
			service.delRestTran(param);
		} catch (Exception e) {
			result = 0;
		}
		// 로그인 한사람의 키값을 삭제하는 쿼리문을 보내서 글쓰는 사람의 i_user값이 맞다면 삭제가 되도록한다.
		System.out.println("result : " + result);
		return "redirect:/";
		// 매퍼,서비스에도 등록한다.
	}
	@RequestMapping(value = "/recMenus",method=RequestMethod.POST)
	public String recMenus(MultipartHttpServletRequest mReq
			,RedirectAttributes ra) {
		System.out.println("recMenus 왔음");
		
		int i_rest = service.insRecMenus(mReq);
		
		ra.addAttribute("i_rest",i_rest); //자료전달은 쿼리스트링으로.
		return "redirect:/rest/detail"; //쿼리스트링이 안붙여도 값전달은 된다.
	}
	//로그인한사람이 삭제권한을 가지는데 아무나 로그인한 사람이 삭제할수있는 권한을 가지면 안된다.
	@RequestMapping("/ajaxDelRecMenu")
	@ResponseBody public int ajaxDelRecMenu(RestPARAM param,HttpSession hs) {
		String path = "/resources/img/rest/"+param.getI_rest()+"/rec_menu/";
		String realPath = hs.getServletContext().getRealPath(path);
		param.setI_user(SecurityUtils.getLoginUserPk(hs)); //로그인유저의 pk값을 담아라.
		return service.delRestRecMenu(param, path); //쿼리스트링이 안붙여도 값전달은 된다.
	}
	@RequestMapping("/ajaxDelMenu")
	@ResponseBody public int ajaxDelMenu(RestPARAM param) { //i_rest,seq,menu_pic
		return service.delRestMenu(param); //루트에 들어가줘야된다.
		//글쓴이가 삭제요청을 했다.
		//restInterceptor에서 로그인 함사람의 i_rest값을 비교하는 지점을 담당해서 세션이 필요없다.
	}
	@RequestMapping("/menus")
	public String menus(@ModelAttribute RestFile param,HttpSession hs
			,RedirectAttributes ra) {
		int i_user = SecurityUtils.getLoginUserPk(hs);
		int result = service.insRestMenu(param, i_user);
		ra.addAttribute("i_rest", param.getI_rest());
		return "redirect:/rest/detail";
	}
}
