package com.download.m3u8;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.download.m3u8.common.AppGlobal;
import com.download.m3u8.common.ShareQueue;
import com.download.m3u8.parser.UdemyVietNam;
import com.download.m3u8.process.ReadFile;
import com.download.m3u8.thread.ThreadShareQueue;
import com.download.m3u8.utils.FileUtil;
import com.download.m3u8.utils.StringUtil;

public class Application {
    private final static Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private final ReadFile readFile = new ReadFile();

    private void addQueue(String user, String pass) throws Exception {
        if (new File("data/ShareQueue.obj").exists()) {
            ShareQueue.shareQueue = (ConcurrentLinkedQueue<String>) FileUtil.readObjectFromFile("data/ShareQueue.obj");
            return;
        }

        UdemyVietNam udemyVietNam = new UdemyVietNam();
        udemyVietNam.readCourse(user, pass).forEach(d -> {
            System.out.println(d.getID_COURSE());
            System.out.println(StringUtil.stripAccentsNone(d.getCOURSE_NAME(), "_"));
            try {
                udemyVietNam.readPlayCourse(d.getID_COURSE()).forEach(v -> {
                    ConcurrentLinkedQueue<String> linkList = readFile.read(String.format("http://210.211.96.151:1935/vod/_definst_/mp4:%s/playlist.m3u8", v.getId()), d.getCOURSE_NAME());
                    ShareQueue.shareQueue.addAll(linkList);
                });
            } catch (Exception ex) {
                LOGGER.error("ERROR[addQueue]", ex);
            }
        });

        FileUtil.writeObjectToFile(ShareQueue.shareQueue, "data/ShareQueue.obj");

        FileUtil.writeObjectToFile(ShareQueue.shareQueue, "data/ShareQueue_BK.obj");

        // 210.211.96.151:1935
//        udemyVietNam.readPlayCourse("59").forEach(v -> {
//            ConcurrentLinkedQueue<String> linkList = readFile.read(String.format("http://210.211.96.151:1935/vod/_definst_/mp4:%s/playlist.m3u8", v.getId()));
//            ShareQueue.shareQueue.addAll(linkList);
//        });
    }

    private void execute(String user, String pass) {
        try {

            addQueue(user, pass);

            ShareQueue.shareQueue.forEach(v -> {
                try {
                    checkFileExists(v);
                } catch (Exception ex) {
                    LOGGER.error("ERROR[Application]", ex);
                }
            });

            FileUtil.writeObjectToFile(ShareQueue.shareQueue, "data/ShareQueue.obj");

            Thread.sleep(5000);

            new Thread(new ThreadShareQueue()).start();

        } catch (Exception e) {
            LOGGER.error("ERROR[Application]",e);
        }
    }

    public void checkFileExists(String linkFile) throws Exception {

        String[] structFile = linkFile.split("\\|");

        String link = structFile[1];

        String fileName = FileUtil.fileName(link);

        String folder = FileUtil.folderInputName(link, structFile[0]);

        if(new File(folder + fileName).exists()) {
            LOGGER.info(String.format("[EXISTS_FILE=%s]", fileName));

            URL url = new URL(link);
            URLConnection urlConnection = url.openConnection();

            long fileLength = urlConnection.getContentLengthLong();

            File fileExists = new File(folder + fileName);

            long existingFileSize = fileExists.length();

            if (existingFileSize < fileLength) {
                fileExists.delete();
                LOGGER.info(String.format("[DELETE_DOWNLOAD_FILE=%s][FILE_SIZE=%s/%s]", fileExists.getName(), fileLength, existingFileSize));
            }

            if (existingFileSize == fileLength) {
                LOGGER.info(String.format("[COMPLETED_DOWNLOAD_FILE=%s][FILE_SIZE=%s/%s]", fileExists.getName(), fileLength, existingFileSize));
                ShareQueue.shareQueue.remove(linkFile);
            }
        }
    }

    public static void main(String[] args) {
        Application application = new Application();
        if (args != null && args.length == 2) {
            application.execute(args[0], args[1]);
        } else {
            application.execute(AppGlobal.USER_NAME_1, AppGlobal.PASSWORD_1);
        }
    }
}
