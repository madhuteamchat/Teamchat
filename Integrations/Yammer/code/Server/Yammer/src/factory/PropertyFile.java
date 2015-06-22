package factory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertyFile {
	
	 static Properties prop = new Properties();
	
	 
   public static void setProperty(String username,String authtoken) {
	   OutputStream output = null;
		try {
	 
			output = new FileOutputStream("config.properties");
	        System.out.println("setting authtoken for "+username+" value is"+authtoken);
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
	   InputStream input = null;
	   String value="";
		try {
			 
			input = PropertyFile.class.getResourceAsStream("/config.properties");
	 		// load a properties file
			prop.load(input);
	 
			// get the property value and print it out
			value=prop.getProperty(property);
	 
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	   return value;
   }
   }

