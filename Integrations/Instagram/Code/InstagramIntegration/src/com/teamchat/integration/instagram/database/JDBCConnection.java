package com.teamchat.integration.instagram.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.teamchat.integration.instagram.properties.DBProperty;



public class JDBCConnection {


	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	   static String DB_URL = "jdbc:mysql://localhost/";

	   //  Database credentials
//	   static final String USER = "tcinterns";
//	   static final String PASS = "PakyovBosh7";
//	   static final String USER = "root";
//	   static final String PASS = "gupshup";
	   
	   static String USER="null";
	   static String PASS="null";
	   static String dbname="null";
	   Connection conn = null;
	   Statement stmt = null;
	
	   
	   public JDBCConnection()
		{
			DBProperty dbp=new DBProperty();
			dbp.loadParams();
			USER=dbp.getDBUser();
			PASS=dbp.getDBPass();
			dbname=dbp.getDBName();
			DB_URL="jdbc:mysql://localhost/"+dbname;
		}
	
	public String retreive(String id)
	{
		String gc;
		gc="access_token";
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "select * from Instagram where id=\""+id+"\"";
		      ResultSet rs = stmt.executeQuery(sql);

		      while(rs.next()){
		         //Retrieve by column name
		         gc = rs.getString("access_token");

		      }
		      System.out.println("at="+gc);
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
	
	public void insert(String id,String at)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating insert statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "insert into Instagram values (\""+id+"\",\""+at+"\")";
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
	
	public void delete(String id)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "delete from Instagram where id=\""+id+"\"";
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
	
	public void displaytable()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "SELECT * FROM Youtube";
		      ResultSet rs = stmt.executeQuery(sql);

		      while(rs.next()){
		         //Retrieve by column name
		         String id = rs.getString("id");
		         String at = rs.getString("access_token");

		         //Display values
		         System.out.println("ID: " + id);
		         System.out.println("AT: " + at);
		      }
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
	}

}
