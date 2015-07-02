package com.teamchat.integrations.trello;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TrelloApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * Servlet implementation class TrelloServlet
 */

public class TrelloServlet extends HttpServlet {
	private static final String API_KEY = PropertiesFile.getValue("api_key");
	private static final String API_SECRET = PropertiesFile
			.getValue("api_secret");
	private static final String PROTECTED_RESOURCE_URL = "https://trello.com/1/members/me";
	private static final long serialVersionUID = 1L;
	String callbackUrl = PropertiesFile.getValue("callback_servlet");
	OAuthService service = new ServiceBuilder().provider(TrelloApi.class)
			.callback(callbackUrl).apiKey(API_KEY).apiSecret(API_SECRET)
			.build();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TrelloServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getParameter("email") != null) {
			String email = request.getParameter("email");
			String room_id = request.getParameter("room_id");

			Token request_token = service.getRequestToken();
			ManageDB.insert(email, request_token.getToken(),
					request_token.getSecret(), null, room_id,null,null);

			String auth_url = service.getAuthorizationUrl(request_token);
			response.sendRedirect(response.encodeRedirectURL(auth_url));
		} else if(request.getParameter("oauth_verifier")!=null){
			String ver = request.getParameter("oauth_verifier");
			String oauth_token = request.getParameter("oauth_token");
			String request_secret = ManageDB.retrieveRequestSecret(oauth_token);
			Token requestToken = new Token(oauth_token, request_secret);

			Verifier verifier = new Verifier(ver);

			Token accessToken = service.getAccessToken(requestToken, verifier);
			OAuthRequest oauth_request = new OAuthRequest(Verb.GET,
					PROTECTED_RESOURCE_URL);
			service.signRequest(accessToken, oauth_request);
			Response response1 = oauth_request.send();
			System.out.println("Response is : " + response1.getBody());
			String user_id=new GetDetails().getUserID(accessToken.getToken());
			ManageDB.updateAccessTokenandTrelloID(requestToken.getToken(), accessToken.getToken(),user_id);
			String rid = ManageDB.getRoomIDbyToken(requestToken.getToken());
			response.setContentType("text/html");
			// New location to be redirected
			String site = "";
			if (accessToken.getToken().length() > 5) {
				site = "success.html";
			} else {
				site = "error.html";
			}
			// redirect to location
			response.sendRedirect(site);

			new TrelloBot().postMsg("Connected...", rid);
		}
		else{
			response.sendRedirect("error.html");
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

}
