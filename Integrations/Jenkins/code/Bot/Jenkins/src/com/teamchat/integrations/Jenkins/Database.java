package com.teamchat.integrations.Jenkins;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;


public class Database {
	String URL="jdbc:mysql://localhost/Bot";
	String DRIVER1="com.mysql.jdbc.Driver";
	String USER ="tcinterns";
	String PASSWORD ="PakyovBosh7";
    static Connection con=null;
    static Statement stmt;
    TeamchatAPI api;
    Database()
    {
    if(con==null)
    	connect();
    this.api=Bot.api;
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
    
    public void insertUID(String email,String UID, String name)
    {
    	try{
        	String insertTableSQL = "INSERT INTO Bot.JenkinsUIDs "
        			+ "(Email, UID, Name) VALUES"
        			+ "(?,?,?)";
        	PreparedStatement preparedStatement = con.prepareStatement(insertTableSQL);
        	preparedStatement.setString(1,email);
        	preparedStatement.setString(2,UID);
        	preparedStatement.setString(3, name);
        	
        	// execute insert SQL stetement
        	preparedStatement .executeUpdate();
        	} catch(SQLException e)
        	{
        		System.out.println("SQL addUID: ");
        		e.printStackTrace();
        	}
    }
    
    public Boolean hasUID(String UID)
    {String query = "Select * from Bot.JenkinsUIDs where UID='"+UID+"'" ;
 	if(con==null)
 		connect();
    try {
 				Statement stmt =(Statement) con.createStatement();
 				ResultSet  rs =  stmt.executeQuery(query);
 				if(rs.next())
 				{
 				   	return true;
 				}
 		} 
 		catch (SQLException e) {
 			e.printStackTrace();
 			System.out.println("=====SQL exception found while connection with the driver======");
 		}
 		return false;
 		
    }
    public String[] getUID(String email)
    {   
    	String ans="";
    	String query = "Select * from Bot.JenkinsUIDs where Email='"+email+"'" ;
     	if(con==null)
     		connect();
        try {
     				Statement stmt =(Statement) con.createStatement();
     				ResultSet  rs =  stmt.executeQuery(query);
     				while(rs.next())
     				{
     				  ans+="-"+rs.getString("UID");
     				}
     				return ans.split("-");
     		} 
     		catch (SQLException e) {
     			e.printStackTrace();
     			System.out.println("=====SQL exception found while connection with the driver======");
     		}
     		return null;
     		
    }
    public int countUID(String email)
    {
    	String query = "Select * from Bot.JenkinsUIDs where Email='"+email+"'" ;
     	if(con==null)
     		connect();
        try {
     				Statement stmt =(Statement) con.createStatement();
     				ResultSet  rs =  stmt.executeQuery(query);
     				int i=0;
     				if(rs.next())
     				{
     				   i++;
     				}
     				return i;
     		} 
     		catch (SQLException e) {
     			e.printStackTrace();
     			System.out.println("=====SQL exception found while connection with the driver======");
     		}
     		return 0;
    }
    public Boolean insertRoom(String RoomID,String UID)
    { if(hasUID(UID))
    	{
    	String Name=getName(UID);
    	
    	try{
        	String insertTableSQL = "INSERT INTO Bot.JenkinsRooms "
        			+ "(UID,Name,Room) VALUES"
        			+ "(?,?,?)";
        	PreparedStatement preparedStatement = con.prepareStatement(insertTableSQL);
        	preparedStatement.setString(1,UID);
        	preparedStatement.setString(2, Name);
            preparedStatement.setString(3,RoomID);
        	
        	// execute insert SQL stetement
        	preparedStatement .executeUpdate();
        	} catch(SQLException e)
        	{
        		System.out.println("SQL addUID: ");
        		e.printStackTrace();
        	}
    	System.out.println("Value added: "+Name+" "+UID+" "+RoomID);
    	return true;}
    return false;
    }
    
    public String getName(String UID)
    {
        	String query = "Select * From Bot.JenkinsUIDs Where UID='"+UID+"'";
        	if(con==null)
         		connect();
            try {
         				Statement stmt =(Statement) con.createStatement();
         				ResultSet  rs =  stmt.executeQuery(query);
         				if(rs.next())
         				{
         				   	return rs.getString("Name");
         				}
         		} 
         		catch (SQLException e) {
         			e.printStackTrace();
         			System.out.println("=====SQL exception found while connection with the driver======");
         		}
            return null;
    }
    public void postInRooms(String UID, String msg)
    {   
    	String query = "Select * from Bot.JenkinsRooms where UID='"+UID+"'" ;
 	if(con==null)
 		connect();
    try {
 				Statement stmt =(Statement) con.createStatement();
 				ResultSet  rs =  stmt.executeQuery(query);
 				while(rs.next())
 				{   msg="<font color='blue'><b><u>"+rs.getString("Name")+"</u></b></font><br/>"+msg;
 					api.perform(api.context().byId(rs.getString("Room")).post(new PrimaryChatlet()
                                                                              .setQuestionHtml(msg)));
 					
 				}
 		} 
 		catch (SQLException e) {
 			e.printStackTrace();
 			System.out.println("=====SQL exception found while connection with the driver======");
 		}
    }
    
    public String generateUID(String email,String userid,String name)
    {System.out.println("User Id got "+userid);
    	String uid=userid+name+countUID(email);
	insertUID(email,uid,name);
	return uid;
    }
    
    public String deleteRoom(String UID,String room)
    {
    	String query = "Delete * from Bot.JenkinsRooms where UID='"+UID+"' AND Room='"+room+"'" ;
    	String name=getName(UID);
     	if(con==null)
     		connect();
        try {
     				Statement stmt =(Statement) con.createStatement();
     				 if(stmt.execute(query))
     			    return name;
     		} 
     		catch (SQLException e) {
     			e.printStackTrace();
     			System.out.println("=====SQL exception found while connection with the driver======");
     		}
        return null;
    }
    
}
