package com.teamchat.integrations.gotomeeting;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

public class GetProperties
{
	public static boolean loadPropertyFileFromDisk(String filePath,String user) throws Exception
	{
		String os = System.getProperty("os.name");
		if (os.toLowerCase().contains("windows"))
		{
			filePath = "d:" + filePath;

		}
		Properties configProp = new Properties();
		FileInputStream fIS = new FileInputStream(filePath);
		configProp.load(fIS);
		return configProp.containsKey(user);
	}

	public static void storePropertyFileToDisk(Properties property, String filePath, String comments) throws Exception
	{
		String os = System.getProperty("os.name");
		if (os.toLowerCase().contains("windows"))
		{
			filePath = "d:" + filePath;

		}
		OutputStream output = new FileOutputStream(filePath);
		property.store(output, comments);
		output.close();
	}
	
	
	public boolean check(String user) {
	 
		boolean exists=false;
		
		try
		{
			String propFilePath = "/home/intern7/workspace/gotomeeting/resources/config.properties";
			//Properties props = loadPropertyFileFromDisk(propFilePath,user);
			exists=loadPropertyFileFromDisk(propFilePath, user);
			//props.setProperty("namwww", "Prakhar,jdnckjnc");
			//storePropertyFileToDisk(props, propFilePath, "This is a new property");
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exists;
		
	}
	public void push(String user,String gotoemail,String gotopwd) throws Exception{
		String propFilePath = "/home/intern7/workspace/gotomeeting/resources/config.properties";
		Properties props = loadPropertyFileFromDisk1(propFilePath,user);
		props.setProperty(user, gotoemail+","+gotopwd);
		storePropertyFileToDisk(props, propFilePath, "This is a new property");
		}
	
	public static Properties loadPropertyFileFromDisk1(String filePath,String user) throws Exception
	{
		String os = System.getProperty("os.name");
		if (os.toLowerCase().contains("windows"))
		{
			filePath = "d:" + filePath;

		}
		Properties configProp = new Properties();
		FileInputStream fIS = new FileInputStream(filePath);
		configProp.load(fIS);
		return configProp;
		
	}

	public String[] pull(String user) throws Exception{
		String propFilePath = "/home/intern7/workspace/gotomeeting/resources/config.properties";
		String str[]=new String[2];
		str=loadPropertyFileFromDisk2(propFilePath, user).split(",");
		return str;
	}
	
	public static String loadPropertyFileFromDisk2(String filePath,String user) throws Exception
	{
		String os = System.getProperty("os.name");
		if (os.toLowerCase().contains("windows"))
		{
			filePath = "d:" + filePath;

		}
		Properties configProp = new Properties();
		FileInputStream fIS = new FileInputStream(filePath);
		configProp.load(fIS);
		return configProp.getProperty(user);
		
	}


}
