package com.teamchat.integration.joke;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class JokeBot
{
	@OnKeyword("help")
	public void help(TeamchatAPI api)
	{
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.help)));
	}

	@OnKeyword("joke")
	public void joke(TeamchatAPI api) throws Exception
	{
		String url = "http://api.icndb.com/jokes/random";
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

			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/joke.png' width='150' /></center>"
					+ "<div><center><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'>\"" + json.getJSONObject("value").getString("joke") + "\"</p></center></div>")));
		}
		else
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/joke.png' width='150' /></center>"
					+ "<div>Sorry! no results.</div>")));
		}
	}
}