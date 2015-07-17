package com.teamchat.integrations.Desk;

import java.io.IOException;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class DeskBot {
	public static final String key = "5L6lbMypiOvqb5trotHh";
	public static final String secret = "bl8lgUKcBcju2IEjGtlwJQLYlJ0bSjP3h0OguInQ";
	public static final String token = "dH6GhQSUqVVmmfgzy8Ed";
	public static final String token_secret = "oqQtYya4CIjEAN9u8Z06QuxQ1fWD2b3BNoJaWa8J";
	public static String desk_email = "";
	public static String desk_password = "";
	public static String desk_website = "";

	@OnKeyword("help")
	public void Help(TeamchatAPI api) {
		Servlet.api = api;
		String msg = "Hey, this is Desk Bot. You can use me to manage your support activities with Desk.<br>Please enter your desk website name, email & password to login.";
		String s = "<table style=width:100%><tr><th>Keywords</th><th>Function</th></tr><tr><td>help</td><td>Login to Desk</td></tr><tr><td>notify</td><td>Create Custom Action to receive notifcations</td></tr>";
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Name").name("webname"));
		f.addField(api.objects().input().label("Email").name("email"));
		f.addField(api.objects().input().label("Password").name("password"));
		api.performPostInCurrentRoom(new PrimaryChatlet().setQuestionHtml(msg)
				.setReplyScreen(f).setReplyLabel("Login").alias("getcreds"));
		api.performPostInCurrentRoom(new PrimaryChatlet().setQuestionHtml(s));
	}

	@OnAlias("getcreds")
	public void getName(TeamchatAPI api) {
		String email = api.context().currentReply().senderEmail();
		desk_website = api.context().currentReply().getField("webname");
		desk_email = api.context().currentReply().getField("email");
		desk_password = api.context().currentReply().getField("password");
		DeskFetch.Login(email);
	}

	@OnKeyword("notify")
	public void Notify(TeamchatAPI api) {
		String email = api.context().currentSender().getEmail();
		String msg = "<a href=https://"
				+ desk_website
				+ ".desk.com/admin/apps target=_blank>Click here</a> to install webhooks (Custom Action) for your desk account.<br>Enter <a>http://integration.teamchat.com:8084/DeskIntegration/Servlet?email="
				+ email
				+ "</a> as the Webhook URL.<br><a href=https://"
				+ desk_website
				+ ".desk.com/admin/case-management/rules target=_blank>Click here</a> to create rules for your Webhook.";
		api.performPostInCurrentRoom(new PrimaryChatlet().setQuestionHtml(msg));
	}

	public static void PostCase(TeamchatAPI api, String s, String email) {
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Case ID").name("case_id"));
		f.addField(api.objects().input().label("Message").name("message"));
		api.perform(api
				.context()
				.create()
				.add(email)
				.post(new PrimaryChatlet().setQuestionHtml(s).setReplyScreen(f)
						.setReplyLabel("Reply").alias("casereply")));
	}

	@OnAlias("casereply")
	public void CaseReply(TeamchatAPI api) throws IOException {
		String case_id = api.context().currentReply().getField("case_id");
		String message = api.context().currentReply().getField("message");
		String email = api.context().currentReply().senderEmail();
		DeskFetch.CaseReply(case_id, message, email);
	}

}