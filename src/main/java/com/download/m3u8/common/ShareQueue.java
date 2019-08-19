package com.download.m3u8.common;

import java.util.concurrent.ConcurrentLinkedQueue;

public final class ShareQueue {
    public final static int THREAD_DOWNLOAD = 6;

    public static ConcurrentLinkedQueue<String> shareQueue = new ConcurrentLinkedQueue<>();
    public static ConcurrentLinkedQueue<String> shareQueueDownload = new ConcurrentLinkedQueue<>();
}
