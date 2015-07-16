package com.teamchat.integrations.NewRelic;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.teamchat.client.sdk.TeamchatAPI;

@WebServlet("/NewRelicServlet")
public class NewRelicServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static TeamchatAPI api;

	/**
	 * Default constructor.
	 */
	public NewRelicServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String s1 = new String();
		String s2 = new String();
		String l2 = new String();
		String l3 = new String();

		request.getInputStream();
		String email1 = request.getParameter("email");
		@SuppressWarnings("resource")
		Scanner s = new Scanner(request.getInputStream(), "UTF-8")
				.useDelimiter("\\A");
		String l = s.hasNext() ? s.next() : "";
		String l1 = URLDecoder.decode(l, "UTF-8");

		if (l1.charAt(0) == 'a') {
			l2 = l1.substring(6);
			try {
				JSONObject j = new JSONObject(l2);
				s1 = j.getString("long_description");
				s2 = j.getString("alert_url");
				NewRelicBot.post(s1, s2, email1);
			} catch (JSONException je) {
				je.printStackTrace();
			}
		} else if (l1.charAt(0) == 'd') {
			l3 = l1.substring(11);
			try {
				JSONObject j1 = new JSONObject(l3);
				s1 = j1.getString("description");
				s2 = j1.getString("deployment_url");
				NewRelicBot.post(s1, s2, email1);
			} catch (JSONException j) {
				j.printStackTrace();
			}
		} else {
			try {
				JSONObject j = new JSONObject(l1);
				s1 = j.getString("long_description");
				s2 = j.getString("alert_url");
				NewRelicBot.post(s1, s2, email1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}