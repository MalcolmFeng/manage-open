package com.inspur.bigdata.manage.open.cloud.utils;

import java.io.*;
import java.net.URL;

public class FileReader {

	public static String getFile(String fileName) {
		String result = null;
		ClassLoader classLoader = FileReader.class.getClassLoader();
		URL url = classLoader.getResource(fileName);
		File file = new File(url.getFile());
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			result = new String(filecontent, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

}
