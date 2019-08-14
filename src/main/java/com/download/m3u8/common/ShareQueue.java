package com.download.m3u8.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class ShareQueue {
    public final static int THREAD_DOWNLOAD = 10;

    public static ConcurrentLinkedQueue<String> shareQueue = new ConcurrentLinkedQueue<>();
    public static Map<String, String> shareQueueDownload = new HashMap<>();
}
