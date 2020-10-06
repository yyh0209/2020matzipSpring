package com.koreait.matzip.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestFile;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;

@Mapper
public interface RestMapper {
	int insRest(RestPARAM param); // 매장등록
	int insRestRecMenu(RestRecMenuVO param); // 매장등록
	int insRestMenu(RestRecMenuVO param);
	int selRestChkUser(int i_rest);
	List<RestDMI> selRestList(RestPARAM param);
	RestDMI selRest(RestPARAM param);
	List<RestRecMenuVO> selRestMenus(RestPARAM param);
	List<RestRecMenuVO> selRestRecMenus(RestPARAM param); //select때는 restPARAM을 씀.
	
	int delRestRecMenu(RestPARAM param);
	int delRestMenu(RestPARAM param);
	int delRest(RestPARAM param);
	
	int updAddHits(RestPARAM param);
}
