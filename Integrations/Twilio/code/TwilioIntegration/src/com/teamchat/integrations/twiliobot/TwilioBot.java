package com.teamchat.integrations.twiliobot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.twilio.sdk.TwilioRestException;


public class TwilioBot {
	 public static final String AuthToken = PropertiesFile.getValue("AuthToken"); 
	 public static final String Applicationsid = PropertiesFile.getValue("Applicationsid");;
	
	@OnKeyword("help")
	public void HelloWorld(TeamchatAPI api) 
	{
	 api.perform(
	 api.context().currentRoom().post(
	 new PrimaryChatlet()
	 .setQuestionHtml("<h3><center>Hey!!, This is Twilio Bot<center> </h3>"+"<h4>If you want to message, call, send reminders <br> view logs of "
	 		+ "your messages you can use me using the following keywords:</h4>"
	 		+ "<ul><li><b>Login</b> to login in your twilio account </li> "
	 		+ "<li><b>sendmessg</b> to send message  </li>  "
	 		+"<li><b>reminder</b> to send a reminder  </li>"
	 		+"<li><b>call</b> to make call  </li>"
)

	 ));

	}
	
	@OnKeyword("configure")
	public void configure(TeamchatAPI api){
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml(
								"Login to your Twilio account, and copy account sid  and twilio number and paste below. ")
							
						.setReplyScreen(
								api.objects()
										.form()

										.addField(
												api.objects().input()
														.label("Account Sid")
														.name("acctsid"))
										.addField(
												api.objects().input()
														.label("Twilio Number")
														.name("number"))

						).alias("configans")));

	}
	
	@OnAlias("configans")
	public void configans(TeamchatAPI api)
	{
		 String accountsid= api.context().currentReply().getField("acctsid");
		 String telnumber = api.context().currentReply().getField("number");
		 String smail = api.context().currentReply().senderEmail();
		 ManageDB db = new ManageDB();
		db.insert(smail, accountsid, telnumber);
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
		 String smail = api.context().currentReply().senderEmail();
	String to = api.context().currentReply().getField("To");
	String msg = api.context().currentReply().getField("msg");
	SendMessg sm = new SendMessg();
	ManageDB db = new ManageDB();
	String AccountSid = db.retrieve(smail);
	String telnumber = db.retrieve1(smail);
	
int c =	sm.value(to, msg,AuthToken,AccountSid,telnumber);
 if(c==1)
 {
		api.perform(
				api.context().currentRoom().post(
				new TextChatlet("your message has been deliverd")));
				
 }
 else
 {
	 api.perform(
				api.context().currentRoom().post(
				new TextChatlet("your message has not been delivered please try again")));
 }
	}
	
	@OnKeyword("reminder")
	public void onCreate1(TeamchatAPI api)
	{
		
		api.perform(
				api.context().currentRoom().post(
				new PrimaryChatlet()
				.setQuestion("Please enter the details about your message ")
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
	{  String smail = api.context().currentReply().senderEmail();
		ManageDB db = new ManageDB();
	String AccountSid = db.retrieve(smail);
	String telnumber = db.retrieve1(smail);
	  String dtime = api.context().currentReply().getField("schedule");
	  String[] dt = dtime.split(":");
	  String msg = api.context().currentReply().getField("message");
	  String rcp = api.context().currentReply().getField("recipent");
	  String sname = api.context().currentReply().sender();
	 // String smail = api.context().currentReply().senderEmail();
	  Timer t =new Timer();
	  DateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	  Calendar cal = Calendar.getInstance();
	  cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dt[0]));
	  cal.set(Calendar.MINUTE, Integer.parseInt(dt[1]));
	  System.out.println(dateformat.format(cal.getTime()));
	  t.schedule(new reminder(rcp,msg,AuthToken,AccountSid,telnumber), cal.getTime());
	}
	@OnKeyword("call")
	public void onCreate21(TeamchatAPI api)
	{
		
		api.perform(			api.context().currentRoom().post(
			new PrimaryChatlet()
			.setQuestionHtml("Please enter the number that  you need to call")
			.setReplyLabel("Call")
			.setReplyScreen(api.objects().form()
.addField(api.objects().input().label("To").name("to"))
.addField(api.objects().input().label("From  you can use any of register numbers").name("from"))
					
					)
						.alias("answer3")
					));
	}
	@OnAlias("answer3")
	
	public void call(TeamchatAPI api) throws TwilioRestException
	{ String smail = api.context().currentReply().senderEmail();
		ManageDB db = new ManageDB();
		String AccountSid = db.retrieve(smail);
		String telnumber = db.retrieve1(smail);
	String to = api.context().currentReply().getField("to");
	String from = api.context().currentReply().getField("from");
	
	 Callme.make(AccountSid,to,from,AuthToken,Applicationsid);
	}
@OnKeyword("logout")
	
	public void logout(TeamchatAPI api)
	{String smail = api.context().currentSender().getEmail();
	
	//apig=api;
		ManageDB db  =new ManageDB();
		db.delete(smail);
	}
}
