package com.download.m3u8.common;

import org.apache.commons.lang3.StringUtils;

public class AppGlobal {
	public final static String FOLDER_ROOT = "D:/UdemyVN/";
	public final static String URL_STUDY = "http://study.udemyvietnam.vn/";
	public final static String USER_NAME_1 = "study.mkt@gmail.com";
	public final static String PASSWORD_1 = "tambooks@mkt2019";
	public final static String USER_NAME_ID = "172857";
	//http://study.udemyvietnam.vn/CommonClass/StudyServices.asmx/GetCourse?idUser=172857

	public final static String USER_NAME_2 = "study.uvn@gmail.com";
	public final static String PASSWORD_2 = "tambooks@quanly2019";

	public final static String makeUrl(String url) {
		return StringUtils.replaceAll(url, "\\s+", "+");
	}
}