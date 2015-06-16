package com.teamchat.integration.instagram.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.teamchat.integration.instagram.notification.DeleteSub;
import com.teamchat.integration.instagram.properties.DBProperty;

public class InstaSubDB {
	
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
	

		ArrayList<String> sidlist=new ArrayList<String>();
		ArrayList<String> oidlist=new ArrayList<String>();
		ArrayList<String> objlist=new ArrayList<String>();
		
		
		public InstaSubDB()
		{
			DBProperty dbp=new DBProperty();
			dbp.loadParams();
			USER=dbp.getDBUser();
			PASS=dbp.getDBPass();
			dbname=dbp.getDBName();
			DB_URL="jdbc:mysql://localhost/"+dbname;
		}
	
	public ArrayList<String> retreiveUidList(String sid)
	{
		ArrayList<String> uidlist=new ArrayList<String>();
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "select * from instasubscribe where sid=\""+sid+"\"";
		      ResultSet rs = stmt.executeQuery(sql);

		      while(rs.next()){
		         //Retrieve by column name
		        uidlist.add(rs.getString("id"));
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
		return uidlist;
	}
	
	public void retreiveSubsList(String uid)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "select * from instasubscribe where id=\""+uid+"\"";
		      ResultSet rs = stmt.executeQuery(sql);

		      while(rs.next()){
		         //Retrieve by column name
		        sidlist.add(rs.getString("sid"));
		        oidlist.add(rs.getString("object_id"));
		        objlist.add(rs.getString("object"));
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
	
	public ArrayList<String> sidList()
	{
		return sidlist;
	}
	
	public ArrayList<String> oidList()
	{
		return oidlist;
	}
	
	public ArrayList<String> objList()
	{
		return objlist;
	}
	
	public void insert(String id,String object,String object_id,String sid)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating insert statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "insert into instasubscribe values(\""+id+"\",\""+object+"\",\""+object_id+"\",\""+sid+"\")";
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
	
	public void delete(String uid,String sid)
	{
		int count=0;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "delete from instasubscribe where sid=\""+sid+"\" and id=\""+uid+"\"";
		      stmt.executeUpdate(sql);
		      sql = "select * from instasubscribe where sid=\""+sid+"\"";
		      ResultSet rs = stmt.executeQuery(sql);

		      while(rs.next()){
		         //Retrieve by column name
		        count++;

		      }
		      stmt.close();
		      conn.close();
		      if(count==0)
		      {
		    	  new DeleteSub().unSub(sid);
		    	  new InstaLocation().delete(sid);
		      }
	  }catch(SQLException se){
	      //Handle errors for JDBC
	      se.printStackTrace();
	   }catch(Exception e){
	      //Handle errors for Class.forName
	      e.printStackTrace();
	   }
	}
	
	/*public void displaytable()
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
	}*/
	
	public boolean inInstaSubsDB(String id,String obj,String objid)
	{
		int count=0;
		String sid="";
		boolean dup=false;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);
		   System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "select * from instasubscribe where object=\""+obj+"\" and object_id=\""+objid+"\"";
		      ResultSet rs = stmt.executeQuery(sql);

		      while(rs.next()){
		         //Retrieve by column name
		    	if(rs.getString("id").equals(id))
		    		  dup=true;
		        count++;
		        sid=rs.getString("sid");

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
		if(dup)
			return false;
		if(count>0)
		{
			insert(id, obj, objid, sid);
			return false;
		}
		else
			return true;
	}

}
