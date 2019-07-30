package com.download.m3u8;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ThreadDownload implements Runnable {

	private String linkDownload;
	private String folderDownload;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			dowwnloadFile();
		} catch (Exception e) {
			System.out.println("##Loi "+e+" file: " + linkDownload);
		}
	}
	
	public void dowwnloadFile() throws Exception {
		File f = new File(folderDownload);
		if(!f.exists()) {
			f.mkdirs();
		}
		String fileName = linkDownload.substring(linkDownload.lastIndexOf("/") + 1);
		if(fileName.contains("?")) {
			fileName = fileName.substring(0, fileName.indexOf("?"));
		}
		if(new File(folderDownload + fileName).exists()) {
			System.out.println("## Exists file: " + fileName);
			ReadFileM3U8.listFinish.remove(linkDownload);
		} else {
			URL url = new URL(linkDownload);
			
			URLConnection urlConnection = url.openConnection();
			//urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");
			System.out.println("## Start: " + fileName);
			long start = System.currentTimeMillis();
			InputStream inputStream = urlConnection.getInputStream();

			FileOutputStream fileOutputStream = new FileOutputStream(new File(folderDownload + fileName));
			
			byte[] b = new byte[1024];
			int read = 0;
			while ((read = inputStream.read(b, 0, b.length)) != -1) {
				fileOutputStream.write(b, 0, read);
			}
			fileOutputStream.flush();
			fileOutputStream.close();
			inputStream.close();
			long end = (System.currentTimeMillis() - start) / 1000;
			ReadFileM3U8.listFinish.remove(linkDownload);
			double speed =  0;
			System.out.println("## Finish: " + fileName + " ##Time: " + end + "s" + " ##Speed: " +speed+ " Kbps");
			System.out.println("###########################################################");
		}
	
	}
	
	

	public String getLinkDownload() {
		return linkDownload;
	}

	public void setLinkDownload(String linkDownload) {
		this.linkDownload = linkDownload;
	}

	public String getFolderDownload() {
		return folderDownload;
	}

	public void setFolderDownload(String folderDownload) {
		this.folderDownload = folderDownload;
	}
	

}
