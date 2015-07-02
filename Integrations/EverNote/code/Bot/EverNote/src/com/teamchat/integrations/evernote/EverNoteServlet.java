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
	String consumerKey = PropertiesFile.getValue("consumer_key");
	String consumerSecret = PropertiesFile.getValue("consumer_secret");
	EvernoteService EVERNOTE_SERVICE = EvernoteService.PRODUCTION;
	String callbackURL = PropertiesFile.getValue("callback_servlet_url");
	public OAuthService service = null;
	
	public void init() {
		// TODO Auto-generated constructor stub

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("mail") != null) {

			Class<? extends EvernoteApi> providerClass = EvernoteApi.class;
			if (EVERNOTE_SERVICE == com.evernote.auth.EvernoteService.PRODUCTION) {
				providerClass = org.scribe.builder.api.EvernoteApi.class;
			}
			service = new ServiceBuilder().provider(providerClass)
					.apiKey(consumerKey).apiSecret(consumerSecret)
					.callback(callbackURL).build();
						
			String mail = request.getParameter("mail");
			String room_id = request.getParameter("room_id");
			Token requestToken = service.getRequestToken();
			CallbackServlet.service=service;
			ManageDB.insert(mail, requestToken.getToken(),requestToken.getSecret(), "", room_id);
			String authUrl = EVERNOTE_SERVICE.getAuthorizationUrl(requestToken
					.getToken());
			response.sendRedirect(response.encodeRedirectURL(authUrl));
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
