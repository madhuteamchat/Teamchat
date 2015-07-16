/* Author: Prakhar Vashisht
 * Date: May 29, 2015
 * 
 * Description:
 * 	 A user can request leave for the day by sending the keyword 'leavereq' to the bot.
   - The bot will send a poll message to the manager asking for approval.
   - If the manager replies no, the bot informs the user that "leave rejected by manager".
   - If the manager replies yes, the bot sends a poll message to HR asking for approval.
   - If the HR replies no, the bot informs the user that "leave rejected by HR".
   - If the HR replies yes, the bot informs the user that "leave accepted".
*/

package com.teamchat.leaverep;

import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;

public class LeaveReporting {

	public static final String bot="bot888@outlook.com";
	public static final String botpwd="bazingacooper04";
	public static final String manager="prakhar888.v@gmail.com";
	public static final String hr="prakhar888@icloud.com";
	public static String user="";
	
	public static void main(String[] args) {
		TeamchatAPIImpl.fromFile("config.json")
		.setEmail(bot)
		.setPassword(botpwd)
		.startReceivingEvents(new LeaveReporting());
		}
	
	@OnKeyword("leavereq")
	public void onRequest(TeamchatAPI api){
		api.perform(api.context().currentRoom().post(
				new PrimaryChatlet()
				.setQuestion("Are you sure. Please reply.")
				.setReplyScreen(api.objects().form()
						.addField(api.objects().select().label("Reply").name("user_rep")
								.addOption("Yes")
								.addOption("No"))
						)
						.alias("user_rep")));
	}
		
	@OnAlias("user_rep")	
	public void getManagerReply(TeamchatAPI api){	
		if(api.context().currentReply().getField("user_rep").equals("Yes")){
			System.out.println("Waiting for manager reply...");
			user=api.context().currentReply().senderEmail();
			Room r=api.context().create().setName("Report Summary").add(manager);
			api.perform(r.post(
						new PrimaryChatlet()
						.setQuestion(user + " has requested for a leave.")
						.setReplyScreen(api.objects().form()
								.addField(api.objects().select().label("Do you allow?").name("man_rep")
										.addOption("Yes")
										.addOption("No"))
								)
								.alias("man_rep")));
				}
		}
	
	@OnAlias("man_rep")
	public void afterManagerReply(TeamchatAPI api){
		if(api.context().currentReply().getField("man_rep").equals("Yes")){
			System.out.println("Waiting for HR reply....");
			Room r=api.context().create().setName("Report Summary").add(hr);
			api.perform(r.post(
						new PrimaryChatlet()
						.setQuestion(user + " has requested for a leave.")
						.setReplyScreen(api.objects().form()
								.addField(api.objects().select().label("Do you allow?").name("hr_rep")
										.addOption("Yes")
										.addOption("No"))
								)
								.alias("hr_rep")));
			}
		
		else{
			Room r=api.context().create().setName("Report Summary").add(user);
			api.perform(r.post( new TextChatlet("Leave request rejected by manager")));
			
		}
	}
	
	@OnAlias("hr_rep")
	public void getHRReply(TeamchatAPI api){
		if(api.context().currentReply().getField("hr_rep").equals("Yes")){
			Room r=api.context().create().setName("Report Summary").add(user);
			api.perform(r.post( new TextChatlet("Leave accepted")));
			}
		else{
			Room r=api.context().create().setName("Report Summary").add(user);
			api.perform(r.post( new TextChatlet("Leave request rejected by HR")));
		}
	}
}
