package com.teamchat.integration.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import com.teamchat.integration.factory.ParseJSONAuth;
import com.teamchat.integration.factory.PropertyFile;

public class YammerLogin extends HttpServlet {
	
	String clientid="VxmwJQvzJg8wR3nVO49nTA";
	String clientsecret="Z8nna7DDumINJmn8A7VEth6xQJMPdIcUm95bJR5w";
	String redirecturi="http://localhost:8080/Yammer/redirect.html";
	String username="";
	
	  public void doGet(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, java.io.IOException {

		  if(request.getParameter("email") != null) {
			  username=request.getParameter("email");
		  System.out.println("redirecting to Yammer");
		    String contextPath= "https://www.yammer.com/dialog/oauth?client_id="+clientid+"&redirect_uri="+redirecturi+"&response_type=token";
		    response.sendRedirect(response.encodeRedirectURL(contextPath));
		  }
		  else
		  {
			  String token= request.getParameter("token");
			  System.out.println("code="+token);
			/*  String inputLine="";
			  String contextPath="https://www.yammer.com/oauth2/access_token?client_id="+clientid+"&client_secret="+clientsecret+"&code="+token+"&grant_type=authorization_code";  
		//	  response.sendRedirect(response.encodeRedirectURL(contextPath));
			  System.out.println("Now making connection to "+contextPath);
			    try {
			        URL urldemo = new URL(contextPath);
			        URLConnection yc = urldemo.openConnection();
			        BufferedReader in = new BufferedReader(new InputStreamReader(
			                yc.getInputStream()));
			        
			        while ((inputLine = in.readLine()) != null);
			            System.out.println(inputLine);
			        in.close();
			        }catch(Exception e) {
			            System.out.println(e);
			        }
			    System.out.println("The json response is"+inputLine);
			    ParseJSONAuth parse=new ParseJSONAuth();
				String access_token=parse.getToken(inputLine);*/
				PropertyFile.setProperty(username,token);
				response.sendRedirect(response.encodeRedirectURL("http://localhost:8080/Yammer/loggedin.html"));
		  }
		  
	   }
}
