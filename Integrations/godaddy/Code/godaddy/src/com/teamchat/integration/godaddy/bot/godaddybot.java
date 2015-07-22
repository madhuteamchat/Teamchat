package com.teamchat.integration.godaddy.bot;
/*
 * *
 * @author:Anuj Arora
 */
import java.io.IOException;

import com.google.gson.Gson;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.integration.godaddy.classes.Godaddymain;

public class godaddybot {

	@OnKeyword("help")
	public void help(TeamchatAPI api) {

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<h4><b>Hi, I'm Godaddy Bot.</b></h4>"
								+ "<img src=\"https://brettanthonymusic.com/wp-content/uploads/WSvLIGB.png\" height=\"90\" width=\"230\" />"
								+ "<br />"
								+ "<br /><b>You can use me to search any domain i.e if it is available or not, using following keyword:</b>"
								+ "<br />"
								+ "<ul type=\"square\"; style=\"color:#359FD8\"; ><li><a1 style=\"color:black\";><b>search - </b></a1><a2 style=\"color:#359FD8\"; align=\"justify\";>"
								+ "Type this command to search any domain"
								+ "</a2></li></ul>")));

		// list of keywords
		// 1.)search
	

	}
	
	//Use this keyword to search any domain
		@OnKeyword("search")
		public void login(TeamchatAPI api) {
	
			Form f = api.objects().form();
			f.addField(api.objects().input().label("Domain name to search for:").name("domain"));
			
			PrimaryChatlet prime = new PrimaryChatlet();
			prime.setQuestionHtml(
					"<h2><b>It's Go Time</b></h2>"
					+ "<img src='http://www.aussiecoupons.com/images/merchants/godaddy.png'/>"
					+ "<br />"
					+ "<h5><b>Enter any domain name you want to search for in the reply option.</b><h5>"
					+ "<br /><b>Note: </b><a1  style=\"color:#359FD8\";>Field is required.</a1>")
					.setReplyScreen(f).setReplyLabel("Reply").alias("getdata");
			api.perform(api.context().currentRoom().post(prime));
		}
		
		@OnAlias("getdata")
		public void getdata(TeamchatAPI api) throws IOException {
			String check="";
			String color="";
			String domain = api.context().currentReply().getField("domain");
			godaddyintegrator ob1 = new godaddyintegrator();
			String resp = ob1.getavailability(domain);
		
			if (resp.equals("Error")) {
				PrimaryChatlet prime = new PrimaryChatlet();
				api.perform(api
						.context()
						.currentRoom()
						.post(prime.setQuestionHtml("<br /><b>Error :</b>"
								+ "<br /><b>Status Code: 403</b>"
								+ "<br /><b>StatusDesc: Forbidden</b>"
								+ "<br /><b>Something went wrong!</b>")));

			}
			else
			{
				// parsing a json like this
//*************************************************				
//				    {
//					  "price": 39990000,
//					  "currency": "USD",
//					  "period": 1,
//					  "available": false,
//					  "domain": "example.guru"
//					}
//*************************************************
				Gson gson = new Gson();
				//main class for getters and setters : Godaddymain
				Godaddymain data = gson.fromJson(resp, Godaddymain.class);
				
				if(data.getAvailable()==true)
				{
					check = "Available";
					color = "#3CD00A";
				}
				else
				{
				    check = "Unavailable";
				    color = "#A80000";
				}
				PrimaryChatlet prime1 = new PrimaryChatlet();
				prime1.setQuestionHtml("<h3><b>Domain Details: </b>"+"<b style=\"color:#359FD8\";>"+data.getDomain()+"</b></h3>"
						+ "<ul type=\"square\"; style=\"color:#359FD8\";><li><a1 style=\"color:black\";><b>Availability - </b></a1><a2 style=\"color:"+color+"\";>"
						+ check
						+ "</a2></li><li><a7 style=\"color:black\";><b>Price - </b></a7><a8 style=\"color:#359FD8\";>"
						+ data.getPrice()
						+ "</a8></li><li><a9 style=\"color:black\";><b>Currency - </b></a9><a10 style=\"color:#359FD8\";>"
						+ data.getCurrency() + "</a10></li>"
						+"<li><a9 style=\"color:black\";><b>Period - </b></a9><a10 style=\"color:#359FD8\";>"
						+ data.getPeriod() + " years</a10></li>"
						+ "</ul>");
				
				api.perform(api.context().currentRoom().post(prime1));
			}
		}
		
}
