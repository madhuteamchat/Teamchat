package com.teamchat.integration.mailchimp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class Listm {

	public String listid = null;
	public String at;
	public String us;
	
	public String  semail;

	public static String[] id;

	public int length;
	public int memb = 0;
	public int Unsub = 0;
	public int avgsubs = 0;
	public int avgunsubs = 0;

	// public String[] id;
	public static String[] name;

	public static int[] membercount;
	public static int[] unsubscrib;
	public static int[] avgsub;
	public static int[] avgunsub;

	

	public void nlistGrowthHistory(TeamchatAPI api, String apikey, String us1) {
		at = apikey;
		us = us1;
		

		String result = "";
		URL urldemo;
		try {
			urldemo = new URL("https://" + us
					+ ".api.mailchimp.com/2.0/lists/list/?apikey=" + at);

			URLConnection yc;
			yc = urldemo.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputline;
			while ((inputline = in.readLine()) != null) {
				result += inputline;

				System.out.println(inputline + "\n");
			}
			in.close();
			// System.out.println(result);
			JSONObject jaobj = new JSONObject(result);
			System.out.println(jaobj.toString());
			length = jaobj.getInt("total");

			JSONArray a = jaobj.getJSONArray("data");

			id = new String[length];
			name = new String[length];
			membercount = new int[length];
			unsubscrib = new int[length];
			avgsub = new int[length];
			avgunsub = new int[length];
			System.out.println("length is : " + length);
			for (int i = 0; i < length; i++) {
				JSONObject temp = a.getJSONObject(i);
				System.out.println("from for loop" + temp.toString());

				String temp1 = temp.getString("id");
				System.out.println(temp1);
				id[i] = temp1;

				String temp2 = temp.getString("name");
				name[i] = temp2;
				System.out.println(temp2);
				JSONObject stats = temp.getJSONObject("stats");
				System.out.println(stats.toString());
				System.out.println("hi Ajit Shukla");
				String temp3 = stats.get("member_count").toString();
				membercount[i] = Integer.parseInt(temp3);
				String temp4 = stats.get("unsubscribe_count").toString();
				unsubscrib[i] = Integer.parseInt(temp4);

				String temp5 = stats.get("avg_sub_rate").toString();
				avgsub[i] = Integer.parseInt(temp5);
				String temp6 = stats.get("avg_unsub_rate").toString();
				avgunsub[i] = Integer.parseInt(temp6);
			}
            for(int i=0;i<name.length;i++)
            {
            	System.out.println(name[i]);
            }
			MailchimpBot.name = name;
			
			MailchimpBot.lid = id;
			Webhook.name=name;
			Webhook.id=id;
			
			for (int i = 0; i < length; i++) {
				System.out.println(" id =" + id[i]);
				System.out.println("name =" + name[i]);
				System.out.println(" membercount=" + membercount[i]);
				System.out.println(" unsubscriber=" + unsubscrib[i]);
				System.out.println("avag sub =" + avgsub[i]);
				System.out.println(" avgunsubscriber =" + avgunsub[i]);

			}

		} catch (Exception e) {
			JSONObject jaobj = new JSONObject(result);

		}

	}

	public void listrepotrs(TeamchatAPI api, String id1) {
		try {
			listid = id1;

			System.out.println("List id =" + listid);
			for (int i = 0; i < id.length; i++) {

				if (listid.equals(id[i])) { // System.out.println("inside the loop");
					memb = membercount[i];
					System.out.println("inside the loop");
					Unsub = unsubscrib[i];
					avgsubs = avgsub[i];
					avgunsubs = avgunsub[i];
				}
			}

			System.out.println("MemberCount =" + memb);
			System.out.println("Unsubscriber =" + Unsub);
			System.out.println("Avasub =" + avgsubs);
			System.out.println("avgUnsubscriber" + avgunsubs);
			
			if(avgsubs==0&&avgunsubs==0)
			{
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
						.setQuestionHtml(	"MemberCount =" + memb
							+ "<br>Unsubscriber =" + Unsub
							+ "<br/>AverageSubscriber-Rate =" + avgsubs
							+ "<br/>AvarageUnsubscriber-Rate =" + avgunsubs
							
							))

			);
			}
			else if(avgsubs==0)
			{
				System.out.println("im in aggunsubs chart");
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet()
						.setQuestionHtml(	"MemberCount =" + memb
								+ "<br>Unsubscriber =" + Unsub
								+ "<br/>AverageSubscriber-Rate =" + avgsubs
								+ "<br/>AvarageUnsubscriber-Rate =" + avgunsubs
																+"<br/>Avg Subscriber-rate vs Avg Unsubscriber-rate"
								+"<div style=\"overflow:scroll;overflow-y:scroll;overflow-x:scroll;\"> <img style=\"-webkit-user-select:none\" src=\"http://chart.apis.google.com/chart?cht=p3&chs=450x170&chd=t:"
								+ avgunsubs
								+"&chco=80C65A&chl=Unsubscriber-rate \"></div>"
								))

				);
				
				
			}
			
			else if(avgunsubs==0)
				
			{
				System.out.println("im in avgsubs chart");
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet()
						.setQuestionHtml(	"MemberCount =" + memb
								+ "<br>Unsubscriber =" + Unsub
								+ "<br/>AverageSubscriber-Rate =" + avgsubs
								+ "<br/>AvarageUnsubscriber-Rate =" + avgunsubs
								
						+"<br/><br/>Avg Subscriber-rate vs Avg Unsubscriber-rate"								+"<div style=\"overflow:scroll;overflow-y:scroll;overflow-x:scroll;\"> <img style=\"-webkit-user-select:none\" src=\"http://chart.apis.google.com/chart?cht=p3&chs=450x170&chd=t:"
								+ avgsubs
								+"&chco=B8860B&chl=Subscriber-rate \"></div>"
								))
								
								

				);
				
				
				
			}
			else
			{
				
				System.out.println("In both rate");
				
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet()
						.setQuestionHtml(	"MemberCount =" + memb
								+ "<br>Unsubscriber =" + Unsub
								+ "<br/>AverageSubscriber-Rate =" + avgsubs
								+ "<br/>AvarageUnsubscriber-Rate =" + avgunsubs
						+"<br/>Avg Subscriber-rate vs Avg Unsubscriber-rate"
						+"<div style=\"overflow:scroll;overflow-y:scroll;overflow-x:scroll;\">  <img  style=\"-webkit-user-select:none\" src=\"http://chart.apis.google.com/chart?cht=p3&chs=450x170&chd=t:"
						+ avgsubs
						+ ","
						
						+avgunsubs+"&amp;chco=006400,FF0000&chl=Avgsubscriber|AvgUnsubscriber\"></div>")));			
								
				
			}
			
		} catch (Exception e) {
			System.out.println(e);

			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
						.setQuestionHtml(	
							"<h5 style=\"color:#F80000 ;\">You have not selected any list name</h5>")));
		}
	}
	
	
	public void webhookadd(TeamchatAPI api,String acctoken,String us1,String smail,String tempid)
	{
		at = acctoken;
		us = us1;
		listid=tempid;
		semail=smail;
		
		String result = "";
		URL urldemo;
		try {
			urldemo = new URL("https://" + us
					+ ".api.mailchimp.com/2.0/lists/webhook-add/?apikey=" + at+"&id="+listid+"&url="+PropertiesFile.getValue("webhook_url")+"?email="+semail);

			URLConnection yc;
			yc = urldemo.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputline;
			while ((inputline = in.readLine()) != null) {
				result += inputline;

				System.out.println(inputline + "\n");
			}
			in.close();
			
			System.out.println("RETURN CONTENT OF WEBHOOK= "+result);
			
			JSONObject jaobj = new JSONObject(result);
			  String idwebhook=jaobj.getString("id");
			  
			  System.out.println("New WebhookId ="+idwebhook);
		
		
	}
		 catch (Exception e) {
				System.out.println(e);
				
				
				
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet()
							.setQuestionHtml(	
								"<h5 style=\"color:#F80000 ;\">Either You have not selected any list name OR<br/>This list is already added in your Webhook </h5>")));
			}
		

         }
	
}
