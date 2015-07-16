//INTEGRATION: SLACK

package com.teamchat.integrations.slack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.annotations.OnMsg;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class Slack {

	static String token = new String();

	static String code = new String();
	public static String email = new String();
	public static int wait;
	
	public static String client_id = new String(); //  client_id=5090557084.5108581326
	public static String client_secret = new String(); // client_secret=6f41ca2089675e9cadddae278f18b3cc
	public static String redirect_uri = new String(); //redirect_uri=http://integrations.teamchat.com:8083/Slack/slack_auth
	
	public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException
	{
		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;

	}

	@OnKeyword("slack")
	public void ConnectToSlack(TeamchatAPI api) throws Exception {
		
		//Get client id, client secret, and redirect uri from properties file
		
		Properties prop1 = loadPropertyFromClasspath("slack.properties", Slack.class);
		
		System.out.println("Reading data from properties file.");
		 
		// get the property value and print it out
		System.out.println(prop1.getProperty("client_id"));
		client_id = prop1.getProperty("client_id").trim();
		System.out.println(prop1.getProperty("client_secret").trim());
		client_secret = prop1.getProperty("client_secret");
		System.out.println(prop1.getProperty("redirect_uri").trim());
		redirect_uri = prop1.getProperty("redirect_uri");
 
		System.out.println("Read data from properties file.");

		String starturl = "https://slack.com/oauth/authorize?client_id="+client_id+"&redirect_uri="+redirect_uri+"&scope=identify,read,post,client,admin";
		String urlToPost = createEmbeddedLink(starturl, "Connect", "http");
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<a href='"+urlToPost+"' target='_blank'>Click Here to authorize</a>")));
		System.out.println("Haha, wait till I find your authorization info.");
		
		

		EventListener.api1 = api;

		email = api.context().currentSender().getEmail();

		slack_auth.email = email;

		System.err.println("email:" + slack_auth.email);

		wait = 0;
		while (wait != 1) {
			Thread.sleep(1000);
		}

		code = SlackDB.useCode(email);

		System.err.println("code in Slack.java: " + code);
		if (wait == 1) {
			start(api, code);
		}
		// To open new pop-up:
		// onClick="MyWindow=window.open('https://slack.com/oauth/authorize?client_id=5090557084.5108581326&redirect_uri=http://interns.teamchat.com:8083/slack_auth/slack_auth&scope=identify,read,post,client,admin','MyWindow',width=600,height=300);
		// return false;

	}

	public void start(TeamchatAPI api, String code) throws Exception {

		String s = "https://slack.com/api/oauth.access?client_id="+client_id+"&client_secret="+client_secret+"&code="
				+ code
				+ "&redirect_uri="+redirect_uri+"&pretty=1";

		HttpClient client = HttpClientBuilder.create().build();// this default is
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
			
			//call to save accesstoken function to DB
			
			SlackDB.saveToken(email, token);
			
			System.out.println("Recieved access token. Free to access any data now!");

			api.performPostInCurrentRoom(new TextChatlet(
					"Successfully connected"));

			// Free to call for notifications (notifyme)
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
						.setQuestionHtml("Hey there! i'm Slack Bot! I can make your life easier by linking your Slack to Teamchat!"
								+ "<br>You can enter the following keywords to perform functions-"
								+ "	<br><b>slack</b>: To connect to your Slack "
								+ "	<br><b>notifyme</b>: To get notifications from your Slack account "
								+ " <br><b>channel</b>: To Create new Channel"
								+ " <br><b>teaminfo</b>: To get information about your team "
								+ " <br><b>userlist</b>: To get list of your Slack team members "
								+ " <br><b>search</b>: To search your Slack messages "
								+ " <br><b>message</b>: To post message to a channel"
								+ " <br><b>delete</b>: To delete the most recent message you posted")));

		System.out.println("Posting help data.");
	}

	@OnKeyword("notifyme")
	public void notifyMe(TeamchatAPI api) throws Exception {
		token = SlackDB.useToken(email);
		System.err.println("Assigning token to event listener");
		EventListener.listenForEvents(token);
		System.err.println("Assigned token to event listener");
	}

	public static String createEmbeddedLink(String url, String title, String protocol) throws JSONException
	{
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
			IOException, JSONException {

		token = SlackDB.useToken(email);
		
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
		Boolean value = (Boolean) j.get("ok"); // value true means successful
												// channel creation

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

	public Field getOptions(TeamchatAPI api, String[] name) // For using to post
															// message to any
															// channel in Slack
			throws ClientProtocolException, IOException, JSONException {

		// Get Options from JSON

		token = SlackDB.useToken(email);
		
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
			IOException, JSONException {

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
			IOException, JSONException {
		String msgToSend = api.context().currentReply().getField("msg");
		String actualmessage = msgToSend.replace(" ", "%20")
				.replace("?", "%3F"); // For get request to url
		String channelToSend = api.context().currentReply().getField("ch");

		token = SlackDB.useToken(email);
		
		for (int i = 0; i < name.length; i++) {
			if (channelToSend.equals(name[i])) {
				channelID = chid[i]; // Received Channel ID and the message to
										// send to this channel

				// Create a new HTTP request

				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(
						"https://slack.com/api/chat.postMessage?token=" + token
								+ "&channel=" + channelID + "&text="
								+ actualmessage + "&pretty=1");

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
			IOException, JSONException {
		
		token = SlackDB.useToken(email);
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(
				"https://slack.com/api/chat.delete?token=" + token
						+ "&channel=" + channelID + "&ts=" + timestamp
						+ "&pretty=1");

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
			IOException, JSONException {
		
		token = SlackDB.useToken(email);

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
			IOException, JSONException {
		
		token = SlackDB.useToken(email);

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

	// For testing:

	/*
	 * @OnKeyword("url") public void getURl(TeamchatAPI api) throws
	 * IllegalStateException, IOException {
	 * 
	 * 
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
	 * StringBuilder sb = new StringBuilder(); while ((line = rd.readLine()) !=
	 * null) sb.append(line); String output = sb.toString();
	 * 
	 * JSONObject j = new JSONObject(output);
	 * 
	 * String url = j.get("url").toString(); System.out.println(url);
	 * 
	 * try { Properties properties = new Properties();
	 * properties.setProperty("url", url);
	 * 
	 * File file = new File("test.properties"); FileOutputStream fileOut = new
	 * FileOutputStream(file); properties.store(fileOut, "URL");
	 * fileOut.close(); } catch (FileNotFoundException e) { e.printStackTrace();
	 * } catch (IOException e) { e.printStackTrace(); } // Add call to
	 * javascript code for running the websocket url }
	 */

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
			IOException, JSONException {
		
		token = SlackDB.useToken(email);
		
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
