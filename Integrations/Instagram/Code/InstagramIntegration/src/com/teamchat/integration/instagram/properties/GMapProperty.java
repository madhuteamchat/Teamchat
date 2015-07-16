package com.teamchat.integration.instagram.properties;

import java.io.IOException;
import java.util.Properties;

public class GMapProperty {

	String apikey="null";
	
	public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException
	{

		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;

	}
	
	public void loadParams()
	{
	    Properties props = new Properties();
	    try {
			props=loadPropertyFromClasspath("googlemap.properties", GMapProperty.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	 
	    apikey = props.getProperty("apikey", "null");
	}
	
	public String getApiKey()
	{
		return apikey;
	}
	
}
