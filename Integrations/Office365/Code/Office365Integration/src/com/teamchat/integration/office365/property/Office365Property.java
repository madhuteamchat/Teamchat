package com.teamchat.integration.office365.property;

import java.io.IOException;
import java.util.Properties;

public class Office365Property {

	String client_id="null";
	String client_secret="null";
	String redirect_url="null";
	String login_url="null";
	
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
			props=loadPropertyFromClasspath("office365.properties", Office365Property.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	    client_id = props.getProperty("client_id", "null");
	    redirect_url = props.getProperty("redirect_url", "null");
	    login_url = props.getProperty("login_url", "null");
	    client_secret = props.getProperty("client_secret", "null");
	}
	
	public String getClientId()
	{
		return client_id;
	}
	
	public String getClientSecret()
	{
		return client_secret;
	}
	
	public String getRedirectUrl()
	{
		return redirect_url;
	}
	
	public String getLoginUrl()
	{
		return login_url;
	}
	
}
