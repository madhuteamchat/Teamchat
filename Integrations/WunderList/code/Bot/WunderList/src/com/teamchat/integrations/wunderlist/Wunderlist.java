package com.teamchat.integrations.wunderlist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

public class Wunderlist {

	public String getLists(String accessToken) {
		// TODO Auto-generated method stub
		String inputline = "";
		String result = "";
		String print = "";
		String contextPath = "https://a.wunderlist.com/api/v1/lists?client_id="
				+ PropertiesFile.getValue("client_id") + "&access_token="
				+ accessToken;
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
				print = print + "<li>" + jobj.getString("title") + "</li>";
			}
			print = print + "</ul>";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return print;
	}

	public String getTasks(String accessToken, int listid) {
		// TODO Auto-generated method stub
		String inputline = "";
		String result = "";
		String print = "";
		String contextpath = "https://a.wunderlist.com/api/v1/tasks?client_id="
				+ PropertiesFile.getValue("client_id") + "&access_token="
				+ accessToken + "&list_id=" + listid;
		try {
			URL urldemo = new URL(contextpath);
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
			for (int j = 0; j < jsonarray.length(); j++) {
				JSONObject jobj = jsonarray.getJSONObject(j);
				print = print + "<li>" + jobj.getString("title") + "</li>";
			}
			print = print + "</ul>";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return print;
	}

	public int addTask(String accessToken, int inboxid, String title,
			Boolean star) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String url = "https://a.wunderlist.com/api/v1/tasks";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		post.addHeader("X-Access-Token", accessToken);
		post.addHeader("X-Client-ID", PropertiesFile.getValue("client_id"));
		post.setHeader("Content-Type", "application/json;charset=UTF-8");

		JSONObject jobj = new JSONObject();
		jobj.put("list_id", inboxid);
		jobj.put("title", title);
		jobj.put("starred", star);
		String jsonstring = jobj.toString();

		StringEntity entity = new StringEntity(jsonstring, "UTF-8");
		entity.setContentType("application/json");
		post.setEntity(entity);

		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int rspcode = response.getStatusLine().getStatusCode();

		return rspcode;
	}

	public String getOtherTasks(String accessToken) {
		// TODO Auto-generated method stub
		String print = "";
		String inputline = "";
		String result = "";
		String contextPath = "https://a.wunderlist.com/api/v1/lists?client_id="
				+ PropertiesFile.getValue("client_id") + "&access_token="
				+ accessToken;
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
				if (!jobj.getString("list_type").equals("inbox")) {
					print = print + "<li>Tasks on the list <i>'"
							+ jobj.getString("title") + "'</i></li>";
					print = print + ""
							+ getTasks(accessToken, jobj.getInt("id")) + "";
				}
			}
			print = print + "</ul>";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return print;
	}

	public int addlist(String accessToken, String title) {
		// TODO Auto-generated method stub
		String url = "https://a.wunderlist.com/api/v1/lists";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		post.addHeader("X-Access-Token", accessToken);
		post.addHeader("X-Client-ID", PropertiesFile.getValue("client_id"));
		post.setHeader("Content-Type", "application/json;charset=UTF-8");

		JSONObject jobj = new JSONObject();
		jobj.put("title", title);
		String jsonstring = jobj.toString();

		StringEntity entity = new StringEntity(jsonstring, "UTF-8");
		entity.setContentType("application/json");
		post.setEntity(entity);

		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int rspcode = response.getStatusLine().getStatusCode();

		return rspcode;
	}

	public Boolean createWebhook(String accessToken) {
		// TODO Auto-generated method stub
		int[] list_id = new GetDetails().getListIds(accessToken);
		Boolean flag = false;
		for (int i = 0; i < list_id.length; i++) {
			String url = "https://a.wunderlist.com/api/v1/webhooks";
			String webhookurl = PropertiesFile.getValue("webhook_url");

			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(url);
			post.addHeader("X-Access-Token", accessToken);
			post.addHeader("X-Client-ID", PropertiesFile.getValue("client_id"));
			post.setHeader("Content-Type", "application/json;charset=UTF-8");

			JSONObject jobj = new JSONObject();
			jobj.put("list_id", list_id[i]);
			jobj.put("url", webhookurl);
			jobj.put("processor_type", "generic");
			jobj.put("configuration", "");
			String jsonstring = jobj.toString();

			System.err.println("JSON request is : " + jsonstring);
			StringEntity entity = new StringEntity(jsonstring, "UTF-8");
			entity.setContentType("application/json");
			post.setEntity(entity);
			HttpResponse response = null;
			try {
				response = client.execute(post);
				int rspcode = response.getStatusLine().getStatusCode();
				if (rspcode == 201) {
					flag = true;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!flag) {
				break;
			}
		}
		return flag;
	}

	public Boolean deleteWebhook(int uid, String accessToken) {
		// TODO Auto-generated method stub
		Boolean flag = false;
		int revision = new GetDetails().getRevision(accessToken);
		int[] list_id = new GetDetails().getListIds(accessToken);
		for (int i = 0; i < list_id.length; i++) {
			int[] webhook_id = new GetDetails().getWebhookIds(accessToken,
					list_id[i]);
			for (int j = 0; j < webhook_id.length; j++) {
				String contextPath = "https://a.wunderlist.com/api/v1/webhooks/"
						+ webhook_id[j]
						+ "?client_id="
						+ PropertiesFile.getValue("client_id")
						+ "&access_token="
						+ accessToken
						+ "&revision="
						+ revision;

				try {
					URL urldemo = new URL(contextPath);
					HttpURLConnection yc;
					yc = (HttpURLConnection) urldemo.openConnection();
					yc.setRequestMethod("DELETE");
					yc.setRequestProperty("Content-Type", "application/json");
					yc.setDoOutput(true);

					int rspcode = yc.getResponseCode();
					if (rspcode == 204) {
						flag = true;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	public static String createEmbeddedLink(String url, String title,
			String protocol) {
		JSONObject object = new JSONObject();
		JSONObject web = new JSONObject();
		web.put("title", title);
		web.put("cancelBtnName", "Back");
		web.put("minWidth", "200");
		web.put("draggable", "true");
		web.put("newWindow", "true");
		web.put("url", url);
		object.put("web", web);
		System.out.println(object.toString());
		byte[] byteArray = Base64.encodeBase64(object.toString().getBytes());
		String encodedString = new String(byteArray);
		String fUrl = protocol + "://teamchat:data=" + encodedString;

		return fUrl;

	}

}
