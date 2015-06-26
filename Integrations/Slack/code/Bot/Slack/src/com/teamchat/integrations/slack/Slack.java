//Owner: Amey Ambade
//Task 2
//Date of Submission: Friday, June 5, 2015
//Note: Authentication available only for one user: ameyambade@gmail.com

package com.teamchat.integrations.slack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.annotations.OnMsg;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class Slack {

	String token = new String();

	public static void opencon() {

	}

	String code = new String();

	@OnKeyword("slack")
	public void ConnectToSlack(TeamchatAPI api) throws IllegalStateException,
			IOException, InterruptedException {
		// api.perform(api.context().currentRoom().post(new
		// PrimaryChatlet().setQuestionHtml("<html><body><a href=''>Click here to connect your Teamchat Account to Slack</a>")));
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<a href='https://slack.com/oauth/authorize?client_id=5090557084.5108581326&redirect_uri=http://interns.teamchat.com:8080/slack_auth/slack_auth&scope=identify,read,post,client,admin' target='_blank'>Click Here to authorize</a>")));
		System.out.println("Haha, wait till I find your authorization info.");

		Thread.sleep(30000); // You can find a better way, Amey
		start(api);
		// To open new pop-up:
		// onClick="MyWindow=window.open('https://slack.com/oauth/authorize?client_id=5090557084.5108581326&redirect_uri=http://interns.teamchat.com:8080/slack_auth/slack_auth&scope=identify,read,post,client,admin','MyWindow',width=600,height=300);
		// return false;

	}

	public void start(TeamchatAPI api) throws IllegalStateException,
			IOException {
		// Get code from properties file created by servlet
		try (FileReader reader = new FileReader("/opt/tomcat0.7/webapps/code.properties")) {
			Properties properties = new Properties();
			properties.load(reader);
			System.out.println("Getting code from properties file");
			code = properties.getProperty("code");
			System.out.println(code);
			properties.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String s = "https://slack.com/api/oauth.access?client_id=5090557084.5108581326&client_secret=6f41ca2089675e9cadddae278f18b3cc&code="
				+ code
				+ "&redirect_uri=http://interns.teamchat.com:8080/slack_auth/slack_auth&pretty=1";

		HttpClient client = new DefaultHttpClient(); // this default is
														// deprecated, I could
														// instead use
														// HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(s);

		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString();

		JSONObject acc = new JSONObject(output);
		Boolean value = (Boolean) acc.get("ok");

		if (!value) {
			String status = (String) acc.get("error");

			System.out.println("Oh, shit, couldn't get access token");

			api.performPostInCurrentRoom(new TextChatlet(
					" Could not connect, error:" + status));

		} else {

			token = acc.get("access_token").toString(); // This token is unique
														// to the user

			System.out
					.println("Recieved access token. Free to access any data now!");

			api.performPostInCurrentRoom(new TextChatlet(
					"Successfully connected"));
		}
	}

	String name[] = new String[100];
	String chid[] = new String[100];
	String channelID = new String();
	String timestamp = new String();

	@OnKeyword("help")
	public void help(TeamchatAPI api) {

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("Please enter the following keywords to perform functions-"
								+ " <br>channel: To Create new Channel "
								+ " <br>teaminfo: To get information about your team "
								+ " <br>userlist: To get list of your Slack team members "
								+ " <br>search: To search your Slack messages "
								+ " <br>message: To post message to a channel"
								+ " <br>delete: To delete the most recent message you posted")));

		System.out.println("Posting help data.");
	}
	
	@OnKeyword("rtm")
	public void RTM(TeamchatAPI api) throws IllegalStateException, IOException, URISyntaxException{
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(
				"https://slack.com/api/rtm.start?token=" + token
						+ "&pretty=1");

		System.out.println("Request to create channel sent.");

		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString();

		JSONObject j = new JSONObject(output);
		Boolean value = (Boolean) j.get("ok");

		if (value){
			String url = j.get("url").toString();
			System.err.println(url);
			Websocket.tempurl=url;
			Websocket.connectEventStream();
		//	SaveURL newurl = new SaveURL();
		//	newurl.saveURL(url);
		}
		
	}

	@OnKeyword("channel")
	public void CreateChannel(TeamchatAPI api) {
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion(
								"Please enter the name of the channel you want to create:")
						.setReplyScreen(
								api.objects()
										.form()
										.addField(
												api.objects()
														.input()
														.label("Name of Channel")
														.name("channel")))
						.alias("chname")));

		System.out.println("Acquired the channel name from form.");

	}

	@OnAlias("chname")
	public void Chname(TeamchatAPI api) throws ClientProtocolException,
			IOException {

		String channelname = api.context().currentReply().getField("channel");
		api.data().addField("channel", "name", channelname);

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(
				"https://slack.com/api/channels.create?token=" + token
						+ "&name=" + channelname + "&pretty=1");

		System.out.println("Request to create channel sent.");

		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString();

		JSONObject j = new JSONObject(output);
		Boolean value = (Boolean) j.get("ok");

		if (!value) {

			System.out.println("Error encountered creating channel.");

			String status = (String) j.get("error");

			api.performPostInCurrentRoom(new TextChatlet(" Channel: "
					+ channelname + " already exists, error:" + status));

		} else {

			System.out.println("Posting successfully created channel.");

			api.performPostInCurrentRoom(new TextChatlet("Channel "
					+ channelname + " successfully created"));
		}

	}

	@OnAlias("createdchannel")
	public void created(TeamchatAPI api) {

		String channelname = api.data().getField("channel", "name");

		System.out.println("Created channel.");

		api.performPostInCurrentRoom(new TextChatlet("New Channel: "
				+ channelname + " created"));

	}

	public Field getOptions(TeamchatAPI api, String[] name)
			throws ClientProtocolException, IOException {

		// Get Options from JSON

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(
				"https://slack.com/api/channels.list?token=" + token
						+ "&pretty=1");

		System.out.println("Request to get list of channels sent.");

		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString();
		JSONObject ch = new JSONObject(output);

		Boolean ok = (Boolean) ch.get("ok");
		if (ok) {

			System.out.println("Recieved list of channels. Displaying.");

			JSONArray channellist = ch.getJSONArray("channels");

			for (int i = 0; i < channellist.length(); i++) {
				JSONObject channel = (JSONObject) channellist.get(i);
				name[i] = channel.get("name").toString();
				chid[i] = channel.get("id").toString();
			}
		}

		else {
			api.perform(api.context().currentRoom()
					.post(new TextChatlet("Invalid")));
		}

		Field f = api.objects().select().label("Channels").name("ch");
		for (int i = 0; i < name.length; i++) {
			f.addOption(name[i]);
		}

		return f;

	}

	@OnKeyword("message")
	public void postMessage(TeamchatAPI api) throws ClientProtocolException,
			IOException {

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion(
								"Please fill the details to post a message in Slack ")
						.setReplyScreen(
								api.objects()
										.form()
										.addField(
												api.objects().input()
														.addOption("Message")
														.label("Message")
														.name("msg"))
										.addField(getOptions(api, name)))
						.alias("sendmsg")));

		System.out.println("Details extracted from form.");

	}

	@OnAlias("sendmsg")
	public void sendMessage(TeamchatAPI api) throws IllegalStateException,
			IOException {
		String msgToSend = api.context().currentReply().getField("msg");
		String actualmessage = msgToSend.replace(" ", "%20")
				.replace("?", "%3F"); // For get request to url
		String channelToSend = api.context().currentReply().getField("ch");

		for (int i = 0; i < name.length; i++) {
			if (channelToSend.equals(name[i])) {
				channelID = chid[i]; // Received Channel ID and the message to
										// send to this channel

				// Create a new HTTP request

				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(
						"https://slack.com/api/chat.postMessage?token=" + token
								+ "&channel=" + channelID + "&text="
								+ actualmessage + "&pretty=1"); // Add
				// as_user=<username>
				// After OAuth

				System.out.println("Request to send message sent.");

				HttpResponse response = client.execute(request);
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line = "";
				StringBuilder sb = new StringBuilder();
				while ((line = rd.readLine()) != null)
					sb.append(line);
				String output = sb.toString();
				JSONObject ch = new JSONObject(output);

				Boolean ok = (Boolean) ch.get("ok");
				if (ok) {

					System.out.println("Posting message.");

					api.perform(api
							.context()
							.currentRoom()
							.post(new TextChatlet(
									"Successfully posted message in Slack")));
					timestamp = ch.getString("ts");
					System.out.println(timestamp);
				}

				else {
					api.perform(api.context().currentRoom()
							.post(new TextChatlet("Invalid")));
				}

				break;
			}

		}

	}

	@OnKeyword("delete")
	public void deleteMsg(TeamchatAPI api) throws ClientProtocolException,
			IOException {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(
				"https://slack.com/api/chat.delete?token=" + token
						+ "&channel=" + channelID + "&ts=" + timestamp
						+ "&pretty=1"); // Add
										// as_user=<username>
										// After OAuth
		System.out.println("Request to delete previous message sent.");

		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString();
		JSONObject ch = new JSONObject(output);

		Boolean ok = (Boolean) ch.get("ok");
		if (ok) {

			System.out.println("Printing delete success message.");

			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet(
							"Successfully deleted most recent message in Slack")));
			timestamp = ch.getString("ts");
			System.out.println(timestamp);
		}

		else {
			api.perform(api.context().currentRoom()
					.post(new TextChatlet("Invalid")));
		}

	}

	@OnKeyword("teaminfo")
	public void teamInfo(TeamchatAPI api) throws ClientProtocolException,
			IOException {

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("https://slack.com/api/team.info?token="
				+ token + "&pretty=1");
		System.out.println("Request for team information sent.");

		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString();

		JSONObject j = new JSONObject(output);
		JSONObject team = j.getJSONObject("team");

		String id = (String) team.get("id");
		String teamname = (String) team.get("name");
		String domain = (String) team.get("domain");

		System.out.println("Posting the team info.");

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion("Your Slack team name is: " + teamname
								+ ", ID is: " + id + " ,and domain is: "
								+ domain)));
	}

	@OnKeyword("userlist")
	public void userlist(TeamchatAPI api) throws ClientProtocolException,
			IOException {

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("https://slack.com/api/users.list?token="
				+ token + "&pretty=1");
		System.out.println("Request for user list sent.");
		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString();

		JSONObject j = new JSONObject(output);
		Boolean ok = (Boolean) j.get("ok");
		if (ok) {
			JSONArray mem = j.getJSONArray("members");
			String out[] = new String[100];

			StringBuilder allnames = new StringBuilder();
			for (int i = 0; i < mem.length(); i++) {
				JSONObject name = (JSONObject) mem.get(i);

				String names = (String) name.get("name");
				String userid = (String) name.get("id");

				out[i] = names;
				Boolean isadmin = (Boolean) name.get("is_admin");
				if (isadmin) {
					names = names.concat("(admin)");
				}

				allnames.append(", ").append(names); // Make a list of all names

			}
			String listallnames = allnames.toString();

			System.out.println("Posting list of users.");

			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestion("These are the users in your Slack team:"
									+ listallnames)));

		} else {
			api.perform(api.context().currentRoom()
					.post(new TextChatlet("Invalid")));
		}
	}

	@OnKeyword("url")
	public void getURl(TeamchatAPI api) throws IllegalStateException,
			IOException {

		/*
		 * HttpClient client = new DefaultHttpClient(); HttpGet request = new
		 * HttpGet(
		 * "https://slack.com/api/rtm.start?token=xoxp-5090557084-5090557086-5142984056-81c64e&pretty=1"
		 * );
		 * 
		 * HttpResponse response = client.execute(request);
		 * 
		 * BufferedReader rd = new BufferedReader(new InputStreamReader(response
		 * .getEntity().getContent())); String line = "";
		 * 
		 * StringBuilder sb = new StringBuilder(); while ((line = rd.readLine())
		 * != null) sb.append(line); String output = sb.toString();
		 * 
		 * JSONObject j = new JSONObject(output);
		 * 
		 * String url = j.get("url").toString(); System.out.println(url);
		 * 
		 * try { Properties properties = new Properties();
		 * properties.setProperty("url", url);
		 * 
		 * File file = new File("test.properties"); FileOutputStream fileOut =
		 * new FileOutputStream(file); properties.store(fileOut, "URL");
		 * fileOut.close(); } catch (FileNotFoundException e) {
		 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
		 * // Add call to javascript code for running the websocket url
		 */}

	@OnKeyword("search")
	public void SearchMessages(TeamchatAPI api) {
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion(
								"Please enter the keywords you want to search in your Slack messages:")
						.setReplyScreen(
								api.objects()
										.form()
										.addField(
												api.objects().input()
														.label("Search query")
														.name("searchquery")))
						.alias("query")));
		System.out.println("Details taken from form.");
	}

	@OnAlias("query")
	public void Query(TeamchatAPI api) throws ClientProtocolException,
			IOException {

		String query = api.context().currentReply().getField("searchquery");

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(
				"https://slack.com/api/search.messages?token=" + token
						+ "&query=" + query + "&pretty=1");
		System.out.println("Search request sent.");
		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString();

		JSONObject j = new JSONObject(output);

		JSONObject msgs = j.getJSONObject("messages");
		JSONArray matches = msgs.getJSONArray("matches");

		StringBuilder strtodisp = new StringBuilder();

		if (matches.length() == 0) {
			api.perform(api.context().currentRoom()
					.post(new PrimaryChatlet().setQuestion("Nothing found!")));
		} else {

			for (int i = 0; i < matches.length(); i++) {
				JSONObject name = (JSONObject) matches.get(i);

				JSONObject name2 = (JSONObject) matches.get(i);
				String poster = (String) name2.get("username"); // Who posted
																// message

				JSONObject channel = name2.getJSONObject("channel"); // In which
																		// channel
																		// it
																		// was
																		// posted
				String channelname = (String) channel.get("name");

				String text = (String) name.get("text"); // What was posted

				strtodisp.append("User ").append(poster).append(" had posted ")
						.append(text).append(" in channel ")
						.append(channelname).append("<br>");
			}

		}

		String str = strtodisp.toString();
		System.out.println("Searching...");
		api.performPostInCurrentRoom(new PrimaryChatlet().setQuestionHtml(str));
	}

}
