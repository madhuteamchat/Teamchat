package com.teamchat.integrations.evernote;

import java.io.IOException;
import java.io.PrintWriter;

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
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.thrift.TException;

/**
 * Servlet implementation class EverNoteServlet
 */
@WebServlet("/EverNoteServlet")
public class EverNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String consumerKey = PropertiesFile.getValue("consumer_key");
	String consumerSecret = PropertiesFile.getValue("consumer_secret");
	EvernoteService EVERNOTE_SERVICE = EvernoteService.PRODUCTION;
	String callbackURL = PropertiesFile.getValue("callback_servlet_url");

	public void init() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("name") != null) {
			Class<? extends EvernoteApi> providerClass = EvernoteApi.class;
			if (EVERNOTE_SERVICE == com.evernote.auth.EvernoteService.PRODUCTION) {
				providerClass = org.scribe.builder.api.EvernoteApi.class;
			}

			String mail = request.getParameter("name");
			String room_id = request.getParameter("room_id");
			String state = mail + "," + room_id;
			EverNoteVerf.mail = mail;
			EverNoteVerf.room_id = room_id;
			OAuthService service = new ServiceBuilder().provider(providerClass)
					.apiKey(consumerKey).apiSecret(consumerSecret)
					.callback(callbackURL).build();
			Token requestTokenObject = service.getRequestToken();

			String authUrl = EVERNOTE_SERVICE
					.getAuthorizationUrl(requestTokenObject.getToken());
			EverNoteVerf.service = service;
			EverNoteVerf.requestTokenObject = requestTokenObject;

			System.out.println("Details are : "+authUrl+"&state="+state);
			response.sendRedirect(response.encodeRedirectURL(authUrl+"&state="+state));
		} else {
			EverNoteVerf env = new EverNoteVerf();
			String state=request.getParameter("state");
			System.out.println("**************");
			System.out.println("Details are : "+state);
			env.getverf(request.getParameter("oauth_verifier"));
			PrintWriter writer = response.getWriter();
			writer.print("Connected successfully. Now you can access your EverNote from your Teamchat");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	public void destroy() {

	}
}
