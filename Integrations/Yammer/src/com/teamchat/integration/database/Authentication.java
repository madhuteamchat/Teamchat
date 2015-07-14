package com.teamchat.integration.database;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.teamchat.integration.factory.HostDomain;

public class Authentication {
	
	static java.sql.Connection connection = null;
    static java.sql.Statement stmt = null;
    static String dbUser=HostDomain.dbusername;
    static String dbPassword=HostDomain.dbpassword;
    static String url="jdbc:mysql://localhost/Bot";
	
    public static void setToken(String id,String accessToken) throws Exception{
    	   
    String insertQuery="insert into yammer values (\""+id+"\",\""+accessToken+"\")"; 
     
    	  Class.forName("com.mysql.jdbc.Driver");
    	   connection = DriverManager.getConnection(url, dbUser, dbPassword);
           stmt = connection.createStatement();
           stmt.execute(insertQuery);

         try { 
               stmt.close();
               connection.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
 //   }
    
    public static String getToken(String id){
    	
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
