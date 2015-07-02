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
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.evernote.auth.EvernoteService;

/**
 * Servlet implementation class CallbackServlet
 */
@WebServlet("/CallbackServlet")
public class CallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String consumerKey = PropertiesFile.getValue("consumer_key");
	String consumerSecret = PropertiesFile.getValue("consumer_secret");
	EvernoteService EVERNOTE_SERVICE = EvernoteService.PRODUCTION;
	String callbackURL = PropertiesFile.getValue("callback_servlet_url");
	public static OAuthService service;
	public Token requestToken;
   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CallbackServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Class<? extends EvernoteApi> providerClass = EvernoteApi.class;
		if (EVERNOTE_SERVICE == com.evernote.auth.EvernoteService.PRODUCTION) {
			providerClass = org.scribe.builder.api.EvernoteApi.class;
		}
		service = new ServiceBuilder().provider(providerClass)
				.apiKey(consumerKey).apiSecret(consumerSecret)
				.callback(callbackURL).build();
	
		String oauth_token = request.getParameter("oauth_token");
		String ver = request.getParameter("oauth_verifier");
		String request_secret=ManageDB.retrieveRequestSecret(oauth_token);
		requestToken=new Token(oauth_token, request_secret);
		Verifier verifier = new Verifier(ver);
		Token accessToken = service.getAccessToken(requestToken, verifier);
		ManageDB.update(requestToken.getToken(), accessToken.getToken());
		String room_id = ManageDB.getRoomIDbyToken(requestToken.getToken());
		response.setContentType("text/html");
		String site = "";
		if (accessToken.getToken()!=null) {
			site = "success.html";
		} else {
			site = "error.html";
		}
		// redirect to location
		response.sendRedirect(site);

		new EverNote().postMsg("Connected...", room_id);


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
