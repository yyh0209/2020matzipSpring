package com.koreait.matzip;

import java.io.File;

import javax.servlet.http.Part;

public class FileUtils {
	public static void makeFolder(String path) {
		File dir = new File(path);
		if(!dir.exists()) {
			//파일이 존재하지않을때는
			dir.mkdirs(); //폴더를 만들어준다.
		}
	}
	public static String getExt(String fileNm) {
		return fileNm.substring(fileNm.lastIndexOf("."));
//파일명의 난수화에서 확장자를 제외한다.
	}
	public static String getFileName(Part part) {
		for(String content: part.getHeader("content-disposition").split(";")) {
			if(content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=')+1).trim().replace("\"", "");
				//파일명에다 문자열 공백이 생겼다면.
				//content-disposition
			}
		}
		return null;
	}
}
