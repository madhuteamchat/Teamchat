package com.teamchat.integration;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Servlet implementation class Authentication
 */

@WebServlet("/Authentication")
public class Authentication extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String access_token = "";


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Authentication() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
	}

	private void sendPost(String url, String urlParameters,String email) {

		System.out.println("AUthorization called 2\n\n\n");
		try {

			URL obj = new URL(url);
			
			System.out.println("URL is " + url + urlParameters);

			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			// add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "Parameter");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

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
			Token token = (Token) gson.fromJson(response.toString(), Token.class);

			
			access_token = token.getAccess_token();

		    

			System.out.println("Access Token Is:" + access_token);
			
			DatabaseHandler db = new DatabaseHandler(email,access_token);
			
			TodoistIntegration.TODOIST(db);
						
			
			String TokenFromDB = db.getData(email);
			
			if(null == TokenFromDB)
			{
				//first time login
				db.insertData();
			}
			
			if(true == TokenFromDB.equals(access_token.toString()))
			{
				//do nothing....
			}
			else
			{
				db.updateData(email, access_token);	
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String user_agent = request.getHeader("User-Agent"), code = request
				.getParameter("code"),  email=request.getParameter("state"), client_id = "71be67ab414940e9866a8e09afa03a16", client_secret = "df194f80129743da961870e34fe99139", redirect_uri = "http://interns.teamchat.com:8084/Todoist/Authentication";
       
        System.out.println("Email is :"+email);
        
		PrintWriter pw = response.getWriter();

		try {

			//TeamchatAPI api = null;
			sendPost("https://todoist.com/oauth/access_token", "&client_id="
					+ client_id + "&client_secret=" + client_secret + "&code="
					+ code + "&redirect_uri=" + redirect_uri,email);
		} catch (Exception e) {

			pw.println(e);
		}

		pw.println("SuccessFully Authorised..");
		//pw.println("<script>window.close()</script>");

	}

}
