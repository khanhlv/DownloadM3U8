package com.download.m3u8.process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.download.m3u8.common.AppGlobal;
import com.download.m3u8.common.ShareQueue;

public class DownloadFile implements Runnable {
    private final static Logger LOGGER = LoggerFactory.getLogger(DownloadFile.class);

    private String link;

    public DownloadFile(String link) {
        this.link = link;
    }

    @Override
    public void run() {
        try {
            downloadFile();
        } catch (Exception ex) {
            LOGGER.error(String.format("[ERROR_FILE=%s]", link), ex);
            ShareQueue.shareQueue.add(link);
        }
    }

    public void downloadFile() throws Exception {

        String fileName = link.substring(link.lastIndexOf("/") + 1);

        if(fileName.contains("?")) {
            fileName = fileName.substring(0, fileName.indexOf("?"));
        }

        String htmlPath = link.substring(0, link.lastIndexOf("/"));
        String folder = htmlPath.substring(htmlPath.lastIndexOf(":") + 1);
        folder = folder.substring(0, folder.lastIndexOf("."));
        folder = AppGlobal.FOLDER_ROOT + folder;

        File f = new File(folder);

        if(!f.exists()) {
            f.mkdirs();
        }

        if(fileName.contains("?")) {
            fileName = fileName.substring(0, fileName.indexOf("?"));
        }

        if(new File(folder + fileName).exists()) {
            LOGGER.info(String.format("[EXISTS_FILE=%s]", fileName));
        } else {
            URL url = new URL(link);
            URLConnection urlConnection = url.openConnection();
            LOGGER.info(String.format("[START_DOWNLOAD_FILE=%s]", fileName));

            long start = System.currentTimeMillis();
            InputStream inputStream = urlConnection.getInputStream();

            FileOutputStream fileOutputStream = new FileOutputStream(new File(folder + fileName));

            byte[] b = new byte[1024];
            int read = 0;
            while ((read = inputStream.read(b, 0, b.length)) != -1) {
                fileOutputStream.write(b, 0, read);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();

            long fileLength = urlConnection.getContentLengthLong();

            long existingFileSize = new File(folder + fileName).length();

            if (existingFileSize < fileLength) {
                new File(folder + fileName).delete();
                LOGGER.info(String.format("[DELETE_DOWNLOAD_FILE=%s][FILE_SIZE=%s/%s]", fileName, fileLength, existingFileSize));
                ShareQueue.shareQueue.add(link);
            }

            if (existingFileSize == fileLength) {
                long end = (System.currentTimeMillis() - start) / 1000;
                LOGGER.info(String.format("[END_DOWNLOAD_FILE=%s][TIME=%s][FILE_SIZE=%s/%s]", fileName, end, fileLength, existingFileSize));
                ShareQueue.shareQueueDownload.remove(link);
            }
        }
    }
}
