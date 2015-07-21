package com.teamchat.integration;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
	
//	String URL="jdbc:mysql://localhost/ToDoIst";
//	String DRIVER="com.mysql.jdbc.Driver";
//	String USER ="root";                  //database Username
//	String PASSWORD ="priyanka";             //database Password
	
		
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
		
		public DatabaseHandler(String email)
		{
		 getData(email);
		}
		
		//Function to get data from the table in the database
		
		public String getData(String email)
		{
			
		String Mytoken = null;	
		String query = "Select * from Bot.ToDoIst where email='"+email+"'" ;
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
			
		
		// Function to update the values in the table
		public void updateData(String email, String access_token)
		{ 
			String query="Update Bot.ToDoIst SET access_token=? where email=?";
			
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
		
		public void updateProject(String newProjectname, String projectname )
		{ 
			String query="Update Bot.project SET newProjectname=? where projectname=?";
			
			System.out.println(query);
		    
			try {
				    Class.forName(DRIVER);
					con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
					PreparedStatement pstmt =(PreparedStatement) con.prepareStatement(query);
					pstmt.setString(1, projectname);
					pstmt.setString(2, newProjectname);
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
	

	
		//Function to dynamically insert values into the table
		
	public void insertData()
	{
		String query = " insert into Bot.ToDoIst (email,token) values (?, ?);";
		
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
	
	public void insertProjectsIntoDB(String emailid,int projectid, String projectname)
	{
		String query = " insert into Bot.projectDetails (emailid,projectid,projectname) values (?, ?,?);";
		
		System.out.println(query);
	    
		try {
			    Class.forName(DRIVER);
			    
				con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement pstmt =(PreparedStatement) con.prepareStatement(query);
				
				pstmt.setString(1, emailid);
				pstmt.setInt(2, projectid);
				pstmt.setString(3, projectname);
				
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
	

	public void insertTasksIntoDB(String emailid, int projectid, int taskid, String taskname)
	{
		String query = " insert into Bot.taskDetails (emailid, projectid,taskid,taskname) values (?, ?,?,?);";
		
		System.out.println(query);
	    
		try { 
			    Class.forName(DRIVER);
			    
				con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement pstmt =(PreparedStatement) con.prepareStatement(query);
				
				pstmt.setString(1, emailid);
				pstmt.setInt(2, projectid);
				pstmt.setInt(3, taskid);
				pstmt.setString(4, taskname);
				
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
    //Function to delete data in the table
	public void deleteData(String email,int expires_in)
	{
		String query="Delete from Bot.ToDoIst where email=?";
		System.out.println(query);
		try{
			Class.forName(DRIVER);
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
					System.out.println("SQL exception while closing the connection.");
				   }
			}
		}	
	}
	
	public void deleteProject( String projectname )
	{
		String query="Delete from Bot.project where projectname=?";
		System.out.println(query);
		try{
			Class.forName(DRIVER);
			con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
			PreparedStatement pstmt= (PreparedStatement) con.prepareStatement(query);			
			pstmt.setString(1,projectname);
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
					System.out.println("SQL exception while closing the connection.");
				   }
			}
		}	
	}
	
	
    //Function to check whether he email already exist in the table
	public Boolean checkData()
	{
		String query = "Select * from Bot.ToDoIst where email='"+email+"'" ;
		System.out.println(query);
		try{
			Class.forName(DRIVER);
			con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
			Statement stmt=(Statement) con.createStatement();
			ResultSet  rs =  stmt.executeQuery(query);
			if(rs.next())
		    {		
				return true;	
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
		return false;
	}


	public List<ProjectDetails> getAllProjectsByEmailId() throws SQLException {
	
	List<ProjectDetails> list = new ArrayList<ProjectDetails>();
		
	ProjectDetails objProjectDetails = null;
	String query = "Select * from Bot.projectDetails where emailid='"+getEmail()+"'" ;
	System.out.println(query);
	try{
		Class.forName(DRIVER);
		
		con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
		
		Statement stmt=(Statement) con.createStatement();
		
		ResultSet  rs =  stmt.executeQuery(query);
		while(rs.next())
		{		
			
			objProjectDetails = new ProjectDetails();
			objProjectDetails.setEmailId(rs.getString("emailid"));
			objProjectDetails.setProjectId(rs.getInt("projectid"));
			objProjectDetails.setProjectName(rs.getString("projectname"));
			
		    list.add(objProjectDetails);
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
	
		return list;
	}
	
	public List<TaskDetails> getAllTasksByEmailId() throws SQLException {
		
	List<TaskDetails> list = new ArrayList<TaskDetails>();
		
	TaskDetails objTaskDetails = null;
	String query = "Select * from Bot.taskDetails where emailid='"+getEmail()+"'" ;
	System.out.println(query);
	try{
		Class.forName(DRIVER);
		
		con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
		
		Statement stmt=(Statement) con.createStatement();
		
		ResultSet  rs =  stmt.executeQuery(query);
		while(rs.next())
		{		
			
			objTaskDetails = new TaskDetails();
			objTaskDetails.setEmailId(rs.getString("emailid"));
			objTaskDetails.setProjectId(rs.getInt("projectid"));
			objTaskDetails.setTaskId(rs.getInt("taskid"));
			objTaskDetails.setTaskName(rs.getString("taskname"));
			
		    list.add(objTaskDetails);
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
	
		return list;
	}

	
	public void CleanUpAllTasksByEmailId() throws SQLException {
		
		String query = "delete  from Bot.taskDetails where emailid= ?" ;
		System.out.println(query);
		try{
			Class.forName(DRIVER);
			
			con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
		
		    PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
		    preparedStmt.setString(1, getEmail());
		 
		     
		    preparedStmt.execute();
			
			
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


	public void CleanUpAllProjectsByEmailId() throws SQLException {
		
	String query = "delete  from Bot.projectDetails where emailid= ?" ;
	System.out.println(query);
	try{
		Class.forName(DRIVER);
		
		con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
	
	    PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
	    preparedStmt.setString(1, getEmail());
	 
	     
	    preparedStmt.execute();
		
		
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

	
	public void UpdateProjectByEmailId(String emailid, String newProjectName, String oldProjectName) throws SQLException {
		
	String query = "update Bot.projectDetails set projectname = ? where emailid= ? and projectname = ?" ;
	System.out.println(query);
	try{
		Class.forName(DRIVER);
		
		con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
	
	    PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
	    preparedStmt.setString(1, newProjectName);
	    preparedStmt.setString(2, getEmail());
	    preparedStmt.setString(3, oldProjectName);
	 
	    preparedStmt.execute();
		
		
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
	
	
	public void UpdateTaskByEmailId(String emailid, String newTaskName, String oldTaskName) throws SQLException {
		
	String query = "update Bot.taskDetails set taskname = ? where emailid= ? and taskname = ?" ;
	System.out.println(query);
	try{
		Class.forName(DRIVER);
		
		con = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
	
	    PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
	    preparedStmt.setString(1, newTaskName);
	    preparedStmt.setString(2, getEmail());
	    preparedStmt.setString(3, oldTaskName);
	 
	    preparedStmt.execute();
		
		
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
	

	
	
	
	public void DeleteProjectByEmailId(String emailid, String projectname) throws SQLException {
		
		Statement stmt = null;
		try{
			Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USER, PASSWORD);
             stmt = con.createStatement();
            String sql = "DELETE FROM Bot.projectDetails WHERE emailid= '"+emailid+"'AND projectname= '" +projectname+ "'" ;;
            stmt.executeUpdate(sql);
            System.out.println("Data deleted sucessfully");

			
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
	
	
public void DeleteTakByEmailId(String emailid, String taskname) throws SQLException {
		
		Statement stmt = null;
		try{
			Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USER, PASSWORD);
             stmt = con.createStatement();
            String sql = "DELETE FROM Bot.taskDetails WHERE emailid= '"+emailid+"'AND taskname= '" +taskname+ "'" ;;
            stmt.executeUpdate(sql);
            System.out.println("Data deleted sucessfully");

			
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
	

public int getProjectIdByProjectName(String projectName, String emailid) throws SQLException, InstantiationException, IllegalAccessException {
		
	int projectId =0 ;	
	
	
	String query = "SELECT projectid FROM Bot.projectDetails WHERE emailid= '" +emailid+ "' AND projectname= '" +projectName+ "'";
	System.out.println(query);
	Connection conn;
	Statement stmt;
	ResultSet rs;
	
		try{
			//ProjectDetails objProjectDetails = null;
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(URL,USER,PASSWORD);
			stmt = conn.createStatement();
		
			rs =  stmt.executeQuery(query);
				
			 rs.next();
		         //Retrieve by column name
		         projectId  = rs.getInt(1);
		    //Display values
		         System.out.print("ID: " + projectId);
		         
		      
				
				System.out.println("Project Id Is:"+projectId);
				System.out.println("Email Id Is:"+emailid);
				System.out.println("ProjectName Is:"+projectName);
				     
		
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
	
		return projectId;
	}

	
	
	

public int getTaskIdByTaskName(String taskName, String emailid) throws SQLException, InstantiationException, IllegalAccessException {
	
	int taskId =0 ;	
	
	
	String query = "SELECT taskid FROM Bot.taskDetails WHERE emailid= '" +emailid+ "' AND taskname= '" +taskName+ "'";
	System.out.println(query);
	Connection conn;
	Statement stmt;
	ResultSet rs;
	
		try{
			//ProjectDetails objProjectDetails = null;
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(URL,USER,PASSWORD);
			stmt = conn.createStatement();
		
			rs =  stmt.executeQuery(query);
				
			 rs.next();
		         //Retrieve by column name
		         taskId  = rs.getInt(1);
		    //Display values
		         System.out.print("ID: " + taskId);
		         
		      
				
				System.out.println("Project Id Is:"+taskId);
				System.out.println("Email Id Is:"+emailid);
				System.out.println("ProjectName Is:"+taskName);
				     
			
		
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
	
		return taskId;
	}

	
	
}			
	
	

	
	
