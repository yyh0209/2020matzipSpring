package com.koreait.matzip.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
//mybatis에서 쓰는 mapper 찾기위한 용도

import com.koreait.matzip.rest.model.RestVO;
import com.koreait.matzip.user.model.UserDMI;
import com.koreait.matzip.user.model.UserPARAM;
import com.koreait.matzip.user.model.UserVO;

@Mapper
//인터페이스중에 매핑이라고 하는 놈을 찾는다.
public interface UserMapper {
	int insUser(UserVO p);
	int insFavorite(UserPARAM param);
	UserDMI selUser(UserPARAM p);
	List<UserDMI> selFavoriteList(UserPARAM param);
	int delFavorite(UserPARAM param);
	//나중에 리턴타입도 바꿀수있다.
	//select.만 int타입이 아닌 리턴타입이다.
	//변수명과 칼럼명과 매칭이 되야된다.
	//매장등록
}
