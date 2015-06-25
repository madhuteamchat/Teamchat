/**
 * 
 */
package com.teamchat.integrations.basecamp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * @author Puranjay Jain
 *
 */
public class Db_handler {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private static final String DB_URL = "jdbc:mysql://localhost/Bot?user=root&password=pappupasshogaya";

	// storing data handler
	public boolean StorageHandler(String email, String href, Token token) {
		// check if token already exists or not
		if (CheckToken(email, href, token)) {
			return StoreToken(email, href, token);
		} else {
			return UpdateToken(email, href, token);
		}
	}

	// check if data exists in the server against that email
	private boolean CheckToken(String email, String href, Token token) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(DB_URL);
			statement = connect.createStatement();
			resultSet = statement
					.executeQuery("select email from authorized where email='"
							+ email + "'");
			// check if result set is empty or not
			while (resultSet.next()) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		// default case
		return true;
	}

	// set the authentication data into the table
	private boolean StoreToken(String email, String href, Token token) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(DB_URL);
			statement = connect.createStatement();
			preparedStatement = connect
					.prepareStatement("insert into authorized values (default, ?, ?, ?, ?, ?, ?)");
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, href);
			preparedStatement.setString(3, token.getAccess_token());
			preparedStatement.setString(4, token.getRefresh_token());
			preparedStatement.setString(5, token.getExpires_in());
			preparedStatement.setBoolean(6, true);
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
	private boolean UpdateToken(String email, String href, Token token) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(DB_URL);
			statement = connect.createStatement();
			preparedStatement = connect
					.prepareStatement("update authorized set access_token = ?, refresh_token = ?, expires_in = ?, authenticated = ? where email = ?");
			preparedStatement.setString(0, token.getAccess_token());
			preparedStatement.setString(1, token.getRefresh_token());
			preparedStatement.setString(2, token.getExpires_in());
			preparedStatement.setBoolean(3, true);
			preparedStatement.setString(4, email);
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// helper classes for future reference (should be removed in production
	// version)

	public void readDataBase() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager.getConnection(DB_URL);

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery("select * from feedback.comments");
			writeResultSet(resultSet);

			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect
					.prepareStatement("insert into  feedback.comments values (default, ?, ?, ?, ? , ?, ?)");
			// "myuser, webpage, datum, summery, COMMENTS from feedback.comments");
			// Parameters start with 1
			preparedStatement.setString(1, "Test");
			preparedStatement.setString(2, "TestEmail");
			preparedStatement.setString(3, "TestWebpage");
			preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
			preparedStatement.setString(5, "TestSummary");
			preparedStatement.setString(6, "TestComment");
			preparedStatement.executeUpdate();
			preparedStatement = connect
					.prepareStatement("SELECT myuser, webpage, datum, summery, COMMENTS from feedback.comments");
			resultSet = preparedStatement.executeQuery();
			writeResultSet(resultSet);
			// Remove again the insert comment
			preparedStatement = connect
					.prepareStatement("delete from feedback.comments where myuser= ? ; ");
			preparedStatement.setString(1, "Test");
			preparedStatement.executeUpdate();
			resultSet = statement
					.executeQuery("select * from feedback.comments");
			writeMetaData(resultSet);
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}
	}

	private void writeMetaData(ResultSet resultSet) throws SQLException {
		// Now get some metadata from the database
		// Result set get the result of the SQL query

		System.out.println("The columns in the table are: ");

		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
			System.out.println("Column " + i + " "
					+ resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			String user = resultSet.getString("myuser");
			String website = resultSet.getString("webpage");
			String summery = resultSet.getString("summery");
			Date date = resultSet.getDate("datum");
			String comment = resultSet.getString("comments");
			System.out.println("User: " + user);
			System.out.println("Website: " + website);
			System.out.println("Summery: " + summery);
			System.out.println("Date: " + date);
			System.out.println("Comment: " + comment);
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

	public static void main(String[] args) {// This will load the MySQL driver,
											// each DB has its own driver
	// Setup the connection with the DB
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connect = DriverManager.getConnection(DB_URL);
			connect.close();
			System.out.println("success!!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
