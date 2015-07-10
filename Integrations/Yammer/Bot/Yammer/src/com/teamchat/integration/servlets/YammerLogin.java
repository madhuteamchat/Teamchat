package com.teamchat.integration.servlets;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import com.teamchat.integration.database.Authentication;
import com.teamchat.integration.factory.ParseJSONAuth;
import com.teamchat.integration.factory.PropertyFile;

public class YammerLogin extends HttpServlet {
	
	  public void doGet(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, java.io.IOException {
		    String clientid=PropertyFile.getProperty("client_id");
		    String client_secret=PropertyFile.getProperty("client_secret");
			String hostname=PropertyFile.getProperty("host");
			String port=PropertyFile.getProperty("port");
			String redirecturi="http://"+hostname+":"+port+"/Yammer/yammerlogin";
		
			
		  if(request.getParameter("code") == null) {
			  //request from teamchat
			  String username=request.getParameter("email");
		      System.out.println("redirecting to Yammer");
		      String contextPath= "https://www.yammer.com/oauth2/authorize?state="+username+"&client_id="+clientid+"&response_type=code&redirect_uri="+redirecturi;
		      response.sendRedirect(response.encodeRedirectURL(contextPath));
		  }
		  else
		  {   //request from Yammer
			  try{
			  String code= request.getParameter("code");
			  String useremail=request.getParameter("state");
			  System.out.println("code="+code);
			  URL urldemo = new URL("https://www.yammer.com/oauth2/access_token");
				 HttpURLConnection yammerconnect = (HttpURLConnection)urldemo.openConnection();
				 yammerconnect.setRequestMethod("POST");
			     StringBuilder postData = new StringBuilder();
			     postData.append("client_id="+clientid);
			     postData.append("&client_secret="+client_secret);
			     postData.append("&redirect_uri="+redirecturi);
			     postData.append("&grant_type="+"authorization_code");
			     postData.append("&code="+code);
			     byte[] postDataBytes = postData.toString().getBytes("UTF-8");
			     yammerconnect.setDoOutput(true);
			     yammerconnect.getOutputStream().write(postDataBytes);
			     System.out.println("response code "+yammerconnect.getResponseCode());
			     if(yammerconnect.getResponseCode()==401) {
			       response.sendRedirect(response.encodeRedirectURL("http://"+hostname+":"+port+"/Yammer/error.html"));
			       yammerconnect.disconnect();
			     }
			     else {
			     String token=ParseJSONAuth.getToken(HTTPRequest.getBodyOfResponse(yammerconnect));
			     yammerconnect.disconnect();
			     System.out.println("access token is"+token+" and useremail is "+useremail);
				Authentication.setToken(useremail,token);
				response.sendRedirect(response.encodeRedirectURL("http://"+hostname+":"+port+"/Yammer/success.html"));
			     }
			  }
			  catch(Exception e) {  
				  e.printStackTrace();
				  response.getWriter().print(e.getMessage());
			  }
				
		  }
		  
	   }
}
