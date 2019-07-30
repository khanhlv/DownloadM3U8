package com.download.m3u8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ReadFileM3U8 {

	public static ArrayList<String> listFinish = new ArrayList<String>();

	public void downloadThread(String urlM3U8) {
		
		ConcurrentLinkedQueue<String> listLink = readFile(urlM3U8);
		while (true) {
			if (listLink.size() > 0) {
				if (listFinish.size() < AppGlobal.THREAD_NUMBER) {

					int count = listFinish.size() == 0 ? AppGlobal.THREAD_NUMBER : AppGlobal.THREAD_NUMBER - listFinish.size();
					for (int i = 0; i < count; i++) {
						if(listLink.size() > 0) {
							String linkDownload = listLink.poll();
							listFinish.add(linkDownload);

							String htmlPath = linkDownload.substring(0, linkDownload.lastIndexOf("/"));
							String folderPath = htmlPath.substring(htmlPath.lastIndexOf(":") + 1);
							folderPath = folderPath.substring(0, folderPath.lastIndexOf("."));
		
							ThreadDownload threadDownload = new ThreadDownload();
							threadDownload.setFolderDownload(AppGlobal.FOLDER_ROOT + folderPath + "/" + "INPUT/");
							threadDownload.setLinkDownload(linkDownload);
		
							new Thread(threadDownload).start();
						}
						
					}
				}

			}

			try {
				Thread.sleep(5000);
				System.out.println("Connection ...");
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(listLink.size() == 0 && listFinish.size() == 0) {
				System.out.println("##Join file ..... ");
				joinFile(urlM3U8);
				break;
			}
			
			
		}

	}

	public void joinFile(String urlM3U8) {

		try {
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
						// TODO Auto-generated method stub
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

	private ConcurrentLinkedQueue<String> listLink = new ConcurrentLinkedQueue<String>();
	
	public ConcurrentLinkedQueue<String> readFile(String urlM3U8) {
		
		try {

			
			URL url = new URL(urlM3U8);
	        
			URLConnection urlConnection = url.openConnection();

			String htmlPath = urlM3U8.substring(0, urlM3U8.lastIndexOf("/"));

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				if(line.contains(".m3u8")) {
					if(line.contains("http://")) {
						readFile(line);
					} else {
						readFile(htmlPath + "/" + line);
					}
				}
				if (line.contains(".ts")) {
					listLink.add(htmlPath + "/" + line);
					//System.out.println(htmlPath + "/" + line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listLink;
	}

	public static void main(String[] args) {
		try {
			
			String url = "http://dbbvppqb.cdnviet.com/medianet_vod/_definst_/mp4:medianet/yeah1/DEN_XANH_TINH_BAN/YSP_DXTB_010.mp4/playlist.m3u8".replaceAll("\\s+", "+");
			//new ReadFileM3U8().downloadThread(url);
			new ReadFileM3U8().joinFile(url);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}