package com.inspur.bigdata.manage.open.cloud.utils;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	public static Properties getProperties(String properties) {
		try {
			InputStream in = PropertiesUtil.class.getClassLoader()
					.getResourceAsStream(properties);
			Properties p = new Properties();
			p.load(in);
			return p;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static String getValue(String properties,String key){
		Properties p = getProperties(properties);
		return p.getProperty(key);
	}
}
