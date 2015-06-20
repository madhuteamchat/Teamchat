package com.teamchat.integrations.evernote;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertyFile {
	
	static Properties prop = new Properties();
	static InputStream input=null;
	static OutputStream output = null;
	static String propFileName="/home/intern9/config.properties";
	
	public static void setProperty(String email,String accessToken) throws IOException{
		output = new FileOutputStream(propFileName);
		prop.setProperty(email,accessToken);
		prop.store(output,null);
		if(output!=null){
			output.close();
		}
	}
	
	public static String getProperty(String property) throws IOException{
		String value="";
		try (FileReader reader = new FileReader(propFileName)) {
			Properties properties = new Properties();
			properties.load(reader);
			value = properties.getProperty(property);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	return value;	
	}
	
	public static void remove(String mail) throws FileNotFoundException {
		// TODO Auto-generated method stub
		output = new FileOutputStream(propFileName);
		try (FileReader reader = new FileReader(propFileName)) {
			Properties properties = new Properties();
			properties.load(reader);
			properties.remove(mail);
			properties.store(output, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
