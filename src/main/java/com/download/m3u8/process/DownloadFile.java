package com.download.m3u8.process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.download.m3u8.common.ShareQueue;
import com.download.m3u8.utils.FileUtil;

public class DownloadFile implements Runnable {
    private final static Logger LOGGER = LoggerFactory.getLogger(DownloadFile.class);

    private String linkFile;

    public DownloadFile(String linkFile) {
        this.linkFile = linkFile;
    }

    @Override
    public void run() {
        try {
            LOGGER.info(String.format("[LINK_FILE=%s]", linkFile));
            downloadFile();
        } catch (Exception ex) {
            ShareQueue.shareQueue.add(linkFile);
            LOGGER.error(String.format("[ERROR_FILE=%s]", linkFile), ex);
        }
    }

    public void downloadFile() throws Exception {

        String[] structFile = linkFile.split("\\|");

        String link = structFile[1];

        String fileName = FileUtil.fileName(link);

        String folder = FileUtil.folderInputName(link, structFile[0]);

        File f = new File(folder);

        if(!f.exists()) {
            f.mkdirs();
        }

        if(new File(folder + fileName).exists()) {
            LOGGER.info(String.format("[EXISTS_FILE=%s]", fileName));

            URL url = new URL(link);
            URLConnection urlConnection = url.openConnection();

            long fileLength = urlConnection.getContentLengthLong();

            File fileExists = new File(folder + fileName);

            checkFileValid(fileLength, fileExists);
        } else {
            URL url = new URL(link);
            URLConnection urlConnection = url.openConnection();
            LOGGER.info(String.format("[START_DOWNLOAD_FILE=%s]", fileName));

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

            File fileExists = new File(folder + fileName);

            checkFileValid(fileLength, fileExists);
        }
    }

    private void checkFileValid(long fileLength, File fileExists) {
        long existingFileSize = fileExists.length();

        if (existingFileSize < fileLength) {
            fileExists.delete();
            LOGGER.info(String.format("[DELETE_DOWNLOAD_FILE=%s][FILE_SIZE=%s/%s]", fileExists.getName(), fileLength, existingFileSize));
            ShareQueue.shareQueue.add(linkFile);
            ShareQueue.shareQueueDownload.remove(linkFile);
        }

        if (existingFileSize == fileLength) {
            LOGGER.info(String.format("[COMPLETED_DOWNLOAD_FILE=%s][FILE_SIZE=%s/%s]", fileExists.getName(), fileLength, existingFileSize));
            ShareQueue.shareQueueDownload.remove(linkFile);
        }

    }

    public static void main(String[] args) {
        try {
            new DownloadFile("http://210.211.96.151:1935/vod/_definst_/mp4:Tambooks/Lamita_01/1.mp4/media_w559670374_0.ts?index=1").downloadFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
