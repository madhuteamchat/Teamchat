package com.teamchat.integrations.NewRelic;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class NewRelicBot {
	
	
	@OnKeyword("help")
	public static void begin(TeamchatAPI api1) {
		NewRelicServlet.api = api1;
		String email = NewRelicServlet.api.context().currentSender().getEmail();
		NewRelicServlet.api.perform(NewRelicServlet.api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<b>Hey, this is</b><b style=color:blue> New Relic Bot!</b><br><i>You can use me to receive any alert & deployment notifications of your applications monitored by New Relic.<br>Just enter this URL <a>http://interns.teamchat.com:8084/NewRelic/NewRelicServlet?email="
								+ email
								+ " </a>as your New Relic Webhook & we are good to go.</i>")));
	}

	public static void post(String msg1, String msg2, String mail) {
		NewRelicServlet.api.perform(NewRelicServlet.api
				.context()
				.create()
				.setName("p2p")
				.add(mail)
				.post(new PrimaryChatlet().setQuestionHtml("<b>" + msg1
						+ "</b><br><a href=" + msg2
						+ ">Click to view details</a>")));
	}


}
