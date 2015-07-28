package com.teamchat.integrations.Bugsnag;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class BugsnagBot {

	@OnKeyword("help")
	public void notify(TeamchatAPI api) {
		Servlet.api = api;
		String email = api.context().currentSender().getEmail();
		String msg = "<b>Hey, this is</b><b style=color:blue> Bugsnag Bot!</b><br><i>You can use me to receive any error notifications of your applications monitored by Bugsnag.<br><a href=https://bugsnag.com/user/sign_in target=_blank>Click here </a>to login to Bugsnag and enter <a>http://interns.teamchat.com:8083/Bugsnag/Servlet?email="
				+ email + "</a> as your application's webhook URL.";
		api.performPostInCurrentRoom(new PrimaryChatlet().setQuestionHtml(msg));
	}

}