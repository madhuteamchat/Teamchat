package com.teamchat.integrations.pipedrive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.sdk.TeamchatAPI;

public class Deals {
	ArrayList<Integer> a = new ArrayList<Integer>();
	ArrayList<Integer> b = new ArrayList<Integer>();
	ArrayList<Integer> c = new ArrayList<Integer>();
	ArrayList<String> d = new ArrayList<String>();
	ArrayList<String> e = new ArrayList<String>();
	ArrayList<String> f = new ArrayList<String>();
	ArrayList<String> g = new ArrayList<String>();
	ArrayList<String> h = new ArrayList<String>();

	TeamchatAPI api;

	public void deal(String apikey) throws ClientProtocolException, IOException {
		// ArrayList<Integer> a = new ArrayList<Integer>();
		// ArrayList<Integer> b = new ArrayList<Integer>();
		// ArrayList<Integer> c = new ArrayList<Integer>();
		// ArrayList<String> d = new ArrayList<String>();
		// ArrayList<String> e = new ArrayList<String>();
		// ArrayList<String> f = new ArrayList<String>();
		// ArrayList<String> g = new ArrayList<String>();

		String result = "";
		URL urldemo = new URL(
				"https://api.pipedrive.com/v1/deals?start=0&api_token="
						+ apikey);
		URLConnection yc;
		yc = urldemo.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				yc.getInputStream()));
		String inputline;
		while ((inputline = in.readLine()) != null) {
			result += inputline;

			System.out.println(inputline + "\n");
		}
		in.close();

		JSONObject jobj = new JSONObject(result);
		JSONArray ja = jobj.getJSONArray("data");

		for (int i = 0; i < ja.length(); i++) {
			JSONObject jobj2 = ja.getJSONObject(i);
			int id = jobj2.getInt("id");
			System.out.println(id);
			a.add(id);
			JSONObject jobj3 = jobj2.getJSONObject("person_id");
			System.out.println(jobj3.getString("name"));
			d.add(jobj3.getString("name"));
			JSONObject jobj4 = jobj2.getJSONObject("org_id");
			System.out.println(jobj4.getString("name"));
			e.add(jobj4.getString("name"));
			System.out.println(jobj2.getInt("stage_id"));
			b.add(jobj2.getInt("stage_id"));
			System.out.println(jobj2.getString("title"));
			f.add(jobj2.getString("title"));
			System.out.println(jobj2.getInt("value"));
			c.add(jobj2.getInt("value"));
			System.out.println(jobj2.getString("currency"));
			g.add(jobj2.getString("currency"));
			h.add(jobj2.getString("formatted_value"));
		}

		for (int i = 0; i < a.size(); i++) {
			System.out.println(a.get(i).toString());
			System.out.println(b.get(i).toString());
			System.out.println(c.get(i).toString());
			System.out.println(d.get(i));
			System.out.println(e.get(i));
			System.out.println(f.get(i));
			System.out.println(g.get(i));
			System.out.println(h.get(i));
		}

		Pipedrivebot.f = f;
		Pipedrivebot.a = a;
		Pipedrivebot.b = b;
		Pipedrivebot.c = c;
		Pipedrivebot.d = d;
		Pipedrivebot.e = e;
		Pipedrivebot.g = g;
		Pipedrivebot.h = h;

	}

	public Boolean notifyme(String apikey, int userid)
			throws ClientProtocolException, IOException {
		Boolean flag = false;
		String url = "https://api.pipedrive.com/v1/pushNotifications?api_token="
				+ apikey;
		String webhookurl = "http://interns.teamchat.com:8082/Pipedrive/notification";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		JSONObject jobj = new JSONObject();
		jobj.put("user_id", userid);
		jobj.put("subscription_url", webhookurl);
		jobj.put("event", "*.*");
		String jsonstring = jobj.toString();

		System.err.println("JSON request is : " + jsonstring);
		StringEntity entity = new StringEntity(jsonstring, "UTF-8");
		entity.setContentType("application/json");
		post.setEntity(entity);
		HttpResponse response = null;
		try {
			response = client.execute(post);
			int rspcode = response.getStatusLine().getStatusCode();
			System.out.println("Response code is :"+rspcode);
			if (rspcode == 201) {
				flag = true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flag;

	}
}