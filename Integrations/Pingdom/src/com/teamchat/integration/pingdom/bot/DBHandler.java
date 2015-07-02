package com.teamchat.integration.pingdom.bot;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBHandler {
	Connection conn;
	public Statement stmt;
	ResultSet rs;
	//String path = "/home/anuj-intern22/Desktop/gup/eclipse/ZendeskBot/data/zendesk-config.properties";
	//public Properties configProps;
	
	String appkey,username,pass,roomId;
	
	String DB_URL = "jdbc:mysql://localhost:3306/Bot?user=root&password=Anuj";
	
	public DBHandler () {
				
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(DB_URL);
			stmt = conn.createStatement();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void setData (String username, String pass, String appkey, String roomId) {
		this.username = username;
		this.pass = pass;
		this.appkey = appkey;
		this.roomId = roomId;
		
		
		try {
			System.out.println("insert into " + "pingdom_auth" + " values ('" + username + "', '" + pass + "', '" + appkey + "', '" + roomId + "')");
			int c = stmt.executeUpdate("insert into " + "pingdom_auth" + " values ('" + username + "', '" + pass + "', '" + appkey + "', '" + roomId + "')");
			if (c==1)
				System.out.println("Updated");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public String getRoom (String username) {
		String room;
		room="";
		try {
			rs = stmt.executeQuery("Select roomid from " + "pingdom_auth" + " where username='" + username + "'");
			rs.next();
			room = rs.getString(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return room;
	}
	
	public boolean dchk(String username) {
		
		// check if data exists in the server against that email
		
			try {
				rs = stmt
						.executeQuery("select username from pingdom_auth where username='"
								+ username + "'");
				// check if result set is empty or not
				while (rs.next()) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
			// default case
			return false;
		}
	
public String getapp(String username) {
		
		// check if data exists in the server against that email
	String appkey;
	appkey="";
		
			try {
				rs = stmt
						.executeQuery("select appkey from pingdom_auth where username='"
								+ username + "'");
				
				rs.next();
				appkey = rs.getString(1);
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
			// default case
			
			return appkey;
			
		}
}