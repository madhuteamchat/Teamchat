/**
 * 
 */
package com.integration;

import com.github.hipchat.api.HipChat;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

/**
 * @author Himanshu Rathee
 *
 */
public class Notifier
{
	static TeamchatAPI api1;
	public Integer id;
	

	public Notifier()
	{
		
	}
	
	public Notifier(TeamchatAPI api)
	{
		this.api1 = api;
	}

	public void setid(Integer id){
		this.id=id;
	}
	
	public  Integer getid(){
		return id;
	}
	public void Postify(String message)
	{
		api1.perform(api1.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<u>Message-</u>" + message + "<br/>")));

	}

}
