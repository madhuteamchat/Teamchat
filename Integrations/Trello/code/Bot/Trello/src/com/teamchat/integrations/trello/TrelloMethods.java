package com.teamchat.integrations.trello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class TrelloMethods {

	public String getOrganizations(String token) {
		// TODO Auto-generated method stub
		String inputline = "";
		String result = "";
		String print = "";
		String contextPath = "https://trello.com/1/members/my/organizations?key="
				+ PropertiesFile.getValue("api_key") + "&token=" + token;
		try {
			URL urldemo = new URL(contextPath);
			URLConnection yc = urldemo.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			while ((inputline = in.readLine()) != null) {
				result += inputline;
				System.err.println(result);
			}
			in.close();
			JSONArray jsonarray = new JSONArray(result);
			print = print + "<ul>";
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jobj = jsonarray.getJSONObject(i);
				print = print + "<li>" + jobj.getString("displayName")
						+ "</li>";
			}
			print = print + "</ul>";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return print;
	}

	public String getBoards(String token) {
		// TODO Auto-generated method stub
		String inputline = "";
		String result = "";
		String print = "";
		String contextPath = "https://trello.com/1/members/me/boards?key="
				+ PropertiesFile.getValue("api_key") + "&token=" + token;
		try {
			URL urldemo = new URL(contextPath);
			URLConnection yc = urldemo.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			while ((inputline = in.readLine()) != null) {
				result += inputline;
				System.err.println(result);
			}
			in.close();
			JSONArray jsonarray = new JSONArray(result);
			print = print + "<ul>";
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jobj = jsonarray.getJSONObject(i);
				String link = jobj.getString("url");
				String embedded_url = TrelloBot.createEmbeddedLink(link,
						jobj.getString("name"), "https");
				print = print + "<li>" + "<a href=" + embedded_url + ">"
						+ jobj.getString("name") + "</a>" + "</li>";
			}
			print = print + "</ul>";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return print;
	}

	public Boolean createWebhook(String accessToken) {
		// TODO Auto-generated method stub
		Boolean flag = false;
		String url = "https://trello.com/1/tokens/" + accessToken
				+ "/webhooks/?key=" + PropertiesFile.getValue("api_key");
		String webhookurl = PropertiesFile.getValue("webhook_url");
		String user_id = new GetDetails().getUserID(accessToken);

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		JSONObject jobj = new JSONObject();

		jobj.put("callbackURL", webhookurl);
		jobj.put("idModel", user_id);
		String jsonstring = jobj.toString();

		System.err.println("JSON request is : " + jsonstring);
		StringEntity entity = new StringEntity(jsonstring, "UTF-8");
		entity.setContentType("application/json");
		post.setEntity(entity);
		HttpResponse response = null;
		try {
			response = client.execute(post);
			int rspcode = response.getStatusLine().getStatusCode();
			if (rspcode == 200) {
				String json_string = EntityUtils.toString(response.getEntity());
				JSONObject jresp = new JSONObject(json_string);
				System.out.println("response is : " + jresp.toString());
				flag = true;
				// JSONObject jresp = new JSONObject(response.toString());
				String wid = jresp.getString("id");
				ManageDB.updateWebhookID(accessToken, wid);
			} else if (rspcode == 400
					&& response
							.equals("A webhook with that callback, model, and token already exists")) {
				flag = true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flag;
	}

	public Boolean deleteWebhook(String accessToken) {
		// TODO Auto-generated method stub
		Boolean flag = false;
		String webhook_id = ManageDB.getWebhookIDbyToken(accessToken);
		System.out.println("Webhook id is : "+webhook_id);
		String contextPath = "https://trello.com/1/webhooks/" + webhook_id
				+ "?key=" + PropertiesFile.getValue("api_key") + "&token="
				+ accessToken;

		try {
			URL urldemo = new URL(contextPath);
			HttpURLConnection yc;
			yc = (HttpURLConnection) urldemo.openConnection();
			yc.setRequestMethod("DELETE");
			yc.setRequestProperty("Content-Type", "application/json");
			yc.setDoOutput(true);

			int rspcode = yc.getResponseCode();
			System.out.println("Response code is : " + rspcode);
			if (rspcode == 200) {
				flag = true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flag;
	}
}
