package com.teamchat.integration.youtube.properties;

import java.io.IOException;
import java.util.Properties;

public class YoutubeProperty {
	
	String apikey="null";
	String client_id="null";
	String client_secret="null";
	String servlet_url="null";
	String redirect_url="null";
	String upload_url="null";
	
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
			props=loadPropertyFromClasspath("youtube.properties", YoutubeProperty.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	 
	    apikey = props.getProperty("apikey", "null");
	    client_id = props.getProperty("client_id", "null");
	    servlet_url = props.getProperty("servlet_url", "null");
	    upload_url = props.getProperty("upload_url", "null");
	    redirect_url = props.getProperty("redirect_url", "null");
	    client_secret = props.getProperty("client_secret", "null");
	}
	
	public String getApiKey()
	{
		return apikey;
	}
	
	public String getClientId()
	{
		return client_id;
	}
	
	public String getClientSecret()
	{
		return client_secret;
	}
	
	public String getServletUrl()
	{
		return servlet_url;
	}
	
	public String getRedirectUrl()
	{
		return redirect_url;
	}
	
	public String getUploadUrl()
	{
		return upload_url;
	}

}
