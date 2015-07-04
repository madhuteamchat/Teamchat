package com.teamchat.integration.mailchimp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class Access {
	public static final String accessrslt = "\"Everything's Chimpy!\"";

	public void ping(TeamchatAPI api, String apikey) throws IOException {
		String result = "";
		URL urldemo = new URL(
				"https://us11.api.mailchimp.com/1.3/?method=ping&apikey="
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

		if (result.equals(accessrslt)) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet(
							"Hurray, successfully connected to Mailchimp. type help command to getmore commands.")));

		} else {
			api.perform(api.context().currentRoom()
					.post(new TextChatlet(" Invalid MailChimp API Key ")));
		}

	}

}
