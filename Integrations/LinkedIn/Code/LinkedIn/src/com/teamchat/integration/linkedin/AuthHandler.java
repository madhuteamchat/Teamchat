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
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.utils.WebAppTeamChatAPI;
import com.teamchat.integration.linkedin.utils.Utility;

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
		PrintWriter writer = null ;
		try {
			writer = response.getWriter();
		if (request.getParameter("oauth_problem") != null) {
			if (request.getParameter("oauth_problem").equals("user_refused"))
				response.sendRedirect("refuse.html");
		} else {
			OAuthService service;
			Token requestToken, accessToken;
			OAuthRequest requestLink;
			org.scribe.model.Response responseLink;
			DBHandler db = new DBHandler();

			service = new ServiceBuilder().provider(LinkedInApi.class)
					.apiKey("754z46slfke0xm").apiSecret("Jm46YNdPTb2toxoY")
					.build();

			String oAuthToken = request.getParameter("oauth_token");
			String oAuthVerifier = request.getParameter("oauth_verifier");

			String[] data = db.getData(oAuthToken);
			String oAuthSecret = data[1];
			String roomId = data[2];
			String email = data[3];

			requestToken = new Token(oAuthToken, oAuthSecret);

			Verifier v = new Verifier(oAuthVerifier);
			accessToken = service.getAccessToken(requestToken, v);

			System.out.println("\n\n\n\n\n" + accessToken + "\n\n\n\n\n");

			db.setAccessData(email, accessToken.getToken(),
					accessToken.getSecret());

			requestLink = new OAuthRequest(Verb.GET, Utility.people1);
			service.signRequest(accessToken, requestLink); // the access token
															// from step 4
			responseLink = requestLink.send();

			if (responseLink.getCode() != 401) {
				JSONObject j1 = new JSONObject(responseLink.getBody());

				TeamchatAPI api = WebAppTeamChatAPI
						.getTeamchatAPIInstance(getServletConfig());
				api.perform(api
						.context()
						.byId(roomId)
						.post(new TextChatlet("Now you are authorized to send linkedin updates from teamchat")));

				request.setAttribute("name", j1.getString("firstName") + " " + j1.getString("lastName"));
				RequestDispatcher rd = request.getRequestDispatcher("Auth.jsp");
				rd.forward(request, response);
			} else
				response.sendRedirect("error.html");
			}
		} catch (Exception ex) {
			writer.write("There was some error :" + ex.getMessage());
		} finally {	
			response.getWriter().close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
