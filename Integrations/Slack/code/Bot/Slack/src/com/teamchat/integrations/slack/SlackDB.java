//INTEGRATION: SLACK

//STATUS:UNUSED

/*package com.teamchat.integrations.slack;

import java.sql.*;

public class SlackDB {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/Bot";

	// Database credentials
	static final String USER = "tcinterns";
	static final String PASS = "PakyovBosh7";

	public static void saveCode(String email, String code) {
		Connection conn = null;
		Statement stmt = null;
		try {
			//Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			//Opening connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");

			//Execute query
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
					+ code + "\")";
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
		//Slack.wait = 1; //Ask the slack.java bot to proceed to get access token
		
		System.out.println("Goodbye from save code function!");
	}

	public static String useCode(String email) {
		Connection conn = null;
		Statement stmt = null;
		String output = new String();
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

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

}
*/