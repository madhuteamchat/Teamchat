package com.teamchat.integration.office365.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teamchat.integration.office365.property.Office365Property;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	String client_id,client_secret;
	String redirecturi ="null"; 
	String usrname="";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
        Office365Property op=new Office365Property();
        op.loadParams();
        client_id=op.getClientId();
        redirecturi=op.getRedirectUrl();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String sname=request.getParameter("sname");
		String requestUrl ="https://login.microsoftonline.com/common/oauth2/authorize?"
				+"client_id="+client_id+"&"
				+"redirect_uri="+redirecturi+"&"
				+"state="+sname+"&"
				+"response_type=code";
		System.out.println(requestUrl);
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
