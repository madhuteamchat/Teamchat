package com.teamchat.integrations.wunderlist;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class WunderlistBot {

	String accessToken = "";
	String client_id = PropertiesFile.getValue("client_id");
	String client_secret = PropertiesFile.getValue("client_secret");
	public static TeamchatAPI api;
	int inboxid;

	@OnKeyword("help")
	public void help(TeamchatAPI api) {
		WunderlistBot.api = api;
		String print = "<b>I'm wunderlist bot</b><br>";
		print = print
				+ "I will help you stay updated with your Wunderlist account.<br>";
		print = print + "Use me to perform these functions:<br>";
		print = print
				+ "<i><b>connect</b></i>: To sign in to Wunderlist account<br>";
		print = print
				+ "<i><b>disconnect</b></i>: To sign out from Wunderlist account<br>";
		print = print + "<i><b>lists</b></i>: To view lists<br>";
		print = print + "<i><b>addlist</b></i>: To add a list<br>";
		print = print + "<i><b>viewinbox</b></i>: To view tasks in inbox<br>";
		print = print
				+ "<i><b>viewtasks</b></i>: To view tasks in other lists<br>";
		print = print
				+ "<i><b>addtask</b></i>: To add a task to your inbox<br>";
		print = print + "<i><b>notify</b></i>: To manage your notifications";
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(print)));
		print = "You can access multiple Wunderlist accounts by <b>disconnect</b>ing and <b>connect</b>ing.";
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(print)));
	}

	@OnKeyword("connect")
	public void connect(TeamchatAPI api) {
		WunderlistBot.api = api;
		String mail = api.context().currentSender().getEmail();
		accessToken = ManageDB.retrieve(mail);
		if (accessToken == null) {
			String room_id = api.context().currentRoom().getId();
			String print = "";
			String url = PropertiesFile.getValue("wunderlist_servlet")
					+ "?&email=" + mail + "&room_id=" + room_id;
			String embedded_url = createEmbeddedLink(url, "logging in...",
					"http");
			print = print + "<a href=" + embedded_url
					+ ">Login to Wunderlist</a>";
			api.perform(api.context().currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml(print)));

		} else {
			api.perform(api.context().currentRoom()
					.post(new TextChatlet("Connected...")));
		}
	}

	@OnKeyword("disconnect")
	public void disconnect(TeamchatAPI api) throws FileNotFoundException {
		WunderlistBot.api = api;
		try {
			int uid = GetDetails.getUid(accessToken);
			new Wunderlist().deleteWebhook(uid, accessToken);
			WebhookDB.remove(uid);
			String mail = api.context().currentSender().getEmail();
			ManageDB.remove(mail);

			api.perform(api.context().currentRoom()
					.post(new TextChatlet("Disconnected...")));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Boolean isValid(String mail) {
		accessToken = ManageDB.retrieve(mail);
		if (accessToken == null)
			return false;
		else
			return true;
	}

	@OnKeyword("lists")
	public void list(TeamchatAPI api) {
		WunderlistBot.api = api;
		String mail = api.context().currentSender().getEmail();
		if (isValid(mail)) {
			String print = new Wunderlist().getLists(accessToken);
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("Following are the lists present in your acount:<br>"
									+ print)));
		} else {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<b>connect</b> to access your wunderlist account")));
		}
	}

	@OnKeyword("addlist")
	public void createList(TeamchatAPI api) {
		WunderlistBot.api = api;
		String mail = api.context().currentSender().getEmail();
		if (isValid(mail)) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("Enter the name of the list")
							.setReplyScreen(
									api.objects()
											.form()
											.addField(
													api.objects().input()
															.label("Title")
															.name("title")))
							.alias("list")));
		} else {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<b>connect</b> to access your wunderlist account")));
		}
	}

	@OnAlias("list")
	public void addListAlias(TeamchatAPI api) {
		WunderlistBot.api = api;
		String title = api.context().currentReply().getField("title");
		int flag = new Wunderlist().addlist(accessToken, title);
		if (flag == 201)
			api.perform(api.context().currentRoom()
					.post(new TextChatlet("List added successfully")));
		else
			api.perform(api.context().currentRoom()
					.post(new TextChatlet("Unsuccessful attempt. Try again.")));
	}

	@OnKeyword("notify")
	public void webhook(TeamchatAPI api) {
		WunderlistBot.api = api;
		String mail = api.context().currentSender().getEmail();
		if (isValid(mail)) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml(
									"Do you want your webhooks to be ON/OFF")
							.setReplyScreen(
									api.objects()
											.form()
											.addField(
													api.objects()
															.select()
															.label("Turn ON/OFF")
															.name("wb")
															.addOption("ON")
															.addOption("OFF")))
							.alias("dowebhook")));

		} else {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<b>connect</b> to start accessing wunderlist account")));
		}
	}

	@OnAlias("dowebhook")
	public void webhookalias(TeamchatAPI api) {
		WunderlistBot.api = api;
		String option = api.context().currentReply().getField("wb");
		if (option.equals("ON")) {
			try {
				int uid = GetDetails.getUid(accessToken);
				new Wunderlist().deleteWebhook(uid, accessToken);
			} catch (Exception e) {

			}
			Boolean flag = new Wunderlist().createWebhook(accessToken);
			if (flag) {
				api.perform(api.context().currentRoom()
						.post(new TextChatlet("Notifications turned ON.")));
				int uid = GetDetails.getUid(accessToken);
				String rid = api.context().currentRoom().getId();
				new WebhookDB();
				WebhookDB.insert(uid, rid, accessToken);
			} else {
				api.perform(api
						.context()
						.currentRoom()
						.post(new TextChatlet("Unable to process your request.")));
			}
		} else if (option.equals("OFF")) {
			int uid = GetDetails.getUid(accessToken);
			Boolean flag = new Wunderlist().deleteWebhook(uid, accessToken);
			if (flag) {
				new WebhookDB();
				WebhookDB.remove(uid);
				api.perform(api.context().currentRoom()
						.post(new TextChatlet("Notifications turned OFF.")));
			} else {
				api.perform(api
						.context()
						.currentRoom()
						.post(new TextChatlet("Unable to process your request.")));
			}
		}
	}

	@OnKeyword("viewinbox")
	public void inbox(TeamchatAPI api) {
		WunderlistBot.api = api;
		String mail = api.context().currentSender().getEmail();
		if (isValid(mail)) {
			String print = new Wunderlist().getTasks(accessToken, getInboxid());
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("Following are the tasks present in your inbox<br>"
									+ print)));
		} else
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<b>connect</b> to access your wunderlist account")));
	}

	@OnKeyword("viewtasks")
	public void otherLists(TeamchatAPI api) {
		WunderlistBot.api = api;
		String mail = api.context().currentSender().getEmail();
		if (isValid(mail)) {
			String print = new Wunderlist().getOtherTasks(accessToken);
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("Following are the tasks present in your lists<br>"
									+ print)));
		} else
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<b>connect</b> to access your wunderlist account")));
	}

	@OnKeyword("addtask")
	public void addTask(TeamchatAPI api) {
		WunderlistBot.api = api;
		String mail = api.context().currentSender().getEmail();
		if (isValid(mail)) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml(
									"Fill the form to create a task in your inbox")
							.setReplyScreen(
									api.objects()
											.form()
											.addField(
													api.objects().input()
															.label("Title")
															.name("title"))
											.addField(
													api.objects().select()
															.label("Starred")
															.name("starred")
															.addOption("Yes")
															.addOption("No")))
							.alias("add")));
		} else {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<b>connect</b> to access your wunderlist account")));
		}
	}

	@OnAlias("add")
	public void addTaskAlias(TeamchatAPI api)
			throws UnsupportedEncodingException {
		WunderlistBot.api = api;
		String title = api.context().currentReply().getField("title");
		String starred = api.context().currentReply().getField("starred");
		Boolean star;
		if (starred.equals("Yes")) {
			star = true;
		} else {
			star = false;
		}
		int flag = new Wunderlist().addTask(accessToken, getInboxid(), title,
				star);
		if (flag == 201)
			api.perform(api.context().currentRoom()
					.post(new TextChatlet("Task added successfully")));
		else
			api.perform(api.context().currentRoom()
					.post(new TextChatlet("Unsuccessful attempt. Try again.")));
	}

	public int getInboxid() {
		String inputline = "";
		String result = "";
		String contextPath = "https://a.wunderlist.com/api/v1/lists?client_id="
				+ PropertiesFile.getValue("client_id") + "&access_token="
				+ accessToken;
		try {
			URL urldemo = new URL(contextPath);
			URLConnection yc = urldemo.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			while ((inputline = in.readLine()) != null) {
				result += inputline;
			}
			in.close();
			JSONArray jsonarray = new JSONArray(result);
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jobj = jsonarray.getJSONObject(i);
				if (jobj.getString("list_type").equals("inbox")) {
					inboxid = jobj.getInt("id");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inboxid;
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

	/*
	 * public void postMsg(String print, String room_id, String mail) { // TODO
	 * Auto-generated method stub if (room_id.length() < 5) {
	 * api.perform(api.context().create().add(mail) .post(new
	 * PrimaryChatlet().setQuestionHtml(print))); } }
	 */
}
