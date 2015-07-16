package com.teamchat.integration.giphy.bot;
/*
 * *
 * @author:Anuj Arora
 */
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
	
	String emailid,tag,url;
	
	String DB_URL = "jdbc:mysql://localhost:3306/Bot?user=root&password=Anuj";
	
	public DBHandler () {
				//establishing the connection with the database.
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(DB_URL);
			stmt = conn.createStatement();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//insertion of data into your database.
	public void setData (String emailid, String tag, String url) throws SQLException {
		this.emailid =emailid;
		this.tag = tag;
		this.url = url;
		
		try {
			System.out.println("insert into " + "giphy_auth" + " values ('" + emailid + "', '" + tag + "', '" + url + "')");
			int c = stmt.executeUpdate("insert into " + "giphy_auth" + " values ('" + emailid + "', '" + tag + "', '" + url + "')");
			if (c==1)
				System.out.println("Updated");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			conn.close();
		}
	}
	
public boolean emailchk(String emailid) throws SQLException {
		
		// check if data exists in the server against that email
		
			try {
				rs = stmt
						.executeQuery("select emailid from giphy_auth where emailid='"
								+ emailid + "'");
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
	
	
	public boolean dchk(String emailid, String tag) throws SQLException {
		
		// check if data exists in the server against that email
		
			try {
				rs = stmt
						.executeQuery("select tag from giphy_auth where emailid='"
								+ emailid + "' and tag='" + tag + "'");
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
	
	
public boolean countchk(String emailid) throws SQLException {
		
	// check if data exists in the server against that roomid
		
	String em=emailid.trim();
	int count =0;
			try {
				rs = stmt
						.executeQuery("select emailid from giphy_auth where emailid = '"+ em + "'");
				// check if result set is empty or not
				
				while (rs.next()) {
					
					count++;
				}
				if(count ==5)
				{
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

public String[] gettags(String emailid) throws SQLException {
		
		
	String[] tags = new String[5];
	try {
		rs = stmt.executeQuery("Select tag from giphy_auth"+" where emailid='" + emailid + "'");
		int i=0;
		while (rs.next()) {
		tags[i] = rs.getString(1);
		i++;
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}finally {
		conn.close();
	}
	return tags;
			
			
		}

//for getting app-key from our database corresponding to particular username.

public String geturl(String emailid,String tag) throws SQLException {
		
		
	String url;
	url="";
		
			try {
				rs = stmt
						.executeQuery("select url from giphy_auth where emailid='"
								+ emailid + "' and tag='"+ tag + "'");
				
				rs.next();
				url = rs.getString(1);
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn.close();
			}
			// default case
			
			return url;
			
		}
}

