package com.download.m3u8.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;

import com.download.m3u8.common.AppGlobal;

public class JoinFile {
    public void join(String urlM3U8) {
        try {
            urlM3U8 = AppGlobal.makeUrl(urlM3U8);

            String htmlPath = urlM3U8.substring(0, urlM3U8.lastIndexOf("/"));
            String folderPath = htmlPath.substring(htmlPath.lastIndexOf(":") + 1);
            String fileName = folderPath.substring(folderPath.lastIndexOf("/") + 1);
            String folderPathInput = AppGlobal.FOLDER_ROOT + folderPath.substring(0, folderPath.lastIndexOf(".")) + "/INPUT/";
            String folderPathOutput = AppGlobal.FOLDER_ROOT + folderPath.substring(0, folderPath.lastIndexOf(".")) + "/OUTPUT/";

            File f = new File(folderPathInput);

            if (f.exists()) {
                File fO = new File(folderPathOutput);
                if(!fO.exists()) {
                    fO.mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(new File(folderPathOutput + fileName));
                File[] listFile = f.listFiles();

                Arrays.sort(listFile, new Comparator<File>() {

                    @Override
                    public int compare(File o1, File o2) {
                        int f1 = extractNumber(o1.getName());
                        int f2 = extractNumber(o2.getName());
                        return f1-f2;
                    }

                    private int extractNumber(String fileName) {
                        int i = 0;
                        try {
                            fileName = fileName.substring(fileName.lastIndexOf("_")+1);
                            fileName = fileName.substring(0,fileName.indexOf("."));
                            i = Integer.parseInt(fileName);
                        } catch (Exception e) {
                            i = 0;
                        }
                        return i;
                    }

                });

                for (int i = 0; i < listFile.length; i++) {
                    String path = folderPathInput + listFile[i].getName();
                    File fD = new File(path);
                    if (!fD.exists()) {
                        break;
                    }
                    System.out.println(path);
                    InputStream inputStream = new FileInputStream(path);

                    byte[] b = new byte[1024];
                    int read = 0;
                    while ((read = inputStream.read(b, 0, b.length)) != -1) {
                        fileOutputStream.write(b, 0, read);
                    }
                    inputStream.close();

                    //fD.delete();
                }
                fileOutputStream.flush();
                fileOutputStream.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
