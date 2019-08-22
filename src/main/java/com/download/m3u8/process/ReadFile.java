package com.download.m3u8.process;

import com.download.m3u8.common.AppGlobal;
import com.download.m3u8.utils.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ReadFile {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReadFile.class);

    public String fileM3U8(String link) {
        LOGGER.info(String.format("[FILE_M3U8=%s]", link));
        String pathFile = StringUtils.EMPTY;
        try {
            URL url = new URL(link);

            URLConnection urlConnection = url.openConnection();

            String htmlPath = link.substring(0, link.lastIndexOf("/"));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if(line.contains(".m3u8")) {
                    if(line.contains("http://")) {
                        pathFile = line + "?index=1";
                    } else {
                        pathFile = htmlPath + "/" + line + "?index=1";
                    }
                    break;
                }
            }
        } catch (Exception ex) {
            LOGGER.error("ERROR[ReadFile]", ex);
        }
        return pathFile;
    }

    public ConcurrentLinkedQueue<String> read(String urlM3U8, String folderRoot) {
        urlM3U8 = AppGlobal.makeUrl(urlM3U8);

        urlM3U8 = fileM3U8(urlM3U8);

        folderRoot = StringUtil.stripAccentsNone(folderRoot, "_");

        LOGGER.info(String.format("[LINK_M3U8=%s]", urlM3U8));

        ConcurrentLinkedQueue<String> listLink = new ConcurrentLinkedQueue<>();
        try {
            URL url = new URL(urlM3U8);

            URLConnection urlConnection = url.openConnection();

            String htmlPath = urlM3U8.substring(0, urlM3U8.lastIndexOf("/"));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(".ts")) {
                    listLink.add(folderRoot + "|" + htmlPath + "/" + line + "&sk=admin");
                }
            }
        } catch (Exception ex) {
            LOGGER.error("ERROR[ReadFile]", ex);
        }

        return listLink;
    }

    public static void main(String[] args) {
        System.out.println(new ReadFile().fileM3U8("http://study.udemyvietnam.vn/vod/_definst_/mp4:HoangVM_01/11.mp4/playlist.m3u8"));
    }
}
