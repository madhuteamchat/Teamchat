package com.teamchat.integration.instagram.properties;

import java.io.IOException;
import java.util.Properties;

public class InstagramProperty {
	
	String client_id="null";
	String client_secret="null";
	String servlet_url,redirect_url,webhook_url;
	
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
			props=loadPropertyFromClasspath("instagram.properties", InstagramProperty.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	 
	    client_id = props.getProperty("client_id", "null");
	    client_secret = props.getProperty("client_secret", "null");
	    servlet_url = props.getProperty("servlet_url", "null");
	    redirect_url = props.getProperty("redirect_url", "null");
	    webhook_url = props.getProperty("webhook_url", "null");
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
	public String getWebhookUrl()
	{
		return webhook_url;
	}
}
