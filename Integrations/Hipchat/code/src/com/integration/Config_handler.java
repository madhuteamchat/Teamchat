///**
// * 
// */
//package com.integration;
//
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.util.Properties;
//
///**
// * @author Himanshu Rathee NOTE: Please create a config.properties file in your
// *         classpath before using it in a live project ! The classpath is the
// *         folder in which build, src, etc. folders are located
// */
//public class Config_handler {
//	private String email, password, sql_username, sql_password;
//
//	Config_handler() {
//		Properties configProperties = getProperties();
//		email = configProperties.getProperty("email");
//		password = configProperties.getProperty("password");
//		sql_username = configProperties.getProperty("sql_username");
//		sql_password = configProperties.getProperty("sql_password");
//	}
//
//	// setters
//	public void setEmail(String value) {
//		setProperty("email", value, "app_settings");
//	}
//
//	public void setPassword(String value) {
//		setProperty("password", value, "app_settings");
//	}
//
//	public void setSql_username(String value) {
//		setProperty("sql_username", value, "app_settings");
//	}
//
//	public void setSql_password(String value) {
//		setProperty("sql_password", value, "app_settings");
//	}
//
//	// getters
//	public String getEmail() {
//		return email;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//		
//	public String getSql_username() {
//		return sql_username;
//	}
//
//	public String getSql_password() {
//		return sql_password;
//	}
//
//	public Boolean isEmpty() {
//		String[] values = { email, password, sql_username, sql_password };
//		for (String value : values) {
//			if (value == null) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	// methods
//	// initiate a Properties file with defaults
//	public void init_bot_Properties() {
//		String email = "himanshu.rathee@st.niituniversity.in",
//				password = "p@$$word4",
//				sql_username = "root",
//				sql_password = "pappupasshogaya";
//		// set local properties equal to these
//		this.email = email;
//		this.password = password;
//		this.sql_username = sql_username;
//		this.sql_password = sql_password;
//		Properties configProperties = getProperties();
//		File configFile = new File("config.properties");
//		FileWriter writer;
//		try {
//			writer = new FileWriter(configFile);
//			configProperties.setProperty("email", email);
//			configProperties.setProperty("password", password);
//			configProperties.setProperty("sql_username", sql_username);
//			configProperties.setProperty("sql_password", sql_password);
//			configProperties.store(writer, "app_settings");
//			writer.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	// get the default properties file
//	private Properties getProperties() {
//		// open file reader
//		FileReader reader = null;
//		Properties configProperties = new Properties();
//		try {
//			// load configuration file
//			reader = new FileReader(new File("config.properties"));
//			configProperties.load(reader);
//			reader.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return configProperties;
//	}
//
//	// change and store the property
//	private void setProperty(String property_name, String property_value,
//			String property_context) {
//		FileWriter writer = null;
//		try {
//			File configFile = new File("config.properties");
//			Properties configProperties = new Properties();
//			// setting values
//			configProperties.setProperty(property_name, property_value);
//			writer = new FileWriter(configFile);
//			configProperties.store(writer, property_context);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		// close
//		try {
//			writer.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//}