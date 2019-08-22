package com.download.m3u8;

import com.download.m3u8.common.ShareQueue;
import com.download.m3u8.parser.UdemyVietNam;
import com.download.m3u8.process.ReadFile;
import com.download.m3u8.thread.ThreadShareQueue;
import com.download.m3u8.utils.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Application {
    private final static Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private final ReadFile readFile = new ReadFile();

    private void addQueue() throws Exception {
        UdemyVietNam udemyVietNam = new UdemyVietNam();
        udemyVietNam.readCourse().forEach(d -> {
            System.out.println(d.getID_COURSE());
            System.out.println(StringUtil.stripAccentsNone(d.getCOURSE_NAME(), "_"));

//            if (d.getID_COURSE().equals("59")) {
                try {
                    udemyVietNam.readPlayCourse(d.getID_COURSE()).forEach(v -> {
                        ConcurrentLinkedQueue<String> linkList = readFile.read(String.format("http://210.211.96.151:1935/vod/_definst_/mp4:%s/playlist.m3u8", v.getId()), d.getCOURSE_NAME());
                        ShareQueue.shareQueue.addAll(linkList);
                    });
                } catch (Exception ex) {
                    LOGGER.error("ERROR[addQueue]", ex);
                }
//            }
        });

        // 210.211.96.151:1935
//        udemyVietNam.readPlayCourse("59").forEach(v -> {
//            ConcurrentLinkedQueue<String> linkList = readFile.read(String.format("http://210.211.96.151:1935/vod/_definst_/mp4:%s/playlist.m3u8", v.getId()));
//            ShareQueue.shareQueue.addAll(linkList);
//        });
    }

    private void execute() {
        try {

            addQueue();

            Thread.sleep(5000);

            new Thread(new ThreadShareQueue()).start();

        } catch (Exception e) {
            LOGGER.error("ERROR[Application]",e);
        }
    }

    public static void main(String[] args) {
        new Application().execute();
    }
}
