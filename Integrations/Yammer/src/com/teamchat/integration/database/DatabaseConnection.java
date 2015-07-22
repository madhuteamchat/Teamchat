package com.teamchat.integration.database;

import java.sql.DriverManager;

import com.teamchat.integration.factory.HostDomain;

public class DatabaseConnection {


static java.sql.Connection connection = null;
    static java.sql.Statement stmt = null;
    static String dbUser=HostDomain.dbusername;
    static String dbPassword=HostDomain.dbpassword;;
    static String url="jdbc:mysql://localhost/Bot";


public static void remove(String mail) {
// TODO Auto-generated method stub
String delQuery="delete from evernote where userid=\""+mail+"\""; 
    try
    {
           Class.forName("com.mysql.jdbc.Driver");
           connection = DriverManager.getConnection(url, dbUser, dbPassword);
           stmt = connection.createStatement();
           stmt.execute(delQuery);
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

