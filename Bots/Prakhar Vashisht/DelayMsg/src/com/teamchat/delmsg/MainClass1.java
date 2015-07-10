package com.teamchat.delmsg;

import java.util.ArrayList;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;
import com.teamchat.delmsg.DelayedMessaging1;

public class MainClass1 extends DelayedMessaging1{
	public static void main(String args[]) throws Exception{
		ArrayList<DelayedMessaging1> msg=new ArrayList<DelayedMessaging1>();
		for(int i=0;i<100;i++){ 
			msg.add(new DelayedMessaging1());
			newThread(msg.get(i));
		}
	}


	public static void newThread(DelayedMessaging1 dm){
		TeamchatAPIImpl.fromFile("config.json")
		.setEmail(bot)
		.setPassword(botpwd)
		.startReceivingEvents(dm);
	}
	
}
