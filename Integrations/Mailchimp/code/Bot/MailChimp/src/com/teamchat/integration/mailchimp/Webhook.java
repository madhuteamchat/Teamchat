package com.teamchat.integration.mailchimp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Scanner;

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
 * Servlet implementation class Webhook
 */
@WebServlet("/Webhook")
public class Webhook extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static  String[] id;
	public  static  String[] name;
	
	public static  String[]campid;
	
	public static  String[]campname;
	
	//String listname;
//	String campgname;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Webhook() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		System.out.println("working");
		System.out.println("I AM in do get method webhook");
		String mail = request.getParameter("email");

		System.out.println(mail);

		TeamchatAPI api = MailchimpBot.apig;

		Room r = api.context().create().setName("Mailchimp").add(mail);

		api.perform(

		r.post(new PrimaryChatlet()
				.setQuestionHtml(" Hi!! You have New Notification From Mailchimp<br/>"
						+ "Your list is Added to your webhook")));

		// request.getInputStream();

		String result = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(
				request.getInputStream(), "UTF-8"));
		String inputline;
		while ((inputline = in.readLine()) != null) {
			result += inputline;

			System.out.println(inputline + "\n");
		}
		System.out.println("retrive webhook contaents in do get method ="
				+ result);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

//		String inputline = "";
//		BufferedReader in = new BufferedReader(new InputStreamReader(
//				request.getInputStream()));
//		StringBuffer sb = new StringBuffer();
//		while ((inputline = in.readLine()) != null)
//			sb.append(inputline);
//		in.close();
//		System.out.println("**************************************");
//		System.out.println("JSON response in Webhook servlet is: "
//				+ sb.toString());
//		System.out.println("**************************************");
//		
		
		 Enumeration paramNames = request.getParameterNames();
		    while(paramNames.hasMoreElements()) {
		      String paramName = (String)paramNames.nextElement();
		    System .out.println("<TR><TD>" + paramName + "\n<TD>");
		     String[] paramValues = request.getParameterValues(paramName);
		      if (paramValues.length == 1) {
		        String paramValue = paramValues[0];
		        if (paramValue.length() == 0)
		         System. out.print("<I>No Value</I>");
		        else
		        System. out.print(paramValue);
		      } else {
		        System.out.println("<UL>");
		        for(int i=0; i<paramValues.length; i++) {
		         System. out.println("<LI>" + paramValues[i]);
		        }
		       System. out.println("</UL>");
		      }
		    }
		
		
		
		String type= request.getParameter("type");
