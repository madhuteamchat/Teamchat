package com.teamchat.integration.news;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.annotations.OnMsg;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class NewsBot
{
	@OnKeyword("help")
	public void help(TeamchatAPI api)
	{
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.help)));
	}

	@OnMsg
	public void search(TeamchatAPI api) throws Exception
	{
		String msg = api.context().currentChatlet().raw();

		System.out.println(msg);
		boolean isValidJson = true;
		try
		{
			new JSONObject(msg);
		}
		catch (JSONException e)
		{
			isValidJson = false;
		}
		if (!msg.equalsIgnoreCase("help") && !isValidJson)
		{
			msg = msg.replace(" ", "+");
			String url = "http://content.guardianapis.com/search?q=" + msg + "&api-key=jshqay5mj6bnudfkskd3ncgp";

			System.out.println(url);

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
			JSONObject json = new JSONObject(response.toString());
			int pageSize = json.getJSONObject("response").getInt("pageSize");
			JSONArray arr = json.getJSONObject("response").getJSONArray("results");

			StringBuilder news = new StringBuilder();
			
			if (arr.length() != 0 && pageSize > 0)
			{
				for (int i = 0; i < arr.length(); i++)
				{
					JSONObject jsona = arr.getJSONObject(i);
					String section = jsona.getString("sectionName");
					String title = jsona.getString("webTitle");
					String urla = jsona.getString("webUrl");
					
					news.append("<b>" + section + "</b>" 
									+ "<ul>"
									+ "<li>" + title + "</li>"
									+ "<li><a href='" + urla + "' target='_blank'>go to this link</a></li>"
									+ "</ul>"
									+ "<br/><br/>");
				}
				api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(news.toString())));
			}
			else
			{
				api.perform(api.context().currentRoom().post(new TextChatlet("Sorry! no results.")));
			}
		}
	}
}