package com.download.m3u8.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.download.m3u8.common.AppGlobal;
import com.download.m3u8.common.UserAgent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UdemyVietNam {
    private String userAgent = UserAgent.getUserAgent();
    private Map<String, String> mapCookies;

    public List<UdemyCourse> readCourse(String user, String pass) throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("txtUsername", user);
        data.put("txtPassword", pass);

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

        mapCookies = response.cookies();

        Document document = Jsoup.connect("http://study.udemyvietnam.vn/KhoaHocCuaToi.aspx")
                .userAgent(userAgent)
                .cookies(mapCookies)
                .timeout(10000)
                .get();

        String urlListCourse = String.format("http://study.udemyvietnam.vn/CommonClass/StudyServices.asmx/GetCourse?idUser=%s", document.select("#hdfIdUser").attr("value"));

        Document docsCourse = Jsoup.connect(urlListCourse)
                .userAgent(userAgent)
                .ignoreContentType(true)
                .cookies(mapCookies)
                .timeout(10000)
                .get();

        //System.out.println(docsCourse.tagName("body").text());

        List<UdemyCourse> udemyCourseList = new Gson().fromJson(docsCourse.tagName("body").text(), new TypeToken<List<UdemyCourse>>(){}.getType());

//        System.out.println(udemyCourseList.size());

        return udemyCourseList;
    }

    public List<UdemyCoursePlay> readPlayCourse(String id) throws Exception {

        List<UdemyCoursePlay> udemyCoursePlayList = new ArrayList<>();
        Document docsLinkGet = Jsoup.connect("http://study.udemyvietnam.vn/play/study.aspx?courseId=" + id)
                .userAgent(userAgent)
                .cookies(mapCookies)
                .timeout(10000)
                .get();

        Map<String, String> dataStudy = new HashMap<>();

        dataStudy.put("__EVENTTARGET", docsLinkGet.select("input[name=\"__EVENTTARGET\"]").attr("value"));
        dataStudy.put("__EVENTARGUMENT", docsLinkGet.select("input[name=\"__EVENTARGUMENT\"]").attr("value"));
        dataStudy.put("__VIEWSTATE", docsLinkGet.select("input[name=\"__VIEWSTATE\"]").attr("value"));
        dataStudy.put("__VIEWSTATEGENERATOR", docsLinkGet.select("input[name=\"__VIEWSTATEGENERATOR\"]").attr("value"));
        dataStudy.put("txtError", "");
        dataStudy.put("hdf_video_index", docsLinkGet.select("input[name=\"hdf_video_index\"]").attr("value"));
        dataStudy.put("hdf_user_id", docsLinkGet.select("input[name=\"hdf_user_id\"]").attr("value"));
        dataStudy.put("hdf_video_id", docsLinkGet.select("input[name=\"hdf_video_id\"]").attr("value"));
        dataStudy.put("DXScript", "1_16,1_66,1_17,1_18,1_19,1_20,1_13,1_28");
        dataStudy.put("DXCss", "vendor/bootstrap/bootstrap.css,vendor/bootstrap/bootstrap-progressbar-3.css,vendor/font-awesome/css/font-awesome.css,https://fonts.googleapis.com/icon?family=Material+Icons,css/font-awesome.css,css/nprogress.css,css/green.css,css/result.css,css/play.css");
        dataStudy.put("__CALLBACKID", "callbackGetListVideo");
        dataStudy.put("__CALLBACKPARAM", "c0:" + id);
        dataStudy.put("__EVENTVALIDATION", docsLinkGet.select("input[name=\"__EVENTVALIDATION\"]").attr("value"));

        Document docsLink = Jsoup.connect("http://study.udemyvietnam.vn/play/study.aspx?courseId=" + id)
                .userAgent(userAgent)
                .cookies(mapCookies)
                .timeout(10000)
                .data(dataStudy)
                .post();

        String data = docsLink.select("div[data-link]").toString().replaceAll("\\\\r|\\\\'|\\\\n|\\\\t", "");

        Document docData = Jsoup.parse(data);

        docData.select("div[data-link]").forEach(v -> {
            String name = StringUtils.trim(v.select(".chap-item-content .row").get(0).select(".chap-active").text());
            String dataLink = StringUtils.trim(v.attr("data-link"));

            UdemyCoursePlay udemyCoursePlay = new UdemyCoursePlay();
            udemyCoursePlay.setId(dataLink);
            udemyCoursePlay.setName(name);
            udemyCoursePlayList.add(udemyCoursePlay);
        });

        return udemyCoursePlayList;

    }

    public static void main(String[] args) throws Exception {
        UdemyVietNam udemyVietNam = new UdemyVietNam();
        udemyVietNam.readCourse(AppGlobal.USER_NAME_1, AppGlobal.PASSWORD_1);
        System.out.println(udemyVietNam.readPlayCourse("59").size());
    }
}
