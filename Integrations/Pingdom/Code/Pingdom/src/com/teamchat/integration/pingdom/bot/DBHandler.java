package com.teamchat.integration.pingdom.bot;


/*
 * *
 * @author:Anuj Arora
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class DBHandler {
	Connection conn;
	public Statement stmt;
	ResultSet rs;
	String fileName = "com/teamchat/integration/pingdom/bot/pingdom-config.properties";
	public Properties configProps;
	
	String appkey,username,pass,roomId;
	
	String DB_URL = "jdbc:mysql://localhost:3306/";
	
	public DBHandler () {
				//establishing the connection with the database.
		try {
			
			configProps = loadPropertyFromClasspath(fileName, this.getClass());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(DB_URL+ configProps.getProperty("dbname").trim(), configProps.getProperty("dbuser").trim(), configProps.getProperty("dbpass").trim());
			stmt = conn.createStatement();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//insertion of data into your database.
	public void setData (String username, String pass, String appkey, String roomId) throws SQLException {
		this.username = username;
		this.pass = pass;
		this.appkey = appkey;
		this.roomId = roomId;
		
		
		try {
			
			int c = stmt.executeUpdate("insert into " + configProps.getProperty("tablename").trim() + " values ('" + username + "', '" + pass + "', '" + appkey + "', '" + roomId + "')");
			if (c==1)
				System.out.println("Updated");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			conn.close();
		}
	}
	
	
	
	//for getting the roomid from our database corresponding to particular username.
	public String getRoom (String username) throws SQLException {
		String room;
		room="";
		try {
			rs = stmt.executeQuery("Select roomid from " + configProps.getProperty("tablename").trim() + " where username='" + username + "'");
			rs.next();
			room = rs.getString(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			conn.close();
		}
		return room;
	}
	
	public boolean dchk(String username) throws SQLException {
		
		// check if data exists in the server against that email
		
			try {
				rs = stmt
						.executeQuery("select username from "+configProps.getProperty("tablename").trim()+" where username='"
								+ username + "'");
				// check if result set is empty or not
				while (rs.next()) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				conn.close();
			} 
			
			// default case
			return false;
		}
	
	
public boolean roomchk(String roomId) throws SQLException {
		
	// check if data exists in the server against that roomid
		
	String rmm=roomId.trim();
			try {
				rs = stmt
						.executeQuery("select roomId from "+configProps.getProperty("tablename").trim()+" where roomId = '"+ rmm + "'");
				// check if result set is empty or not
				while (rs.next()) {
					
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			// default case
			return false;
		}
	

//for getting username password and appkey from our database corresponding to particular roomid.

public String[] getusrpassapp(String roomId) throws SQLException {
		
		
	String[] usrpassapp = new String[3];
	try {
		rs = stmt.executeQuery("Select username, pass, appkey from "+configProps.getProperty("tablename").trim()+" where roomId='" + roomId + "'");
		rs.next();
		usrpassapp[0] = rs.getString(1);
		usrpassapp[1] = rs.getString(2);
		usrpassapp[2] = rs.getString(3);

	} catch (SQLException e) {
		e.printStackTrace();
	}finally {
		conn.close();
	}
	return usrpassapp;
			
			
		}

//for getting app-key from our database corresponding to particular username.

public String getapp(String username) throws SQLException {
		
		
	String appkey;
	appkey="";
		
			try {
				rs = stmt
						.executeQuery("select appkey from "+configProps.getProperty("tablename").trim()+" where username='"
								+ username + "'");
				
				rs.next();
				appkey = rs.getString(1);
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			// default case
			
			return appkey;
			
		}
public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException
{

	Properties prop = new Properties();
	prop.load(type.getClassLoader().getResourceAsStream(fileName));
	return prop;

}

}
