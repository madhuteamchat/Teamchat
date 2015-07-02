package com.teamchat.integrations.wunderlist;

import java.sql.DriverManager;
import java.sql.ResultSet;

//For giving response to users from the webhook servlet

public class WebhookDB {
	static java.sql.Connection connection = null;
	static java.sql.Statement stmt = null, stmt1 = null;
	static String dbUser = PropertiesFile.getValue("db_user");
	static String dbPassword = PropertiesFile.getValue("db_password");
	static String url = PropertiesFile.getValue("db_url");

	public static void insert(int uid, String rid, String at) {

		System.out.println("userid: " + uid + "room id" + rid);
		String insertQuery = "insert into webhookwl values (\"" + uid + "\",\""
				+ rid + "\", \"" + at + "\")";
		String updateQuery = "UPDATE webhookwl SET roomid=\"" + rid
				+ "\",accesstoken=\"" + at + "\" where userid=\"" + uid + "\"";
		try {
			Class.forName(PropertiesFile.getValue("class_for_name"));
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			stmt1 = connection.createStatement();
			stmt = connection.createStatement();
			try{
				stmt1.execute(insertQuery);
			}catch(Exception e){
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

	public static String retrieveRoomId(String id) {
		String token = null;
		String selQuery = "select roomid from webhookwl where userid=\"" + id
				+ "\"";
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

	public static void remove(int uid) {
		// TODO Auto-generated method stub
		String delQuery = "delete from webhookwl where userid=\"" + uid + "\"";
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

	public String retrieveAccessToken(String uid) {
		// TODO Auto-generated method stub
		String token = null;
		String selQuery = "select accesstoken from webhookwl where userid=\""
				+ uid + "\"";
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
}
