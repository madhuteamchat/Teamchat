package com.teamchat.integration.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import com.teamchat.integration.database.Authentication;
import com.teamchat.integration.factory.PropertyFile;

public class YammerLogin extends HttpServlet {
	
	  public void doGet(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, java.io.IOException {
		    String clientid=PropertyFile.getProperty("client_id");
			String hostname=PropertyFile.getProperty("host");
			String port=PropertyFile.getProperty("port");
			String redirecturi="http://"+hostname+":"+port+"/Yammer/redirect.html";
			String username="";
			
		  if(request.getParameter("email") != null) {
			  username=request.getParameter("email");
		  System.out.println("redirecting to Yammer");
		    String contextPath= "https://www.yammer.com/dialog/oauth?client_id="+clientid+"&redirect_uri="+redirecturi+"&response_type=token";
		    response.sendRedirect(response.encodeRedirectURL(contextPath));
		  }
		  else
		  {   
			  try{
			  String token= request.getParameter("token");
			  System.out.println("code="+token);
				Authentication.setToken(username,token);
				response.sendRedirect(response.encodeRedirectURL("http://"+hostname+":"+port+"/Yammer/loggedin.html"));
			  }
			  catch(Exception e) {  
				  response.getWriter().print(e.getMessage());
			  }
				
		  }
		  
	   }
}
