package com.slack.auth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

/**
 * Servlet implementation class slack_auth
 */
@WebServlet("/slack_auth")
public class slack_auth extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public slack_auth() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(request, response);
	}

	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		System.out.println(request.getParameter("code"));

		String code = request.getParameter("code");
		String s = "https://slack.com/api/oauth.access?client_id=5090557084.5108581326&client_secret=6f41ca2089675e9cadddae278f18b3cc&code="
				+ code
				+ "&redirect_uri=http://localhost:8080/slack_auth/slack_auth&pretty=1";
		// https://slack.com/oauth/authorize?client_id=5090557084.5108581326&redirect_uri=http://localhost:8080/slack_auth/slack_auth&scope=identify,read,post,client,admin
		response.sendRedirect(s); //Need to extract the JSON output of this url since it contains access token. Do it tomorrow.

		
		/*
		HttpClient clt = new DefaultHttpClient();
		HttpGet req = new HttpGet(s);

		HttpResponse resp = clt.execute(req);
		BufferedReader rd = new BufferedReader(new InputStreamReader(resp
				.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString();
		JSONObject j = new JSONObject(output);
*/
	}

}
