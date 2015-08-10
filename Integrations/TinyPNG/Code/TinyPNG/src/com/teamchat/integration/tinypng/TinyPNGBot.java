package com.teamchat.integration.tinypng;

import java.io.IOException;
import java.util.Properties;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class TinyPNGBot {
	Properties configProps;
	
	@OnKeyword("help")
	public void help(TeamchatAPI api) {
		try {
			configProps = loadPropertyFromClasspath("tinypng-config.properties", TinyPNGBot.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String roomId = api.context().currentRoom().getId();
		String fileselectpage = configProps.getProperty("server") + "fileselect.jsp?room=" + roomId;
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("Go to this link to compress your image - <a href='" + fileselectpage + "' target=_blank>Compress</a>")));
	}
	
	public static Properties loadPropertyFromClasspath(String fileName,
			Class<?> type) throws IOException {
		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;
	}
}
