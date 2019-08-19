package com.download.m3u8.parser;

import com.download.m3u8.common.AppGlobal;
import com.download.m3u8.common.UserAgent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UdemyVietNam {
    private void read() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("txtUsername", AppGlobal.USER_NAME_1);
        data.put("txtPassword", AppGlobal.PASSWORD_1);

        String userAgent = UserAgent.getUserAgent();

        Document docs = Jsoup.connect("http://study.udemyvietnam.vn/Login.aspx")
                .userAgent(userAgent).timeout(10000).get();

//        System.out.println(docs);
        data.put("__VIEWSTATEGENERATOR", docs.select("input[name=\"__VIEWSTATEGENERATOR\"]").attr("value"));
        data.put("__EVENTVALIDATION", docs.select("input[name=\"__EVENTVALIDATION\"]").attr("value"));
        data.put("__EVENTTARGET", docs.select("input[name=\"__EVENTTARGET\"]").attr("value"));
        data.put("__EVENTARGUMENT", docs.select("input[name=\"__EVENTARGUMENT\"]").attr("value"));
        data.put("__VIEWSTATE", docs.select("input[name=\"__VIEWSTATE\"]").attr("value"));
        data.put("cmdLogin", "Đăng nhập");
        data.put("txtEmailRegister", "");
        data.put("txtUsernameRegister", "");
        data.put("txtPasswordRegister", "");
        data.put("txtConfirmPassword", "");
        data.put("DXScript", "1_16,1_66,1_17,1_18,1_19,1_20,1_13");
        data.put("DXCss", "vendors//bootstrap/dist/css/bootstrap.min.css,vendors//font-awesome/css/font-awesome.min.css,build/css/custom.min.css");

         Connection.Response response = Jsoup.connect("http://study.udemyvietnam.vn/Login.aspx")
                .userAgent(userAgent).timeout(10000)
                .data(data).execute();

//        System.out.println(response.cookies());

        Document document = Jsoup.connect("http://study.udemyvietnam.vn/KhoaHocCuaToi.aspx")
                .userAgent(userAgent)
                .cookies(response.cookies())
                .timeout(10000)
                .get();


        String urlListCourse = String.format("http://study.udemyvietnam.vn/CommonClass/StudyServices.asmx/GetCourse?idUser=%s", document.select("#hdfIdUser").attr("value"));

        Document docsCourse = Jsoup.connect(urlListCourse)
                .userAgent(userAgent)
                .ignoreContentType(true)
                .cookies(response.cookies())
                .timeout(10000)
                .get();


        //System.out.println(docsCourse.tagName("body").text());

        List<UdemyCourse> udemyCourseList = new Gson().fromJson(docsCourse.tagName("body").text(), new TypeToken<List<UdemyCourse>>(){}.getType());

        System.out.println(udemyCourseList.size());

        Document docsLink = Jsoup.connect("http://study.udemyvietnam.vn/play/study.aspx?courseId=346")
                .userAgent(userAgent)
                .cookies(response.cookies())
                .timeout(10000)
                .get();

        System.out.println(docsLink.select("div[data-link]"));


    }

    public static void main(String[] args) throws Exception {
        new UdemyVietNam().read();
    }
}
