package com.download.m3u8.process;

import com.download.m3u8.common.AppGlobal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ReadFile {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReadFile.class);

    public ConcurrentLinkedQueue<String> read(String urlM3U8) {
        urlM3U8 = AppGlobal.makeUrl(urlM3U8);

        ConcurrentLinkedQueue<String> listLink = new ConcurrentLinkedQueue<>();
        try {
            URL url = new URL(urlM3U8);

            URLConnection urlConnection = url.openConnection();

            String htmlPath = urlM3U8.substring(0, urlM3U8.lastIndexOf("/"));

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if(line.contains(".m3u8")) {
                    if(line.contains("http://")) {
                        read(line);
                    } else {
                        read(htmlPath + "/" + line);
                    }
                }
                if (line.contains(".ts")) {
                    listLink.add(htmlPath + "/" + line + "&sk=admin");
                    LOGGER.info(htmlPath + "/" + line);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("ERROR[ReadFile]", ex);
        }

        return listLink;
    }

    public static void main(String[] args) {
        new ReadFile().read("http://210.211.96.151:1935/vod/_definst_/mp4:Tambooks/Lamita_01/1.mp4/chunklist_w559670374.m3u8?index=1");
    }
}