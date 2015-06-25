package com.teamchat.integration.youtube.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class YoutubeBotServelet
 */
@WebServlet("/YoutubeBotServelet")
public class YoutubeBotServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
//	private static String clientId="494950415885-fqv27scptt61fknafej5is92kd1hrbfe.apps.googleusercontent.com";
//	private static String clientsecret="1HvoCJaWVFMciAfFyrZ9doSg";    
	String client_id,client_secret;
	String redirecturi ="http://interns.teamchat.com:8080/YouTubeIntegration/YoutubeCallBack"; 
	String usrname="";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public YoutubeBotServelet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	client_id=request.getParameter("client_id");
    	client_secret=request.getParameter("client_secret");
    	YoutubeConnect.client_id=client_id;
    	YoutubeConnect.client_secret=client_secret;
    	YoutubeConnect.uid=request.getParameter("name");
    	String requestUrl ="https://accounts.google.com/o/oauth2/auth?"
				+"client_id="+client_id+"&"
				+"redirect_uri="+redirecturi +"&"
				+"scope=https://www.googleapis.com/auth/youtube&"
				+"response_type=code&"
				+"approval_prompt=force&"
				+"access_type=offline";
		response.setContentType("application/x-www-form-urlencoded");
    	 response.sendRedirect(response.encodeRedirectURL(requestUrl));
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

}
