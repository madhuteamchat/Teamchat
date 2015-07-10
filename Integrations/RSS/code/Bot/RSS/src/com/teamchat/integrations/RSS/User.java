package com.teamchat.integrations.RSS;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class User {
	String URL="jdbc:mysql://localhost/Bot";
	String DRIVER1="com.mysql.jdbc.Driver";
	String USER ="root";
	String PASSWORD ="abhishek";
    static Connection con=null;
    static Statement stmt;
    String email;
	
    User(String email)
	{ if(con==null)
		    	connect();
	this.email=email;
	} 
	
	User(String email, String Channel)
	{ if(con==null)
		    	connect();
	this.email=email;
	addChannel(Channel);
	} 
	
public void connect()
{
	if(con==null)
		    	{
		    		try {
		    			Class.forName(DRIVER1);
		    				con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
		    				Statement stmt =(Statement) con.createStatement();
		    				
		    		} catch (ClassNotFoundException e) {
		    			e.printStackTrace();
		    			System.out.println("=====Class not found exception while loading the driver======");
		    		}
		    		catch (SQLException e) {
		    			e.printStackTrace();
		    			System.out.println("=====SQL exception found while connection with the driver======");
		    		}	
	   	}
}

public  User addChannel( String url)
{
	try{
    	String insertTableSQL = "INSERT INTO Bot.RSS "
    			+ "(Email, Channel, LastUpdated,LastFeedID) VALUES"
    			+ "(?,?,?,?)";
    	PreparedStatement preparedStatement = con.prepareStatement(insertTableSQL);
    	preparedStatement.setString(1,email);
    	preparedStatement.setString(2,url);
    	preparedStatement.setString(3, null);
    	preparedStatement.setString(4, null);
    	
    	// execute insert SQL stetement
    	preparedStatement .executeUpdate();
    	} catch(SQLException e)
    	{
    		System.out.println("SQL addChannel: ");
    		e.printStackTrace();
    	}	
	return this;
}
public String[][] getChannels(String email)
{   String ans[][]=new String[3][];
	String channels="",lastfeedid="",lastupdated="";
	String query = "Select * from Bot.RSS where Email='"+email+"'" ;
 	if(con==null)
 		connect();
    try {
 				Statement stmt =(Statement) con.createStatement();
 				ResultSet  rs =  stmt.executeQuery(query);
 				while(rs.next())
 				{
 				  channels+="-"+rs.getString("Channel");
 				  lastfeedid+="-"+rs.getString("LastFeedID");
 				  lastupdated+="-"+rs.getString("LastUpdated");
 				}
 				ans[0]= channels.split("-");
 				ans[1]=lastupdated.split("-");
 				ans[2]=lastfeedid.split("-");
 	            return ans;	
    } 
 		catch (SQLException e) {
 			e.printStackTrace();
 			System.out.println("=====SQL exception found while connection with the driver======");
 		}
 		return null;
 		
}

public User updateChannel( String Channel, String lastUpdated, String lastfeedid)
{
	String query="UPDATE BOT.RSS SET LastUpdated='"+lastUpdated+"', LastFeedID='"+lastfeedid+"' WHERE Email='"+email+"' AND Channel='"+Channel+"'";
	if(con==null)
 		connect();
    try {
 				Statement stmt =(Statement) con.createStatement();
 				int i=  stmt.executeUpdate(query);	
 				
    } 
 		catch (SQLException e) {
 			e.printStackTrace();
 			System.out.println("=====SQL exception found while connection with the driver======");
 		}
    return this;
}

public User deleteChannel(String Channel)
{
	String query="Delete From BOT.RSS Where Email='"+email+"' And Channel='"+Channel+"'";
	if(con==null)
 		connect();
    try {
 				Statement stmt =(Statement) con.createStatement();
 				int i=  stmt.executeUpdate(query);	
 				
    } 
 		catch (SQLException e) {
 			e.printStackTrace();
 			System.out.println("=====SQL exception found while connection with the driver======");
 		}
    return this;
	}

}
