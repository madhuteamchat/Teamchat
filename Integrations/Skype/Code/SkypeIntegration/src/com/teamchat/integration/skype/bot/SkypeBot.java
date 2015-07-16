package com.teamchat.integration.skype.bot;

import java.net.URLEncoder;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class SkypeBot {
	
	

	// Help
	@OnKeyword("help")
	public void instagramhelp(TeamchatAPI api) {
		instagramHelp(api);
	}
		@OnKeyword("Help")
		public void instagramHelp(TeamchatAPI api) {
			api.perform(api.context().currentRoom().post(new PrimaryChatlet()
			.setQuestionHtml("<div align=\"center\"><strong>Skype Help</strong></div>"
			+"Hey! I am <b>Skype bot</b>. "
			+"You can reach out to family and friends worldwide "
			+"through Skype where you call or chat with them."
			+"<br>Type <b>Skype</b> "
			+"to generate a Skype link to connect with your Skype friend."
			)));
			
		}

	
	@OnKeyword("Skype")
	public void getSkypeDetail(TeamchatAPI api) {
		getskypedetail(api);
	}
	
	@OnKeyword("skype")
	public void getskypedetail(TeamchatAPI api) {
		api.perform(api.context().currentRoom().post(new PrimaryChatlet()
		.setQuestionHtml("<strong>Enter the Details </strong> (for group chat type the skype usernames seperated by comma (\",\"))")
		.setReplyScreen(api.objects().form()
				.addField(api.objects().input().label("Skype Username").name("name"))
				.addField(api.objects().input().label("Topic (Optional)").name("topic"))
				.addField(api.objects().select().label("Connect by").name("conn")
						.addOption("AudioCall")
						.addOption("VideoCall")
						.addOption("Chat")))
		.alias("aliasskype")
		));   
	}
	
		
	@OnAlias("aliasskype")
	public void skypeLink(TeamchatAPI api)
	{
		try 
		{
			String name=api.context().currentReply().getField("name");
			String conn=api.context().currentReply().getField("conn");
			String topic="";
			topic=api.context().currentReply().getField("topic");
			if(topic.length()>1)
			{
				topic="&topic="+URLEncoder.encode(topic, "utf-8");
			}			
			if(conn.equals("VideoCall"))
			{
				String skype_name=name.replace(",", ";");
				skype_name=skype_name.replace(" ", "");
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
							.setQuestionHtml(name+" <a href=\"skype:"+name+"?call&video=true"+topic+"\">Start Video Call</a>")));
			}
			else if(conn.equals("Chat"))
			{
				String skype_name=name.replace(",", ";");
				skype_name=skype_name.replace(" ", "");
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
							.setQuestionHtml(name+" <a href=\"skype:"+name+"?chat"+topic+"\">Start Chat</a>")));
			}
			else
			{
				String skype_name=name.replace(",", ";");
				skype_name=skype_name.replace(" ", "");
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
							.setQuestionHtml(name+" <a href=\"skype:"+name+"?call"+topic+"\">Start Call</a>")));
			}
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	

}
