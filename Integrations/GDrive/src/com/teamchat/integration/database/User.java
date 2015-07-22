package com.teamchat.integration.database;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

/**
 * 
 * @author Mallika Gogoi
 *
 */
public class User {
		/*String URL="jdbc:mysql://localhost/Bot";
		String DRIVER1="com.mysql.jdbc.Driver";
		String USER ="tcinterns";                  //database Username
		String PASSWORD ="PakyovBosh7";    */         //database Password
		Connection con= null;
		PreparedStatement preparedStatement = null;
		Properties configProps;
		
		private String email=null;
		private String access_token=null;
		private int expires_in=0;
           
		public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException
		{

			Properties prop = new Properties();
			prop.load(type.getClassLoader().getResourceAsStream(fileName));
			return prop;

		}
		
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
			try {
				configProps = loadPropertyFromClasspath("gdrive.properties", User.class);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		String query = "Select * from " + configProps.getProperty("tablename") + " where email='"+email+"'" ;
		System.out.println(query);
		try{
			Class.forName(configProps.getProperty("DRIVER1"));
			con = (Connection) DriverManager.getConnection(configProps.getProperty("URL"), configProps.getProperty("USER"), configProps.getProperty("PASSWORD"));
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
			try {
				configProps = loadPropertyFromClasspath("gdrive.properties", User.class);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String query="Update "+configProps.getProperty("tablename")+" SET access_token=? where email=?";
		    
			try {
				Class.forName(configProps.getProperty("DRIVER1"));
					con = (Connection) DriverManager.getConnection(configProps.getProperty("URL"), configProps.getProperty("USER"), configProps.getProperty("PASSWORD"));
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
		try {
			configProps = loadPropertyFromClasspath("gdrive.properties", User.class);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String query="Insert into "+configProps.getProperty("tablename")+"(email,access_token,expires_in) values"+"(?,?,?)";
		System.out.println(query);
		try{
			Class.forName(configProps.getProperty("DRIVER1"));
			con = (Connection) DriverManager.getConnection(configProps.getProperty("URL"), configProps.getProperty("USER"), configProps.getProperty("PASSWORD"));
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
		try {
			configProps = loadPropertyFromClasspath("gdrive.properties", User.class);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String query="Delete from "+configProps.getProperty("tablename")+" where email=?";
		System.out.println(query);
		try{
			Class.forName(configProps.getProperty("DRIVER1"));
			con = (Connection) DriverManager.getConnection(configProps.getProperty("URL"), configProps.getProperty("USER"), configProps.getProperty("PASSWORD"));
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
		try {
			configProps = loadPropertyFromClasspath("gdrive.properties", User.class);
			System.out.println("***********************************8");
			System.out.println(configProps);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String query = "Select * from "+configProps.getProperty("tablename")+" where email='"+email+"'" ;
		System.out.println(query);
		try{
			Class.forName(configProps.getProperty("DRIVER1"));
			con = (Connection) DriverManager.getConnection(configProps.getProperty("URL"), configProps.getProperty("USER"), configProps.getProperty("PASSWORD"));
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