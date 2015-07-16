package com.teamchat.integrations.Jenkins;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class Bot {
    static TeamchatAPI api;

@OnKeyword("Help")
public void provideChannels(TeamchatAPI api)
{   
	String html="<h4 align='center'><b>Hey, this is Jenkins Bot<br/></b>"
			+ "<img src='http://kodi.tv/wp-content/uploads/jenkins_logo-300x96.png' height='90' width='230'></h4><br/>"
			+ "You can use me to get Notifications From Jenkins by using following keywords</font><br/><br/>";
	String HtmlInstructions= "<ul><li><b>Connect:</b><font color='blue'> To Connect Jenkins with Teamchat</font>"+
			                 "</li><li><b>Start:</b><font color='blue'>To start Jenkins Notifications in current Room</font>"
			                  +"</li><li><b>Stop:</b><font color='blue'>To stop Jenkins Notifications in current Room</font>"
                              +"</li></ul>";
	
  	api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(html+HtmlInstructions)
  			                                                         
  	                                            )
  	           );
}

@OnKeyword("Connect")
	public void connect(TeamchatAPI api)
	{this.api=api;
	String link="http://tcinterns@interns.teamchat.com:8081/Jenkins/Register?email="+api.context().currentSender().getEmail()+"&id="+api.context().currentSenderId();
	System.out.println(api.context().currentSenderId());
	api.perform(api.context().currentRoom().post(new PrimaryChatlet()
    .setQuestionHtml("Click <a href='"+link+"' target='_blank'>here</a> to Configure a new Jenkins")));

	/*Form f=api.objects().form().addField(api.objects().input().name("name").label("Jenkins Name"))
			                         ;
	    api.perform(api.context().currentRoom().post(new PrimaryChatlet()
	                                                    .setQuestionHtml("Please Enter some details")
	                                                    .setReplyScreen(f)
	                                                    .alias("submit")));*/
	                                                    
	}
	/*@OnAlias("submit")
	public void submit(TeamchatAPI api)
	{
		String name=api.context().currentReply().getField("name");
		String username=api.context().currentReply().sender();
		Database db=new Database();
		String uid=db.generateUID(api.context().currentReply().senderEmail(),username,name);
		api.perform(api.context().currentRoom().post(new PrimaryChatlet()
		                                                .setQuestionHtml("Your UID is: <br/>"+uid)));
	}*/
	@OnKeyword("Start")
	public void config(TeamchatAPI api)
	{   this.api=api;
		api.perform(api.context().currentRoom().post(new PrimaryChatlet()
		                                                .setQuestionHtml("Enter the UID of Jenkins")
		                                                .setReplyScreen(api.objects().form()
		                                                                                .addField(api.objects().input().label("UID")
		                                                                                		                       .name("uid")
		                                                                                         )
		                                                               )
		                                                .setReplyLabel("Enter")
		                                                .alias("uidentered")
		                                                ));
	}
	
	@OnAlias("uidentered")
	public void uidEntered(TeamchatAPI api)
	{   this.api=api;
		String email = api.context().currentReply().senderEmail();
		String uid=api.context().currentReply().getField("uid");
		Database db=new Database();
		if( db.insertRoom(api.context().currentRoom().getId(),uid))
		{  api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("Notifier Successfully Setup for this room")));
		}
		else  api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("Invalid UID")));

	}
	
	@OnKeyword("Stop")
	public void stop(TeamchatAPI api)
	{   this.api=api;
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("Enter the UID of Jenkins")
				                                                         .setReplyScreen(api.objects().form().addField(
				                                                        		                           api.objects().input().name("uid").label("UID"))
				                                                        		         )
				                                                          .alias("deluid")));
	}
	
	@OnAlias("deluid")
	public void del(TeamchatAPI api)
	{  
		String uid=api.context().currentReply().getField("uid");
		Database db=new Database();
		String name;
		if( (name=db.deleteRoom(uid,api.context().currentRoom().getId()))!=null)
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("Successfully Stopped Notifications from "+name+" in this Room")));
		else	api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("Already Stopped Notifications for UID:"+uid+" in this Room")));

		
	}
}
