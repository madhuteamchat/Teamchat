package com.teamchat.integration.room.main;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

/*
**
* @author : Anuj Arora
*/

public class roombot {
	@OnKeyword(value = "Room")
	public void Room(TeamchatAPI api) {
		Form fo = api.objects().form();
		fo.addField(api.objects().input().name("Name").label("Your Name:"));
		fo.addField(api.objects().input().name("roomlink").label("Room Link:"));
		fo.addField(api.objects().input().name("EmailId").label("Recepient's email-Id:"));
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<center><h4 style=\"color:black\";><b>Click on the button below:-</b></h4></center>"
								+ "<br />"
								+ "<center><a href=\"https://room.co/#/\" target=\"_blank\"><img src=\"https://slack.global.ssl.fastly.net/1e47/plugins/room/assets/service_128.png\" border=\"0\" height=\"64\" width=\"64\" ></a></center>"
								+ "<br />"
								+ "<ul type=\"square\"; style=\"color:#359FD8\";><li><a1 style=\"color:#484848\";><b>It will redirect you to a page, Where you will find a Start video chat button</b></a1></li></ul>"
								+ "<ul type=\"square\"; style=\"color:#359FD8\";><li><a1 style=\"color:#484848\";><b>Clicking on it will redirect you to a page Where you will find a link</b></a1></li></ul>"
								+ "<ul type=\"square\"; style=\"color:#359FD8\";><li><a1 style=\"color:#484848\";><b>Copy that link and paste it in the reply with the Email Id of the person you want to interact with</b></a1></li></ul>")
						.setReplyScreen(fo).setReplyLabel("Connect")
						.alias("adduser")));
	}

	@OnAlias(value = "adduser")
	public void adduser(TeamchatAPI api) {
		String Name = api.context().currentReply().getField("Name");
		String EmailId = api.context().currentReply().getField("EmailId");
		String roomlink = api.context().currentReply().getField("roomlink");
		Room roo = api.context().create().setName("RoomBot").add(EmailId);
		api.perform(roo.post(new PrimaryChatlet()
				.setQuestionHtml("<h4><b>Room Details:</b></h4>"
						+ "<ul type=\"square\"; style=\"color:#359FD8\";><li><a1 style=\"color:black\";><b>Contact Person- </b></a1><a2 style=\"color:#359FD8\";>"
						+ Name
						+ "</a2></li></ul>"
						+ "<br /><b>Click on the link below to join the Video Chat.</b>"
						+ "<br /><a href=\"" + roomlink
						+ "\"><b style=\"color:#359FD8\";>Go To Room</b></a>")));
	}

	@OnKeyword(value = "help")
	public void help(TeamchatAPI api) {
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<h4><b>Hi, I'm Room Bot.</b></h4>"
								+ "<img src=\"https://slack.global.ssl.fastly.net/1e47/plugins/room/assets/service_128.png\" height=\"120\" width=\"120\">"
								+ "<br />"
								+ "<br /><b>I'll help you to interact with your friends or relatives with the help of video chat.</b>"
								+ "<br />"
								+ "<ul type=\"square\"; style=\"color:#359FD8\";><li><a1 style=\"color:black\";><b>Room - </b></a1><a2 style=\"color:#359FD8\";>"
								+ "Use This keyword to continue with video chat"
								+ "</a2></li></ul>")));
	}
}
