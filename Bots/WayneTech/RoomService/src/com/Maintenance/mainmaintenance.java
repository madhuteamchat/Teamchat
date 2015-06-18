package com.Maintenance;



import java.util.*;
import java.util.Map.Entry;

import com.tc.sol.server.util.sms.SMSUtil; //For sending SMS
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PollChatlet;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

	public class mainmaintenance
	{
		public static final String bot = "himanshu.rathee@st.niituniversity.in";// bot email name
		public static final String password = "p@$$word4";// botpassword
		public static final String admin = "rathee.coolhimanshu@gmail.com";//admin email
		public static final String electrician = "9671847176", plumber = "8828090019", carpenter = "9680000880", cleaner = "9671847176";// Numbers
		
		
		
		public int secretcode(){																																// of
			Random code=new Random();
		int value =0;
		value=code.nextInt(9999);
		return value;
		}
		public int secret=secretcode();
		@OnKeyword("maintenance")
		// displays form for filling complaint
		public void onroomservice(TeamchatAPI api) throws Exception
		{
			
			Form f = api.objects().form();
			f.addField(api.objects().select().name("nameofworker").label("Person to contact").addOption("Electrician").addOption("Plumber").addOption("Carpenter").addOption("Cleaner"));
			f.addField(api.objects().input().name("name").label("Your Name"));
			f.addField(api.objects().input().name("roomno").label("Your Room no."));
			f.addField(api.objects().input().name("problem").label("Description of Problem (not more than 100 words)"));
			//Room x = api.context().create().setName("Service Request").add(admin).add(api.context().currentReply().senderEmail());
			api.perform(api.context().currentRoom().post(new PrimaryChatlet()
			.setQuestionHtml("<font color='Blue'><h2><b>WELCOME TO MAINTAINTIA</b></h2></font>")));
			
			api.perform(api.context().currentRoom().post(new PrimaryChatlet()
			.setQuestion("**Need Maintenance.You are at the right place.Fill the form given.**")
			.setReplyScreen(f).setReplyLabel("CLICK").alias("post")));
		}

		@OnAlias("post")
		// Displays number of the worker whom you want to contact
		public void onpost(TeamchatAPI api) throws Exception
		{
			SendingSMS send=new SendingSMS();
			System.out.println("\n HERE IS THE CODEEEEEEEE"+secret);
			String nameofworker = api.context().currentReply().getField("nameofworker");
			String name = api.context().currentReply().getField("name");
			String roomno = api.context().currentReply().getField("roomno");
			String problem = api.context().currentReply().getField("problem");
			
			if (nameofworker.equalsIgnoreCase("Electrician"))
			{
		send.sendmessage(electrician,name,roomno,problem,secret);
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestion("Your request is filed.Please wait for sometime or else contact our electricain on  " + electrician+ ".\n You will get a secret code from the worker.Please use that to confirm whether you are satisfied with the work or not. ")));
				String image="<html><body><img src=\"";
				String url="http://previews.123rf.com/images/limbi007/limbi0071210/limbi007121000045/15800957-Orange-cartoon-character-as-electrician-with-toolbox-and-house-White-background--Stock-Photo.jpg\"";
				String size="style=\"";
				String width="width:204px;height:128px;>\"";
				String end="</body></html>";
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml(image + url + size  + width + " s the pic" + end)));
			} else if (nameofworker.equalsIgnoreCase("Plumber"))
			{
				//send.sendmessage(plumber,name,roomno,problem,secret);
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestion("Your request is filed.Please wait for sometime or else contact our plumber on  " + plumber + ".\n You will get a secret code from the worker.Please use that to confirm whether you are satisfied with the work or not. ")));
				String image="<html><body><img src=\"";
				String url="http://images.fineartamerica.com/images-medium-large-5/plumber-repair-faucet-tap-cartoon-aloysius-patrimonio.jpg\"";
				String size="style=\"";
				String width="width:204px;height:128px;>\"";
				String end="</body></html>";
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml(image + url + size  + width + " s the pic" + end)));
			} else if (nameofworker.equalsIgnoreCase("Carpenter"))
			{
				//send.sendmessage(carpenter,name,roomno,problem,secret);
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestion("Your request is filed.Please wait for sometime or else contact our carpenter on  " + carpenter + ".\n You will get a secret code from the worker.Please use that to confirm whether you are satisfied with the work or not. ")));
				String image="<html><body><img src=\"";
				String url="http://www.puzzlepuzzles.com/imatjes/bob-worked-as-a-carpenter_4b67023f8c857-p.jpg\"";
				String size="style=\"";
				String width="width:204px;height:128px;>\"";
				String end="</body></html>";
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml(image + url + size  + width + " s the pic" + end)));
			} else if (nameofworker.equalsIgnoreCase("Cleaner"))
			{
				//send.sendmessage(cleaner,name,roomno,problem,secret);
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestion("Your request is filed.Please wait for sometime or else contact our cleaner on  " + cleaner+ ".\n You will get a secret code from the worker.Please use that to confirm whether you are satisfied with the work or not. ")));
				String image="<html><body><img src=\"";
				String url="http://thumbs.dreamstime.com/z/cartoon-cleaner-16133033.jpg\"";
				String size="style=\"";
				String width="width:204px;height:128px;>\"";
				String end="</body></html>";
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml(image + url + size  + width + " s the pic" + end)));
			}
			Form g = api.objects().form();
			g.addField(api.objects().input().name("code").label("Enter the secret code"));
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestion("**Enter the secret code given to you by our worker OR Enter Secret code=696969 for filing complaint**").setReplyScreen(g).setReplyLabel("CLICK").alias("code")));
		}

		@OnAlias("code")
		// Asks for acknowledgment if work is done or not
		public void oncode(TeamchatAPI api) throws Exception
		{
			System.out.println("\n Code is here also"+secret);
			String code = api.context().currentReply().getField("code");
			int value=Integer.parseInt(code);
			if(value==696969){
				Form g = api.objects().form();
				g.addField(api.objects().input().name("namec").label("Your Name"));
				g.addField(api.objects().input().name("msg").label("File your complaint"));
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestion("**File your complaint**").setReplyScreen(g).setReplyLabel("CLICK")));
			}
			else if (secret==value)
			{
				api.perform(api.context().currentRoom().post(new PollChatlet()
				.setQuestion("The code matches.PLEASE REVIEW THE WORK DONE").setQuestion("Are you satisfied with the work")));
			}
			else
			{
				Form g = api.objects().form();
				g.addField(api.objects().input().name("roomid").label("Enter the secret code"));
				api.perform(api.context().currentRoom()
						.post(new PrimaryChatlet().setQuestion("Incorret code.").setQuestion("Enter the code again")
								.setReplyScreen(g).setReplyLabel("CLICK").alias("code")));
			}
		}

		public static void main(String[] args)// main function
		{
			TeamchatAPI api = TeamchatAPI.fromFile("teamchat2.data").setEmail(bot).setPassword(password).startReceivingEvents(new mainmaintenance());
		}
		}
	


