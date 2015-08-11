package com.teamchat.integration.box;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class BoxBot {
	static Properties configProps;
	
	@OnKeyword("help")
	public void box (TeamchatAPI api) {
		String roomId = api.context().currentRoom().getId();
		
		try {
			configProps = loadPropertyFromClasspath("box-config.properties", BoxBot.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String url = configProps.getProperty("server").trim() + "Select.jsp?roomid=" + roomId + "&id=" + configProps.getProperty("clientid").trim();
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml("Click <a href='" + url + "' target=_blank>here</a> to select and share files or folders")));
	}
	
	public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException {
		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;
	}
	
	public static String createEmbeddedLink(String url, String title, String protocol) {
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

final class SessionIdentifierGenerator {
	  private SecureRandom random = new SecureRandom();

	  public String nextSessionId() {
	    return new BigInteger(130, random).toString(32);
	  }
}