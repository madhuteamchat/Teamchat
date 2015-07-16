/**
 * 
 */
package com.teamchat.integrations.basecamp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Puranjay Jain
 *
 */
public class Db_handler {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	// private static Config_handler config = new Config_handler();
	// replace with server credentials
	private static String DB_URL = "jdbc:mysql://localhost/"
			+ Universal.DB_NAME + "?user=" + Universal.DB_USERNAME
			+ "&password=" + Universal.DB_PASSWORD;

	// Db_handler() {
	// if (config.isEmpty()) {
	// config.init_bot_Properties();
	// config.init_auth_Properties();
	// }
	// }

	// get base camp api's basic stuff
	public Basecamp_basics GetBasicStuff(String email) {
		Basecamp_basics bb = new Basecamp_basics();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(DB_URL);
			statement = connect.createStatement();
			resultSet = statement
					.executeQuery("select * from basecamp_authorized where email = '"
							+ email + "'");
			resultSet.next();
			bb.setAccess_token(resultSet.getString("access_token"));
			bb.setEmail(resultSet.getObject("email").toString());
			bb.setBasecamp_email(resultSet.getObject("basecamp_email")
					.toString());
			bb.setExpires_in(resultSet.getObject("expires_in").toString());
			bb.setHref(resultSet.getObject("href").toString());
			bb.setRefresh_token(resultSet.getObject("refresh_token").toString());
			return bb;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			close();
		}
	}

	// check if data exists in the server against that email
	public boolean isAuthorized(String email) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(DB_URL);
			statement = connect.createStatement();
			resultSet = statement
					.executeQuery("select email from basecamp_authorized where email='"
							+ email + "'");
			// check if result set is empty or not
			while (resultSet.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		// default case
		return false;
	}

	// storing data handler
	public boolean StorageHandler(Basecamp_basics bb) {
		// check if token already exists or not
		if (isAuthorized(bb.getEmail())) {
			return UpdateToken(bb);
		} else {
			return StoreToken(bb);
		}
	}

	// delete user data
	public boolean Delete(String email) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(DB_URL);
			statement = connect.createStatement();
			preparedStatement = connect
					.prepareStatement("DELETE FROM basecamp_authorized WHERE email = ?");
			preparedStatement.setString(1, email);
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			close();
		}
	}

	// set the authentication data into the table
	private boolean StoreToken(Basecamp_basics bb) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(DB_URL);
			statement = connect.createStatement();
			preparedStatement = connect
					.prepareStatement("insert into basecamp_authorized values (default, ?, ?, ?, ?, ?, ?, ?)");
			preparedStatement.setString(1, bb.getEmail());
			preparedStatement.setString(2, bb.getBasecamp_email());
			preparedStatement.setString(3, bb.getHref());
			preparedStatement.setString(4, bb.getAccess_token());
			preparedStatement.setString(5, bb.getRefresh_token());
			preparedStatement.setString(6, bb.getExpires_in());
			preparedStatement.setBoolean(7, true);
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			close();
		}
	}

	// update the authentication data into the table
	private boolean UpdateToken(Basecamp_basics bb) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(DB_URL);
			statement = connect.createStatement();
			preparedStatement = connect
					.prepareStatement("update basecamp_authorized set access_token = ?, refresh_token = ?, expires_in = ?, authenticated = ? where email = ?");
			preparedStatement.setString(1, bb.getAccess_token());
			preparedStatement.setString(2, bb.getRefresh_token());
			preparedStatement.setString(3, bb.getExpires_in());
			preparedStatement.setBoolean(4, true);
			preparedStatement.setString(5, bb.getEmail());
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// You need to close the resultSet
	private void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
