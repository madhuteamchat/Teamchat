package com.teamchat.integration.factory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertyFile {
	
	 static Properties prop = new Properties();
				
   public void setProperty(String username,String authtoken) {
	  OutputStream output = null;
		try {
	    //    System.err.println("/Yammer/src/com/teamchat/integration/factory/config.properties");
			output = new FileOutputStream("config.properties");
			System.err.println("setting authtoken for "+username+" value is"+authtoken);
	
			// set the properties value
			
			prop.setProperty(username, authtoken);
			// save properties to project root folder
			prop.store(output, null);
	 
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	 
		}
	  }
   
   public static String getProperty(String property) {
		Properties prop = new Properties();
	   String value="";
		try {
			 System.out.println("reading from property file");
			// load a properties file
			 prop.load(PropertyFile.class.getClassLoader().getResourceAsStream("config.properties"));
			value=prop.getProperty(property);
		} 
		catch (IOException ex) {
			ex.printStackTrace();
		} 
	   return value;
   }
   
   public static void main(String argv[]) {
	   PropertyFile pf=new PropertyFile();
	   System.out.println(pf.getProperty("HostName"));
   }
   }

