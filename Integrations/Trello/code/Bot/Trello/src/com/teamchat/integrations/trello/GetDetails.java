package com.teamchat.integrations.trello;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetDetails {
	
	/*public String[] getBoardIDs(String token) {
		// TODO Auto-generated method stub
		String inputline = "";
		String result = "";
		String[] print = null;
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
			print = new String[jsonarray.length()];
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jobj = jsonarray.getJSONObject(i);
				print[i]=jobj.getString("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return print;
	}*/

	
	public String getUserID(String token) {
		String inputline = "";
		String result = "";
		String print = "";
		String contextPath = "https://trello.com/1/tokens/" + token
				+ "/member?key=" + PropertiesFile.getValue("api_key");
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
			JSONObject jobj = new JSONObject(result);
			print = jobj.getString("id");
			//String user_name = jobj.getString("fullName");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return print;
	}

	public String[] getWebhookIds(String accessToken) {
		// TODO Auto-generated method stub
		String inputline = "";
		String result = "";
		String[] print = null;
		String contextPath = "https://trello.com/1/token/"+accessToken+"/webhooks?key="+PropertiesFile.getValue("api_key");
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
			print = new String[jsonarray.length()];
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jobj = jsonarray.getJSONObject(i);
				print[i] = jobj.getString("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return print;

	}
	
	public String getMemberNamebyID(String mem_id){
		String mem_name=null;
		String inputline = "";
		String result = "";
		String contextPath = "https://api.trello.com/1/members/"+mem_id+"?key=" + PropertiesFile.getValue("api_key");
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
			JSONObject jobj = new JSONObject(result);
			mem_name = jobj.getString("fullName");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mem_name;
	}
}
