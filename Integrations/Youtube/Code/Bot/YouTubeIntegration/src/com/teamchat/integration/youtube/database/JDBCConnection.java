package com.teamchat.integration.youtube.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.teamchat.integration.youtube.properties.DBProperty;



public class JDBCConnection {

	static String JDBC_DRIVER = "null";  
	   static String DB_URL = "null";
	String dbname;
	   //  Database credentials
	   
	   static String USER="null";
	   static String PASS="null";
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
	
	public String[] retreive(String id)
	{
		String[] gc=new String[2];
		gc[0]="access_token";
		gc[1]="refresh_token";
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "select refresh_token,access_token from Youtube where id=\""+id+"\"";
		      ResultSet rs = stmt.executeQuery(sql);

		      while(rs.next()){
		         //Retrieve by column name
		         gc[0] = rs.getString("access_token");
		         gc[1] = rs.getString("refresh_token");

		      }
		      System.out.println("at="+gc[0]);
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
	
	public void insert(String id,String at,String rt)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating insert statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "insert into Youtube values(\""+id+"\",\""+at+"\",\""+rt+"\")";
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
		   System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "delete from Youtube where id=\""+id+"\"";
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
		         String rt = rs.getString("refresh_token");

		         //Display values
		         System.out.println("ID: " + id);
		         System.out.println("AT: " + at);
		         System.out.println("RT: " + rt);
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
