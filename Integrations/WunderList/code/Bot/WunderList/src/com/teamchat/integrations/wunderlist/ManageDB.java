package com.teamchat.integrations.wunderlist;

import java.sql.DriverManager;
import java.sql.ResultSet;

public class ManageDB {
	static java.sql.Connection connection = null;
	static java.sql.Statement stmt = null, stmt1 = null;
	static String dbUser = PropertiesFile.getValue("db_user");
	static String dbPassword = PropertiesFile.getValue("db_password");
	static String url = PropertiesFile.getValue("db_url");

	public static void insert(String id, String accessToken) {

		System.out.println("id: " + id + " Token : " + accessToken);
		String insertQuery = "insert into wunderlist values (\"" + id + "\",\""
				+ accessToken + "\")";
		String updateQuery = "UPDATE wunderlist SET accesstoken=\""
				+ accessToken + "\" where userid=\"" + id + "\"";
		try {
			Class.forName(PropertiesFile.getValue("class_for_name"));
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			stmt1 = connection.createStatement();
			stmt = connection.createStatement();
			if (!stmt1.execute(insertQuery)) {
				stmt.execute(updateQuery);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				stmt1.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String retrieve(String id) {
		String token = null;
		String selQuery = "select accesstoken from wunderlist where userid=\""
				+ id + "\"";
		try {
			Class.forName(PropertiesFile.getValue("class_for_name"));
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(selQuery);
			while (rs.next()) {
				token = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return token;
	}

	public static void remove(String mail) {
		// TODO Auto-generated method stub
		String delQuery = "delete from wunderlist where userid=\"" + mail
				+ "\"";
		try {
			Class.forName(PropertiesFile.getValue("class_for_name"));
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			stmt = connection.createStatement();
			stmt.execute(delQuery);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}