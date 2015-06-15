//Owner: Amey Ambade
//Task 2
//Date of Submission: Friday, June 5, 2015
//Note: Authentication available only for one user: ameyambade@gmail.com

package slack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.annotations.OnMsg;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class func {

	String token = "xoxp-5090557084-5090557086-5163612031-ded1b5"; // Please
																	// change
																	// this
																	// token on
																	// authorization
																	// with
																	// another
																	// account

	public static void main(String[] args) {
		TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data")
				.setEmail("teamchatbot1@gmail.com").setPassword("ameyambade")
				.startReceivingEvents(new func());
	}

	@OnKeyword("slack")
	public void ConnectToSlack(TeamchatAPI api) {
		// api.perform(api.context().currentRoom().post(new
		// PrimaryChatlet().setQuestionHtml("<html><body><a href=''>Click here to connect your Teamchat Account to Slack</a>")));
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion("Enter your Slack Credentials:")
						.setReplyScreen(
								api.objects()
										.form()
										.addField(
												api.objects()
														.input()
														.label("Email/Username")
														.name("username"))
										.addField(
												api.objects().input()
														.label("Password")
														.name("password")))
						.alias("start")));
	}

	@OnAlias("start")
	public void start(TeamchatAPI api) {

		api.performPostInCurrentRoom(new TextChatlet("Successfully Connected")
				//.alias("getchannels")
				);
	}

	String name[]=new String[100];
	String id[]=new String[100];

	@OnAlias("getchannels")
	public void getChannels(TeamchatAPI api) throws ClientProtocolException,
			IOException {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(
				"https://slack.com/api/channels.list?token=" + token
						+ "&pretty=1");

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
			JSONArray channellist = ch.getJSONArray("channels");
		
			for (int i = 0; i < channellist.length(); i++) {
				JSONObject channel = (JSONObject) channellist.get(i);
				name[i] = channel.get("name").toString();
				id[i] = channel.get("id").toString();
			}
		}
		
		else {
			api.perform(api.context().currentRoom()
					.post(new TextChatlet("Invalid")));
		}
	}

	@OnKeyword("help")
	public void help(TeamchatAPI api) {

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion("Please enter the following keywords to perform functions-"
								+ " channel: To Create new Channel "
								+ " teaminfo: To get information about your team "
								+ " userlist: To get list of your Slack team members "
								+ " search: To search your Slack messages ")));
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
	}
	
	@OnKeyword("invite")
	public void inviteToChannel(TeamchatAPI api)
	{
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion(
								"Please enter following details to invite a user to channel")
						.setReplyScreen() ));
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
			String status = (String) j.get("error");

			api.performPostInCurrentRoom(new TextChatlet(" Channel: "
					+ channelname + " already exists, error:" + status));

		} else {
			api.performPostInCurrentRoom(new TextChatlet("Channel "
					+ channelname + " successfully created"));
		}

	}

	@OnAlias("createdchannel")
	public void created(TeamchatAPI api) {

		String channelname = api.data().getField("channel", "name");

		api.performPostInCurrentRoom(new TextChatlet("New Channel: "
				+ channelname + " created"));

	}

	public Field getOptions(TeamchatAPI api, String [] name){
		
		
		Field f = api.objects().select().label("list").name("list");
		for (int i=0; i<name.length;i++){
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
				.post(new PrimaryChatlet().setQuestion(
						"Please fill the details to post a message in Slack ")
						.setReplyScreen(
								api.objects()
										.form()
										.addField(
												api.objects().input()
														.addOption("Message")
														.label("Message")
														.name("msg"))
										.addField(getOptions(api, name))).alias("d")));

	}

	@OnKeyword("teaminfo")
	public void teamInfo(TeamchatAPI api) throws ClientProtocolException,
			IOException {

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("https://slack.com/api/team.info?token="
				+ token + "&pretty=1");

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

			for (int i = 0; i < mem.length(); i++) {
				JSONObject name = (JSONObject) mem.get(i);

				String names = (String) name.get("name");
				String userid = (String) name.get("id");
				
				out[i] = names;
				Boolean isadmin = (Boolean) name.get("is_admin");
				if (isadmin) {
					names = names.concat("(admin)");
				}
				
				
				int k = i + 1;
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet().setQuestion("User " + k
								+ " in your Slack team is:" + names)));
			}
		} else {
			api.perform(api.context().currentRoom()
					.post(new TextChatlet("Invalid")));
		}
	}
	
	@OnKeyword("url")
	public void getURl(TeamchatAPI api) throws IllegalStateException, IOException{
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(
				"https://slack.com/api/rtm.start?token=xoxp-5090557084-5090557086-5142984056-81c64e&pretty=1");

		HttpResponse response = client.execute(request);
		
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString();

		JSONObject j = new JSONObject(output);
		
		String url = j.get("url").toString();
		System.out.println(url);
		
		try {
			Properties properties = new Properties();
			properties.setProperty("url", url);
			
			File file = new File("test.properties");
			FileOutputStream fileOut = new FileOutputStream(file);
			properties.store(fileOut, "URL");
			fileOut.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

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
	}

	@OnAlias("query")
	public void Query(TeamchatAPI api) throws ClientProtocolException,
			IOException {

		String query = api.context().currentReply().getField("searchquery");

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(
				"https://slack.com/api/search.messages?token=xoxp-5090557084-5090557086-5142984056-81c64e&query="
						+ query + "&pretty=1");

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

		if (matches.length() == 0) {
			api.perform(api.context().currentRoom()
					.post(new PrimaryChatlet().setQuestion("Nothing found!")));
		} else {

			for (int i = 0; i < matches.length(); i++) {
				JSONObject name = (JSONObject) matches.get(i);

				JSONObject name2 = (JSONObject) matches.get(0);
				String poster = (String) name2.get("username");

				JSONObject channel = name2.getJSONObject("channel");
				String channelname = (String) channel.get("name");

				String text = (String) name.get("text");
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet().setQuestion("User " + poster
								+ " had posted:" + text + " in channel:"
								+ channelname)));

			}
		}

	}
}