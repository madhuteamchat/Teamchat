package com.teamchat.integration;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 *  @author:Priyanka Shiyal
 */
@WebServlet("/Authentication")
public class Authentication extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Authentication() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	private void sendPost(String url, String User_agent, String urlParameters,
			String email) throws Exception {
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", User_agent);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

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
		// print result

		Gson gson = new Gson();
		Token token = (Token) gson.fromJson(response.toString(), Token.class);

		String access_token = token.getAccess_token();

		System.out.println("access token in sendpost:" + access_token);

		DatabaseHandler db = new DatabaseHandler(email, access_token);

		RunScope.RUNSCOPE(db);

		String TokenFromDB = db.getData(email);

		if (null == TokenFromDB) {
			// first time login
			db.insertData();
		}

		if (true == TokenFromDB.equals(access_token)) {
			// do nothing....
		} else {
			db.updateData(email, access_token);
		}

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		PrintWriter pw = response.getWriter();

		String user_agent = request.getHeader("User-Agent"), code = request
				.getParameter("code"), client_id = "e907a5b5-42ed-4c36-b3a1-5a641c2a631c", client_secret = "d79bf803-384b-4cb7-8ea9-36236dd597d6", email = request
				.getParameter("state"), redirect_uri = "http://interns.teamchat.com:8084/RunScopeIntegration/Authentication";

		// pw.println("DoGet Called.." +code + "email is:"+ email);

		try {

			sendPost("https://www.runscope.com/signin/oauth/access_token",
					user_agent, "client_id=" + client_id + "&client_secret="
							+ client_secret + "&code=" + code
							+ "&grant_type=authorization_code"
							+ "&redirect_uri=" + redirect_uri, email);
		} catch (Exception e) {

		}
		pw.println("Successfully Authorized..");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter pw = response.getWriter();
		//pw.println("DoPost Called..");
	}

}
