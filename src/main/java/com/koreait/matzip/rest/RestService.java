package com.koreait.matzip.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;
import com.koreait.matzip.CommonUtils;
import com.koreait.matzip.Const;
import com.koreait.matzip.FileUtils;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.model.CodeVO;
import com.koreait.matzip.model.CommonMapper;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestFile;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;
import com.koreait.matzip.rest.model.RestVO;

@Service // 빈등록
public class RestService {
	@Autowired
	private RestMapper mapper;

	@Autowired
	private CommonMapper cMapper;

	// 제네릭변수선언 RestDMI는 RestVO의 멤버들을 상속받는다.
	// 매장의 리스트들을 보냄
	// select
	public List<RestDMI> selRestList(RestPARAM param) {
		return mapper.selRestList(param);
	}

	public List<RestRecMenuVO> selRestMenus(RestPARAM param) {
		return mapper.selRestMenus(param);
	}

	public List<RestRecMenuVO> selRestRecMenus(RestPARAM param) {
		return mapper.selRestRecMenus(param);
	}
	// 매장을 등록.
	public List<CodeVO> selCategoryList() {
		CodeVO p = new CodeVO();
		p.setI_m(1); // 음식점 카테고리는 1번
		return cMapper.selCodeList(p);
	}
	//현재 이 레스토랑에 들어온사람의 ip주소값을 본다.
	//어플리케이션 담당 공용임. 서버당 한개의 어플리케이션을 가지고있다.페이지컨텍스트는 jsp가 만들어질대 마다.생성
	public void addHits(RestPARAM param, HttpServletRequest req) {
		String myIp = req.getRemoteAddr(); //클라이언트의 주소값이 myIp에 들어간다.
		ServletContext ctx = req.getServletContext();
		int i_user = SecurityUtils.getLoginUserPk(req);
		String currentRestReadUser = (String) ctx.getAttribute(Const.CURRENT_REST_READ_IP + param.getI_rest()); //ip주소값 192.0.0.12
		//null이면 한번도 키값을 받은적이 없다. (내가 처음방문했다.) 그러면 조횟수는 무조건 올린다.
		//언제 if문에 들어갈수있냐면 딴 사람의 ip가 들어왔을 경우.
		if(currentRestReadUser == null || currentRestReadUser.equals(myIp)) {
			//마지막으로 읽었던 사람의 i_user값을 얻어와야된다.
			//조회수 올림처리.
			param.setI_user(i_user); //내가 쓴글이면 조횟수 안 올라가게 쿼리문 (WHERE i_user)으로 막는다!!
			mapper.updAddHits(param);
			ctx.setAttribute(Const.CURRENT_REST_READ_IP+ param.getI_rest(),myIp); //타인의 ip주소가 들어왔을땐 ip주소가 오른다.
			//나의 ip주소값을 받는다. 그런데 새로고침이거나 내가 이 주소에 또들어온경우엔 
		}
	}

	public RestDMI selRest(RestPARAM param) {
		return mapper.selRest(param);
	}

	// delete
	@Transactional
	public void delRestTran(RestPARAM param) {
		mapper.delRestRecMenu(param);
		mapper.delRestMenu(param);
		mapper.delRest(param);
	}

	public int delRestMenu(RestPARAM param) {
		// 값이 넘어왔거나 빈값이 아니라면.
		if (param.getMenu_pic() != null && "".equals(param.getMenu_pic())) {
			String path = Const.realPath + "/resources/img/rest/" + param.getI_rest() + "/menu/";
			if (FileUtils.delFile(path + param.getMenu_pic())) {
				return mapper.delRestMenu(param);
			} else {
				return Const.FAIL; // 삭제에 실패하면 0이 넘어감.
			}
		}
		return mapper.delRestMenu(param); // 1이 넘어감.
	}

