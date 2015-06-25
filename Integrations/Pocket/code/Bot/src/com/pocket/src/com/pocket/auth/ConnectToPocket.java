package com.pocket.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class ConnectToPocket {

	public static void main(String[] args) {
		TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data")
				.setEmail("teamchatbot1@gmail.com").setPassword("ameyambade") // Give
																				// bot
																				// creds
																				// :)
				.startReceivingEvents(new ConnectToPocket());
	}

	@OnKeyword("pocket")
	public void connect(TeamchatAPI api) throws ClientProtocolException,
			IOException, InterruptedException {
		/*
		 * api.perform(api .context() .currentRoom() .post(new PrimaryChatlet()
		 * .setQuestionHtml(
		 * "<a href='' target='_blank'>Click here to authenticate</a>")));
		 */
		auth(api);
	}

	static String consumer_key = "42501-a4b7d2d2192589afea89c6e0";
	String acc_tok = new String();

	public void auth(TeamchatAPI api) throws ClientProtocolException,
			IOException, InterruptedException {

		String s = "https://getpocket.com/v3/oauth/request";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(s);
		request.addHeader("Content-Type", "application/json");

		// "consumer_key":"42501-a4b7d2d2192589afea89c6e0",
		// "redirect_uri":"http://localhost:8080/slack_auth/slack_auth"
		String redirect_uri = "http://localhost:8080/slack_auth/slack_auth";

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
		System.out.println("code: "+code);

		String url = "https://getpocket.com/auth/authorize?request_token="
				+ code + "&redirect_uri=" + redirect_uri;
		System.out.println("Going into chatlet");
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml("<a href='" + url
						+ "' target='_blank'>Click here to authenticate</a>")));
		System.out.println("Posted Chatlet"); // Finally the link would take you
												// to the redirect_uri (in this
												// case, localhost:8080)

		Thread.sleep(10000); // Wait for 30 seconds for the user to authorize
								// teamchat to access Pocket

		access(consumer_key, code, api); // call function to extract access
											// token

	}

	public void access(String consumer_key, String code, TeamchatAPI api)
			throws ClientProtocolException, IOException {

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
		acc_tok = output.substring(13, 43);
		System.out.println(acc_tok);
		String username = output.substring(53);
		System.out.println(username);

		api.performPostInCurrentRoom(new TextChatlet(
				"Successfully connected to Pocket"));

		// String res = URLEncoder.encode("access_token", "UTF-8") + "=" +
		// URLEncoder.encode(null, "UTF-8"); chhodo encoding beta abhi
	}

	String link = new String();
	
	@OnKeyword("add")
	public void addLink(TeamchatAPI api) throws ClientProtocolException,
			IOException {

		api.performPostInCurrentRoom(new PrimaryChatlet().setQuestion(
				"Please enter the url of the webpage you want to save.")
				.setReplyScreen(
						api.objects()
								.form()
								.addField(
										api.objects().input().label("Link")
												.name("link"))).alias("addlink")); //Ask user to paste a link
	}
	
	@OnAlias("addlink")
	public void addLinktoPocket(TeamchatAPI api) throws IllegalStateException, IOException{

		link =  api.context().currentReply().getField(link); //get link url from form
		
		String s = "https://getpocket.com/v3/add";
		System.out.println("I'm in");
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(s);
		request.addHeader("Content-Type", "application/json");
//ERROR HERE!!!!
		
		System.out.println(link);	
		JSONObject data = new JSONObject();
		data.put("consumer_key", consumer_key);
		data.put("access_token", acc_tok);
		data.put("url", link);
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
		
		JSONObject a =new JSONObject(output);
		
		api.performPostInCurrentRoom(new TextChatlet("Successfully saved your webpage."));

	}

	@OnKeyword("modify")
	public void modLink(TeamchatAPI api) {

	}

	@OnKeyword("delete")
	public void delLink(TeamchatAPI api) {

	}

	@OnKeyword("help")
	public void helpInfo(TeamchatAPI api) {
		api.performPostInCurrentRoom(new PrimaryChatlet()
				.setQuestionHtml("Add: add new link<br> Modify: modify link <br> Retrieve: Retrieve Data"));
	}

}
