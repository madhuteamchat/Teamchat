
package com.teamchat.integration;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Connection;


/**
 * 
 * @author Priyanka Shiyal
 *
 */
public class DatabaseHandler {
	
		String URL="jdbc:mysql://localhost/Bot";
		String DRIVER="com.mysql.jdbc.Driver";
		String USER ="tcinterns";                  //database Username
		String PASSWORD ="PakyovBosh7";             //database Password
	
//	String URL="jdbc:mysql://localhost/RunScope";
//	String DRIVER="com.mysql.jdbc.Driver";
//	String USER ="root";                  //database Username
//	String PASSWORD ="priyanka";             //database Password
//	
		
		Connection con= null;
		PreparedStatement preparedStatement = null;
		
		private String email=null;
		private String token=null;


		public String getEmail() {
			return email;
			
		}

		public void setEmail(String email) {
			this.email = email;
		}

		
		public String getAccessToken() {
			return token;
		}

		public void setAccess_token(String token) {
			this.token = token;
		}
         
		public DatabaseHandler()
		{
			super();
		}
		
		public DatabaseHandler(String email, String accessToken)
		{
	
			this.email=email;
			this.token=accessToken;
			
		}
		
		public String getData(String email)
		{
			
		String Mytoken = null;	
		String query = "Select * from Bot.RunScope where email='"+email+"'" ;
		System.out.println(query);
		try{
			Class.forName(DRIVER);
			
			con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
			
			Statement stmt=(Statement) con.createStatement();
			
			ResultSet  rs =  stmt.executeQuery(query);
			if(rs.next())
			{		
			    this.email=rs.getString("email");
			    Mytoken=rs.getString("token");
	
			}
			
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("Class not found exception while loading the driver.");
			}
			catch (SQLException e) {
				e.printStackTrace();
				System.out.println("SQL exception found while connection with the driver.");
			}
			finally
			{
				if(con!=null)
				{
					try {
					con.close();
					} catch (SQLException e) {
						e.printStackTrace();
						System.out.println("SQL exception while closing the connection.");
					}
				}
			}
		
			return Mytoken;
		}
			
		
		public void updateData(String email, String access_token)
		{ 
			String query="Update Bot.RunScope SET access_token=? where email=?";
			
			System.out.println(query);
		    
			try {
				    Class.forName(DRIVER);
					con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
					PreparedStatement pstmt =(PreparedStatement) con.prepareStatement(query);
					pstmt.setString(1, access_token);
					pstmt.setString(2, email);
				    pstmt.executeUpdate();
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("Class not found exception while loading the driver.");
			}
			catch (SQLException e) {
				e.printStackTrace();
				System.out.println("SQL exception found while connection with the driver.");
			}
			finally
			{
				if(con!=null)
				{
					try {
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
						System.out.println("SQL exception while closing the connection.");
					}
				}
			}
		}
		
		
	public void insertData()
	{
		String query = " insert into Bot.RunScope (email,token) values (?, ?);";
		
		System.out.println(query);
	    
		try {
			    Class.forName(DRIVER);
			    
				con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement pstmt =(PreparedStatement) con.prepareStatement(query);
				
				pstmt.setString(1, this.email);
				pstmt.setString(2, this.token);
				
			    int rowsaffected =  pstmt.executeUpdate();
			    System.out.println("Rows affected:"+rowsaffected);
			  
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
					System.out.println("SQL exception while closing the connection.");
				}
			}
		}
	}
}			
	
	

	
	
