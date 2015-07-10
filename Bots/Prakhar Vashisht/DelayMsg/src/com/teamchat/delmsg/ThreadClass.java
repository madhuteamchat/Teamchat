package com.teamchat.delmsg;

import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

public class ThreadClass extends Thread {
	
	private String bot=null,botpwd=null;
	
	public void setData(String bot,String botpwd){
		this.bot=bot;
		this.botpwd=botpwd;
	}
	
	public void run(){
		TeamchatAPIImpl.fromFile("config.json")
		.setEmail(bot)
		.setPassword(botpwd)
		.startReceivingEvents(new DelayedMessaging1());

	}

}
