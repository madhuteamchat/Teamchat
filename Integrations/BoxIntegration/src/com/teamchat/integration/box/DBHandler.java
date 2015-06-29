package com.teamchat.integration.box;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBHandler {
	Connection conn;
	Statement stmt;
	ResultSet rs;
	Properties configProps;
	String path;
	
	DBHandler () {
		path = "/home/intern3/Documents/data/solutions-config/workflow-data/box/box-config.properties";
		configProps = new Properties();
		
		try {
			configProps = loadPropertyFileFromDisk(path);
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + configProps.getProperty("dbname").trim(), configProps.getProperty("dbuser").trim(), configProps.getProperty("dbpass").trim());
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setData (String email, String roomId, String state) {
		try {
			stmt.executeUpdate("delete from " + configProps.getProperty("tablename").trim() + " where email='" + email + "'");
			int c = stmt.executeUpdate("insert into "+ configProps.getProperty("tablename").trim() + " (email, roomid, state) values ('" + email + "', '" + roomId + "', '" + state + "')");
			if (c==1)
				System.out.println("Updated");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String[] getData (String state) {
		String data[] = new String[2];
		try {
			rs = stmt.executeQuery("Select email, roomid from " + configProps.getProperty("tablename").trim() + " where state='" + state + "'");
			rs.next();
			for (int i=0;i<2;i++)
				data[i] = rs.getString(i+1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public void setAccessData (String email, String accessToken, String refreshToken) {
		try {
			int c = stmt.executeUpdate("update " + configProps.getProperty("tablename").trim() + " set accesstoken='" + accessToken + "', refreshtoken='" + refreshToken + "' where email='" + email + "'");
			if (c==1)
				System.out.println("Updated");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getAccessData (String email) {
		String data = null;
		try {
			rs = stmt.executeQuery("Select accesstoken from " + configProps.getProperty("tablename").trim() + " where email='" + email + "'");
			rs.next();
			data = rs.getString("accesstoken");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public String getRoomId (String email) {
		String data = null;
		try {
			rs = stmt.executeQuery("Select roomid from " + configProps.getProperty("tablename").trim() + " where email='" + email + "'");
			rs.next();
			data = rs.getString("roomid");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public static Properties loadPropertyFileFromDisk(String filePath) throws Exception
	{
		String os = System.getProperty("os.name");
		if (os.toLowerCase().contains("windows")) {
			filePath = "d:" + filePath;
		}
		
		Properties configProp = new Properties();
		FileInputStream fIS = new FileInputStream(filePath);
		configProp.load(fIS);
		fIS.close();
		return configProp;
	}
}