package com.teamchat.integrations.enchant.database;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.teamchat.integrations.enchant.bot.HostDomain;

import enchantapi.EnchantCredentials;


public class Authentication {
	static java.sql.Connection connection = null;
	static java.sql.Statement stmt = null;
	static String dbUser=HostDomain.dbusername;
	static String dbPassword=HostDomain.dbpassword;
	static String url="jdbc:mysql://localhost/Enchant";
	 
	 public static void setCredentials(String teamchatid,String enchantid,String enchantpassword,String site){	    	
 	    String selQuery="select * from enchant where teamchatid='"+teamchatid+"'";
 	    try
 	    {
 	       Class.forName("com.mysql.jdbc.Driver");
 	       connection = DriverManager.getConnection(url, dbUser, dbPassword);
 	       stmt = connection.createStatement();
 	       ResultSet rs=stmt.executeQuery(selQuery);
 	       if(rs.first()) {
 	    	   //need to update credentials
 	    	   String updateQuery="UPDATE enchant SET username='"+enchantid+"',enchant_password='"+enchantpassword+"',site='"+site+"' WHERE teamchatid='"+teamchatid+"'"; 
 	           System.out.println(updateQuery);
 	    	   stmt.executeUpdate(updateQuery);
 	           stmt.close();
 	       }
 	       else {
 	    	   //inserting for the first time
 	    	  String insertQuery="insert into enchant values ('"+teamchatid+"','"+enchantid+"','"+enchantpassword+"','"+site+"')"; 
 	    	  stmt.execute(insertQuery);
 	    	 System.out.println("EXECUTED "+insertQuery);
 	       }
 	     }
 	     catch (Exception e) {
 	    	   System.err.println("ERROR IN INSERTING VALUES INTO DATABASE");
 	           e.printStackTrace();
 	     }finally {
 	         try {
 	               stmt.close();
 	               connection.close();
 	           } catch (Exception e) {
 	        	   System.err.println("ERROR IN CLOSING");
 	               e.printStackTrace();
 	           }
 	       }
 	}
	 
	 public static EnchantCredentials getCredentials(String teamchatid) {
		 EnchantCredentials login=null;
		 String selQuery="select username,enchant_password,site from enchant where teamchatid='"+teamchatid+"'";
		 try
	 	    {
	 	       Class.forName("com.mysql.jdbc.Driver");
	 	       connection = DriverManager.getConnection(url, dbUser, dbPassword);
	 	       stmt = connection.createStatement();
	 	       ResultSet rs=stmt.executeQuery(selQuery);
	 	       while(rs.next()) {
	 	    	   login=new EnchantCredentials(rs.getString(1),rs.getString(2),rs.getString(3));
	 	       }
	 	    }
		 catch(Exception e) {
			 System.err.println("ERROR WHILE RETREIVING VALUES FROM DATABASE");
			 e.printStackTrace();
		 }
		 return login;
		 
	 }
}
