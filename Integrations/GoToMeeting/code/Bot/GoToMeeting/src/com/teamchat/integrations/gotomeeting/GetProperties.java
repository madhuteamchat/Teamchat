package com.teamchat.integrations.gotomeeting;

import java.sql.*;

public class GetProperties
{
	
	String url = "jdbc:mysql://localhost:3306/"; 
	String dbName = "Bot"; 
	String driver = "com.mysql.jdbc.Driver"; 
	String userName = "webaroo"; 
	String password = "webar00";
	
	public boolean getDB(String userEmail){
		boolean flag=false;
		try{
		
		Class.forName(driver).newInstance(); 
		Connection conn = DriverManager.getConnection(url+dbName,userName,password); 
		Statement st = conn.createStatement(); 
		ResultSet res = st.executeQuery("SELECT * FROM user_data"); 
		
		while (res.next()) { 
			String tcemail = res.getString("tcemail"); 
			 
			if(tcemail.equals(userEmail)) flag=true;
			  
			 
			} 
		conn.close();	
	}
		catch (Exception e) { e.printStackTrace(); }
		return flag;
		}

	public String[] pullDB(String userEmail){
		
		String str[]=new String[2];
		try{
		
		Class.forName(driver).newInstance(); 
		Connection conn = DriverManager.getConnection(url+dbName,userName,password); 
		PreparedStatement st = conn.prepareStatement("select * from user_data where tcemail=?"); 
		st.setString(1, userEmail);
		ResultSet res=st.executeQuery();
		
		while (res.next()) { 
			String email = res.getString("gtmemail"); 
			String pass = res.getString("pass");
			
			str[0]=email;
			str[1]=pass;
			 
			}
		conn.close();
		}
		catch (Exception e) { e.printStackTrace(); }
		return str;
		}

	public void pushDB(String tcemail,String userEmail,String pass){
		
		
		try{
		
		Class.forName(driver).newInstance(); 
		Connection conn = DriverManager.getConnection(url+dbName,userName,password); 
		PreparedStatement st = conn.prepareStatement("insert into user_data values (?,?,?)");
		st.setString(1, tcemail);
		st.setString(2, userEmail);
		st.setString(3, pass);
		int val=st.executeUpdate();
		System.out.println("//////------"+val);
		conn.close(); 
			
		}
		catch (Exception e) { e.printStackTrace(); }
		
		}
	
		public boolean checkLogin(String userEmail){
		boolean flag=false;
		try{
		
		Class.forName(driver).newInstance(); 
		Connection conn = DriverManager.getConnection(url+dbName,userName,password); 
		Statement st = conn.createStatement(); 
		ResultSet res = st.executeQuery("SELECT * FROM user_data"); 
		
		while (res.next()) { 
			String gtmemail = res.getString("gtmemail"); 
			 
			if(gtmemail.equals(userEmail)) flag=true;
			  } 
		conn.close();	
	}
		catch (Exception e) { e.printStackTrace(); }
		return flag;
		}

}
