//INTEGRATION: POCKET

package com.teamchat.integrations.pocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class ConnectToPocket {

	static String consumer_key = "42501-a4b7d2d2192589afea89c6e0";
	String acc_tok = new String();
	String linkToAdd = new String();

	@OnKeyword("pocket")
	public void connect(TeamchatAPI api) throws ClientProtocolException,
			IOException, InterruptedException {
		auth(api);
	}

	public void auth(TeamchatAPI api) throws ClientProtocolException,
			IOException, InterruptedException {

		String s = "https://getpocket.com/v3/oauth/request";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(s);
		request.addHeader("Content-Type", "application/json");

		// "consumer_key":"42501-a4b7d2d2192589afea89c6e0",
	
		String redirect_uri = "http://localhost:8080";

		JSONObject data = new JSONObject();
		data.put("consumer_key", consumer_key);
		data.put("redirect_uri", redirect_uri);

		HttpEntity e = new StringEntity(data.toString());

		request.setEntity(e);
		HttpResponse response = client.execute(request);
		System.out.println("Sending request...");
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString(); // get code

		String code = output.substring(5); // format will be
											// code=1234567-asdfg-23453 so leave
											// the first five characters
		System.out.println("code: " + code);

		String url = "https://getpocket.com/auth/authorize?request_token="
				+ code + "&redirect_uri=" + redirect_uri;
		System.out.println("Going into chatlet");
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml("<a href='" + url
						+ "' target='_blank'>Click here to authenticate</a>")));
		System.out.println("Posted Chatlet"); 
												// Finally the link would take you
												// to the redirect_uri (in this
												// case, localhost:8080)

		Thread.sleep(30000); // Wait for 30 seconds for the user to authorize
								// teamchat to access Pocket

		access(consumer_key, code, api); // call function to extract access
											// token

	}

	public void access(String consumer_key, String code, TeamchatAPI api)
			throws ClientProtocolException, IOException {
		
		if (consumer_key.length()==0 || code.length()==0){
			api.performPostInCurrentRoom(new TextChatlet("Oops! An error occurred! Please try again!"));
		}

		String s = "https://getpocket.com/v3/oauth/authorize";
		System.out.println("I'm in");
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(s);
		request.addHeader("Content-Type", "application/json");

		JSONObject data = new JSONObject();
		data.put("consumer_key", consumer_key);
		data.put("code", code);

		HttpEntity e = new StringEntity(data.toString());

		request.setEntity(e);
		HttpResponse response = client.execute(request);
		System.out.println("Sending request...");

		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString(); // get result
		System.out.println(output);
		acc_tok = output.substring(13, 43); //Extract accesstoken from x-www-urlencoded format response
		System.out.println(acc_tok);
		String username = output.substring(53);
		System.out.println(username);

		api.performPostInCurrentRoom(new TextChatlet(
				"Successfully connected to Pocket"));
	}

	@OnKeyword("help")
	public void help(TeamchatAPI api) {

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("Hey there! I'm Pocket Bot! I can help you save important links to your Pocket right from Teamchat!"
								+ "<br>You can enter the following keywords to perform functions-"
								+ "	<br><b>pocket</b>: To connect to your Pocket account "
								+ " <br><b>add</b>: To add a link to your Pocket"
								+ " <br><b>retrieve</b>: To retrieve links from your Pocket")));
		System.out.println("Posting help data.");
	}

	@OnKeyword("add")
	public void addLink(TeamchatAPI api) throws ClientProtocolException,
			IOException {

		api.perform(api
				.context()
				.currentRoom()
				.post((new PrimaryChatlet()
						.setQuestion(
								"Please enter the url of the webpage you want to save.")
						.setReplyScreen(
								api.objects()
										.form()
										.addField(
												api.objects().input()
														.label("Link")
														.name("link")))
						.alias("addlink")))); // Ask user to paste a link
	}

	@OnAlias("addlink")
	public void addLinktoPocket(TeamchatAPI api) throws IllegalStateException,
			IOException {

		linkToAdd = api.context().currentReply().getField("link");

		if (linkToAdd.startsWith("http://")) { 

		} else {
			linkToAdd = "http://" + linkToAdd;	//In case the link doesn't have it already
		}

		String s = "https://getpocket.com/v3/add";
		System.out.println("I'm in");			//check
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(s);
		request.addHeader("Content-Type", "application/json");

		System.out.println(linkToAdd);
		JSONObject data = new JSONObject();
		data.put("consumer_key", consumer_key);
		data.put("access_token", acc_tok);
		data.put("url", linkToAdd);
		
		HttpEntity e = new StringEntity(data.toString());
		request.setEntity(e);
		HttpResponse response = client.execute(request);
		System.out.println("Sending request...");

		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString(); // get result to string output
		System.out.println(output);

		if (output.contains("400")) {	// error occurred
			api.performPostInCurrentRoom(new TextChatlet(
					"Couldn't save webpage, try again later."));
		} else {
			api.performPostInCurrentRoom(new TextChatlet(
					"Successfully saved your webpage."));
		}
	}

	@OnKeyword("retrieve")
	public void retLink(TeamchatAPI api) {

		api.performPostInCurrentRoom(new PrimaryChatlet()
				.setQuestion("Retrieve links you've saved to Pocket:")
				.setReplyScreen(
						api.objects()
								.form()
								.addField(
										api.objects()
												.input()
												.label("Number of items to be displayed:")
												.name("count"))
								.addField(
										api.objects().select().addOption("All")
												.addOption("Unread Only")
												.addOption("Archived Only")
												.label("State").name("state"))
								.addField(
										api.objects()
												.input()
												.label("Search String (optional)")
												.name("search"))

								.addField(
										api.objects()
												.select()
												.addOption("Articles")
												.addOption("Images")
												.addOption("Videos")
												.label("Content Type (optional)")
												.name("contentType"))
								.addField(
										api.objects()
												.input()
												.label("Tags(optional, seperated by commas)")
												.name("tags"))).alias("ret"));

	}

	@OnAlias("ret")
	public void retrieveLinks(TeamchatAPI api) throws ClientProtocolException,
			IOException {
		String count = api.context().currentReply().getField("count");
		String search = api.context().currentReply().getField("search");

		//Search, tags and contentType are optional parameters for sending the request so handle all possibilities

		if (search.equals(null)) {
			search = null;
		}

		String state = api.context().currentReply().getField("state");

		if (state.equals("Unread Only")) {
			state = "unread";
		} else if (state.equals("Archived Only")) {
			state = "archive";
		} else if (state.equals("All")) {
			state = "all";
		} else if (state.equals(null)) {
			state = null;
		}

		String contentType = api.context().currentReply()
				.getField("contentType");

		if (contentType.equals("Articles")) {	// Since the names in dropdown are 'Articles Only' and the like
			contentType = "article";
		}
		if (contentType.equals("Images")) {
			contentType = "image";
		}
		if (contentType.equals("Videos")) {
			contentType = "video";
		}

		String tags = api.context().currentReply().getField("tags");

		String s = "https://getpocket.com/v3/get";
		System.out.println("I'm in");
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(s);
		request.addHeader("Content-Type", "application/json");

		System.out.println(linkToAdd);
		JSONObject data = new JSONObject();
		data.put("consumer_key", consumer_key);
		data.put("access_token", acc_tok);
		data.put("count", count);
		data.put("state", state);
		if (contentType.equals(null)) {
			// do nothing
		} else {
			data.put("contentType", contentType); //set contentType param
		}
		if (search.equals(null)) {
			// do nothing
		} else {
			data.put("search", search);			  //set search param
		}
		if (tags.equals(null)) {
			// do nothing
		} else {
			data.put("tags", tags);				  //set tags param
		}

		HttpEntity e = new StringEntity(data.toString());
		request.setEntity(e);
		HttpResponse response = client.execute(request);
		System.out.println("Sending request...");

		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString(); // get result
		System.out.println(output);

		JSONObject a = new JSONObject(output);

		int status = (int) a.get("status");

		if (status == 1) {
			JSONObject list = a.getJSONObject("list");
	
			Set<String> keys = list.keySet();
			Object[] arr = keys.toArray();

			String links[] = new String[arr.length];
			String titles[] = new String[arr.length];

			System.err.println("arr " + arr.length + "keys : " + keys.size());
			for (int i = 0; i < arr.length; i++) {
			
				//Manipulate all data from JSON
				
				JSONObject j = (JSONObject) list.get((String) arr[i]);
				String resolved_url = j.getString("resolved_url");
				links[i] = resolved_url;
				String title = j.getString("given_title");

				if (title.length() == 0) {
					title = "Click here";
				}
				titles[i] = title;
			}
			
			String print = "";
			System.err.println(links.length + "and titles " + titles.length);
			for (int i = 0; i < links.length; i++) {
				print = print + "<a href='" + links[i] + "' target='_blank'>"
						+ titles[i] + "</a><br>"; // Every link should open a
													// new tab
			}

			api.performPostInCurrentRoom(new PrimaryChatlet()
					.setQuestionHtml("Search Results:<br>" + print));

		} else {
			api.performPostInCurrentRoom(new TextChatlet(
					"Couldn't get search results, please try again in some time."));
		}
	}

}