	public int delRestRecMenu(RestPARAM param, String realPath) {
		// 파일삭제
		List<RestRecMenuVO> list = mapper.selRestRecMenus(param);
		if (list.size() == 1) { // 레코드 수
			// 1이 넘어왔으면 내가쓴글이 맞고 그 안에는 모든정보가 들어가있다.
			// 빈칸이 이미지가 아니면 그 이미지는 삭제시킴.
			RestRecMenuVO item = list.get(0);
			//
			if (item.getMenu_pic() != null && !item.getMenu_pic().equals("")) {
				// 파일 객체를 쓴다. 경로+사진파일.
				File file = new File(realPath + item.getMenu_pic());
				// 파일이 존재하면.
				if (file.exists()) {
					if (file.delete()) {
						return mapper.delRestRecMenu(param);
					} else {
						return 0;
					}
					// 삭제를 시킨다.
				}
			}
		}
		return mapper.delRestRecMenu(param);
	}

	// insert
	public int insRest(RestPARAM param) {
		return mapper.insRest(param);
	}

	// 메뉴를 삽입.
	public int insRestMenu(RestFile param, int i_user) {
		if (_authFail(param.getI_rest(), i_user)) {
			return Const.FAIL;
		}
		System.out.println("boom" + Const.realPath);

		String path = Const.realPath + "/resources/img/rest/" + param.getI_rest() + "/menu/";
		List<RestRecMenuVO> list = new ArrayList();

		for (MultipartFile mf : param.getMenu_pic()) {
			RestRecMenuVO vo = new RestRecMenuVO();
			list.add(vo);

			String saveFileNm = FileUtils.saveFile(path, mf); // null이면 파일이 없다
			vo.setMenu_pic(saveFileNm);
			vo.setI_rest(param.getI_rest());
			// 문자열이 있으면 파일이 있다고 판단.
		}
		for (RestRecMenuVO vo : list) {
			mapper.insRestMenu(vo);
			// vo를 보내라.
		}
		return Const.SUCCESS;

	}

	// 추천메뉴
	public int insRecMenus(MultipartHttpServletRequest mReq) {
		// 문자열 i_rest를 정수로 바꾼다.
		int i_user = SecurityUtils.getLoginUserPk(mReq.getSession()); // 내가쓴글이 아니면 글등록권한을 가지지못하게 함.
		int i_rest = Integer.parseInt(mReq.getParameter("i_rest"));
		if (_authFail(i_rest, i_user)) {
			return Const.FAIL;
			// 내가쓴 글이 아니면 실패.
		}
		List<MultipartFile> fileList = mReq.getFiles("menu_pic");
		String[] menuNmArr = mReq.getParameterValues("menu_nm");
		String[] menuPriceArr = mReq.getParameterValues("menu_price");

		String path = Const.realPath + "/resources/img/rest/" + i_rest + "/rec_menu/";
		// 3.1.0버전의 서블릿컨텍스트 객체가 넘어온다 그곳에 있는 getRealPath를 써라.
		List<RestRecMenuVO> list = new ArrayList();
		for (int i = 0; i < menuNmArr.length; i++) {
			RestRecMenuVO vo = new RestRecMenuVO(); // 추천메뉴
			list.add(vo); // 추천메뉴들을 더한다.

			String menu_nm = menuNmArr[i];
			int menu_price = CommonUtils.parseStringToInt(menuPriceArr[i]); // 가격
			vo.setI_rest(i_rest); // 이름
			vo.setMenu_nm(menu_nm); // 이름
			vo.setMenu_price(menu_price); // 가격
			// 복수의파일을 저장하는 기능.
			MultipartFile mf = fileList.get(i);
			String saveFileNm = FileUtils.saveFile(path, mf);
			vo.setMenu_pic(saveFileNm);
		}
		for (RestRecMenuVO vo : list) {
			mapper.insRestRecMenu(vo);
			// vo를 보내라.
		}

		return i_rest;
	}

	private boolean _authFail(int i_rest, int i_user) {
		RestPARAM param = new RestPARAM();
		param.setI_rest(i_rest);
		int dbI_user = mapper.selRestChkUser(i_rest); // 글쓴이의 pk값이 넘어온다.
		if (i_user != dbI_user) {
			return true;
			// 로그인 한사람의 i_user값이면 성공
		}
		return false;
	}
}