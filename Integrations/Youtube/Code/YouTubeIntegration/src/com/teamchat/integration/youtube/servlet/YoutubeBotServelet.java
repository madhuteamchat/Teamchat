package com.teamchat.integration.youtube.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teamchat.integration.youtube.properties.YoutubeProperty;

/**
 * Servlet implementation class YoutubeBotServelet
 */
@WebServlet("/YoutubeBotServelet")
public class YoutubeBotServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
  
	String client_id,client_secret;
	String redirecturi ="null"; 
	String usrname="";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public YoutubeBotServelet() {
        super();
        YoutubeProperty yp=new YoutubeProperty();
        yp.loadParams();
        redirecturi=yp.getRedirectUrl();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	String rid=request.getParameter("rid");
    	client_id=request.getParameter("client_id");
    	client_secret=request.getParameter("client_secret");
    	YoutubeConnect.rid=rid;
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
