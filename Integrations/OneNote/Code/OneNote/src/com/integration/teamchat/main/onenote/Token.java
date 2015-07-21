package com.integration.teamchat.main.onenote;

import java.util.List;

import com.google.gson.Gson;
import com.integration.teamchat.onenote.page.Pages;
import com.integration.teamchat.onenote.page.Value;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class Token
{
	public static String token;
	static TeamchatAPI api1;

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public Token(String token)
	{
		super();
		this.token = token;
	}

	public Token()
	{

	}

	public void display() throws Exception
	{
		System.out.println(token);
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder().url("https://www.onenote.com/api/v1.0/me/notes/pages").get().addHeader("authorization", "Bearer " + token).build();

		Response response = client.newCall(request).execute();
		String body = response.body().string();

		Gson gson = new Gson();

		Pages pg = gson.fromJson(body, Pages.class);

		api1.perform(api1.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<b>Your Pages are as follows:-</b>")));
		List<Value> v = pg.getValue();

		for (Value a : v)
		{
			api1.perform(api1.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(a.getTitle())));
		}

	}
}
