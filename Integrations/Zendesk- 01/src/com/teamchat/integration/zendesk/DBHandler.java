package com.teamchat.integration.zendesk;

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
	
	String roomId, formId, ticketId, requesterId, comment;
	
	DBHandler () {
		try {
			configProps = loadPropertyFromClasspath("zendesk-config.properties", DBHandler.class);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + configProps.getProperty("dbname").trim(), configProps.getProperty("dbuser").trim(), configProps.getProperty("dbpass").trim());
			stmt = conn.createStatement();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException {
		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;
	}
	
	public void setData (String roomId, String formId, String ticketId, String requesterId, String comment) {
		this.roomId = roomId;
		this.formId = formId;
		this.ticketId = ticketId;
		this.requesterId = requesterId;
		this.comment = comment;
		
		try {
			System.out.println("insert into " + configProps.getProperty("tablename").trim() + " values ('" + roomId + "', '" + formId + "', '" + ticketId + "', '"
						+ requesterId + "', '" + comment + "')");
			int c = stmt.executeUpdate("insert into " + configProps.getProperty("tablename").trim() + " values ('" + roomId + "', '" + formId + "', '" + ticketId + "', '"
					+ requesterId + "', '" + comment + "')");
			if (c==1)
				System.out.println("Updated");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String[] getData (String formId) {
		String[] data = new String[5];
		try {
			rs = stmt.executeQuery("Select * from " + configProps.getProperty("tablename").trim() + " where formid='" + formId + "'");
			rs.next();
			for (int i=0;i<5;i++)
				data[i] = rs.getString(i+1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public String[] getRoomForm (String ticketId) {
		String[] roomForm = new String[2];
		String query = "Select roomid, formid from " + configProps.getProperty("tablename").trim() + " where ticketid='" + ticketId + "'";
		try {
			rs = stmt.executeQuery(query);
			rs.next();
			roomForm[0] = rs.getString(1);
			roomForm[1] = rs.getString(2);
		} catch (SQLException e) {
			System.out.println(e.getCause());
		}
		return roomForm;
	}
}