package com.teamchat.internship.test;

import java.io.IOException;
import java.util.List;

import com.google.api.services.drive.model.File;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.internship.googleDoc.DriveQuickstart;

public class Test {
	public static String bot=	"bbedant23@gmail.com";
	public static String pwd="mallika1234";
	//String link="";
	
	@OnKeyword("documents")
	public void Start_Setup(TeamchatAPI api)
	{	
		api.perform(
				api.context().currentRoom().post(
						new PrimaryChatlet().setQuestion("Select File List")
						.setReplyScreen(api.objects().form()
								.addField(api.objects().select().addOption("list").label("list").name("list")))
								.alias("listing")));
		}
	
	@OnAlias("listing")
	public void Listing(TeamchatAPI api) throws InterruptedException, IOException
	{
		
	 String html="";
	 
	 String list=api.context().currentReply().getField("list");
	 DriveQuickstart dq =new DriveQuickstart();
	 List<File> results=dq.method1();
	 
	 for (File file : results) {
         html+=file.getTitle()+"<br/>";
     }
	
	 api.perform(api.context().currentRoom().post(new TextChatlet(html)));
	 
	}
	 public static void main(String[] args) {
		 TeamchatAPI api = TeamchatAPI.fromFile("teamchat2.data")
				    .setEmail(bot) 
				    .setPassword(pwd) 
				    .startReceivingEvents(new Test()); 
		 
	}	
	}
