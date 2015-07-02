package com.teamchat.integrations.googleanalytics;

import java.io.FileNotFoundException;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class GAnalyticsBot {
	public static TeamchatAPI api;
	String accessToken;

	@OnKeyword("help")
	public void help(TeamchatAPI api) {
		GAnalyticsBot.api = api;
		String print = "<b>I'm Google Analytics bot</b><br>";
		print = print
				+ "I will help you stay updated with your Google Analytics account.<br>";
		print = print + "Use me to perform these functions:<br>";
		print = print
				+ "<i><b>connect</b></i>: To sign in to Google Analytics account<br>";
		print = print
				+ "<i><b>disconnect</b></i>: To sign out from Google Analytics account<br>";
		print = print + "<i><b>current</b></i>: To get current analysis.<br>";
		print = print
				+ "<i><b>report</b></i>: To get report in specific duration.<br>";
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(print)));
		print = "You can access multiple Google analytics accounts by <b>disconnect</b>ing and <b>connect</b>ing.";
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(print)));
	}

	@OnKeyword("connect")
	public void connect(TeamchatAPI api) {
		GAnalyticsBot.api = api;
		String mail = api.context().currentSender().getEmail();
		accessToken = ManageDB.retrieveAccessToken(mail);
		if (accessToken == null) {
			String room_id = api.context().currentRoom().getId();
			String print = "";
			String url = PropertiesFile.getValue("servlet_url") + "?&mail="
					+ mail + "&rid=" + room_id;
			String embedded_url = createEmbeddedLink(url, "logging in...",
					"http");
			print = print + "<a href=" + embedded_url
					+ ">Login to Google Analytics</a>";
			api.perform(api.context().currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml(print)));

		} else {
			api.perform(api.context().currentRoom()
					.post(new TextChatlet("Connected...")));
		}
	}

	@OnKeyword("disconnect")
	public void disconnect(TeamchatAPI api) throws FileNotFoundException {
		GAnalyticsBot.api = api;
		String mail = api.context().currentSender().getEmail();
		try {
			ManageDB.remove(mail);
			api.perform(api.context().currentRoom()
					.post(new TextChatlet("Disconnected...")));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnKeyword("current")
	public void selectAccount(TeamchatAPI api) {
		GAnalyticsBot.api = api;
		String mail = api.context().currentSender().getEmail();
		String access_token = ManageDB.retrieveAccessToken(mail);
		String[][] accounts = new GAMethods().getAccounts(access_token);
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("Select your account")
						.setReplyScreen(
								api.objects()
										.form()
										.addField(
												getOptions(api, "Account",
														"account", accounts)))
						.alias("account")));
	}

	@OnAlias("account")
	public void selectProperty(TeamchatAPI api) {
		String mail = api.context().currentReply().senderEmail();
		String access_token = ManageDB.retrieveAccessToken(mail);
		String account_name = api.context().currentReply().getField("account");
		String account_id = new GAMethods().getAccountID(access_token,
				account_name);
		System.out.println("Account : " + account_name + " : " + account_id);
		String[][] properties = new GAMethods().getProperties(access_token,
				account_id);
		System.out.println("Got the properties. User need to select one");
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("Select your property")
						.setReplyScreen(
								api.objects()
										.form()
										.addField(
												api.objects().hidden()
														.label("").name("aid")
														.value(account_id))
										.addField(
												getOptions(api, "Property",
														"prop", properties)))
						.alias("prop")));
	}

	@OnAlias("prop")
	public void showReport(TeamchatAPI api) {
		String mail = api.context().currentReply().senderEmail();
		String access_token = ManageDB.retrieveAccessToken(mail);
		String account_id = api.context().currentReply().getField("aid");
		String property_name = api.context().currentReply().getField("prop");
		String property_id = new GAMethods().getPropertyID(access_token,
				account_id, property_name);
		String print = new GAMethods()
				.getActiveusers(access_token, property_id);
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(print)));
	}

	@OnKeyword("report")
	public void selectAccount1(TeamchatAPI api) {
		GAnalyticsBot.api = api;
		String mail = api.context().currentSender().getEmail();
		String access_token = ManageDB.retrieveAccessToken(mail);
		String[][] accounts = new GAMethods().getAccounts(access_token);
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("Select your account")
						.setReplyScreen(
								api.objects()
										.form()
										.addField(
												getOptions(api, "Account",
														"account", accounts)))
						.alias("account1")));
	}

	@OnAlias("account1")
	public void selectProperty1(TeamchatAPI api) {
		String mail = api.context().currentReply().senderEmail();
		String access_token = ManageDB.retrieveAccessToken(mail);
		String account_name = api.context().currentReply().getField("account");
		String account_id = new GAMethods().getAccountID(access_token,
				account_name);
		System.out.println("Account : " + account_name + " : " + account_id);
		String[][] properties = new GAMethods().getProperties(access_token,
				account_id);
		System.out.println("Got the properties. User need to select one");
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("Select your property")
						.setReplyScreen(
								api.objects()
										.form()
										.addField(
												api.objects().hidden()
														.label("").name("aid")
														.value(account_id))
										.addField(
												getOptions(api, "Property",
														"prop", properties))
										.addField(
												api.objects()
														.input()
														.label("Start 'YYYY-MM-DD'")
														.name("start"))
										.addField(
												api.objects()
														.input()
														.label("End 'YYYY-MM-DD'")
														.name("end")))
						.alias("prop1")));
	}

	@OnAlias("prop1")
	public void showReport1(TeamchatAPI api) {
		String mail = api.context().currentReply().senderEmail();
		String access_token = ManageDB.retrieveAccessToken(mail);
		String account_id = api.context().currentReply().getField("aid");
		String property_name = api.context().currentReply().getField("prop");
		String start_date = api.context().currentReply().getField("start");
		String end_date = api.context().currentReply().getField("end");

		String property_id = new GAMethods().getPropertyID(access_token,
				account_id, property_name);
		String print = new GAMethods().getPeriodicReport(access_token,
				property_id, start_date, end_date);
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(print)));
	}

	public Field getOptions(TeamchatAPI api, String label1, String name1,
			String[][] array) {
		// TODO Auto-generated method stub
		Field f = api.objects().select().label(label1).name(name1);
		for (int i = 0; i < array.length; i++) {
			f.addOption(array[i][0]);
		}
		return f;
	}

	public void postMsg(String print, String rid) {
		// TODO Auto-generated method stub
		System.out.println("Printing in........." + rid);

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
