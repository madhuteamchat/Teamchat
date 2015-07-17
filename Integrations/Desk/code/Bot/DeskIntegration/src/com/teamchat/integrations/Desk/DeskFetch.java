package com.teamchat.integrations.Desk;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class DeskFetch {

	public static void CaseReply(String id, String msg, String email) {
		// String urlParameters = "{\"body\":\"" + msg
		// + "\", \"direction\":\"out\"}";
		// System.out.println(urlParameters);
		JSONObject postData = new JSONObject();
		postData.put("body", msg);
		postData.put("direction", "out");
		// byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
		String resp = "", line;
		String se = new String(postData.toString());
		String request = "https://" + DeskBot.desk_website
				+ ".desk.com/api/v2/cases/" + id + "/replies";
		try {
			URL url = new URL(request);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");

			String userpass = DeskBot.desk_email + ":" + DeskBot.desk_password;
			String basicAuth = "Basic "
					+ new String(new Base64().encode(userpass.getBytes()));
			conn.setRequestProperty("Authorization", basicAuth);
			conn.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(se);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			while ((line = br.readLine()) != null) {
				resp += line;
			}
			System.out.println(resp);
			wr.flush();
			wr.close();
			Servlet.api.perform(Servlet.api
					.context()
					.create()
					.add(email)
					.post(new PrimaryChatlet()
							.setQuestionHtml("Reply Successful!")));

		} catch (Exception e) {
			e.printStackTrace();
			Servlet.api
					.perform(Servlet.api
							.context()
							.create()
							.add(email)
							.post(new PrimaryChatlet()
									.setQuestionHtml("Oops! An error occurred. Please try again.")));
		}
	}

	public static void Login(String email) {
		String request = "https://" + DeskBot.desk_website
				+ ".desk.com/api/v2/users/current";
		String resp = "", line;
		try {
			URL url = new URL(request);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			String userpass = DeskBot.desk_email + ":" + DeskBot.desk_password;
			String basicAuth = "Basic "
					+ new String(new Base64().encode(userpass.getBytes()));
			conn.setRequestProperty("Authorization", basicAuth);
			conn.setDoOutput(true);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			while ((line = br.readLine()) != null) {
				resp += line;
			}
			System.out.println(resp);
			Servlet.api.perform(Servlet.api
					.context()
					.create()
					.add(email)
					.post(new PrimaryChatlet()
							.setQuestionHtml("Login Successful!")));

		} catch (Exception e) {
			e.printStackTrace();
			Servlet.api
					.perform(Servlet.api
							.context()
							.create()
							.add(email)
							.post(new PrimaryChatlet()
									.setQuestionHtml("Oops! An error occurred. Please try again.")));
		}

	}
}