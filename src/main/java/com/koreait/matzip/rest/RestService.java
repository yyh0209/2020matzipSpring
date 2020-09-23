package com.koreait.matzip.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;
import com.koreait.matzip.CommonUtils;
import com.koreait.matzip.Const;
import com.koreait.matzip.FileUtils;
import com.koreait.matzip.model.CodeVO;
import com.koreait.matzip.model.CommonMapper;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;
import com.koreait.matzip.rest.model.RestVO;

@Service
public class RestService {
	@Autowired
	private RestMapper mapper;

	@Autowired
	private CommonMapper cMapper;

	// 제네릭변수선언 RestDMI는 RestVO의 멤버들을 상속받는다.
	// 매장의 리스트들을 보냄
	public List<RestDMI> selRestList(RestPARAM param) {
		return mapper.selRestList(param);
	}

	// 매장을 등록.
	public List<CodeVO> selCategoryList() {
		CodeVO p = new CodeVO();
		p.setI_m(1); // 음식점 카테고리는 1번
		return cMapper.selCodeList(p);
	}

	public int insRest(RestPARAM param) {
		return mapper.insRest(param);
	}

	@Transactional
	public void delRestTran(RestPARAM param) {
		mapper.delRestRecMenu(param);
		mapper.delRestMenu(param);
		mapper.delRest(param);
	}

	public int delRestRecMenu(RestPARAM param) {
		return mapper.delRestRecMenu(param);
	}

	public int delRestMenu(RestPARAM param) {
		return mapper.delRestMenu(param);
	}

	public int insRecMenus(MultipartHttpServletRequest mReq) {
		// 문자열 i_rest를 정수로 바꾼다.
		int i_rest = Integer.parseInt(mReq.getParameter("i_rest"));
		List<MultipartFile> fileList = mReq.getFiles("menu_pic");
		// 사진 파일 리스트를 저장한다.
		String[] menuNmArr = mReq.getParameterValues("menu_nm");
		// 메뉴 명
		String[] menuPriceArr = mReq.getParameterValues("menu_price");

		String path = mReq.getServletContext().getRealPath("/resource/img/rest/" + i_rest + "/rec_menu/");
		// 3.1.0버전의 서블릿컨텍스트 객체가 넘어온다 그곳에 있는 getRealPath를 써라.
		List<RestRecMenuVO> list = new ArrayList<RestRecMenuVO>();
		//
		for (int i = 0; i < menuNmArr.length; i++) {
			RestRecMenuVO vo = new RestRecMenuVO();
			list.add(vo);
			String menu_nm = menuNmArr[i];
			int menu_price = CommonUtils.parseStringToInt(menuPriceArr[i]); //가격
			vo.setMenu_nm(menu_nm); //이름
			vo.setMenu_price(menu_price); //가격
			// 복수의파일을 저장하는 기능.
			MultipartFile mf = fileList.get(i);

			if (mf.isEmpty()) {continue;}
			String originFileNm = mf.getOriginalFilename();
			String ext = FileUtils.getExt(originFileNm);
			String saveFileNm = UUID.randomUUID() + ext;
			// 중복된 파일명을 방지하는 난수를 더하면
			try {
				mf.transferTo(new File(path + saveFileNm));
				vo.setMenu_pic(saveFileNm);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return i_rest;
	}

	public RestDMI selRest(RestPARAM param) {
		return mapper.selRest(param);
	}
}