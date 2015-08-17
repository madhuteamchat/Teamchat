package com.teamchat.integration.textsentimentanalysis;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class TextSentimentAnalysisBot
{
	@OnKeyword("help")
	public void help(TeamchatAPI api)
	{
		Form f = api.objects().form();
		f.addField(api.objects().select().label("Select sentiment anlytics type").addOption("Text").addOption("Url").addRegexValidation(".+", "please select one option").name("sentiment_type"));
		f.addField(api.objects().input().label("Enter text/url (add http:// or https:// before url)").name("enteredtext").addRegexValidation(".+", "field cannot be left blank"));

		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.help).setReplyScreen(f).setReplyLabel("Analyse").alias("analysis")));
	}

	@OnKeyword("analytics")
	public void convert(TeamchatAPI api) throws Exception
	{
		Form f = api.objects().form();
		f.addField(api.objects().select().label("Select sentiment anlytics type").addOption("Text").addOption("Url").addRegexValidation(".+", "please select one option").name("sentiment_type"));
		f.addField(api.objects().input().label("Enter text/url (add http:// or https:// before url)").name("enteredtext").addRegexValidation(".+", "field cannot be left blank"));

		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.help).setReplyScreen(f).setReplyLabel("Analyse").alias("analysis")));
	}

	@OnAlias("analysis")
	public void converted(TeamchatAPI api) throws Exception
	{
		String type = api.context().currentReply().getField("sentiment_type");
		String text = api.context().currentReply().getField("enteredtext");
		String encodedText = URLEncoder.encode(text, "UTF-8");
		
		if (type.equals("Text"))
		{
			String url = Utility.apiUrl.replace("__text", encodedText);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("X-Mashape-Key", Utility.apiKey);
			con.setRequestProperty("Accept", "application/json");

			int responseCode = con.getResponseCode();

			if (responseCode == 200)
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null)
				{
					response.append(inputLine);
				}
				in.close();

				JSONObject json = new JSONObject(response.toString());
				String sentiText = json.getString("sentiment-text");
				Double sentiScore = json.getDouble("sentiment-score");
				
				api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.response
										.replace("__CopyHere", "text").replace("__EnteredText", text).replace("__SentiText", sentiText)
										.replace("__SentiScore", sentiScore + ""))));
			}
			else
			{
				api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.sorry)));
			}
		}
		else if (type.equals("Url"))
		{
			String url = Utility.apiUrl.replace("__text", encodedText);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("X-Mashape-Key", Utility.apiKey);
			con.setRequestProperty("Accept", "application/json");

			int responseCode = con.getResponseCode();

			if (responseCode == 200)
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null)
				{
					response.append(inputLine);
				}
				in.close();

				JSONObject json = new JSONObject(response.toString());
				String sentiText = json.getString("sentiment-text");
				Double sentiScore = json.getDouble("sentiment-score");
				String extractedContent = "";
				
				String resp = Utility.response
										.replace("__CopyHere", "url").replace("__EnteredText", text).replace("__SentiText", sentiText)
										.replace("__SentiScore", sentiScore + "");
				
				if (json.has("extracted-content") && !json.isNull("extracted-content")) {
					extractedContent = json.getString("extracted-content");
					resp = resp + Utility.resp.replace("__ExtractedContent", extractedContent);
				}
				
				api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(resp)));
			}
			else
			{
				api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.sorry)));
			}
		}
	}
}