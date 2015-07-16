package com.teamchat.integration.giphy.bot;
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

	String fileName = "com/teamchat/integration/giphy/bot/giphy-config.properties";
	public Properties configProps;
	
	String emailid,tag,url;
	
	String DB_URL = "jdbc:mysql://localhost:3306/";
	
	public DBHandler () {
		
try {
			
			configProps = loadPropertyFromClasspath(fileName, this.getClass());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				//establishing the connection with the database.
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(DB_URL+ configProps.getProperty("dbname").trim(), configProps.getProperty("dbuser").trim(), configProps.getProperty("dbpass").trim());
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
			
			int c = stmt.executeUpdate("insert into " + configProps.getProperty("tablename").trim() + " values ('" + emailid + "', '" + tag + "', '" + url + "')");
			if (c==1)
				System.out.println("Updated");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			conn.close();
		}
	}
	// checking if emailid is present in the database or not
public boolean emailchk(String emailid) throws SQLException {
		
		
			try {
				rs = stmt
						.executeQuery("select emailid from "+ configProps.getProperty("tablename").trim() +" where emailid='"+emailid+"'");
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
						.executeQuery("select tag from "+configProps.getProperty("tablename").trim()+" where emailid='"
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
	
	//checking if there are 5 same emailids or not.
public boolean countchk(String emailid) throws SQLException {
		
	// check if data exists in the server against that roomid
		
	String em=emailid.trim();
	int count =0;
			try {
				rs = stmt
						.executeQuery("select emailid from "+configProps.getProperty("tablename").trim()+" where emailid = '"+ em + "'");
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
	

//for getting tags from our database corresponding to particular emailid.

public String[] gettags(String emailid) throws SQLException {
		
		
	String[] tags = new String[5];
	try {
		rs = stmt.executeQuery("Select tag from "+configProps.getProperty("tablename").trim()+" where emailid='" + emailid + "'");
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

//for getting url from our database corresponding to particular emailid and tag.

public String geturl(String emailid,String tag) throws SQLException {
		
		
	String url;
	url="";
		
			try {
				rs = stmt
						.executeQuery("select url from "+configProps.getProperty("tablename").trim()+" where emailid='"
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
// loading property file.
public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException
{

	Properties prop = new Properties();
	prop.load(type.getClassLoader().getResourceAsStream(fileName));
	return prop;

}

}

