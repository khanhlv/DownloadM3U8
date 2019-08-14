package com.download.m3u8.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.download.m3u8.common.ShareQueue;
import com.download.m3u8.process.DownloadFile;

public class ThreadDownloadFile implements Runnable {
    private final static Logger LOGGER = LoggerFactory.getLogger(ThreadDownloadFile.class);

    @Override
    public void run() {
        while (true) {
            try {

                ShareQueue.shareQueueDownload.forEach((k, v) -> {
                    new Thread(new DownloadFile(k)).start();
                });

                Thread.sleep(5000);
            } catch (Exception ex) {
                LOGGER.error("ERROR[ThreadDownloadFile]", ex);
            }
        }

    }
}