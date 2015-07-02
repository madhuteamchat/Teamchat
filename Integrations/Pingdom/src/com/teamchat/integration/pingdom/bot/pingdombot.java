package com.teamchat.integration.pingdom.bot;


import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.integration.pingdom.classes.Check;
import com.teamchat.integration.pingdom.classes.Checks;



public class pingdombot {

	String Username,App_key,roomId,url;
	String Password;
	@OnKeyword("help")
	public void help(TeamchatAPI api) {

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<br /><b>Hi, I'm Pingdom Bot.</b>"
								+ "<br />I'll help you to interact with your friends or relatives with the help of video chat."
								+ "<br />Write a command <a><b>\"Room\"</b></a> for extracting the link."
								+ "<br /> After that just click on the image and it will redirect you to a page, Where you will find a Start video chat button."
								+ "<br />Clicking on it will redirect you to a page Where you will find a link , Copy that link and paste it in the reply with the Email Id of the person you want to interact with.")));
	
	}
	@OnKeyword("pingdom")
	public void login(TeamchatAPI api) {
		
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Username").name("Username"));
		f.addField(api.objects().input().label("Password").name("Password"));
		//f.addField(api.objects().input().label("App_key").name("App_key"));
		
		PrimaryChatlet prime = new PrimaryChatlet();
		prime.setQuestionHtml("<img src=\"http://monitorama.com/2014/images/logo_pingdom.png\" height=\"90\" width=\"230\">"+"<br /><b>Enter your login credentials :</b>").setReplyScreen(f)
				.setReplyLabel("Enter").alias("checkdata");

		api.perform(api.context().currentRoom().post(prime));
	}
	
	@OnAlias("checkdata")
	public void checkdata(TeamchatAPI api) {
	
		String Username = api.context().currentReply().getField("Username");
		String Password = api.context().currentReply().getField("Password");
     
		DBHandler ob1 = new DBHandler();
		
		boolean flag;
		
		flag = ob1.dchk(Username);
     	
		if(flag==false)
		{
			
			this.Username = Username;
			this.Password = Password;
			
			Form f = api.objects().form();
			f.addField(api.objects().input().label("App_key").name("App_key"));
			
			PrimaryChatlet prime = new PrimaryChatlet();
			prime.setQuestionHtml("<br /><b>Enter App-Key of your application :</b>").setReplyScreen(f)
					.setReplyLabel("Enter").alias("addkey");

			api.perform(api.context().currentRoom().post(prime));
			
		}
		
		else{
			DBHandler ob3 = new DBHandler();
			String App_key;
			App_key= ob3.getapp(Username);
			roomId = api.context().currentRoom().getId();
			
			this.App_key = App_key;
		
			
			ob3.setData(Username,Password,this.App_key,roomId);
			
			PrimaryChatlet prime = new PrimaryChatlet();
			api.perform(api.context().currentRoom().post(prime.setQuestionHtml("<br /><b>------Login Credentials Ok!!---------</b>"
					+ "<br /><b>Now type \"host\" for doing further operations</b>")));
		}
		
	}
	
	@OnAlias("addkey")
	public void addkey(TeamchatAPI api) {
	
		String App_key = api.context().currentReply().getField("App_key");
		
		roomId = api.context().currentRoom().getId();
		
		this.App_key = App_key;
		DBHandler ob1 = new DBHandler();
		
		ob1.setData(Username,Password,this.App_key,roomId);
		
		
	}
	
	@OnKeyword("host")
	public void host(TeamchatAPI api) {
		Form fo=api.objects().form();
		
		fo.addField(api.objects().input().name("Hostname").label("Hostname"));
		
		fo.addField(
								api.objects().select().name("perform").label("What you want to perform?")
										.addOption("Check"));
					
				
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet().setQuestionHtml("<br /><b>Enter Details :</b>")
								.setReplyScreen(fo)
								.setReplyLabel("Enter").alias("sendreq")));
				
	
		
	}
	
	@OnAlias("sendreq")
	public void sendreq(TeamchatAPI api) {
		
		String Hostname = api.context().currentReply().getField("Hostname");
		String perform = api.context().currentReply().getField("perform");
		
		System.out.println(Hostname+"  "+perform+"  "+App_key);
		
		if(perform.equals("Check"))
		{
			url = "https://api.pingdom.com/api/2.0/checks";
			
			pingdomintegrator ob1 = new pingdomintegrator();
			Check[] checks = ob1.parse(ob1.getChecks(Username, Password, App_key, url).toString());
			for (Check check : checks) {
				System.out.println(check.getHostname());
			
		}
		
		
		
	
		}
	}
	
}
