package com.teamchat.integration.poetry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class PoetryBot
{
	@OnKeyword("help")
	public void help(TeamchatAPI api) throws Exception
	{
		Form f = api.objects().form();
		Field fd = api.objects().select().label("Authors").name("selectPoet").addRegexValidation(".+", "field cannot be left blank");

		String url = Utility.apiUrlAuthors;

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
			if (json.has("authors") && !json.isNull("authors"))
			{
				JSONArray jsonArr = json.getJSONArray("authors");
				if (jsonArr.length() != 0)
				{
					for (int i = 0; i < jsonArr.length(); i++)
					{
						fd.addOption(jsonArr.getString(i));
					}
				}
				f.addField(fd);
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet().setQuestionHtml(Utility.help).setReplyScreen(f).setReplyLabel("Authors").showDetails(true).setDetailsLabel("replies").allowComments(true)
								.alias("selectAuthor")));
			}
			else
			{
				api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.sorry)));
			}
		}
		else
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.sorry)));
		}
	}

	@OnAlias("selectAuthor")
	public void selectAuthor(TeamchatAPI api) throws Exception
	{
		String author = api.context().currentReply().getField("selectPoet");
		String authorN = URLEncoder.encode(author, "UTF-8");

		String url = Utility.apiUrlAuthorPoems.replace("__Author", authorN);

		Form f = api.objects().form();
		Field fd = api.objects().select().label("Poems of " + author).name("selectPoetry").addRegexValidation(".+", "field cannot be left blank");

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

			JSONArray jsonArr = new JSONArray(response.toString());
			if (jsonArr.length() != 0)
			{
				for (int i = 0; i < jsonArr.length(); i++)
				{
					JSONObject json = jsonArr.getJSONObject(i);
					fd.addOption(json.getString("title"));
				}
				f.addField(fd);
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet().setQuestionHtml(Utility.image + Utility.paraStart + "Please select a Poetry" + Utility.paraEnd).setReplyScreen(f).setReplyLabel("Poetries")
								.showDetails(true).setDetailsLabel("replies").allowComments(true).alias("selectPoetry")));
			}
			else
			{
				api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.sorry)));
			}
		}
		else
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.sorry)));
		}
	}

	@OnAlias("selectPoetry")
	public void selectPoem(TeamchatAPI api) throws Exception
	{
		String poetry = api.context().currentReply().getField("selectPoetry");
		String poetryN = URLEncoder.encode(poetry, "UTF-8");

		String url = Utility.apiUrlPoems.replace("__Poems", poetryN);

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("X-Mashape-Key", Utility.apiKey);
		con.setRequestProperty("Accept", "application/json");

		int responseCode = con.getResponseCode();

		StringBuilder resp = new StringBuilder();
		resp.append(Utility.image);

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

			JSONArray jsonArr = new JSONArray(response.toString());
			if (jsonArr.length() != 0)
			{
				for (int i = 0; i < jsonArr.length(); i++)
				{
					resp.append(Utility.resp.replace("__Poetry", poetry));
					resp.append("<center>" + Utility.paraStart);
					JSONObject json = jsonArr.getJSONObject(i);
					JSONArray jsona = json.getJSONArray("lines");
					for (int j = 0; j < jsona.length(); j++)
					{
						resp.append(jsona.getString(j) + "<br/>");
					}
					resp.append(Utility.paraEnd + "</center>");
				}
				api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(resp.toString()).allowComments(true)));
			}
			else
			{
				api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.sorry)));
			}
		}
		else
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.sorry)));
		}
	}
}