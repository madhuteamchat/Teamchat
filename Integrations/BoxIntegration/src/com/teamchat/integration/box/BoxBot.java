package com.teamchat.integration.box;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Properties;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class BoxBot {
	static Properties configProps;
	static String path = "/home/intern3/Documents/data/solutions-config/workflow-data/box/box-config.properties";
	
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
			configProps = loadPropertyFileFromDisk(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<a href='http://localhost:8080/BoxIntegration/Select.jsp?id=" + configProps.getProperty("clientid").trim()
								+ "&roomid=" + roomId + "' target=_blank>Select files or folders</a>")));
	}
	
	public static Properties loadPropertyFileFromDisk(String filePath) throws Exception {
		String os = System.getProperty("os.name");
		if (os.toLowerCase().contains("windows")) {
			filePath = "d:" + filePath;
		}
		
		Properties configProp = new Properties();
		FileInputStream fIS = new FileInputStream(filePath);
		configProp.load(fIS);
		fIS.close();
		return configProp;
	}
}

final class SessionIdentifierGenerator {
	  private SecureRandom random = new SecureRandom();

	  public String nextSessionId() {
	    return new BigInteger(130, random).toString(32);
	  }
}
