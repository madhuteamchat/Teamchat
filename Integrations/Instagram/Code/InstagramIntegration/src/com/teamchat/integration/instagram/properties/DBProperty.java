package com.teamchat.integration.instagram.properties;

import java.io.IOException;
import java.util.Properties;

public class DBProperty {

	String dbuser="null";
	String dbpass="null";
	String dbname="null";
	String dbdrivername="null";
	String dburl="null";
	
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
			props=loadPropertyFromClasspath("database.properties", DBProperty.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
	 
	    dbuser = props.getProperty("dbuser", "null");
	    dbpass = props.getProperty("dbpass", "null");
	    dbname = props.getProperty("dbname", "null");
	    dbdrivername = props.getProperty("dbdrivername", "null");
	    dburl = props.getProperty("dburl", "null");
	}
	
	public String getDBUser()
	{
		return dbuser;
	}
	
	public String getDBPass()
	{
		return dbpass;
	}
	
	public String getDBName()
	{
		return dbname;
	}
	
	public String getDBDriverName()
	{
		return dbdrivername;
	}
	
	public String getDBURL()
	{
		return dburl;
	}
}
