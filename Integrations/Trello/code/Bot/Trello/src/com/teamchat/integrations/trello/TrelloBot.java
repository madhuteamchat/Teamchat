package com.teamchat.integrations.trello;

import java.io.FileNotFoundException;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.integrations.trello.ManageDB;

public class TrelloBot {
	public static TeamchatAPI api;

	@OnKeyword("help")
	public void help(TeamchatAPI api) {
		TrelloBot.api = api;
		String print = "<b>I'm Trello bot</b><br>";
		print = print
				+ "I will help you stay updated with your Trello account.<br>";
		print = print + "Use me to perform these functions:<br>";
		print = print
				+ "<i><b>connect</b></i>: To sign in to Trello account.<br>";
		print = print
				+ "<i><b>disconnect</b></i>: To sign out from Trello account<br>";
		print = print
				+ "<i><b>organizations</b></i>: To view all the organizations a user is a member of<br>";
		print = print + "<i><b>boards</b></i>: To view all the boards<br>";
		print = print
				+ "<i><b>notify</b></i>: To manage your notifications<br>";
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(print)));
		print = "You can access multiple Trello accounts by <b>disconnect</b>ing and <b>connect</b>ing.";
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(print)));
	}

	@OnKeyword("connect")
	public void connect(TeamchatAPI api) {
		TrelloBot.api = api;
		String mail = api.context().currentSender().getEmail();
		String room_id = api.context().currentRoom().getId();
		String accessToken = null;

		accessToken = ManageDB.retrieveAccessToken(mail);
		if (accessToken == null || accessToken.length() < 5) {
			String print = "";
			String embedded_url = createEmbeddedLink(
					PropertiesFile.getValue("trello_servlet") + "?&room_id="
							+ room_id + "&email=" + mail, "logging in...",
					"http");
			print = print + "<a href=" + embedded_url + ">Login to Trello</a>";
			api.perform(api.context().currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml(print)));

		} else {
			api.perform(api.context().currentRoom()
					.post(new TextChatlet("Configuration successful...")));
		}
	}

	@OnKeyword("disconnect")
	public void disconnect(TeamchatAPI api) throws FileNotFoundException {
		TrelloBot.api = api;
		String mail = api.context().currentSender().getEmail();
		String token = ManageDB.retrieveAccessToken(mail);
		new TrelloMethods().deleteWebhook(token);
		ManageDB.remove(mail);
		api.perform(api.context().currentRoom()
				.post(new TextChatlet("Disconnected...")));
	}

	@OnKeyword("notify")
	public void notify(TeamchatAPI api) {
		TrelloBot.api = api;
		String mail = api.context().currentSender().getEmail();
		String token = ManageDB.retrieveAccessToken(mail);
		if (token != null) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("Turn ON/OFF your notifications")
							.setReplyScreen(
									api.objects()
											.form()
											.addField(
													api.objects().select()
															.label("TURN")
															.name("turn")
															.addOption("ON")
															.addOption("OFF")))
							.alias("notify")));
		} else {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("First <b><i>connect</i></b> to manage your notifications")));

		}
	}

	@OnAlias("notify")
	public void notifyAlias(TeamchatAPI api) {
		String mail = api.context().currentReply().senderEmail();
		String turn = api.context().currentReply().getField("turn");
		String token = ManageDB.retrieveAccessToken(mail);
		if (turn.equals("ON")) {
			if (new TrelloMethods().createWebhook(token)) {
				api.perform(api
						.context()
						.currentRoom()
						.post(new TextChatlet(
								"Notifications turned ON successfully")));
			}
		} else if (turn.equals("OFF")) {
			System.out.println("turn off.....");
			if (new TrelloMethods().deleteWebhook(token)) {
				ManageDB.stopNotifications(mail);
				api.perform(api
						.context()
						.currentRoom()
						.post(new TextChatlet(
								"Notifications turned OFF successfully")));

			} else {
				api.perform(api
						.context()
						.currentRoom()
						.post(new TextChatlet(
								"Unable to turn OFF your notifications")));
			}
		}
	}

	@OnKeyword("organizations")
	public void getOrgzns(TeamchatAPI api) {
		TrelloBot.api = api;
		String mail = api.context().currentSender().getEmail();
		String token = ManageDB.retrieveAccessToken(mail);

		String print = new TrelloMethods().getOrganizations(token);
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(print)));
	}

	@OnKeyword("boards")
	public void getBoards(TeamchatAPI api) {
		TrelloBot.api = api;
		String mail = api.context().currentSender().getEmail();
		String token = ManageDB.retrieveAccessToken(mail);
		String print = new TrelloMethods().getBoards(token);
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(print)));
	}

	public void postMsg(String print, String rid) {
		// TODO Auto-generated method stub
		Room r = api.context().byId(rid);
		api.perform(r.post(new PrimaryChatlet().setQuestionHtml(print)));
	}

	public static String createEmbeddedLink(String url, String title,
			String protocol) {
		JSONObject object = new JSONObject();
		JSONObject web = new JSONObject();
		web.put("title", title);
		web.put("cancelBtnName", "Back");
		web.put("minWidth", "200");
		web.put("draggable", "true");
		web.put("newWindow", "true");
		web.put("url", url);
		object.put("web", web);
		System.out.println(object.toString());
		byte[] byteArray = Base64.encodeBase64(object.toString().getBytes());
		String encodedString = new String(byteArray);
		String fUrl = protocol + "://teamchat:data=" + encodedString;

		return fUrl;

	}
}
