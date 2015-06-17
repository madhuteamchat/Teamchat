package com.slack.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class getjson {

	public void getJSON(String s) throws ClientProtocolException, IOException{

		HttpClient clt = new DefaultHttpClient();
		HttpGet req = new HttpGet(s);

		HttpResponse resp = clt.execute(req);
		BufferedReader rd = new BufferedReader(new InputStreamReader(resp
				.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString();
		JSONObject j = new JSONObject(output);

	}
}
