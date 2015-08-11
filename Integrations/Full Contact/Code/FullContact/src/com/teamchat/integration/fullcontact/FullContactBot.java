package com.teamchat.integration.fullcontact;

import java.io.IOException;
import java.util.Properties;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class FullContactBot {
	Properties configProps;
	
	@OnKeyword("help")
	public void help(TeamchatAPI api) {
		try {
			configProps = loadPropertyFromClasspath("fullcontact-config.properties", FullContactBot.class);
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
						.setQuestionHtml("Upload image of card to get full contact - <a href='" + fileselectpage + "' target=_blank>Card Image</a><br/>")));
	}
	
	@OnKeyword ("email")
	public void email (TeamchatAPI api) {
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Email")
				.addRegexValidation(".+", "please enter a value")
				.name("email"));
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("Enter your email to get full contact")
						.setReplyScreen(f).setReplyLabel("Enter")
						.showDetails(true).setDetailsLabel("entered")
						.alias("details")));
	}
	
	@OnAlias("details")
	public void fullContact (TeamchatAPI api) {
		try {
			configProps = loadPropertyFromClasspath("fullcontact-config.properties", FullContactBot.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String roomId = api.context().currentRoom().getId();
		
		String email = api.context().currentReply().getField("email");
		String apiUrl = "https://api.fullcontact.com/v2/person.json?email=" + email + "&webhookUrl=http%3A%2F%2Finterns.teamchat.com%2FFullContact%2FPerson%3Froom%3D" + roomId + "&apiKey=" + configProps.getProperty("apikey");
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("Go to this link <a href='" + apiUrl + "' target=_blank>Search</a>")));
	}
	
	public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException {
		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;
	}
}