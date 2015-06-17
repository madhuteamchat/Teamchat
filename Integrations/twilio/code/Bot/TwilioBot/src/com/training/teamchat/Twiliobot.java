package com.training.teamchat;
import java.util.*;
import java.text.*;

import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.ReportChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;
import com.teamchat.client.sdk.chatlets.PollChatlet;

import com.twilio.sdk.TwilioRestException;



public class Twiliobot {

//String sd ="<h1>hi</h1>";
	public static void main(String[] args)
	{
		TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data")
				 .setEmail("bot4433@gmail.com") //change to your teamchatregistered email id
				 .setPassword("5678") //change to your teamchat password
				 .startReceivingEvents(new Twiliobot()); //Wait for other user tosend message
				}

	@OnKeyword("sendmessg")
	public void sendmessg(TeamchatAPI api)
	{
	 api.perform(
	 api.context().currentRoom().post(
			 new PrimaryChatlet()
				.setQuestion("Please enter the details needed..")
				.setReplyScreen(api.objects().form()
	
	.addField(api.objects().input().label("To").name("To"))
	.addField(api.objects().input().label("Message").name("msg"))
						)
						.alias("answer1")
						));
	 
	}
	
	@OnAlias("answer1")
	
		public void sendmesg(TeamchatAPI api) throws TwilioRestException
		{
		
		String to = api.context().currentReply().getField("To");
		String msg = api.context().currentReply().getField("msg");
		SendMessg sm = new SendMessg();
	int c =	sm.value(to, msg);
	 if(c==1)
	 {
			api.perform(
					api.context().currentRoom().post(
					new TextChatlet("your message has been deliverd")));
					
	 }
		}
	
	@OnKeyword("reminder")
	public void onCreate1(TeamchatAPI api)
	{
		
		api.perform(
				api.context().currentRoom().post(
				new PrimaryChatlet()
				.setQuestion("Please enter the details about your message")
				.setReplyScreen(api.objects().form()
	.addField(api.objects().input().label("SCHEDULE TIME IN HH:MM").name("schedule"))
	.addField(api.objects().input().label("MESSAGE").name("message"))
	.addField(api.objects().input().label("RECIPENT").name("recipent"))
					
						)
						.alias("answer2")
						));
	}
	
	@OnAlias("answer2")
	public void schedule(TeamchatAPI api)
	{
	  String dtime = api.context().currentReply().getField("schedule");
	  String[] dt = dtime.split(":");
	  String msg = api.context().currentReply().getField("message");
	  String rcp = api.context().currentReply().getField("recipent");
	  String sname = api.context().currentReply().sender();
	  String smail = api.context().currentReply().senderEmail();
	  Timer t =new Timer();
	  DateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	  Calendar cal = Calendar.getInstance();
	  cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dt[0]));
	  cal.set(Calendar.MINUTE, Integer.parseInt(dt[1]));
	  System.out.println(dateformat.format(cal.getTime()));
	  t.schedule(new reminder(rcp,msg), cal.getTime());
	}
	
	@OnKeyword("call")
	public void onCreate21(TeamchatAPI api)
	{
		
		api.perform(
				api.context().currentRoom().post(
				new PrimaryChatlet()
				.setQuestion("Please enter the number that  you need to call")
				.setReplyScreen(api.objects().form()
	.addField(api.objects().input().label("To").name("to"))
					
						)
						.alias("answer3")
						));
	}
	@OnAlias("answer3")
	
	public void call(TeamchatAPI api) throws TwilioRestException
	{
	
	String to = api.context().currentReply().getField("to");
	
	 Callme.make(to);
	}

}
