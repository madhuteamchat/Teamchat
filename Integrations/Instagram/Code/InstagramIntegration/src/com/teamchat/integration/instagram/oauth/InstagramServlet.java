package com.teamchat.integration.instagram.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class InstagramServlet
 */
@WebServlet("/InstagramServlet")
public class InstagramServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InstagramServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String redirecturi="http://interns.teamchat.com:8086/InstagramIntegration/InstagramCallBack";
		String client_id="858afa5bc35e419c82d02ad8c58d037e";
//		String client_secret="f4dde757b8074f7893ce7fb4a5010ccc";
//		String rid=request.getParameter("rid");
//    	InstagramToken.rid=rid;
    	InstagramToken.uid=request.getParameter("name");
		String url="https://api.instagram.com/oauth/authorize/?client_id="+client_id+"&redirect_uri="+redirecturi+"&scope=likes+comments&response_type=code";
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
