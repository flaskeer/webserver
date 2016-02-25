package com.hao.util;

import java.io.File;
import java.nio.file.Files;





public class FileUtil {

	
		public static void listAllFiles(File file){
			if(file.exists() && file.length() > 0){
				if(file.isDirectory()){
					File[] listFiles = file.listFiles();
					for (int i = 0; i < listFiles.length; i++) {
						if(listFiles[i].isDirectory()){
							listAllFiles(listFiles[i]);
						}else{
							System.out.println("--" + listFiles[i].getName());
						}
					}
				}
			}
		}
	
	
	public static void main(String[] args) {
		listAllFiles(new File("d:\\java"));
	}
	
}
