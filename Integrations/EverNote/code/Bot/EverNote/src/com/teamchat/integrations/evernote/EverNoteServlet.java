package com.teamchat.integrations.evernote;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.EvernoteApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import com.evernote.auth.EvernoteService;

/**
 * Servlet implementation class EverNoteServlet
 */
@WebServlet("/EverNoteServlet")
public class EverNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final String consumerKey="botbegins";
	static final String consumerSecret="1344399f32a08272";
	static final EvernoteService EVERNOTE_SERVICE = EvernoteService.PRODUCTION;
	public String authUrl="";
	public OAuthService service;
	public Token requestTokenObject;
  
	public void init() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Class<? extends EvernoteApi> providerClass = EvernoteApi.class;
        if (EVERNOTE_SERVICE == com.evernote.auth.EvernoteService.PRODUCTION) {
          providerClass = org.scribe.builder.api.EvernoteApi.class;
        }
		EverNoteConnect.temp=request.getParameter("name");
        String callbackURL="http://interns.teamchat.com:8081/EverNote/Sample";
        service = new ServiceBuilder()
        										.provider(providerClass)
        										.apiKey(consumerKey)
        										.apiSecret(consumerSecret)
        										.callback(callbackURL)
        										.build();
		requestTokenObject = service.getRequestToken();
		
		authUrl = EVERNOTE_SERVICE.getAuthorizationUrl(requestTokenObject.getToken());
		
		EverNoteVerf.authUrl=authUrl;
		EverNoteVerf.requestTokenObject=requestTokenObject;
		EverNoteVerf.service=service;
	    response.sendRedirect(response.encodeRedirectURL(authUrl));
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	public void destroy(){
		
	}
}
