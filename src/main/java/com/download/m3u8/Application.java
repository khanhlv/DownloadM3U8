package com.download.m3u8;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.download.m3u8.common.ShareQueue;
import com.download.m3u8.process.ReadFile;
import com.download.m3u8.thread.ThreadDownloadFile;
import com.download.m3u8.thread.ThreadShareQueue;

public class Application {
    private final static Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private final ReadFile readFile = new ReadFile();
    private void addQueue() {
        ConcurrentLinkedQueue<String> linkList = readFile.read("http://study.udemyvietnam.vn/vod/_definst_/mp4:Tambooks/Lamita_01/1.mp4/chunklist_w559670374.m3u8?index=1");

        ShareQueue.shareQueue.addAll(linkList);
    }

    private void execute() {
        try {

            new Thread(new ThreadDownloadFile()).start();

            Thread.sleep(1000);

            addQueue();

            new Thread(new ThreadShareQueue()).start();
        } catch (InterruptedException e) {
            LOGGER.error("ERROR[Application]",e);
        }
    }

    public static void main(String[] args) {
        new Application().execute();
    }
}
