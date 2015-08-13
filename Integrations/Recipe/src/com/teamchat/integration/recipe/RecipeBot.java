package com.teamchat.integration.recipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class RecipeBot
{
	Properties configProps;
	
	@OnKeyword("help")
	public void help(TeamchatAPI api)
	{
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.help)));
	}

	@OnKeyword("food")
	public void recipe(TeamchatAPI api)
	{
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Enter a keyword for search, for eg. Kheer").name("search"));

		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("Enter recipe name you want to search for.").setReplyScreen(f).setReplyLabel("Enter").alias("search")));
	}
	
	@OnAlias("search")
	public void search(TeamchatAPI api) throws Exception
	{
		try
		{
			configProps = loadPropertyFromClasspath("recipe-config.properties", RecipeBot.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		String search = api.context().currentReply().getField("search");
		search = URLEncoder.encode(search, "UTF-8");
		
		String url = "http://food2fork.com/api/search?key=_apikey&q=__recipe";
		//String url = "http://api.yomomma.info/";

		URL obj = new URL(url.replace("_apikey", configProps.getProperty("apikey")).replace("__recipe", search));
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
			
			int count = json.getInt("count");
			
			if (count != 0)
			{
				StringBuilder recipes = new StringBuilder();
				recipes.append("<div style='padding-left:5px; padding-right:5px; margin-bottom:5px'>");
				
				JSONArray jsona = json.getJSONArray("recipes");
				
				for (int i = 0; i<jsona.length() && i<5; i++)
				{
					JSONObject jsonab = jsona.getJSONObject(i);
					String title = jsonab.getString("title");
					String sourceUrl = jsonab.getString("source_url");
					String pubUrl = jsonab.getString("publisher_url");
					String img = jsonab.getString("image_url");
					
					recipes.append ("<center><div><img src='" + img + "' width='150'/></div></center>");
					recipes.append("<p style='margin-top:5px; padding-right:5px; padding-left:5px'><b><a href='" + pubUrl + "' target='_blank'>" + pubUrl + "</a></b>" 
									+ "<ul>"
									+ "<li>" + title + "</li>"
									+ "<li><a href='" + sourceUrl + "' target='_blank'>go to this link</a></li>"
									+ "</ul>"
									+ "<br/><br/>");
				}
				recipes.append("</div>");
				api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<div>" + recipes.toString() + "</div>")));
			}
		}
		else
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/recipe.png' width='150' /></center>"
					+ "<div>Sorry! no results.</div>")));
		}
	}
	
	public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException
	{
		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;
	}
}