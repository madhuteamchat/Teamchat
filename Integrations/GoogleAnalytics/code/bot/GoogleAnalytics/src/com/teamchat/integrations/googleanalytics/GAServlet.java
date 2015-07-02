package com.teamchat.integrations.googleanalytics;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GAServlet
 */
@WebServlet("/GAServlet")
public class GAServlet extends HttpServlet {
	String client_id = PropertiesFile.getValue("client_id");
	String client_secret = PropertiesFile.getValue("client_secret");
	String redirectUrl = PropertiesFile.getValue("redirect_url");
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public GAServlet() {
		// TODO Auto-generated constructor stub

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String rid=request.getParameter("rid");
    	String mail=request.getParameter("mail");
    	String requestUrl ="https://accounts.google.com/o/oauth2/auth?"
				+"client_id="+client_id+"&"
				+"redirect_uri="+redirectUrl +"&"
				+"scope=https://www.googleapis.com/auth/analytics&"
				+"response_type=code&"
				+"approval_prompt=force&"
				+ "state="+mail+"&"
				+"access_type=offline";
		response.setContentType("application/x-www-form-urlencoded");
		ManageDB.insert(mail,null,null,rid);
    	response.sendRedirect(response.encodeRedirectURL(requestUrl));
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
