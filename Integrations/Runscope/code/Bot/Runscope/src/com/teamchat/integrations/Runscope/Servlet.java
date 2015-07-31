package com.teamchat.integrations.Runscope;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static TeamchatAPI api;

	public Servlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String code = request.getParameter("code");
		PrintWriter writer = response.getWriter();
		writer.println("<script>window.close();</script>");
		String url = "https://www.runscope.com/signin/oauth/access_token";
		HttpClient clt = HttpClientBuilder.create().build();
		HttpPost req = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>(5);
		nvps.add(new BasicNameValuePair("code", code));
		nvps.add(new BasicNameValuePair("client_id", RunscopeBot.client_id));
		nvps.add(new BasicNameValuePair("client_secret",
				RunscopeBot.client_secret));
		nvps.add(new BasicNameValuePair("redirect_uri",
				RunscopeBot.callback_url));
		nvps.add(new BasicNameValuePair("grant_type", "authorization_code"));
		UrlEncodedFormEntity sd = new UrlEncodedFormEntity(nvps, "UTF-8");
		req.setEntity(sd);
		try {
			HttpResponse resp = clt.execute(req);
			BufferedReader rd = new BufferedReader(new InputStreamReader(resp
					.getEntity().getContent()));
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = rd.readLine()) != null)
				sb.append(line);
			String output = sb.toString();
			JSONObject j = new JSONObject(output);
			RunscopeBot.access_token = j.getString("access_token");
			api.performPostInCurrentRoom(new PrimaryChatlet()
					.setQuestionHtml("Login Successful"));
			RunscopeBot.selectBucket(api);
		} catch (Exception e) {
			e.printStackTrace();
			api.performPostInCurrentRoom(new PrimaryChatlet()
					.setQuestionHtml("Oops a problem occurred. Please try again."));
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
