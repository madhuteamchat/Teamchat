/* Author: Prakhar Vashisht
 * Date: May 29, 2015
 * 
 * Description:
 * When a user sends the keyword 'delaymsg' he/she gets the form which has following fields:
   	  1. scheduled time - entered in HH:MM form
      2. message
      3. recipient
   At the scheduled time, the message should is posted to the recipient.
   - It should be possible to schedule multiple such messages
*/

package com.teamchat.delmsg;

import java.util.Calendar;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;
import com.teamchat.delmsg.ThreadClass;

public class DelayedMessaging1 implements Runnable {
	public static final String bot="bot888@outlook.com";
	public static final String botpwd="bazingacooper04";
	String time="",msg="",rcp="";
	int min_diff,current_sec,sec_diff;
	final TeamchatAPI api=null;
	
	@Override
	public void run() {
		api.perform(
				api.context().currentRoom().post(
						new PrimaryChatlet()
						.setQuestion("Please fill the following form for sending delayed message ")
						.setReplyScreen(api.objects().form()
								.addField(api.objects().input().label("Receiving Time").name("time"))
								.addField(api.objects().input().label("Message").name("msg"))
								.addField(api.objects().input().label("Recipient").name("rcp"))
								)
								.alias("delaymsg")));
				}
	
	@OnAlias("delaymsg")
	public void getDetails(TeamchatAPI api) throws InterruptedException{
		
		 time=api.context().currentReply().getField("time");
		 msg=api.context().currentReply().getField("msg");
		 rcp=api.context().currentReply().getField("rcp");
		
		 sec_diff=performCal(time);
		 
		 System.out.println("Waiting for " + (float)sec_diff/60 + " minutes");
		 ThreadClass t=new ThreadClass();
		 t.setData(bot, botpwd);
		 t.start();
		 sleep(sec_diff*1000);
		 	
		 
		 Room r=api.context().create().setName("Report Summary")
					.add(rcp);
			api.perform(r.post(new TextChatlet(msg)));
			System.out.println("Message delivered");
			TeamchatAPIImpl.fromFile("config.json")
			.setEmail(bot)
			.setPassword(botpwd)
			.startReceivingEvents(new DelayedMessaging1());
			
}
	
	@OnKeyword("delaymsg")
	public int onDelayMessage(TeamchatAPI api) {
		
	private int performCal(String time) {
		
		Calendar cal=Calendar.getInstance();
		int current_hour=cal.get(Calendar.HOUR_OF_DAY);
		int sch_hour=Integer.parseInt(time.substring(0, 2));
		int hour_diff=sch_hour-current_hour;
		
		int current_min=cal.get(Calendar.MINUTE);
		int sch_min=Integer.parseInt(time.substring(3, 5));
		int min_diff=sch_min-current_min;
		
		current_sec=cal.get(Calendar.SECOND);
		
		min_diff=(hour_diff>=0)?(hour_diff*60)+min_diff:min_diff+((24+hour_diff)*60);
		sec_diff=min_diff*60;
		sec_diff=sec_diff-current_sec;
		
		System.out.println("Message will be delivered after " + min_diff + " minutes or " + (float)min_diff/60 + " hours");
		return sec_diff;
		
	}

	
}
}