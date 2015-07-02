package com.teamchat.integrations.pipedrive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

/**
 * Servlet implementation class notification
 */
@WebServlet("/notification")
public class notification extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	TeamchatAPI api;
	
	public String mail;
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public notification() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		
		System.out.println("I am in notification page");
		PrintWriter out = response.getWriter();
		
		out.println("hi i am .......");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		System.out.println("I am do post  in notification page");
		PrintWriter out = response.getWriter();
		
		out.println("hi i am .......");
		
		

		String result = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(
				request.getInputStream(), "UTF-8"));
		String inputline;
		while ((inputline = in.readLine()) != null) {
			result += inputline;

			System.out.println(inputline + "\n");
		}
		
		in.close();
		System.out.println("retrive  contaents in do post method ="
				+ result);
		
	
		JSONObject j = new JSONObject(result);
	
	  System.out.println(j.toString());
	 
	
//	int  v=j.getInt("v");
//	System.out.println("version of data is ="+v);
	
     JSONObject j2 =j.getJSONObject("current");
  


  String title=j2.getString("title");
 
   int stateid=j2.getInt("stage_id");
  
   String formatedvalue=j2.getString("formatted_weighted_value");
   
   
   String personname=j2.getString("person_name");
   
   String org= j2.getString("org_name");
  
   String event=j.getString("event");
   
   
   TeamchatAPI api= Pipedrivebot.apig;
   
   mail=Pipedrivebot.email;
   Room r = api.context().create().setName("pipedrive").add(mail);
	 
   
   
   if(stateid==1)
   {
  
System.out.println("Hi new Notification :");
System.out.println(event);
System.out.println("Title ="+title);

System.out.println("Stage  of Deal = Idea");

System.out.println("value ="+formatedvalue);

System.out.println("Person  Name :"+personname);

System.out.println("Organization Name :"+org);



		api.perform(r.post(new PrimaryChatlet()
		.setQuestionHtml("Hi You Have New Notification from Pipedrive:<br>  "+event+"<br>Title ="+title+"<br>Stage of deal = Idea<br> Value ="+formatedvalue+
				"Person Name :"+personname+"<br> Organization Name :"+org
				
				)));
   }
   
   
   else
   if(stateid==2)
   {
  
System.out.println("Hi new Notification :");
System.out.println(event);
System.out.println("Title ="+title);

System.out.println("Stage  of Deal = Contact Made");

System.out.println("value ="+formatedvalue);

System.out.println("Person  Name :"+personname);

System.out.println("Organization Name :"+org);



api.perform(r.post(new PrimaryChatlet()
.setQuestionHtml("Hi You Have New Notification from Pipedrive:<br>  "+event+"<br>Title ="+title+"<br>Stage of deal = Contact Made<br> Value ="+formatedvalue+
		"<br>Person Name :"+personname+"<br> Organization Name :"+org
		
		)));
   }
   else
   if(stateid==3)
   {
	   
	   System.out.println("Hi new Notification :");
	   System.out.println(event);
	   System.out.println("Title ="+title);

	   System.out.println("Stage  of Deal = Needs Discovered");

	   System.out.println("value ="+formatedvalue);

	   System.out.println("Person  Name :"+personname);

	   System.out.println("Organization Name :"+org);
	   
	   

api.perform(r.post(new PrimaryChatlet()
.setQuestionHtml("Hi You Have New Notification from Pipedrive:<br>  "+event+"<br>Title ="+title+"<br>Stage of deal = Needs Discovered<br> Value ="+formatedvalue+
		"<br>Person Name :"+personname+"<br> Organization Name :"+org
		
		)));
   }
	
   else
   if(stateid==4)
   {
	   
	   System.out.println("Hi new Notification :");
	   System.out.println(event);
	   System.out.println("Title ="+title);

	   System.out.println("Stage  of Deal = Proposal Presented");

	   System.out.println("value ="+formatedvalue);

	   System.out.println("Person  Name :"+personname);

	   System.out.println("Organization Name :"+org);
	   
	   

api.perform(r.post(new PrimaryChatlet()
.setQuestionHtml("Hi You Have New Notification from Pipedrive:<br>  "+event+"<br>Title ="+title+"<br>Stage of deal = Proposal Presented<br> Value ="+formatedvalue+
		"<br>Person Name :"+personname+"<br> Organization Name :"+org
		
		)));
   }
   
   
   else
   if(stateid==5)
   {
	   
	   System.out.println("Hi new Notification :");
	   System.out.println(event);
	   System.out.println("Title ="+title);

	   System.out.println("Stage  of Deal = In Negotiation ");

	   System.out.println("value ="+formatedvalue);

	   System.out.println("Person  Name :"+personname);

	   System.out.println("Organization Name :"+org);
	   
	   api.perform(r.post(new PrimaryChatlet()
	   .setQuestionHtml("Hi You Have New Notification from Pipedrive:<br>  "+event+"<br>Title =<b>"+title+"</b><br>Stage of deal = <b>In Negotiation</b><br> Value ="+formatedvalue+
	   		"<br>Person Name :"+personname+"<br> Organization Name :"+org
	   		
	   		)));
   }
   
	
		
		
		
		
	}

}