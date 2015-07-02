package com.teamchat.integrations.googleanalytics;

import java.sql.DriverManager;
import java.sql.ResultSet;

public class ManageDB {
	static java.sql.Connection connection = null;
	static java.sql.Statement stmt = null, stmt1 = null;
	static String dbUser = PropertiesFile.getValue("db_user");
	static String dbPassword = PropertiesFile.getValue("db_password");
	static String url = PropertiesFile.getValue("db_url");

	public static void insert(String mail, String accessToken,
			String refreshToken, String room_id) {
		// TODO Auto-generated method stub
		String insertQuery = "insert into ganalytics values (\"" + mail
				+ "\",\"" + accessToken + "\",\"" + refreshToken + "\",\""
				+ room_id + "\")";
		String updateQuery = "update ganalytics SET refreshtoken=\""
				+ refreshToken + "\", accesstoken=\"" + accessToken
				+ "\",roomid=\"" + room_id + "\" where mail=\"" + mail + "\"";

		try {
			Class.forName(PropertiesFile.getValue("class_for_name"));
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			stmt = connection.createStatement();
			stmt1 = connection.createStatement();
			if (!stmt.execute(insertQuery)) {
				stmt1.execute(updateQuery);
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
	}

	public static String retrieveAccessToken(String mail) {
		String token = null;
		String selQuery = "select accesstoken from ganalytics where mail=\""
				+ mail + "\"";
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
		String delQuery = "delete from ganalytics where mail=\"" + mail + "\"";
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

	public static void update(String mail, String accesstoken,
			String refreshtoken) {
		// TODO Auto-generated method stub
		String updateQuery = "update ganalytics SET refreshtoken=\""
				+ refreshtoken + "\", accesstoken=\"" + accesstoken
				+ "\" where mail=\"" + mail + "\"";

		try {
			Class.forName(PropertiesFile.getValue("class_for_name"));
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			stmt = connection.createStatement();
			if (!stmt.execute(updateQuery)) {

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

	}

	public static String getRoomID(String sname) {
		// TODO Auto-generated method stub
		String room_id = null;
		String selQuery = "select roomid from ganalytics where mail=\"" + sname
				+ "\"";
		try {
			Class.forName(PropertiesFile.getValue("class_for_name"));
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(selQuery);
			while (rs.next()) {
				room_id = rs.getString(1);
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
		return room_id;
	}

	public static void updateAccesstoken(String accesstoken, String refreshtoken) {
		// TODO Auto-generated method stub
		String updateQuery = "update ganalytics SET accesstoken=\""
				+ accesstoken + "\" where refreshtoken=\"" + refreshtoken
				+ "\"";

		try {
			Class.forName(PropertiesFile.getValue("class_for_name"));
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			stmt = connection.createStatement();
			if (!stmt.execute(updateQuery)) {

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

	}

	public static String getrefreshToken(String access_token) {
		// TODO Auto-generated method stub
		String token = null;
		String selQuery = "select refreshtoken from ganalytics where accesstoken=\""
				+ access_token + "\"";
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