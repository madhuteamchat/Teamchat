package com.teamchat.integrations.wunderlist;

import java.io.IOException;
import java.util.Properties;

public class PropertiesFile {

	public static Properties loadPropertyFromClasspath(String fileName,
			Class<?> type) throws IOException {

		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;

	}

	public static String getValue(String name) {
		Properties prop = null;
		try {
			prop = loadPropertyFromClasspath("wunderlist.properties",
					PropertiesFile.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String value = prop.getProperty(name);
		return value;
	}
}
