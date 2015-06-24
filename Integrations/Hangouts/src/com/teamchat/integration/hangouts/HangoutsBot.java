package com.teamchat.integration.hangouts;

import java.io.IOException;
import java.util.Properties;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class HangoutsBot {
	@OnKeyword("help")
	public void help(TeamchatAPI api) {
		Properties configProps = new Properties();
		
		try {
			configProps = loadPropertyFromClasspath("hangouts-config.properties", HangoutsBot.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Room Name").addRegexValidation("[^()]", "please enter a value").name("room"));
		f.addField(api.objects().input().label("Url").addRegexValidation("[^()]", "please enter a value").name("url"));
		
		String url = TeamchatEmbeddLink.createEmbeddedLink(configProps.getProperty("server") + "Hangouts.jsp", "Start Hangouts", "http");
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<div><a href='" + url + "' target='_blank'><img src='" + configProps.getProperty("server") + "hangouts.bmp'/></a></div>"
								+ "<div>Enter hangout room name and link</div>")
						.setReplyScreen(f).setReplyLabel("Link")
						.showDetails(true)
						.setDetailsLabel("Rooms")
						.alias("hangout")));
	}
	
	@OnAlias("hangout")
	public void hangout (TeamchatAPI api) {
		String link = api.context().currentReply().getField("url");
		String room = api.context().currentReply().getField("room");
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml("Join the room - <a href='" + link +"' target='_blank'>'" + room + "'</a>")));
	}
	
	public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException {
		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;
	}
}