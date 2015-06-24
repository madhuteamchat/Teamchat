package com.teamchat.integration.office365.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.teamchat.integration.office365.property.DBProperty;



public class JDBCConnection {


	static String JDBC_DRIVER = "null";  
	   static String DB_URL = "null";

	   //  Database credentials
	   
	   static String USER="null";
	   static String PASS="null";
	   static String dbname="null";
	   Connection conn = null;
	   Statement stmt = null;
	
	   
	   public JDBCConnection()
		{
			DBProperty dbp=new DBProperty();
			dbp.loadParams();
			JDBC_DRIVER=dbp.getDBDriverName();
			USER=dbp.getDBUser();
			PASS=dbp.getDBPass();
			dbname=dbp.getDBName();
			DB_URL=dbp.getDBURL()+dbname;
		}
	
	public String retreiveAccessToken(String teamchat_id)
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
		      sql = "select * from Office365 where teamchat_id=\""+teamchat_id+"\"";
		      ResultSet rs = stmt.executeQuery(sql);

		      while(rs.next()){
		         //Retrieve by column name
		         gc = rs.getString("access_token");

		      }
//		      System.out.println("at="+gc);
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
	
	public String retreiveOffice365ID(String teamchat_id)
	{
		String gc;
		gc="office365_id";
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "select * from Office365 where teamchat_id=\""+teamchat_id+"\"";
		      ResultSet rs = stmt.executeQuery(sql);

		      while(rs.next()){
		         //Retrieve by column name
		         gc = rs.getString("office365_id");

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
		return gc;
	}
	
	public String retreiveRefreshToken(String teamchat_id)
	{
		String gc;
		gc="refresh_token";
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "select * from Office365 where teamchat_id=\""+teamchat_id+"\"";
		      ResultSet rs = stmt.executeQuery(sql);

		      while(rs.next()){
		         //Retrieve by column name
		         gc = rs.getString("refresh_token");

		      }
//		      System.out.println("rt="+gc);
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
	
	
	public void insert(String teamchat_id,String office365_id,String access_token,String refresh_token)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating insert statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql="select * from Office365 where teamchat_id='"+teamchat_id+"'";
		      ResultSet rs=stmt.executeQuery(sql);
		      if(rs.next())
		      {
		    	  sql="update Office365 set office365_id='"+office365_id+"', access_token='"+access_token+"', refresh_token='"+refresh_token+"'"
		    	  		+ " where teamchat_id='"+teamchat_id+"'";
		      }
		      else
		      {
		    	  sql = "insert into Office365 values (\""+teamchat_id+"\",\""+office365_id+"\",\""+access_token+"\",\""+refresh_token+"\")";
		      }
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
	
	public void update(String teamchat_id,String access_token,String refresh_token)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating insert statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "update Office365 set access_token=\""+access_token+"\",refresh_token=\""+refresh_token+"\""
		      		+ "where teamchat_id=\""+teamchat_id+"\"";
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
	
	public void delete(String teamchat_id)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "delete from Office365 where teamchat_id=\""+teamchat_id+"\"";
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
