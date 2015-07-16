package com.teamchat.integrations.Jenkins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

/**
 * Servlet implementation class Teamchat
 */
@WebServlet("/Teamchat")
public class Teamchat extends HttpServlet {
	private static final long serialVersionUID = 1L;
    static TeamchatAPI api;
    /**
     * Default constructor. 
     */
    public Teamchat() {
      //  api=Bot.api;// TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		BufferedReader br=request.getReader();
	StringBuffer buffer=new StringBuffer();
	String line;
	while((line=br.readLine())!=null)
	{
		buffer.append(line);
	}
	JSONObject jsonObject;
	try{
   jsonObject=new JSONObject(buffer.toString());
   System.out.println(jsonObject.toString());
   JSONObject build=new JSONObject(jsonObject.get("build").toString());
   JSONObject teamchatData= new JSONObject(jsonObject.get("teamchat_data").toString());
  // System.out.println("Username is: "+teamchatData.getString("username"));
   String status=null,phase=null,displayName="";
   int number=0;
   try{
  status=build.getString("status");
  }
   
   catch(Exception e){e.printStackTrace();}
   displayName=build.getString("display_name");
   number=build.getInt("number");
   phase=build.getString("phase");
   System.out.println(status+" "+number+" "+phase+" "+displayName);
   
   String username=teamchatData.getString("username");
   String uid=teamchatData.getString("uid");

   String html="<font color=blue>"
		   +"Project Name: <font color=green>"+displayName+"</font><br/>"
		  + "Build No: <font color=green>"+number+"</font><br/>"
	   	   +(status==null? "":"Status: <font color=green>"+status+"</font><br/>")
   		   + "Phase: <font color=green>"+phase+"</font></font>";
   Database db=new Database();
   if(db.hasUID(uid))
   db.postInRooms( uid,html);
   else
   System.out.println("Message Dropped");
	}catch(Exception e){
	e.printStackTrace();}
    }

}
