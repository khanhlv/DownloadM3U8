package com.download.m3u8;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadRTMP {
	public void dowwnloadFile(String urlDownload,String perfix,int end) throws Exception {
		//String file = "0000";
		FileOutputStream fileOutputStream = new FileOutputStream(new File("D:/DownloadMedia/Media.mp4"));
		String file = perfix;
		for(int i=1;i <= end;i++) {
			if(perfix.length() == 1) {
				file = ""+i;
			}
			if(perfix.length() == 2) {
				if(i < 10) {
					file = "0"+i;
				}
				if(i >= 10 && i < 100) {
					file = ""+i;
				}
			}
			
			if(perfix.length() == 3) {
				if(i < 10) {
					file = "00"+i;
				}
				if(i >= 10 && i < 100) {
					file = "0"+i;
				}
				if(i >= 100 && i < 1000) {
					file = ""+i;
				}
			}
			
			if(perfix.length() == 4) {
				if(i < 10) {
					file = "000"+i;
				}
				if(i >= 10 && i < 100) {
					file = "00"+i;
				}
				if(i >= 100 && i < 1000) {
					file = "0"+i;
				}
				if(i >= 1000) {
					file = ""+i;
				}
			}
			
			if(perfix.length() == 5) {
				if(i < 10) {
					file = "0000"+i;
				}
				if(i >= 10 && i < 100) {
					file = "000"+i;
				}
				if(i >= 100 && i < 1000) {
					file = "00"+i;
				}
				if(i >= 1000 && i < 10000) {
					file = "0"+i;
				}
				if(i >= 10000) {
					file = ""+i;
				}
			}
			String linkDownload = String.format(urlDownload, file);
			System.out.println("-----------------------------------------------");
			System.out.println("## "+ linkDownload);
			URL url = new URL(linkDownload);
			URLConnection urlConnection = url.openConnection();
			
			InputStream inputStream = urlConnection.getInputStream();

			byte[] b = new byte[1024];
			int read = 0;
			while ((read = inputStream.read(b, 0, b.length)) != -1) {
				fileOutputStream.write(b, 0, read);
			}
			inputStream.close();
			System.out.println("==> DONE DOWNLOAD");
			
		}
		fileOutputStream.flush();
		fileOutputStream.close();
		System.out.println("DONE FILE");
		
	}
	
	public static void main(String[] args) {
		try {
			String file = "http://stv1hls.r15s91.vcdn.vn/streaming/f53b34bc0a510c08378ba59b9230a263/5459144f/Video480/2014/1102/a5/2913875e305901babda536840b3aefec/segment%s.ts";
			new DownloadRTMP().dowwnloadFile(file,"0",94);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
