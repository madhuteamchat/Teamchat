package com.teamchat.integration.mailchimp;

import java.sql.DriverManager;
import java.sql.ResultSet;

public class ManageDB {

	static java.sql.Connection connection = null;
	static java.sql.Statement stmt = null;
	static String dbUser = PropertiesFile.getValue("db_user");
	static String dbPassword = PropertiesFile.getValue("db_password");
	static String url =PropertiesFile.getValue("db_url");

	public static void insert(String smail, String accessToken, String us) {

		String insertQuery = "insert into mailchimp values (\"" + smail
				+ "\",\"" + accessToken + "\",\"" + us+ "\")";

		try {
			Class.forName(PropertiesFile.getValue("class_for_name"));
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			stmt = connection.createStatement();
			stmt.execute(insertQuery);

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

	public static String retrieve(String smail) {
		String token = null;
		//String datacenter=null;
	String selQuery = "select acctoken from mailchimp where smail=\"" +smail
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
	
	
	public static String retrieve1(String smail) {
		//String token = null;
		String datacenter=null;
	String selQuery = "select datacenter from mailchimp where smail=\"" +smail
			+ "\"";
	try {
		Class.forName(PropertiesFile.getValue("class_for_name"));
		connection = DriverManager.getConnection(url, dbUser, dbPassword);
		stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(selQuery);
	while (rs.next()) {
			datacenter=rs.getString(1);
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
		return datacenter;
	}
	

	public static void upadate(String smail,String acctoken,String us) {

		String updateQuery="UPDATE mailchimp SET acctoken=\""+acctoken+"\", datacenter=\""+us+"\" where smail=\""+smail+"\""; 
		

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

	public static void remove(String smail) {
		// TODO Auto-generated method stub
		String delQuery="delete from mailchimp where smail=\""+smail+"\""; 
    	try
	     {
	            Class.forName(PropertiesFile.getValue("class_for_name"));
	            connection = DriverManager.getConnection(url, dbUser, dbPassword);
	            stmt = connection.createStatement();
	            stmt.execute(delQuery);
	      }
	      catch (Exception e) {
	            e.printStackTrace();
	      }finally {
	          try {
	                stmt.close();
	                connection.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	}
	
}
