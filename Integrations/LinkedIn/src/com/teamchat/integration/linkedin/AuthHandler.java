package com.teamchat.integration.linkedin;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.utils.WebAppTeamChatAPI;

/**
 * Servlet implementation class LinkedinServlet
 */
@WebServlet("/AuthHandler")
public class AuthHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthHandler() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		OAuthService service;
		Token requestToken, accessToken;
		OAuthRequest requestLink;
		org.scribe.model.Response responseLink;
		DBHandler db = new DBHandler();
		PrintWriter out = response.getWriter();
		
		service = new ServiceBuilder()
        .provider(LinkedInApi.class)
        .apiKey("754z46slfke0xm")
        .apiSecret("Jm46YNdPTb2toxoY")
        .build();
		
		String oAuthToken = request.getParameter("oauth_token");
		String oAuthVerifier = request.getParameter("oauth_verifier");
		
		String[] data = db.getData(oAuthToken);
		String oAuthSecret = data[1];
		String roomId = data[2];
		String email = data[3];
		
		requestToken = new Token (oAuthToken, oAuthSecret);
		
		Verifier v = new Verifier(oAuthVerifier);
		accessToken = service.getAccessToken(requestToken, v);
		
		db.setAccessData(email, accessToken.getToken(), accessToken.getSecret());
		
		requestLink = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/people/~?format=json");
		service.signRequest(accessToken, requestLink); // the access token from step 4
		responseLink = requestLink.send();
		
		JSONObject j1 = new JSONObject (responseLink.getBody());
		
		TeamchatAPI api = WebAppTeamChatAPI.getTeamchatAPIInstance(getServletConfig());
		api.perform(api
				.context()
				.byId(roomId)
				.post(new PrimaryChatlet().setQuestionHtml("Hello! " + j1.getString ("firstName") + " " + j1.getString ("lastName") + "<br/>Connected Successfully")));

//		out.print("<img src='http://www.teamchat.com/wp-content/uploads/2013/04/logoteamchat.png' width=300 height=100/><br/><div style=\"text-align: center; color: #52b66e; font-size: 30px; vertical-align: 5px; padding-bottom: 10px;\">Hello! Dear "
//				+ j1.getString ("firstName") + " " + j1.getString ("lastName")
//				+ ", authenticated successfully<br/>Go back to <a href=\'https://enterprise.teamchat.com/webjim-echat/html/dashboard.html?v=3.0\'>Teamchat</a></div>");
		RequestDispatcher rd = request.getRequestDispatcher("Authenticated.jsp");
		rd.include(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
