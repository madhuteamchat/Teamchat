package com.teamchat.integration.database;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.teamchat.integration.factory.PropertyFile;

public class Authentication {
	 static java.sql.Statement stmt = null;
	 static java.sql.Connection connection = null;   
	    
    public static void setToken(String id,String accessToken) throws Exception{
    	  String url=PropertyFile.getProperty("dburl");	
    	  String dbUser=PropertyFile.getProperty("dbusername");	
  	      String dbPassword=PropertyFile.getProperty("dbpassword");	
  	    try { 
          String selQuery="select authtoken from yammer where email=\""+id+"\"";
    	  Class.forName("com.mysql.jdbc.Driver");
    	   connection = DriverManager.getConnection(url, dbUser, dbPassword);
           stmt = connection.createStatement();
           /* FIRST CHECK IF THE DATABASE CONTAINS THE USER ID */
           ResultSet rs=stmt.executeQuery(selQuery); 
           if(rs.first())
            {
               System.out.println("user exists already");
               String updateQuery="update yammer set authtoken='"+accessToken+"' where email='"+id+"';"; 
               stmt.execute(updateQuery);
          }
           else {
        	   System.out.println("adding new user");
               String insertQuery="insert into yammer values (\""+id+"\",\""+accessToken+"\")"; 
               stmt.execute(insertQuery);
           } 
  	    }
  	    catch (SQLException e) {
               e.printStackTrace();
           }
  	    finally{
  	        stmt.close();
            connection.close();
  	    }
       }
    
    public static String getToken(String id){
    	    String url=PropertyFile.getProperty("dburl");	
  	        String dbUser=PropertyFile.getProperty("dbusername");	
	        String dbPassword=PropertyFile.getProperty("dbpassword");
    	    String token=null;
    	    String selQuery="select authtoken from yammer where email=\""+id+"\"";
    	    try
    	    {
    	       Class.forName("com.mysql.jdbc.Driver");
    	       connection = DriverManager.getConnection(url, dbUser, dbPassword);
    	       stmt = connection.createStatement();
    	       ResultSet rs=stmt.executeQuery(selQuery);
    	            while(rs.next())
    	           {
    	                token=rs.getString(1);
    	           }
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
    	return token;
    	}
}
