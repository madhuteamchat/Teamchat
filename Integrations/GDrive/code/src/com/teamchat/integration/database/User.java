package com.teamchat.integration.database;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

/**
 * 
 * @author Mallika Gogoi
 *
 */
public class User {
		String URL="jdbc:mysql://localhost/Bot";
		String DRIVER1="com.mysql.jdbc.Driver";
		String USER ="tcinterns";                  //database Username
		String PASSWORD ="PakyovBosh7";             //database Password
		Connection con= null;
		PreparedStatement preparedStatement = null;
		
		private String email=null;
		private String access_token=null;
		private int expires_in=0;

		public int getExpires_in() {
			return expires_in;
		}

		public void setExpires_in(int expires_in) {
			this.expires_in = expires_in;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getAccess_token() {
			return access_token;
		}

		public void setAccess_token(String access_token) {
			this.access_token = access_token;
		}
         
		public User()
		{
			super();
		}
		public User(String email, String access_token,int expires_in)
		{
	
			this.email=email;
			this.access_token=access_token;
			this.expires_in=expires_in;
			insertData();
		}
		
		public User(String email,int expires_in)
		{
		    deleteData(email,expires_in);	
		}

		public User(String email)
		{
		 getData(email);
		}
		
		//Function to get data from the table in the database
		
		public void getData(String email)
		{
		String query = "Select * from Bot.GDriveTable where email='"+email+"'" ;
		System.out.println(query);
		try{
			Class.forName(DRIVER1);
			con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
			Statement stmt=(Statement) con.createStatement();
					ResultSet  rs =  stmt.executeQuery(query);
					if(rs.next())
				   {		
			   this.email=rs.getString("email");
				access_token=rs.getString("access_token");
			    expires_in=rs.getInt("expires_in");
	
					}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("=====Class not found exception while loading the driver======");
			}
			catch (SQLException e) {
				e.printStackTrace();
				System.out.println("=====SQL exception found while connection with the driver======");
			}
			finally
			{
				if(con!=null)
				{
					try {
					con.close();
					} catch (SQLException e) {
						e.printStackTrace();
						System.out.println("=====SQL exception while closing the connection======");
					}
				}
			}
		}
		
		// Function to update the values in the table
		public void updateData(String email,int expires_in)
		{ 
			String query="Update Bot.GDriveTable SET access_token=? where email=?";
			
			System.out.println(query);
		    
			try {
				Class.forName(DRIVER1);
					con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
					PreparedStatement pstmt =(PreparedStatement) con.prepareStatement(query);
					pstmt.setString(1, this.access_token);
					pstmt.setString(2, this.email);
				    pstmt.executeUpdate(query);
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("=====Class not found exception while loading the driver======");
			}
			catch (SQLException e) {
				e.printStackTrace();
				System.out.println("=====SQL exception found while connection with the driver======");
			}
			finally
			{
				if(con!=null)
				{
					try {
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
						System.out.println("=====SQL exception while closing the connection======");
					}
				}
			}
		}
	
		//Function to dynamically insert values into the table
		
	public void insertData()
	{
		String query="Insert into Bot.GDriveTable"+"(email,access_token,expires_in) values"+"(?,?,?)";
		System.out.println(query);
		try{
			Class.forName(DRIVER1);
			con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt= (PreparedStatement) con.prepareStatement(query);			
			pstmt.setString(1, this.email);
			pstmt.setString(2, this.access_token);
			pstmt.setInt(3,this.expires_in);
			pstmt.executeUpdate();
	
	        }
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(con!=null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("=====SQL exception while closing the connection======");
				}
			}
		}
}
		//Function to delete data in the table
	public void deleteData(String email,int expires_in)
	{
		String query="Delete from Bot.GDriveTable where email=?";
		System.out.println(query);
		try{
			Class.forName(DRIVER1);
			con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt= (PreparedStatement) con.prepareStatement(query);			
			pstmt.setString(1,email);
			pstmt.executeUpdate();
	
	        }
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(con!=null)
			{
				try {
					con.close();
				    } catch (SQLException e) {
					e.printStackTrace();
					System.out.println("=====SQL exception while closing the connection======");
				   }
			}
		}	
	}
  //Function to check whether he email already exist in the table
	public Boolean checkData()
	{
		String query = "Select * from Bot.GDriveTable where email='"+email+"'" ;
		System.out.println(query);
		try{
			Class.forName(DRIVER1);
			con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
			Statement stmt=(Statement) con.createStatement();
					ResultSet  rs =  stmt.executeQuery(query);
					if(rs.next())
				   {		
			     return true;
	
					}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("=====Class not found exception while loading the driver======");
			}
			catch (SQLException e) {
				e.printStackTrace();
				System.out.println("=====SQL exception found while connection with the driver======");
			}
			finally
			{
				if(con!=null)
				{
					try {
					con.close();
					} catch (SQLException e) {
						e.printStackTrace();
						System.out.println("=====SQL exception while closing the connection======");
					}
				}
			}
		return false;
	}
	
}