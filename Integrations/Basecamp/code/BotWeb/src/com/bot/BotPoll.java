package com.bot;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PollChatlet;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class BotPoll
{

	private int yes;
	private int no;

	public static void main(String[] args)
	{
		TeamchatAPI api = TeamchatAPI.fromFile("bot46.config").setEmail("madhu@webaroo.com").setPassword("pass").startReceivingEvents(new BotPoll());
	}

	@OnKeyword(value = "start")
	public void start(TeamchatAPI api)
	{

		PollChatlet poll = new PollChatlet();
		poll.setQuestion("How is this event going on ?");
		poll.setDetailsLabel("Feedback");

		api.performPostInCurrentRoom(poll.alias("eventpoll"));
	}
	
	@OnKeyword(value = "result")
	public void result(TeamchatAPI api)
	{

		System.err.println("Yes : "+yes +"||"+"No :" +no);
		
		api.performPostInCurrentRoom(new TextChatlet("Yes : "+yes +"| No :" +no));
	}

	@OnAlias(value = "eventpoll")
	public void onEventPoll(TeamchatAPI api)
	{
		if (api.context().currentReply().getField("resp").equalsIgnoreCase("yes"))
		{
			yes++; 		
		}
		else
		{
			no++;
		}
	}
}
