package com.teamchat.integrations.basecamp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.basecamp.classes.Authorization;
import com.google.gson.Gson;
import java.net.URLEncoder;

/**
 * Servlet implementation class Redirect_url
 */
@WebServlet(description = "get the verification code from this url", urlPatterns = { "/Redirect_url" })
public class Redirect_url extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static String currentEmail = "";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Redirect_url() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		// TODO Do required initialization
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	// HTTP POST request
	private int sendPost(String url, String User_agent, String urlParameters)
			throws Exception {
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", User_agent);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		// urlParameters must be in this format
		// "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		Gson gson = new Gson();
		// put response in token class
		Token token = (Token) gson.fromJson(response.toString(), Token.class);
		// System.out.println(token.getAccess_token());
		Db_handler db = new Db_handler();
		String get_response = sendGet_auth(
				Universal.AUTHORIZATION_URL,
				Universal.APP_NAME, "",
				token.getAccess_token());
		// parsing a json like this
		// {
		// "accounts": [
		// {
		// "product": "bcx",
		// "name": "Teamchat",
		// "id": 2965117,
		// "href": "https://basecamp.com/2965117/api/v1"
		// }
		// ],
		// "identity": {
		// "email_address": "puru1joy@gmail.com",
		// "last_name": "Jain",
		// "first_name": "Puranjay",
		// "id": 11321861
		// },
		// "expires_at": "2015-07-02T14:08:18Z"
		// }
		Authorization auth = (Authorization) gson.fromJson(get_response,
				Authorization.class);
		Basecamp_basics bb = new Basecamp_basics(currentEmail, auth
				.getIdentity().getEmailAddress(), auth.getAccounts().get(0)
				.getHref(), token.getExpires_in(), token.getRefresh_token(),
				token.getAccess_token());
		System.out.println("Authenticated: " + db.StorageHandler(bb));
		return responseCode;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		int resp_code = 400;
		try {
			resp_code = sendPost(
					Universal.TOKEN_URL,
					request.getHeader("User-Agent"),
					"type=web_server&client_id=" + Universal.CLIENT_ID
							+ "&redirect_uri="
							+ URLEncoder
									.encode(Universal.REDIRECT_URL, "UTF-8")
							+ "&client_secret=" + Universal.CLIENT_SECRET
							+ "&code=" + request.getParameter("code"));
		} catch (Exception e) {
			// TODO: handle exception
			out.println(e);
		}
		// TODO retrieve CLIENT_ID ,etc from db
		// redirect according to request
		// Set response content type
		response.setContentType("text/html");
		// New location to be redirected
		String site = "";
		if (resp_code <= 200) {
			site = "success.html";
		} else {
			site = "error.html";
		}
		// redirect to location
		response.sendRedirect(site);
		// out.println("<script>window.close();</script>");
	}

	// HTTP GET request
	private String sendGet_auth(String url, String User_agent,
			String urlParameters, String token) throws Exception {
		// url example http://www.google.com/search
		// urlParameters example q=Search&browser=chrome
		URL obj = null;
		if (urlParameters.isEmpty()) {
			obj = new URL(url);
		} else {

			obj = new URL(url + "?" + urlParameters);
		}
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", User_agent);
		con.setRequestProperty("Authorization", "Bearer " + token);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}
}
