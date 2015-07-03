package com.teamchat.integrations.appearin;

import java.io.IOException;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

public class AppearInBot {

	@OnKeyword("help")
	public void help(TeamchatAPI api){
		api.perform(api.context().currentRoom().post(new TextChatlet("Type 'start' to start using appear.in")));
	}
	@OnKeyword("start")
	public void start(TeamchatAPI api){
		api.perform(api.context().currentRoom().post(
				new PrimaryChatlet()
				.setQuestion("Enter a name for URL")
				.setReplyScreen
				(
				api.objects().form()
				.addField(api.objects().input().label("String").name("str"))
				)
				.alias("start")
				));
	}
	@OnAlias("start")
	public void connect(TeamchatAPI api) throws IOException{
		
		String name=(api.context().currentReply().getField("str"));
		String print= "<a href=\"https://appear.in/"+name+"\" target=\"_blank\">start conversation</a>";
		api.perform(api.context().currentRoom().post(new TextChatlet(print)));
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TeamchatAPI api = TeamchatAPIImpl.fromFile("teamchat.data")
				.setEmail("botbegins@gmail.com")  //change to your teamchat registered email id
				.setPassword("botbeginsit"); //change to your teamchat password
				api.startReceivingEvents(new AppearInBot()); //Wait for other user to send message

	}
}