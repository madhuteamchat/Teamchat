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
	@SuppressWarnings("unused")
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	private static Config_handler config = new Config_handler();
	private static String DB_URL = "jdbc:mysql://localhost/Bot?user="
			+ config.getSql_username() + "&password="
			+ config.getSql_password();

	Db_handler() {
		if (config.isEmpty()) {
			config.init_bot_Properties();
		}
	}

	// get base camp api's basic stuff
	public Basecamp_basics GetBasicStuff(String email) {
		Basecamp_basics bb = new Basecamp_basics();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(DB_URL);
			statement = connect.createStatement();
			resultSet = statement
					.executeQuery("select * from authorized where email = '"
							+ email + "'");
			resultSet.next();
			bb.setAccess_token(resultSet.getString("access_token"));
			bb.setEmail(resultSet.getObject("email").toString());
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
					.executeQuery("select email from authorized where email='"
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
