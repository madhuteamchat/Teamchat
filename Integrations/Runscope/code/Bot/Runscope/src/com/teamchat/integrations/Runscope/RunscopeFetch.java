package com.teamchat.integrations.Runscope;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

public class RunscopeFetch {

	public static JSONArray getBuckets() {
		String access_token = RunscopeBot.access_token;
		String url = "https://api.runscope.com/buckets";
		HttpClient clt = HttpClientBuilder.create().build();
		HttpGet req = new HttpGet(url);
		req.addHeader("Authorization", "Bearer " + access_token);
		BufferedReader rd;
		JSONArray out = new JSONArray();
		try {
			HttpResponse resp = clt.execute(req);
			rd = new BufferedReader(new InputStreamReader(resp.getEntity()
					.getContent()));
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = rd.readLine()) != null)
				sb.append(line);
			String output = sb.toString();
			System.out.println(output);
			JSONObject j = new JSONObject(output);
			JSONArray j1 = j.getJSONArray("data");
			String[] name = new String[j1.length()];
			String[] key = new String[j1.length()];
			for (int i = 0; i < j1.length(); i++) {
				JSONObject j2 = j1.getJSONObject(i);
				name[i] = j2.getString("name");
				key[i] = j2.getString("key");
				JSONObject in = new JSONObject();
				in.put("name", name[i]);
				in.put("key", key[i]);
				out.put(in);
			}
		} catch (Exception cpe) {
			cpe.printStackTrace();
		}
		return out;
	}

	public static JSONArray getTest(String key) {
		String access_token = RunscopeBot.access_token;
		String url = "https://api.runscope.com/buckets/" + key + "/radar";
		HttpClient clt = HttpClientBuilder.create().build();
		HttpGet req = new HttpGet(url);
		req.addHeader("Authorization", "Bearer " + access_token);
		BufferedReader rd;
		JSONArray out = new JSONArray();
		try {
			HttpResponse resp = clt.execute(req);
			rd = new BufferedReader(new InputStreamReader(resp.getEntity()
					.getContent()));
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = rd.readLine()) != null)
				sb.append(line);
			String output = sb.toString();
			JSONObject j = new JSONObject(output);
			JSONArray j1 = j.getJSONArray("data");
			String[] desc = new String[j1.length()];
			String[] rslt = new String[j1.length()];
			for (int i = 0; i < j1.length(); i++) {
				JSONObject j2 = j1.getJSONObject(i);
				desc[i] = j2.getString("name");
				rslt[i] = j2.getString("test_results_url");
				JSONObject in = new JSONObject();
				in.put("name", desc[i]);
				in.put("result", rslt[i]);
				out.put(in);
			}
		} catch (Exception cpe) {
			cpe.printStackTrace();
		}
		return out;
	}

	public static String getResutls(String res) {
		String access_token = RunscopeBot.access_token;
		HttpClient clt = HttpClientBuilder.create().build();
		HttpGet req = new HttpGet(res);
		req.addHeader("Authorization", "Bearer " + access_token);
		BufferedReader rd;
		String status = new String();
		try {
			HttpResponse resp = clt.execute(req);
			rd = new BufferedReader(new InputStreamReader(resp.getEntity()
					.getContent()));
			String line = "";
			StringBuilder sb = new StringBuilder();
			while ((line = rd.readLine()) != null)
				sb.append(line);
			String output = sb.toString();
			JSONObject j = new JSONObject(output);
			JSONArray j1 = j.getJSONArray("data");
			JSONObject j2 = j1.getJSONObject(0);
			status = j2.getString("result");
		} catch (Exception cpe) {
			cpe.printStackTrace();
		}
		return status;
	}

}