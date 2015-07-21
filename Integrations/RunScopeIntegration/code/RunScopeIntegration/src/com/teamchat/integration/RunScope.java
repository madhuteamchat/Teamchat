package com.teamchat.integration;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class RunScope {

	public String finalUrl = "";

	public static String token = "";

	private static TeamchatAPI api;

	public static DatabaseHandler db = null;

	@OnKeyword("help")
	public void onhelp(TeamchatAPI api) {

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<html><body>"
								+ "<b><font color =#159CEB>Hey, This Is RunScope Bot</font></center></b><br></br>"
								+ "<img img src=https://s3.amazonaws.com/com.twilio.prod.cms-assets/twilio-cms/showcase/logo-runscope-large_2.png style=width:230px;height:90px; >"
								+ "<p>Now you can testing and monitoring in RunScope on teamchat using the following keywords:</p><br></br>"
								+ "<font color =#159CEB>Type:</font><br></br>"
								+ " <b>runscope</b>    : <font color =#159CEB> Authorize To RunScope</font><br></br>"
								+ " <b>enterapi</b>    : <font color =#159CEB>Enter API URL</font><br></br>"
								+ " <b>continue</b>    : <font color =#159CEB>continue Keyword for Scheduling To Update API URL Test </font><br></br>"
								+ "</body></html>")));
	}

	@SuppressWarnings("static-access")
	@OnKeyword("runscope")
	public void onRunScope(TeamchatAPI api) {

		this.api = api
				.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet()
								.setQuestionHtml("<a target='_blank' href='https://www.runscope.com/signin/oauth/authorize?client_id=e907a5b5-42ed-4c36-b3a1-5a641c2a631c&Client Secret=d79bf803-384b-4cb7-8ea9-36236dd597d6&response_type=code&redirect_uri=http://interns.teamchat.com:8084/RunScopeIntegration/Authentication&state="
										+ api.context().currentSender()                                                                                                                                                                                     
												.getEmail()
					          					+ "&response_type=code&scope=api:read%20messsage:write'>Click here to connect your Teamchat Account To RunScope</a>")
					          					
                   
						));

	}

	public static void RUNSCOPE(DatabaseHandler dbHandler) {

		try {

			db = dbHandler;
			token = db.getAccessToken();
			SuccessRunScopeALIAS();
			}
		catch (JSONException | IOException e) {
			e.printStackTrace();
		}

	}

	public final static void SuccessRunScopeALIAS() throws IOException,
			JSONException {
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml("<html><body><font color =#159CEB>Successfully Login...</font></body></html>")));
	    }

	@OnKeyword("enterapi")
	public void OnApiEnter(TeamchatAPI api) {
		api.perform(

		api.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion("Enter API URL.")
						.setReplyScreen(
								api.objects()
										.form()
										.addField(
												api.objects().input()
														.label("Api")
														.name("api")))
						.alias("apialias")));

	}

	@OnAlias("apialias")
	public void OnApi(TeamchatAPI api) throws Exception {
		String url = api.context().currentReply().getField("api");

		finalUrl = url;

		URL url1 = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) url1
				.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();

		int code = connection.getResponseCode();

		if (code == 200) {
			System.out.println("Response code of the object is " + code);
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<html> <body>"
									+ "<b>Responce Code Is:</b>"
									+ "<font color = #159CEB>"
									+ code
									+ "</font><br></br>"
									+ "<b>URL Of API Is:</b> "
									+ "<font color = #159CEB>"
									+ url1
									+ "</font><br></br>"
									+ "<b>Do You Want To Continue? If Yes Then Type<font color=#159CEB> continue </font> Keyword.</b>"
									+ "</body></html>")));

		} else {
			System.out.println("Response code of the object is " + code);
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<html> <body>"
									+ "<b>Responce Code Is:</b>"
									+ "<font color = #159CEB>"
									+ code
									+ "</font><br></br>"
									+ "<b>URL Of API Is:</b> "
									+ "<font color = #159CEB>"
									+ url1
									+ "</font><br></br>"
									+ "<b>Do You Want To Continue? If Yes Then Type<font color=#159CEB> continue </font> Keyword.</b>"
									+ "</body></html>")));
		}

	}

	// @OnAlias("apialias")
	// public void OnApi(TeamchatAPI api) throws Exception {
	// String url = api.context().currentReply().getField("api");
	// System.out.println("URL Is:" + url);
	// String temp = "";
	//
	// try {
	//
	// int l;
	// l = url.length();
	// System.out.println("URL length is:" + l);
	// for (int i = 0; i < l; i++) {
	// if (url.charAt(i) == '.') {
	// temp = temp + '-';
	// } else
	// temp = temp + url.charAt(i);
	// }
	//
	// url = temp;
	// System.out.println("Change URL is:" + url);
	// url = url + "-0ues7kz0fm75.runscope.net/";
	//
	// System.out.println("Final Url Is:" + url);
	//
	// finalUrl = url;
	//
	// URL url1 = new URL(url);
	//
	// URLConnection conn = url1.openConnection();
	//
	// Map<String, List<String>> headerFields = conn.getHeaderFields();
	//
	// Set<String> headerFieldsSet = headerFields.keySet();
	//
	// Iterator<String> hearerFieldsIter = headerFieldsSet.iterator();
	//
	// while (hearerFieldsIter.hasNext()) {
	//
	// String headerFieldKey = hearerFieldsIter.next();
	//
	// List<String> headerFieldValue = headerFields
	// .get(headerFieldKey);
	//
	//
	// StringBuilder sb = new StringBuilder();
	//
	//
	//
	// for (String value : headerFieldValue) {
	//
	// sb.append(value);
	//
	// sb.append("");
	//
	// }
	//
	//
	// System.out.println("Header Is:"+ headerFieldKey);
	// System.out.println("Header Value Is:"+ sb.toString());
	//
	// HeaderDetails.put(headerFieldKey, sb.toString());
	//
	// }
	// }
	//
	// catch (Exception e) {
	// System.err.println("Fatal error: " + e.getMessage());
	// e.printStackTrace();
	// }
	//
	// finally {
	//
	// }
	//
	// api.perform(api.context().currentRoom().post(new PrimaryChatlet()
	// .setQuestionHtml("<html>"
	// + "<body>"
	// +"HeaderName And Value Loaded Successfully..<br>"
	// +"Enter Keyword header To View Header Details... "
	// + "</body></html>")
	// ));
	//
	// }

	@OnKeyword("continue")
	public void OnContinue(TeamchatAPI api) throws Exception {

		api.perform(

		api.context()
				.currentRoom()
				.post(

				new PrimaryChatlet()
						.setQuestion(
								"Schedule Of Time in Minute You Want To Saw The Api..")

						.setReplyScreen(
								api.objects()
										.form()
										.addField(
												api.objects().select()
														.label("ScheduleTime")
														.name("scheduletime")
														.addOption("1")
														.addOption("2")
														.addOption("3")
														.addOption("4")
														.addOption("5")
														.addOption("6")
														.addOption("7")
														.addOption("8")
														.addOption("9")
														.addOption("10")
														.addOption("15")))
						.alias("continuealias")));

	}

	@SuppressWarnings("static-access")
	@OnAlias("continuealias")
	public void OnContinueAlias(TeamchatAPI api) throws Exception {

		String ScheduleTimeFromUI = api.context().currentReply()
				.getField("scheduletime");

		long TimeInMilliSeconds = 60 * 1000 * (Long
				.parseLong(ScheduleTimeFromUI));

		Thread t = new Thread();
		t.sleep(TimeInMilliSeconds);

		URL url1 = new URL(finalUrl);
		HttpURLConnection connection = (HttpURLConnection) url1
				.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();

		int code = connection.getResponseCode();

		if (code == 200) {
			System.out.println("Response code of the object is " + code);
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml("<html><body>"
							+ "<b>Responce Code Is:</b>"
							+ "<font color = #159CEB>"
							+ code 
							+ "</font><br></br>"
							+ "<b>URL Of API Is: </b>"
							+ "<font color = #159CEB>" 
							+ url1
							+ "</font></body></html>")));

		} 
		else{
			System.out.println("Response code of the object is " + code);
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<html><body>"
									+ "<b>Responce Code Is:</b>"
									+ "<font color = #159CEB>"
									+ code
									+ "</font><br></br>"
									+ "<b>URL Of API Is:</b> "
									+ "<font color = #159CEB>"
									+ url1
									+"</font><br></br>"
									+ "<b>Do You Want To Continue? If Yes Then Type<font color = #159CEB> continue </font> Keyword.</b>"
									+ "</body></html>")));

		}

	}

	// @SuppressWarnings("static-access")
	// @OnAlias("continuealias")
	// public void OnContinueAlias(TeamchatAPI api) throws Exception {
	//
	// String ScheduleTimeFromUI =
	// api.context().currentReply().getField("scheduletime");
	//
	// long TimeInMilliSeconds = 60 *1000*(Long.parseLong(ScheduleTimeFromUI));
	//
	// Thread t = new Thread();
	// t.sleep(TimeInMilliSeconds);
	//
	//
	//
	// HeaderDetails.clear();
	// try{
	//
	// URL url1 = new URL(finalUrl);
	//
	// URLConnection conn = url1.openConnection();
	//
	// Map<String, List<String>> headerFields = conn.getHeaderFields();
	//
	// Set<String> headerFieldsSet = headerFields.keySet();
	//
	// Iterator<String> hearerFieldsIter = headerFieldsSet.iterator();
	//
	// while (hearerFieldsIter.hasNext()) {
	//
	// String headerFieldKey = hearerFieldsIter.next();
	//
	// List<String> headerFieldValue = headerFields
	// .get(headerFieldKey);
	//
	// StringBuilder sb = new StringBuilder();
	//
	//
	//
	// for (String value : headerFieldValue) {
	//
	// sb.append(value);
	//
	// sb.append("");
	//
	// }
	//
	//
	// System.out.println("Header Is:"+ headerFieldKey);
	// System.out.println("Header Value Is:"+ sb.toString());
	//
	// HeaderDetails.put(headerFieldKey, sb.toString());
	//
	// }
	// }
	//
	// catch (Exception e) {
	// System.err.println("Fatal error: " + e.getMessage());
	// e.printStackTrace();
	// }
	//
	// finally {
	//
	// }
	//
	// api.perform(api.context().currentRoom().post(new PrimaryChatlet()
	// .setQuestionHtml("<html>"
	// + "<body>"
	// +"HeaderName And Value Loaded Successfully..<br>"
	// +"Enter Keyword header To View Header Details... "
	// + "</body></html>")
	// ));
	//
	// }

	// @OnKeyword("header")
	// public void OnViewHeaderDetails(TeamchatAPI api){
	//
	// int length = HeaderDetails.size();
	// String arr[]=new String[length];
	// System.out.println("length is:"+length);
	//
	// int i=0;
	// for (Map.Entry<String, String> entry : HeaderDetails.entrySet()) {
	//
	// String key = "Header Is:"+entry.getKey() +"  " +
	// "Value Is:"+entry.getValue();
	// arr[i]=key;
	// i++;
	//
	// }
	//
	//
	// String output="";
	// for(int j=0;j<arr.length;j++)
	// {
	// output=output+arr[j]+"<br>";
	// }
	//
	//
	// System.out.println("Your Output Is:"+output);
	//
	//
	// api.perform(api.context().currentRoom().post(new PrimaryChatlet()
	// .setQuestionHtml("<html>"
	// + "<body>"+ "Header Is:"
	// + output
	// +"Do You Want To Continue? If Yes Then Type continue Keyword."
	// + "</body></html>")
	// ));
	//
	//
	//
	// }
}
