package com.teamchat.youtube_integration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.Properties;

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
	
	private static String clientId="494950415885-fqv27scptt61fknafej5is92kd1hrbfe.apps.googleusercontent.com";
	private static String clientsecret="1HvoCJaWVFMciAfFyrZ9doSg";    
	String redirecturi ="https://localhost:8443/YoutubeIntegration/YoutubeCallBack"; 
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
    	usrname=request.getParameter("name");
    	System.out.println("**********************************"+usrname);
    	new YoutubeConnect().uid=usrname;
    	String requestUrl ="https://accounts.google.com/o/oauth2/auth?"
				+"client_id="+clientId+"&"
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
