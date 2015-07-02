package com.teamchat.integrations.wunderlist;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetDetails {

	public String getListName(String uid, int listid) {
		// TODO Auto-generated method stub
		String accessToken = new WebhookDB().retrieveAccessToken(uid);
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
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jobj = jsonarray.getJSONObject(i);
				if (jobj.getInt("id") == listid) {
					print = jobj.getString("title");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return print;
	}

	public String[] getTaskName(String uid, int task_id) {
		// TODO Auto-generated method stub
		String accessToken = new WebhookDB().retrieveAccessToken(uid);
		String inputline = "";
		String result = "";
		String[] print = new String[4];
		String contextPath = "https://a.wunderlist.com/api/v1/tasks/" + task_id
				+ "?client_id=" + PropertiesFile.getValue("client_id")
				+ "&access_token=" + accessToken;
		try {
			URL urldemo = new URL(contextPath);
			URLConnection yc = urldemo.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			while ((inputline = in.readLine()) != null) {
				result += inputline;
			}
			in.close();
			JSONObject jsonobj = new JSONObject(result);
			print[0] = jsonobj.getString("title");
			print[1] = "";
			print[1] = print[1] + jsonobj.getInt("list_id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return print;
	}

	public String getUserName(String uid, String author_id) {
		// TODO Auto-generated method stub
		String accessToken = new WebhookDB().retrieveAccessToken(uid);
		String inputline = "";
		String result = "";
		String print = "";
		String contextPath = "https://a.wunderlist.com/api/v1/users?client_id="
				+ PropertiesFile.getValue("client_id") + "&access_token="
				+ accessToken;
		try {
			URL urldemo = new URL(contextPath);
			URLConnection yc = urldemo.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			while ((inputline = in.readLine()) != null) {
				result += inputline;
			}
			in.close();
			JSONArray jsonarray = new JSONArray(result);
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jobj = jsonarray.getJSONObject(i);
				if (("" + jobj.getInt("id")).equals(uid)) {
					print = jobj.getString("name");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return print;
	}

	public int[] getListIds(String accessToken) {
		// TODO Auto-generated method stub
		String inputline = "";
		String result = "";
		int[] print = null;
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
			}
			in.close();
			JSONArray jsonarray = new JSONArray(result);
			print = new int[jsonarray.length()];
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jobj = jsonarray.getJSONObject(i);
				print[i] = jobj.getInt("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return print;
	}

	public int[] getWebhookIds(String accessToken, int list_id) {
		// TODO Auto-generated method stub
		String inputline = "";
		String result = "";
		int[] print = null;
		String contextPath = "https://a.wunderlist.com/api/v1/webhooks?list_id="
				+ list_id
				+ "&client_id="
				+ PropertiesFile.getValue("client_id")
				+ "&access_token="
				+ accessToken;
		try {
			URL urldemo = new URL(contextPath);
			URLConnection yc = urldemo.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			while ((inputline = in.readLine()) != null) {
				result += inputline;
			}
			in.close();
			JSONArray jsonarray = new JSONArray(result);
			print = new int[jsonarray.length() + 5];
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jobj = jsonarray.getJSONObject(i);
				print[i] = jobj.getInt("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return print;
	}

	public int getRevision(String accessToken) {
		// TODO Auto-generated method stub
		String inputline = "";
		String result = "";
		int revision = 0;
		String contextPath = "https://a.wunderlist.com/api/v1/user?client_id="
				+ PropertiesFile.getValue("client_id") + "&access_token="
				+ accessToken;
		try {
			URL urldemo = new URL(contextPath);
			URLConnection yc = urldemo.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			while ((inputline = in.readLine()) != null) {
				result += inputline;
			}
			in.close();
			JSONObject jsonobj = new JSONObject(result);
			revision = jsonobj.getInt("revision");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return revision;
	}

	public static int getUid(String accessToken) {
		// TODO Auto-generated method stub
		String inputline = "";
		String result = "";
		int id = 0;
		String contextPath = "https://a.wunderlist.com/api/v1/user?client_id="
				+ PropertiesFile.getValue("client_id") + "&access_token="
				+ accessToken;
		try {
			URL urldemo = new URL(contextPath);
			URLConnection yc = urldemo.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			while ((inputline = in.readLine()) != null) {
				result += inputline;
			}
			in.close();
			JSONObject jsonobj = new JSONObject(result);
			id = jsonobj.getInt("id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
}
