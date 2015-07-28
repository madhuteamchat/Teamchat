package com.teamchat.integrations.Bugsnag;

import java.io.IOException;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static TeamchatAPI api;

	public Servlet() {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.getInputStream();
		System.out.println("********************");
		String email = request.getParameter("email");
		@SuppressWarnings("resource")
		Scanner s = new Scanner(request.getInputStream(), "UTF-8")
				.useDelimiter("\\A");
		String l = s.hasNext() ? s.next() : "";
		try {
			JSONObject j = new JSONObject(l);
			JSONObject j1 = j.getJSONObject("trigger");
			String msg1 = j1.getString("message");
			JSONObject j2 = j.getJSONObject("error");
			String msg2 = j2.getString("message");
			String url = j2.getString("url");

			String post = "<h4><b>" + msg1 + "</b><br>" + msg2
					+ "</h4><a href=" + url + " target=_blank>View Details</a>";
			System.out.println(post);
			api.perform(api.context().create().add(email)
					.post(new PrimaryChatlet().setQuestionHtml(post)));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}