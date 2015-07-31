package com.teamchat.integration.linkedin;

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

	DBHandler() {
		configProps = new Properties();

		try {
			configProps = loadPropertyFromClasspath(
					"linkedin-config.properties", DBHandler.class);
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
					+ configProps.getProperty("dbname").trim(), configProps
					.getProperty("dbuser").trim(),
					configProps.getProperty("dbpass").trim());
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Properties loadPropertyFromClasspath(String fileName,
			Class<?> type) throws IOException {
		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;
	}

	public void setData(String reqTok, String reqSec, String roomId,
			String email) {
		try {
			stmt.executeUpdate("delete from "
					+ configProps.getProperty("linktable").trim());
			int c = stmt.executeUpdate("insert into "
					+ configProps.getProperty("linktable").trim()
					+ " values ('" + reqTok + "', '" + reqSec + "', '" + roomId
					+ "', '" + email + "')");
			if (c == 1)
				System.out.println("Updated");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String[] getData(String reqTok) {
		String[] data = new String[4];
		try {
			rs = stmt.executeQuery("Select * from "
					+ configProps.getProperty("linktable").trim()
					+ " where reqTok='" + reqTok + "'");
			rs.next();
			for (int i = 0; i < 4; i++)
				data[i] = rs.getString(i + 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}

	public void setAccessData(String email, String accessToken,
			String accessSecret) {
		try {
			stmt.executeUpdate("delete from "
					+ configProps.getProperty("accesstable").trim()
					+ " where email='" + email + "'");
			int c = stmt.executeUpdate("insert into "
					+ configProps.getProperty("accesstable").trim()
					+ " values ('" + email + "', '" + accessToken + "', '"
					+ accessSecret + "')");
			if (c == 1)
				System.out.println("Updated");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String[] getAccessData(String email) {
		String[] data = new String[3];
		try {
			rs = stmt.executeQuery("Select * from "
					+ configProps.getProperty("accesstable").trim()
					+ " where email='" + email + "'");
			rs.next();
			for (int i = 0; i < 3; i++)
				data[i] = rs.getString(i + 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public int remove(String email) {
		int success=0;
		
		try {
			success = stmt.executeUpdate("delete from "
					+ configProps.getProperty("accesstable").trim()
					+ " where email='" + email + "'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return success;
	}
	
	public int check (String email) {
		int acc=1;
		
		try {
			rs = stmt.executeQuery("Select * from "
					+ configProps.getProperty("accesstable").trim()
					+ " where email='" + email + "'");
			if (!rs.next()) {
				acc=0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return acc;
	}
}