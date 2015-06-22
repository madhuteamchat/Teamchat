/**
 * 
 */
package com.Maintenance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author Puranjay Jain
 *
 */
public class Db_handler {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private static final String DB_URL = "jdbc:mysql://localhost/configdb?user=root&password=pappupasshogaya";

	// set the authentication data into the table
	public boolean Store(String email, String customer_name, String worker_name,
			int room_number, String problem, Boolean work_status,String api_context) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(DB_URL);
			statement = connect.createStatement();
			preparedStatement = connect
					.prepareStatement("insert into customers values (default, ?, ?, ?, ?, ?, ?, ?)");
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, customer_name);
			preparedStatement.setString(3, worker_name);
			preparedStatement.setInt(4, room_number);
			preparedStatement.setString(5, problem);
			preparedStatement.setBoolean(6, work_status);
			preparedStatement.setString(7, api_context);
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			close();
		}
	}
	
	//update status
	public boolean UpdateStatus(String api_context,Boolean status){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(DB_URL);
			statement = connect.createStatement();
			preparedStatement = connect
					.prepareStatement("update customers set work_status = ? where api_context = ?");
			preparedStatement.setBoolean(1, status);
			preparedStatement.setString(2, api_context);
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			close();
		}
	}

	// get the data
	public ArrayList<Customer> Get() {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		String name, problem;
		int id;
		Boolean status;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(DB_URL);
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select * from customers");
			while (resultSet.next()) {
				id = (int) resultSet.getObject("id");
				name = (String) resultSet.getObject("customer_name");
				problem = (String) resultSet.getObject("problem");
				status = (Boolean) resultSet.getObject("work_status");
				customers.add(new Customer(id,name,problem,status));
			}
			return customers;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			close();
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
