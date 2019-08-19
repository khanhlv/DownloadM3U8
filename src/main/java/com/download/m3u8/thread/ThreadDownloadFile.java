package com.download.m3u8.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.download.m3u8.common.ShareQueue;
import com.download.m3u8.process.DownloadFile;

public class ThreadDownloadFile implements Runnable {
    private final static Logger LOGGER = LoggerFactory.getLogger(ThreadDownloadFile.class);

    public ThreadDownloadFile() {
        LOGGER.info("START_THREAD_DOWNLOAD");
    }

    @Override
    public void run() {
        while (true) {
            try {

                if (ShareQueue.shareQueueDownload.size() > 0) {
                    for (int i = 0; i < ShareQueue.shareQueueDownload.size(); i++) {
                        String link = ShareQueue.shareQueueDownload.poll();
                        new Thread(new DownloadFile(link)).start();
                    }
                }
                LOGGER.info(String.format("THREAD_DOWNLOAD[SIZE=%s]", ShareQueue.shareQueueDownload.size()));

                Thread.sleep(5000);
            } catch (Exception ex) {
                LOGGER.error("ERROR[ThreadDownloadFile]", ex);
            }
        }

    }
}