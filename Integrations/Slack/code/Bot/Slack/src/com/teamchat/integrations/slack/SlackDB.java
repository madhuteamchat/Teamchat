//INTEGRATION: SLACK

package com.teamchat.integrations.slack;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class SlackDB {
	// JDBC driver name and database URL
	
	public static Properties loadPropertyFromClasspath(String fileName,
			Class<?> type) throws IOException {
		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;

	}
	
	static String JDBC_DRIVER = new String();//"com.mysql.jdbc.Driver";
	static String DB_URL = new String();//"jdbc:mysql://localhost/slack";	
	
	// Database credentials

	// Pick creds from the properties file.

	static String USER = new String();
	static String PASS = new String();

		public static void saveCode(String email, String code) throws IOException {
		Connection conn = null;
		Statement stmt = null;

		Properties prop1 = loadPropertyFromClasspath("slack.properties",
				Slack.class);

		System.out.println("Reading database credentials from properties file.");
		System.out.println(prop1.getProperty("client_id"));
		
		JDBC_DRIVER=prop1.getProperty("JDBC_DRIVER");
		DB_URL = prop1.getProperty("DB_URL");
	
		USER = prop1.getProperty("USER").trim();
		System.out.println(prop1.getProperty("client_secret").trim());
		PASS = prop1.getProperty("PASS");

		try {
			// Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// Opening connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");

			// Execute query
			System.out.println("Inserting records into the table...");
			stmt = conn.createStatement();

			String s = "Select email FROM Slack";
			ResultSet rs1 = stmt.executeQuery(s);

			while (rs1.next()) {
				if (rs1.getString("email").equals(email)) {

					String delQuery = "DELETE FROM Slack WHERE email = \""
							+ email + "\"";
					System.out.println("Repeated email, deleting entry");
					stmt.execute(delQuery);
					break;
				}
			}

			System.err.println("Inserting value");
			String sql = "INSERT INTO Slack " + "VALUES (\"" + email + "\",\""
					+ code + "\", \"" + null + "\")"; // null is access token
														// for now
			System.err.println("Inserted value");
			stmt.executeUpdate(sql);
			System.out.println("Inserted records into the table...");
		} catch (SQLException se) {
			// Handle errors for JDBC
			System.err.println("JDBC ERROR");
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			System.err.println("CLASS.FORNAME ERROR");
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
			// Slack.wait = 1; //Ask the slack.java bot to proceed to get access
			// token

		System.out.println("Goodbye from save code function!");
	}

	public static String useCode(String email) throws IOException {
		Connection conn = null;
		Statement stmt = null;
		String output = new String();
		
		Properties prop1 = loadPropertyFromClasspath("slack.properties",
				Slack.class);

		
		JDBC_DRIVER=prop1.getProperty("JDBC_DRIVER");
		DB_URL = prop1.getProperty("DB_URL");
		
		System.out.println("Reading database credentials from properties file.");
		System.out.println(prop1.getProperty("client_id"));
		USER = prop1.getProperty("USER").trim();
		System.out.println(prop1.getProperty("client_secret").trim());
		PASS = prop1.getProperty("PASS");

		try {
			// STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");

			// STEP 4: Execute a query
			stmt = conn.createStatement();

			String sql = "SELECT code FROM Slack WHERE email=\"" + email + "\"";

			System.err.println("Executing code select query");

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				output = rs.getString("code");
			}

		} catch (SQLException se) {
			// Handle errors for JDBC
			System.err.println("JDBC ERROR");
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			System.err.println("CLASS.FORNAME ERROR");
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
		System.out.println("Goodbye from access code function!");
		return output;
	}// end main

	public static void saveToken(String email, String access_token) throws IOException {
		Connection conn = null;
		Statement stmt = null;
		
		Properties prop1 = loadPropertyFromClasspath("slack.properties",
				Slack.class);

		
		JDBC_DRIVER=prop1.getProperty("JDBC_DRIVER");
		DB_URL = prop1.getProperty("DB_URL");
		
		System.out.println("Reading database credentials from properties file.");
		System.out.println(prop1.getProperty("client_id"));
		USER = prop1.getProperty("USER").trim();
		System.out.println(prop1.getProperty("client_secret").trim());
		PASS = prop1.getProperty("PASS");

		try {
			// Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// Opening connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");

			// Execute query
			System.out
					.println("Inserting (access token) records into the table...");
			stmt = conn.createStatement();

			String s = "Select email FROM Slack";
			ResultSet rs1 = stmt.executeQuery(s);

			/*
			 * while (rs1.next()) { if (rs1.getString("email").equals(email)) {
			 * 
			 * String delQuery = "DELETE FROM Slack WHERE email = \"" + email +
			 * "\""; System.out.println("Repeated email, deleting entry");
			 * stmt.execute(delQuery); break; } }
			 */
			System.err.println("Inserting value");
			// String sql = "INSERT INTO Slack " + "VALUES (\"" + email +
			// "\",\""+ code + "\", \"" + null + "\")"; //null is access token
			// for now

			while (rs1.next()) {
				if (rs1.getString("email").equals(email)) {

					String updateQuery = "UPDATE Slack SET access_token = \""
							+ access_token + "\" WHERE Slack.email = \""
							+ email + "\"";
					System.out.println("Repeated email, deleting entry");
					stmt.execute(updateQuery);
					System.err.println("Inserted value");
					break;
				}
			}

			System.out.println("Inserted access token into the table...");
		} catch (SQLException se) {
			// Handle errors for JDBC
			System.err.println("JDBC ERROR");
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			System.err.println("CLASS.FORNAME ERROR");
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
			// Slack.wait = 1; //Ask the slack.java bot to proceed to get access
			// token

		System.out.println("Goodbye from save access token function!");
	}

	public static String useToken(String email) throws IOException {
		Connection conn = null;
		Statement stmt = null;
		String output = new String();
		
		Properties prop1 = loadPropertyFromClasspath("slack.properties",
				Slack.class);
		
		
		JDBC_DRIVER=prop1.getProperty("JDBC_DRIVER");
		DB_URL = prop1.getProperty("DB_URL");

		System.out.println("Reading database credentials from properties file.");
		System.out.println(prop1.getProperty("client_id"));
		USER = prop1.getProperty("USER").trim();
		System.out.println(prop1.getProperty("client_secret").trim());
		PASS = prop1.getProperty("PASS");

		try {
			// STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");

			// STEP 4: Execute a query
			stmt = conn.createStatement();

			String sql = "SELECT access_token FROM Slack WHERE email=\""
					+ email + "\"";

			System.err.println("Executing access token select query");

			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				output = rs.getString("access_token");
			}

		} catch (SQLException se) {
			// Handle errors for JDBC
			System.err.println("JDBC ERROR");
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			System.err.println("CLASS.FORNAME ERROR");
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
		System.out.println("Goodbye from get access token function!");
		return output;
	}// end main
}
