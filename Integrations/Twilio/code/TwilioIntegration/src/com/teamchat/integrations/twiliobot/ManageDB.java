package com.teamchat.integrations.twiliobot;

import java.sql.DriverManager;
import java.sql.ResultSet;



public class ManageDB {

	static java.sql.Connection connection = null;
    static java.sql.Statement stmt = null;
    static String dbUser=PropertiesFile.getValue("db_user");
    static String dbPassword=PropertiesFile.getValue("db_password");
    static String url=PropertiesFile.getValue("db_url");
	
	
    public static void insert(String smail,String apikey,String telno)
    {
    	String insertQuery="insert into twilio values (\""+smail+"\",\""+apikey+"\",\""+telno+"\")"; 
    	
    	
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
		String apikey=null;
    	String selQuery="select apikey from twilio where mailid=\""+smail+"\"";
    	try
	     {
    		Class.forName(PropertiesFile.getValue("class"));
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			stmt = connection.createStatement();
			ResultSet rs=stmt.executeQuery(selQuery);
            while(rs.next())
	            {
                	apikey=rs.getString(1);
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
		return apikey;
	}

    public static String retrieve1(String smail){
		String telno=null;
    	String selQuery="select telno from twilio where mailid=\""+smail+"\"";
    	try
	     {
    		Class.forName(PropertiesFile.getValue("class"));
			connection = DriverManager.getConnection(url, dbUser, dbPassword);
			stmt = connection.createStatement();
			ResultSet rs=stmt.executeQuery(selQuery);
            while(rs.next())
	            {
                	telno=rs.getString(1);
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
		return telno;
	}
    public static void delete(String smail)
    {
    	String deleteQuery="delete from twilio where mailid =\""+smail+"\"";
    	
    	
    	try
	     {
	            Class.forName(PropertiesFile.getValue("class"));
	            connection = DriverManager.getConnection(url, dbUser, dbPassword);
	            stmt = connection.createStatement();
	            stmt.execute(deleteQuery);
	     
    	
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
