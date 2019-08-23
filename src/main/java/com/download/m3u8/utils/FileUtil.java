package com.download.m3u8.utils;

import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.download.m3u8.common.AppGlobal;
import com.download.m3u8.common.ShareQueue;

public class FileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    public static String fileName(String link) {
        String fileName = link.substring(link.lastIndexOf("/") + 1);

        if(fileName.contains("?")) {
            fileName = fileName.substring(0, fileName.indexOf("?"));
        }

        fileName = fileName.substring(fileName.lastIndexOf("_") + 1);

        return fileName;
    }

    public static String folderInputName(String link, String folderName) {
        String htmlPath = link.substring(0, link.lastIndexOf("/"));
        String folder = htmlPath.substring(htmlPath.lastIndexOf(":") + 1);
        folder = folder.substring(0, folder.lastIndexOf("."));
        folder = AppGlobal.FOLDER_ROOT + StringUtil.stripAccentsNone(folderName, "_") + "/" + folder + "/INPUT/";

        return folder;
    }

    public static void writeObjectToFile(Object serObj) {
        FileOutputStream fileOut = null;
        ObjectOutputStream objectOut = null;
        try {
            fileOut = new FileOutputStream("data/ShareQueue.obj");
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            LOGGER.info(String.format("OBJECT_WRITE[SHARE_QUEUE=%s]", ShareQueue.shareQueue.size()));
        } catch (Exception ex) {
            LOGGER.error("ERROR[writeObjectToFile]", ex);
        }  finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {

                }
            }
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {

                }
            }
        }
    }

    public static Object readObjectFromFile() {
        FileInputStream fileInput = null;
        ObjectInputStream objectInput = null;

        try {
            fileInput = new FileInputStream("data/ShareQueue.obj");
            objectInput = new ObjectInputStream(fileInput);

            LOGGER.info(String.format("OBJECT_READ[SHARE_QUEUE=%s]", ShareQueue.shareQueue.size()));

            return objectInput.readObject();
        } catch (Exception ex) {
            LOGGER.error("ERROR[readObjectFromFile]", ex);
            return null;
        } finally {
            if (objectInput != null) {
                try {
                    objectInput.close();
                } catch (IOException e) {

                }
            }
            if (fileInput != null) {
                try {
                    fileInput.close();
                } catch (IOException e) {

                }
            }
        }
    }
}
