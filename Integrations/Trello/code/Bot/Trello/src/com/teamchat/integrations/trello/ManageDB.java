package com.teamchat.integrations.trello;

import java.sql.DriverManager;
import java.sql.ResultSet;

public class ManageDB {
	static java.sql.Connection connection = null;
	static java.sql.Statement stmt = null, stmt1 = null;
	static String dbUser = PropertiesFile.getValue("db_user");
	static String dbPassword = PropertiesFile.getValue("db_password");
	static String url = PropertiesFile.getValue("db_url");

	public static void insert(String mail, String requestToken,
			String requestsecret, String accessToken, String room_id,
			String webhook_id, String trello_id) {
		// TODO Auto-generated method stub
		String insertQuery = "insert into trello values (\"" + mail + "\",\""
				+ requestToken + "\",\"" + requestsecret + "\",\""
				+ accessToken + "\",\"" + room_id + "\",\"" + webhook_id
				+ "\",\"" + trello_id + "\")";
		String updateQuery = "update trello SET trelloid=\"" + trello_id
				+ "\", requesttoken=\"" + requestToken + "\", accesstoken=\""
				+ accessToken + "\",roomid=\"" + room_id + "\",webhookid=\""
				+ webhook_id + "\" where mail=\"" + mail + "\"";

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
				stmt1.close();
				stmt.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static String retrieveRequestSecret(String requesttoken) {
		String token = null;
		String selQuery = "select requestsecret from trello where requesttoken=\""
				+ requesttoken + "\"";
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

	public static String retrieveAccessToken(String mail) {
		String token = null;
		String selQuery = "select accesstoken from trello where mail=\"" + mail
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

	public static void remove(String mail) {
		// TODO Auto-generated method stub
		String delQuery = "delete from trello where mail=\"" + mail + "\"";
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

	public static void updateAccessTokenandTrelloID(String requestToken,
			String accessToken, String trello_id) {
		// TODO Auto-generated method stub
		String updateQuery = "update trello SET  trelloid=\"" + trello_id
				+ "\", accesstoken=\"" + accessToken
				+ "\" where requesttoken=\"" + requestToken + "\"";
		try {
			Class.forName(PropertiesFile.getValue("class_for_name"));
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			stmt = connection.createStatement();
			stmt.execute(updateQuery);
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

	public static void updateWebhookID(String access_token, String webhook_id) {
		// TODO Auto-generated method stub
		String updateQuery = "update trello SET  webhookid=\"" + webhook_id
				+ "\" where accesstoken=\"" + access_token + "\"";
		try {
			Class.forName(PropertiesFile.getValue("class_for_name"));
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			stmt = connection.createStatement();
			stmt.execute(updateQuery);
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

	public static String getRoomIDbyToken(String requesttoken) {
		// TODO Auto-generated method stub
		String room_id = null;
		String selQuery = "select roomid from trello where requesttoken=\""
				+ requesttoken + "\"";
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

	public static void stopNotifications(String mail) {
		// TODO Auto-generated method stub
		String updateQuery = "update trello SET  webhookid=null where mail=\""
				+ mail + "\"";
		try {
			Class.forName(PropertiesFile.getValue("class_for_name"));
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			stmt = connection.createStatement();
			stmt.execute(updateQuery);
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

	public static String getRoomIDbyTrelloID(String trello_id) {
		// TODO Auto-generated method stub
		String room_id = null;
		String selQuery = "select roomid from trello where trelloid=\""
				+ trello_id + "\"";
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

	public static String getWebhookIDbyToken(String accessToken) {
		// TODO Auto-generated method stub
		String webhook_id = null;
		String selQuery = "select webhookid from trello where accesstoken=\""
				+ accessToken + "\"";
		try {
			Class.forName(PropertiesFile.getValue("class_for_name"));
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(selQuery);
			while (rs.next()) {
				webhook_id = rs.getString(1);
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
		return webhook_id;
	}
}