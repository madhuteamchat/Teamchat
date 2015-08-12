package com.teamchat.integration.disastersnews;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class DisastersNewsBot
{
	@OnKeyword("help")
	public void help(TeamchatAPI api)
	{
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.help)));
	}

	@OnKeyword("news")
	public void joke(TeamchatAPI api) throws Exception
	{
		String url = "http://api.rwlabs.org/v1/disasters";
		//String url = "http://api.yomomma.info/";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

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

			JSONArray data  = json.getJSONArray("data");
			
			StringBuilder news = new StringBuilder();
			
			news.append("<ul>");
			
			for (int i=0;i<data.length();i++)
			{
				JSONObject jsona = data.getJSONObject(i);
				String newsa = jsona.getJSONObject("fields").getString("name");
				news.append("<li>" + newsa + "</li>");
			}
			
			news.append("</ul>");
			
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(news.toString())));
		}
	}
}