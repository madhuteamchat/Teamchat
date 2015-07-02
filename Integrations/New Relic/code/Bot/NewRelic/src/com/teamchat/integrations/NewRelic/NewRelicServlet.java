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

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PollChatlet;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

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
				post(s1, s2, email1);
			} catch (JSONException je) {
				je.printStackTrace();
			}
		} else if (l1.charAt(0) == 'd') {
			l3 = l1.substring(11);
			try {
				JSONObject j1 = new JSONObject(l3);
				s1 = j1.getString("description");
				s2 = j1.getString("deployment_url");
				post(s1, s2, email1);
			} catch (JSONException j) {
				j.printStackTrace();
			}
		} else {
			try {
				JSONObject j = new JSONObject(l1);
				s1 = j.getString("long_description");
				s2 = j.getString("alert_url");
				post(s1, s2, email1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@OnKeyword("help")
	public static void begin(TeamchatAPI api1) {
		api = api1;
		String email = api.context().currentSender().getEmail();
		api.perform(api
				.context()
				.currentRoom()
				.post(new TextChatlet(
						"<b>Hey, this is</b><b style=color:blue> New Relic Bot!</b><br><i>You can use me to receive any alert & deployment notifications of your applications monitored by New Relic.<br>Just enter this URL <a>http://interns.teamchat.com:8080/NewRelic/NewRelicServlet?email="
								+ email
								+ " </a>as your New Relic Webhook & we are good to go.</i>")));
	}

	public static void post(String msg1, String msg2, String mail) {
		api.perform(api
				.context()
				.create()
				.setName("p2p")
				.add(mail)
				.post(new PrimaryChatlet().setQuestionHtml("<b>" + msg1
						+ "</b><br><a href=" + msg2 + ">" + msg2 + "</a>")));
	}

}