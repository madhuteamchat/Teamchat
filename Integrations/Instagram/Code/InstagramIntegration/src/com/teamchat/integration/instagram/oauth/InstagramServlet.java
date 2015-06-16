package com.teamchat.integration.instagram.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teamchat.integration.instagram.properties.InstagramProperty;

/**
 * Servlet implementation class InstagramServlet
 */
@WebServlet("/InstagramServlet")
public class InstagramServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
	String redirecturi,client_id;
    public InstagramServlet() {
        super();
        // TODO Auto-generated constructor stub
        InstagramProperty ip=new InstagramProperty();
        ip.loadParams();
        redirecturi=ip.getRedirectUrl();
        client_id=ip.getClientId();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
    	
		String url="https://api.instagram.com/oauth/authorize/?client_id="+client_id+"&redirect_uri="+redirecturi+"&state="+request.getParameter("name")+"&scope=likes+comments&response_type=code";
		response.setContentType("application/x-www-form-urlencoded");
		response.sendRedirect(response.encodeRedirectURL(url));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
