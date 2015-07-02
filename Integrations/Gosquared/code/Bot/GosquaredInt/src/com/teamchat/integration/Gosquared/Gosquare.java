package com.teamchat.integration.Gosquared;

import java.io.IOException;
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
import com.teamchat.client.sdk.chatlets.TextChatlet;

/**
 * Servlet implementation class Gosquare
 */

public class Gosquare extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Gosquare() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//System.out.println("request");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("response");
		request.getInputStream();
		String mail = request.getParameter("email");
		Scanner s=new Scanner(request.getInputStream(),"UTF-8").useDelimiter("\\A");
		String l=s.hasNext()?s.next():"";
		System.out.println(l);
		try{
			JSONObject jobj =new JSONObject(l);
			int visitorss=jobj.getInt("concurrents");
//			String trigger=jobj.getString("triggeredAlert");
//			String sitedetails=jobj.getString("siteDetails");
			System.out.println(visitorss);
			TeamchatAPI api=Gosquaredbot.apig;
			
			Room r = api.context().create().setName("gosquared").add(mail);
			// api.perform(r.post(new
			// TextChatlet("Below threshold sales for "+productcode+" of  "+sales)));
			api.perform(r.post(new PrimaryChatlet()
				.setQuestionHtml("hi,you a notification in your gosquared account againest your set trigger<br> Number of visitors :"+visitorss)));
			//request.
		}
		 catch(Exception e)
		 {
			e.printStackTrace();
		 }
//		JSONObject jobj =new JSONObject(l);
//		int visitorss=jobj.getInt("concurrents");
////		String trigger=jobj.getString("triggeredAlert");
////		String sitedetails=jobj.getString("siteDetails");
//		System.out.println(visitorss);
//		TeamchatAPI api=Gosquaredbot.apig;
//		
//		Room r = api.context().create().setName("gosquared").add(mail);
//		// api.perform(r.post(new
//		// TextChatlet("Below threshold sales for "+productcode+" of  "+sales)));
//		api.perform(r.post(new TextChatlet("hi<br> Reason :<br> Number of visitors :"+visitorss+"<br> Details :")));
//		//request.
//	}
	}
}
