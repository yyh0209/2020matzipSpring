package com.koreait.matzip.rest;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;

@Mapper
public interface RestMapper {
	int insRest(RestPARAM param); // 매장등록
	List<RestDMI> selRestList(RestPARAM p);
	RestDMI selRest(RestPARAM param);
	
	int delRestRecMenu(RestPARAM param);
	int delRestMenu(RestPARAM param);
	int delRest(RestPARAM param);
}
