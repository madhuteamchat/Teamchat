package com.teamchat.integrations.GoogleCalendar;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;

	@WebServlet("/GDocCallback")
	public class GDocCallback extends HttpServlet {
		private static final long serialVersionUID = 1L;
	     
	    public GDocCallback() {
	        super();
	    }
	    public static TeamchatAPI api1;


		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			System.out.println("CallBack");
			GDocConnect gdc=new GDocConnect();
			api1 = Test.api1;

			if(request.getParameter("code")==null)
			{
				api1.perform(api1
						.context()
						.currentRoom()
						.post(new TextChatlet(
								"Access Denied")));
			}
			else
			{
				api1.perform(api1
						.context()
						.currentRoom()
						.post(new TextChatlet(
								"Login Successful")));
			}
			
			gdc.getaccesstoken(request.getParameter("code"),request.getParameter("state"));
			PrintWriter writer = response.getWriter();
			
			writer.println("<script>window.close();</script>");
			
		}

		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		}

}
