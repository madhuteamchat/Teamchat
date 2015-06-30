package com.teamchat.integration.box;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Properties;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class BoxBot {
	static Properties configProps;
	
	@OnKeyword("Help")
	public void Box (TeamchatAPI api) {
		box (api);
	}
	
	@OnKeyword("help")
	public void box (TeamchatAPI api) {
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml("'Share' - Share multiple file or folders")));
	}
	
	@OnKeyword("Share")
	public void Share (TeamchatAPI api) {
		share (api);
	}
	
	@OnKeyword("share")
	public void share (TeamchatAPI api) {
		String roomId = api.context().currentRoom().getId();

		try {
			configProps = loadPropertyFromClasspath("box-config.properties", BoxBot.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String url = configProps.getProperty("server").trim() + "Select.jsp?roomid=" + roomId;
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<a href='" + url + "' target=_blank>Select files or folders</a>")));
	}
	
	public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException {
		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;
	}
}

final class SessionIdentifierGenerator {
	  private SecureRandom random = new SecureRandom();

	  public String nextSessionId() {
	    return new BigInteger(130, random).toString(32);
	  }
}