//		
//		String firstname=request.getParameter("data[merges][FNAME]");
//		String email =request.getParameter("data[merges][EMAIL]");
//		String listid=request.getParameter("data[list_id]");
		
		
	
		
		
		System.out.println("type="+type);
		
		for(int i=0;i<name.length;i++)
		{
			System.out.println("I am in loop printing name of list");
			
			System.out.println(name[i]);
			
			
		}
		

	
		String mail = request.getParameter("email");
		TeamchatAPI api1 = MailchimpBot.apig;
	
	 Room r1 = api1.context().create().setName("Mailchimp").add(mail);
	 
	 
	if (type.equals("subscribe"))
	{
		String firstname=request.getParameter("data[merges][FNAME]");
		String email =request.getParameter("data[merges][EMAIL]");
		String listid=request.getParameter("data[list_id]");
		
		System.out.println("list id ="+listid);
		
		String listname=null;
		
		for(int i=0;i<id.length;i++)
		{
			if(listid.equals(id[i]))
			{
				listname=name[i];
				
				System.out.println("Im in subscriber loop");
			}
		}
		System.out.println("list name ="+listname);
	api1.perform(
	
	 r1.post(new PrimaryChatlet()
	 .setQuestionHtml(" Hi!! You have New Notification From Mailchimp <br>"+type
	 + "<br>Email id =" + email + "<br> Name ="
	 + firstname + "<br>For list =" + listname)));
	
	
	}
	
	
	else
		
	if(type.equals("unsubscribe"))
			{
		String firstname=request.getParameter("data[merges][FNAME]");
		String email =request.getParameter("data[merges][EMAIL]");
		String listid=request.getParameter("data[list_id]");
		
		
		String reason=request.getParameter("data[reason]");
		String campgid=request.getParameter("data[campaign_id]");
		String listname =null;
		String campgname =null;
		
		
		for(int i=0;i<id.length;i++)
		{
			if(listid.equals(id[i]))
			{
				 System.out.println("I am in list unsubscriber block");
				listname=name[i];
				
				
			}
		}
		

	
		
		for(int i=0;i<campid.length;i++)
		{
			if(campgid.equals(campid))
			{
				campgname=campname[i];
				System.out.println("I am in campaname unsubscribe block");
			}
		}
		
		
		
		api1.perform(
				
				 r1.post(new PrimaryChatlet()
				 .setQuestionHtml(" Hi!! You have New Notification From Mailchimp <br>"+type
				 + "<br>Email id =" + email + "<br> Name ="
				 + firstname + "<br>For list =" + listname+"<br/> For Campaign ="+campgname+"<br >Reason :"+reason)));
		
			}
	
	
	else
		if(type.equals("profile"))
		   {
			
			String firstname=request.getParameter("data[merges][FNAME]");
			String email =request.getParameter("data[merges][EMAIL]");
			String listid=request.getParameter("data[list_id]");
			
			
			String listname=null;
		//	String campgname=null;
			for(int i=0;i<id.length;i++)
			{
				if(listid.equals(id[i]))
				{
					listname=name[i];
				}
			}
			
			api1.perform(
					
					 r1.post(new PrimaryChatlet()
					 .setQuestionHtml(" Hi!! You have New Notification From Mailchimp <br> Updated  "+type
					 + "<br>Email id =" + email + "<br> Name ="
					 + firstname + "<br>For list =" + listname)));
		}
		else
			if(type.equals("upemail"))
			{
				
				String listid=request.getParameter("data[list_id]");

				String Newemail=request.getParameter("data[new_email]");
				
				String oldemail=request.getParameter("data[old_email]");
				
				String listname=null;
			//	String campgname=null;
			
				for(int i=0;i<id.length;i++)
				{
					if(listid.equals(id))
					{
						listname=name[i];
					}
				}
				
				api1.perform(
						
						 r1.post(new PrimaryChatlet()
						 .setQuestionHtml(" Hi!! You have New Notification From Mailchimp <br> Email Address Change"
						 + "<br> New Email id =" + Newemail + "<br>Old Email id="
						 + oldemail+ "<br>For list =" + listname)));
				
			}
	    
			else
				 if(type.equals("campaign"))
				 {
					 String reason=request.getParameter("data[reason]");

								String status=request.getParameter("data[status]");
								
								String subject=request.getParameter("data[subject]");
								
								String cleanedemails=request.getParameter("data[email]");
								String listid=request.getParameter("data[list_id]");
								
								String listname = null;
								
								String campgname =null;
								for(int i=0;i<id.length;i++)
								{
									if(listid==id[i])
									{
										listname=name[i];
									}
								}
								

						api1.perform(
								
								 r1.post(new PrimaryChatlet()
								 .setQuestionHtml(" Hi!! You have New Notification From Mailchimp <br>Campaign Sending Status"
								 + "<br> Subject  =" +subject + "<br>Status ="
								 +status+ "<br>For list = " + listname+"<br>Reason = "+reason)));
						
					 
				 }
	
				 else
					  if(type.equals("cleaned"))
					  
					  {
						  String reason=request.getParameter("data[reason]");

							String status=request.getParameter("data[status]");
							
							String subject=request.getParameter("data[subject]");
							
							String cleanedemails=request.getParameter("data[email]");
							String listid=request.getParameter("data[list_id]");
							String campgid=request.getParameter("data[campaign_id]");
							
							String listname =null;
							String campgname=null;
							for(int i=0;i<id.length;i++)
							{
								if(listid.equals(id[i]))
								{
									listname=name[i];
								}
							}
							

							
							for(int i=0;i<campid.length;i++)
							{
								if(campgid.equals(campid[i]))
								{
									campgname=campname[i];
								}
							}
							
						  
						  api1.perform(
									
									 r1.post(new PrimaryChatlet()
									 .setQuestionHtml(" Hi!! You have New Notification From Mailchimp <br>Cleaned Emails Reason will be one of  hard bounces or abuse"
									 + "<br> Emails  =" +cleanedemails+ "<br>For Campaign ="
									 +campgname+ "<br>For list=" + listname+"<br>Reason = "+reason)));
							
						  
					  }
	}
	
}
