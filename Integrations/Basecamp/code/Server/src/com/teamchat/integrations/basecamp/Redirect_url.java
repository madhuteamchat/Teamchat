package com.teamchat.integrations.basecamp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class Redirect_url
 */
@WebServlet(description = "get the verification code from this url", urlPatterns = { "/Redirect_url" })
public class Redirect_url extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
	private void sendPost(String url, String User_agent, String urlParameters)
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

		if (responseCode == 200) {
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
			Token token = (Token) gson.fromJson(response.toString(),
					Token.class);
			System.out.println(token.access_token());
		} else {

		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// get configuration info from config file
		Config_handler config = new Config_handler();
		if (config.isEmpty()) {
			config.initProperties();
			System.out
					.println("Properties file has been created on the Server!");
		}
		PrintWriter out = response.getWriter();
		try {
			sendPost("https://launchpad.37signals.com/authorization/token",
					request.getHeader("User-Agent"),
					"type=web_server&client_id=" + config.getclient_id()
							+ "&redirect_uri=" + config.getredirect_uri()
							+ "&client_secret=" + config.getclient_secret()
							+ "&code=" + request.getParameter("code"));
		} catch (Exception e) {
			// TODO: handle exception
			out.println(e);
		}
		// TODO retrieve CLIENT_ID ,etc from db
		out.println("<script>window.close();</script>");
	}

}
