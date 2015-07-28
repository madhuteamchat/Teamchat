package com.teamchat.integrations.GoogleCalendar;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/GDocBotServlet")
public class GDocBotServlet extends HttpServlet {
	
		private static final long serialVersionUID = 1L;
	
		String client_id,client_secret;
		String redirecturi ="http://interns.teamchat.com:8081/Zcal/GDocCallback"; 
		String usrname="";
	    /**
	     * @see HttpServlet#HttpServlet()
	     */
	    public GDocBotServlet() {
	        super();
	    }
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    	client_id=request.getParameter("client_id");
	    	client_secret=request.getParameter("client_secret");
	    	String email=request.getParameter("email");
	    	//GDocConnect.client_id=client_id;
	    	//GDocConnect.client_secret=client_secret;
	    	//GDocConnect.uid=request.getParameter("email");
	    	String requestUrl ="https://accounts.google.com/o/oauth2/auth?"
					+"client_id="+client_id+"&"
					+"redirect_uri="+redirecturi +"&"
					+"scope=https://www.googleapis.com/auth/calendar.readonly&"
					+"response_type=code&"
					+"state="+email+"&"
					+"approval_prompt=force&"
					+"access_type=offline";
			response.setContentType("application/x-www-form-urlencoded");
	    	response.sendRedirect(response.encodeRedirectURL(requestUrl));			
		}
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {			
		}

}
