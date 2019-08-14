package com.download.m3u8.thread;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.download.m3u8.common.ShareQueue;

public class ThreadShareQueue implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadShareQueue.class);

    public ThreadShareQueue() {
        System.out.println("START_THREAD_QUEUE");
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (ShareQueue.shareQueueDownload.size() < ShareQueue.THREAD_DOWNLOAD) {
                    int size = ShareQueue.THREAD_DOWNLOAD - ShareQueue.shareQueueDownload.size();
                    if (ShareQueue.shareQueue.size() > 0) {
                        for (int i = 0; i < size; i++) {
                            String link = ShareQueue.shareQueue.poll();

                            ShareQueue.shareQueueDownload.put(link, link);
                        }
                    }
                }

                LOGGER.info("SHARE_QUEUE=" + ShareQueue.shareQueue.size());

                Thread.sleep(10000);
            }
        } catch (Exception ex) {
            LOGGER.error("ERROR[ThreadShareQueue]", ex);
        }
    }
}