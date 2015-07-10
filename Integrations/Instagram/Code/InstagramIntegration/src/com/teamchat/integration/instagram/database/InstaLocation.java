package com.teamchat.integration.instagram.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InstaLocation {
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static final String DB_URL = "jdbc:mysql://localhost/Bot";

	   //  Database credentials
	   static final String USER = "tcinterns";
	   static final String PASS = "PakyovBosh7";
//	   static final String USER = "root";
//	   static final String PASS = "gupshup";
	   Connection conn = null;
	   Statement stmt = null;
	
	
	public String retreive(String locid)
	{
		String gc;
		gc="locname";
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "select * from instalocation where locid=\""+locid+"\"";
		      ResultSet rs = stmt.executeQuery(sql);

		      while(rs.next()){
		         //Retrieve by column name
		         gc = rs.getString("locname");

		      }
		      System.out.println("locname="+gc);
		      rs.close();
		      stmt.close();
		      conn.close();
	  }catch(SQLException se){
	      //Handle errors for JDBC
	      se.printStackTrace();
	   }catch(Exception e){
	      //Handle errors for Class.forName
	      e.printStackTrace();
	   }
		return gc;
	}
	
	public void insert(String locid,String locname,String sid)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating insert statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "insert into instalocation values(\""+locid+"\",\""+locname+"\",\""+sid+"\")";
		      stmt.executeUpdate(sql);
		      stmt.close();
		      conn.close();
	  }catch(SQLException se){
	      //Handle errors for JDBC
	      se.printStackTrace();
	   }catch(Exception e){
	      //Handle errors for Class.forName
	      e.printStackTrace();
	   }
	}
	
	public void delete(String sid)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "delete from instalocation where sid=\""+sid+"\"";
		      stmt.executeUpdate(sql);
		      stmt.close();
		      conn.close();
	  }catch(SQLException se){
	      //Handle errors for JDBC
	      se.printStackTrace();
	   }catch(Exception e){
	      //Handle errors for Class.forName
	      e.printStackTrace();
	   }
	}
	

}
