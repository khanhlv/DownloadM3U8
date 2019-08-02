package com.download.m3u8.parser;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.download.m3u8.AppGlobal;
import com.download.m3u8.common.UserAgent;

public class UdemyVietNam {
    private void read() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("txtUsername", AppGlobal.USER_NAME_1);
        data.put("txtPassword", AppGlobal.PASSWORD_1);

         Connection.Response response = Jsoup.connect("http://study.udemyvietnam.vn/Login.aspx")
                .userAgent(UserAgent.getUserAgent()).timeout(10000)
                .data(data).execute();

        System.out.println(response.cookies());

//        Document document = Jsoup.connect("http://study.udemyvietnam.vn/KhoaHocCuaToi.aspx")
//                .userAgent(UserAgent.getUserAgent())
//                .cookies(response.cookies())
//                .timeout(10000)
//                .get();
//        System.out.println(document);

    }

    public static void main(String[] args) throws Exception {
        new UdemyVietNam().read();
    }
}
