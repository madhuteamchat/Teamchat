package com.teamchat.integration.dictionary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class DictionaryBot
{
	@OnKeyword("help")
	public void help(TeamchatAPI api)
	{
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.help)));
	}

	@OnKeyword("search")
	public void emi(TeamchatAPI api) throws Exception
	{
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Word").name("word"));

		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml("Enter word").setReplyScreen(f).setReplyLabel("Enter").alias("word")));
	}

	@OnAlias("word")
	public void calculate(TeamchatAPI api) throws Exception
	{	
		String word = api.context().currentReply().getField("word");
		word = URLEncoder.encode(word, "UTF-8");
		
		String url = "https://montanaflynn-dictionary.p.mashape.com/define?word=" + word;

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("X-Mashape-Key", "");
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
			JSONArray jsona = json.getJSONArray("definitions");
			
			StringBuilder define = new StringBuilder();
			define.append("<div style='padding-left:5px; padding-right:5px; margin-bottom:5px margin-top:5px'>"
					+ "<p style='margin-bottom:5px'><b>'" + word + "' definitions:</b></p>"
					+ "<ul>");
			
			if (jsona.length() != 0)
			{
				for (int i = 0; i < jsona.length() && i < 10; i++)
				{
					JSONObject jsonab = jsona.getJSONObject(i);
					String definition = jsonab.getString("text");
					
					define.append(""
									//+ "<b>" + definition + "</b>" 
									//+ "<ul>"
									+ "<li>" + definition + "</li>"
									//+ "</ul>"
									+ "");
				}
				define.append("</ul>"
						+ "</div>");
				api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/dictionary.png' width='150' /></center>"
							+ "<div>" + define.toString() + "</div>")));
			}
			else
			{
				api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/dictionary.png' width='150' /></center>"
						+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'>Sorry! no results.</p></div>")));
			}
		}
		else
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/dictionary.png' width='150' /></center>"
					+ "<div><p style='padding-left:5px; padding-right:5px;align:center; margin-top:5px'>Sorry! no results.</p></div>")));
		}
	}
}
