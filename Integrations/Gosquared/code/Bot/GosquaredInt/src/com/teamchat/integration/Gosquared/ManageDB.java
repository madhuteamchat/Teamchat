package com.teamchat.integration.Gosquared;

import java.sql.DriverManager;
import java.sql.ResultSet;

public class ManageDB {

	static java.sql.Connection connection = null;
    static java.sql.Statement stmt = null;
    static String dbUser=PropertiesFile.getValue("db_user");
    static String dbPassword=PropertiesFile.getValue("db_password");
    static String url=PropertiesFile.getValue("db_url");
	
    public static void insert(String smail,String apikey,String sitetoken)
    {
    	String insertQuery="insert into gosquared values (\""+smail+"\",\""+apikey+"\",\""+sitetoken+"\")"; 
    	
    	
    	try
	     {
	            Class.forName(PropertiesFile.getValue("class"));
	            connection = DriverManager.getConnection(url, dbUser, dbPassword);
	            stmt = connection.createStatement();
	            stmt.execute(insertQuery);
	     
    	
    }
    catch (Exception e) {
          e.printStackTrace();
    }finally {
        try { 
              stmt.close();
              connection.close();
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
}
    	
    
    
    public static String retrieve(String smail){
		String api=null;
    	String selQuery="select api from gosquared where mailid=\""+smail+"\"";
    	try
	     {
    		Class.forName(PropertiesFile.getValue("class"));
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			stmt = connection.createStatement();
			ResultSet rs=stmt.executeQuery(selQuery);
            while(rs.next())
	            {
                	api=rs.getString(1);
	            }
	      }
	      catch (Exception e) {
	            e.printStackTrace();
	      }finally {
	          try {
	                stmt.close();
	                connection.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
		return api;
	}

    public static String retrieve1(String smail){
		String stoken=null;
    	String selQuery="select stoken from gosquared where mailid=\""+smail+"\"";
    	try
	     {
    		Class.forName(PropertiesFile.getValue("class"));
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			stmt = connection.createStatement();
			ResultSet rs=stmt.executeQuery(selQuery);
            while(rs.next())
	            {
                	stoken=rs.getString(1);
	            }
	      }
	      catch (Exception e) {
	            e.printStackTrace();
	      }finally {
	          try {
	                stmt.close();
	                connection.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
		return stoken;
	}

	
    
    public static void update(String smail,String sitetoken)
    {
    	String updateQuery="UPDATE gosquared SET stoken=\""+sitetoken+"\" where mailid=\""+smail+"\""; 
    	
    	
    	try
	     {
	            Class.forName(PropertiesFile.getValue("class"));
	            connection = DriverManager.getConnection(url, dbUser, dbPassword);
	            stmt = connection.createStatement();
	            stmt.execute(updateQuery);
	     
    	
    }
    catch (Exception e) {
          e.printStackTrace();
    }finally {
        try { 
              stmt.close();
              connection.close();
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
}
    
}
