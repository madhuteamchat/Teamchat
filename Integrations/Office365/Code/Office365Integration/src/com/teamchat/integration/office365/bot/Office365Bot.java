package com.teamchat.integration.office365.bot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.integration.office365.database.JDBCConnection;
import com.teamchat.integration.office365.database.Office365Subscribe;
import com.teamchat.integration.office365.property.Office365Property;
import com.teamchat.integration.office365.webhook.CreateWebhook;
import com.teamchat.integration.office365.webhook.TeamchatPost;

public class Office365Bot {

	String login_url="";
	
	public Office365Bot() {
		// TODO Auto-generated constructor stub
		Office365Property op=new Office365Property();
		op.loadParams();
		login_url=op.getLoginUrl();
		Timer t=new Timer();
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 8);
		t.schedule(new RenewSubSchedule(), cal.getTime(), 24*60*60*1000);
	}
	
	@OnKeyword("help")
	public void onhelpEntry(TeamchatAPI api) {
		TeamchatPost.tpapi=api;
		api.perform(
				api.context().currentRoom().registerForEvents().post(
				new PrimaryChatlet()
				.setQuestionHtml("<!DOCTYPE html><html><head></head><body>Hi! I am the <b>Office 365</b> bot of teamchat! You can subscribe for "
						 +       "notifications from your Office 365 account."         
						 +       "<br/>"
						 +       "Type <b>'subscribe'</b> to subscribe for notifications.<br/>"
						 +       "Type <b>'unsubscribe'</b> to unsubscribe notifications.<br/>"
						 +       "Type <b>'login'</b> to login to your Office365 account.<br/>"
						 +       "Type <b>'sublist'</b> to list subscriptions.<br/>"
						 +       "Type <b>'logout'</b> to Sign out.<br/>"
		                 +       "</body></html>")));
			
	}
	
	@OnKeyword("subscribe")
	public void onsubscribeEntry(TeamchatAPI api) {
		TeamchatPost.tpapi=api;
		String teamchat_id=api.context().currentSender().getEmail();
		String token=new JDBCConnection().retreiveAccessToken(teamchat_id);
		if(token.equals("access_token")) {
			onloginEntry(api);
		}
		else
		{
			System.out.println("subscribing");
		
			Office365Subscribe offsub=new Office365Subscribe();
			ArrayList<String> sub_type_list=offsub.retreiveSubList(teamchat_id);
			ArrayList<String> unsub=new ArrayList<String>();
			if(!sub_type_list.contains("Email"))
				unsub.add("Email");
			if(!sub_type_list.contains("Calendar"))
				unsub.add("Calendar");
			if(unsub.size()>0)
			{
				api.perform(
					api.context().currentRoom().registerForEvents().post(
							new PrimaryChatlet()
								.setQuestionHtml("<!DOCTYPE html><html><head></head><body>"
											+         "Choose option to Subscribe"
												+       "</body></html>")
								.setReplyScreen(api.objects().form()
										.addField(unsubField(api, unsub)))
								.alias("Subscribe")
							));
			}
			else
			{
				api.perform(
						api.context().currentRoom().registerForEvents().post(
								new PrimaryChatlet()
								.setQuestionHtml("<!DOCTYPE html><html><head></head><body>"
											+         "Nothing to subscribe."
												+       "</body></html>")
							));
			}
		
		
		
		
		}
			
	}
	
	@OnKeyword("unsubscribe")
	public void onunsubscribeEntry(TeamchatAPI api) {
		TeamchatPost.tpapi=api;
		String teamchat_id=api.context().currentSender().getEmail();
		String token=new JDBCConnection().retreiveAccessToken(teamchat_id);
		if(token.equals("access_token")) {
			onloginEntry(api);
		}
		else {
			System.out.println("unsubscribing");
			Office365Subscribe offsub=new Office365Subscribe();
			ArrayList<String> sub_type_list=offsub.retreiveSubList(teamchat_id);
			if(sub_type_list.size()>0)
			{
				api.perform(
						api.context().currentRoom().registerForEvents().post(
								new PrimaryChatlet()
									.setQuestionHtml("<!DOCTYPE html><html><head></head><body>"
												+         "Choose option to unsubscribe"
													+       "</body></html>")
									.setReplyScreen(api.objects().form()
											.addField(unsubField(api, sub_type_list)))
									.alias("Unsubscribe")
								));
			}
			else
			{
				api.perform(
						api.context().currentRoom().registerForEvents().post(
								new PrimaryChatlet()
									.setQuestionHtml("<!DOCTYPE html><html><head></head><body>"
												+         "Nothing to unsubscribe."
													+       "</body></html>")
								));
			}
		}
			
	}
	
	public Field unsubField(TeamchatAPI api,ArrayList<String> subtypelist)
	{
		Field f=api.objects().select().label("Select").name("notify");
		for(int i=0;i<subtypelist.size();i++)
		{
//		      System.out.println("list: "+placelist.get(i));
		      f.addOption(subtypelist.get(i));
		}
		return f;
	}
	
	@OnKeyword("login")
	public void onloginEntry(TeamchatAPI api) {
		TeamchatPost.tpapi=api;
		System.out.println("logging in");
		String sname=api.context().currentSender().getEmail();
		String link=login_url+"?sname="+sname;
		String teamchatlink=new TeamchatURLlink().createEmbeddedLink(link, "Office365", "http");
		api.perform(
				api.context().currentRoom().registerForEvents().post(
				new PrimaryChatlet()
				.setQuestionHtml("<!DOCTYPE html><html><head></head><body>"
						+"<a href='"+teamchatlink+"'>Click here to login</a>"
		                 +       "</body></html>")));
	
		}
			
	
	@OnKeyword("sublist")
	public void onlistEntry(TeamchatAPI api) {
		TeamchatPost.tpapi=api;
		String teamchat_id=api.context().currentSender().getEmail();
		String token=new JDBCConnection().retreiveAccessToken(teamchat_id);
		if(token.equals("access_token")) {
			onloginEntry(api);
		}
		else {
		Office365Subscribe offsub=new Office365Subscribe();
		ArrayList<String> sublist=offsub.retreiveSubList(teamchat_id);
		if(sublist.size()>0)
		{
			String msg="";
			for(int i=0;i<sublist.size();i++)
				msg+=sublist.get(i)+"<br>";
			msg=" You have Subscribed<br>"+msg;
			api.perform(
					api.context().currentRoom().registerForEvents().post(
					new PrimaryChatlet()
					.setQuestionHtml("<!DOCTYPE html><html><head></head><body>"
							+         msg
			                 +       "</body></html>")
					));
		}
		else
		{

			api.perform(
					api.context().currentRoom().registerForEvents().post(
					new PrimaryChatlet()
					.setQuestionHtml("<!DOCTYPE html><html><head></head><body>"
							+         "Not Yet Subscribed"
			                 +       "</body></html>")
					));
		}
		}
			
	}
	
	@OnAlias("Unsubscribe")
	public void unsubscribe(TeamchatAPI api) {
		TeamchatPost.tpapi=api;
		String teamchat_id=api.context().currentReply().senderEmail();
		String token=new JDBCConnection().retreiveAccessToken(teamchat_id);
		System.out.println("setting subscription");
		String notify=api.context().currentReply().getField("notify");
		if(token.equals("access_token")){
			onloginEntry(api);
		}
		else
		{
			CreateWebhook cw=new CreateWebhook();
			cw.unSubscribe(teamchat_id, notify);
		}
	}
	@OnAlias("Subscribe")
	public void notify(TeamchatAPI api) {
		TeamchatPost.tpapi=api;
		String teamchat_id=api.context().currentReply().senderEmail();
		String token=new JDBCConnection().retreiveAccessToken(teamchat_id);
		System.out.println("setting subscription");
		String notify=api.context().currentReply().getField("notify");
		if(token.equals("access_token")){
			onloginEntry(api);
		}
		else
		{
			if(notify.equals("Calendar"))
			{
				CreateWebhook cw=new CreateWebhook();
				cw.subscribe(teamchat_id, notify);
			}
			else if(notify.equals("Email"))
			{
				CreateWebhook cw=new CreateWebhook();
				cw.subscribe(teamchat_id, notify);
			}
		}
	}
}
