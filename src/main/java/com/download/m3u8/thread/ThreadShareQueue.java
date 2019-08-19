package com.download.m3u8.thread;

import com.download.m3u8.common.ShareQueue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadShareQueue implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadShareQueue.class);

    public ThreadShareQueue() {
        LOGGER.info("START_THREAD_QUEUE");
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
                            if (StringUtils.isNotBlank(link)) {
                                ShareQueue.shareQueueDownload.add(link);
                            }
                        }
                    }
                }

                LOGGER.info(String.format("THREAD_SHARE[QUEUE=%s][DOWNLOAD=%s]",ShareQueue.shareQueue.size(),ShareQueue.shareQueueDownload.size()));

                Thread.sleep(20000);
            }
        } catch (Exception ex) {
            LOGGER.error("ERROR[ThreadShareQueue]", ex);
        }
    }
}