package com.integration;

import java.io.IOException;

import com.google.gson.Gson;
import com.hipchat.getwebhook.Deletewebhook;
import com.hipchat.getwebhook.Item;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class Checkwebhook
{
	String notify, id;

	public Checkwebhook(String notify, String id)
	{
		this.notify = notify;
		this.id = id;
	}

	public boolean check() throws IOException
	{
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url("http://api.hipchat.com/v2/room/" + id + "/webhook").get().addHeader("authorization", "Bearer " + notify + "").build();

		Response response = client.newCall(request).execute();
		Gson gson = new Gson();
		Deletewebhook i = gson.fromJson(response.body().string(), Deletewebhook.class);
		for (Item Item : i.getItems())
		{
			if (Item.getEvent().equalsIgnoreCase("room_message"))
			{
				return true;
			}

		}
		return false;
	}

	public Integer getwebhookid() throws IOException
	{
		Integer webid=0;
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url("http://api.hipchat.com/v2/room/" + id + "/webhook").get().addHeader("authorization", "Bearer " + notify + "").build();

		Response response = client.newCall(request).execute();
		Gson gson = new Gson();
		Deletewebhook i = gson.fromJson(response.body().string(), Deletewebhook.class);
		for (Item Item : i.getItems())
		{
			webid = Item.getId();

		}
		return webid;
	}
}
