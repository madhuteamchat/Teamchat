package com.teamchat.integration.dropbox;

import java.io.IOException;
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
		configProps = new Properties();
		
		try {
			configProps = loadPropertyFromClasspath("dropbox-config.properties", DBHandler.class);
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + configProps.getProperty("dbname").trim(), configProps.getProperty("dbuser").trim(), configProps.getProperty("dbpass").trim());
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setAccessData (String email, String accessToken) {
		try {
			stmt.executeUpdate("delete from " + configProps.getProperty("tablename").trim() + " where email='" + email + "'");
			int c = stmt.executeUpdate("insert into "+ configProps.getProperty("tablename").trim() + " values ('" + email + "', '" + accessToken + "')");
			if (c==1)
				System.out.println("Inserted");
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String[] getAccessData (String email) {
		String data[] = new String[2];
		try {
			rs = stmt.executeQuery("Select * from " + configProps.getProperty("tablename").trim() + " where email='" + email + "'");
			rs.next();
			for (int i=0;i<2;i++)
				data[i] = rs.getString(i+1);
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getCause());
			return null;
		}
		return data;
	}
	
	public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException {
		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;
	}
}