package com.teamchat.integration.youtube.servlet;

import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class TeamchatPost {
	
	public static TeamchatAPI tpapi;
	
	public void postMsg(String msg,String rid)
	{
		System.out.println(msg);
		Room r=tpapi.context().byId(rid);
		tpapi.perform(r.post(new PrimaryChatlet()
					.setQuestionHtml(msg)));
	}

}
