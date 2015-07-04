package com.teamchat.integration.mailchimp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class Campaign {

	// public static String apikey="661bf9ef632710f29ecebf9cb0cc07a2-us11";
	// public static String cid="492f50d68c";

	public String cid = null;
	public String at;
	public String us;
	
	public   int  length ;
	String[] id;
	String[] title;
	int open[];
	int click[];
	int hardbounce[];
	int softbounce[];
	int bounce[];
	int unsubscriber[];
	int abousereport[];
	int emailsent[];
	
	

	public void campaignStats(TeamchatAPI api, String apikey, String us1,String cid1) {
		// TODO Auto-generated method stub
		at = apikey;
		us = us1;
		cid = cid1;
		System.out.println(at);
		System.out.println(us);
		System.out.println(cid);

		String result = "";
		URL urldemo;
		try {
			urldemo = new URL("https://" + us
					+ ".api.mailchimp.com/1.3/?method=campaignStats&apikey="
					+ at + "&cid=" + cid);

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

			JSONObject jaobj = new JSONObject(result);
			System.out
					.println("Syntax error =" + jaobj.getInt("syntax_errors"));

			// System.out.println(jaobj.getString("key"));
			// System.out.println(jaobj.getInt("value"));

			//
			int sbounce = jaobj.getInt("soft_bounces");
			int hbounce = jaobj.getInt("hard_bounces");
			int tbounce = (sbounce + hbounce);
			int forwards_opens = jaobj.getInt("forwards_opens");
			int opens = jaobj.getInt("opens");
			int topens = (forwards_opens + opens);
			int clk = jaobj.getInt("clicks");
			int emailsent = jaobj.getInt("emails_sent");

			int unsub = jaobj.getInt("unsubscribes");
			int abouse = jaobj.getInt("abuse_reports");
			System.out.println("Total Bounce =" + (sbounce + hbounce));
			System.out.println("Unsubscribes =" + jaobj.getInt("unsubscribes"));
			// System.out.println("Number of times the campaign was opened= "+(forwards_opens+opens));
			//
			// System.out.println("Number of times a link in the campaign was clicked ="+jaobj.getInt("clicks"));
			// System.out.println("Number of facebook likes = "+
			// jaobj.getInt("facebook_likes"));
			// System.out.println("Number of email addresses campaign was sent to ="+jaobj.getInt("emails_sent")
			// );
			//
			//
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
						.setQuestionHtml("<align =left>"	
							+"Number of Opens ="
									+ opens
									+ "<br> Number of Clicks ="
									+ clk
									+ "<br>Number of  Emailsent ="
									+ emailsent
									+ "<br>Number of Total Bounce ="
									+ tbounce
									+ "<br>Number of Unsubscriber ="
									+ unsub
									+ "<br>Number of email addresses in campaign that reported campaign for Abuse ="
									+ abouse
			
			
			
			
		//	+<div style=\"overflow:scroll;overflow-y:scroll;overflow-x:scroll;\">   <img style=\"-webkit-user-select: none\" src=\"http://chart.apis.google.com/chart?cht=p3&amp;chs=450x200&amp;chd=t:"+val+"&amp;chco=80C65A,224499,FF0000&amp;chl="+val1+"\"></div>")
			
			
			
			
			
			
			
			
		                      	+"<div style=\"overflow:scroll;overflow-y:scroll;overflow-x:scroll;\">  <img  style=\"-webkit-user-select:none\" src=\"http://chart.apis.google.com/chart?cht=p3&chs=450x170&chd=t:"
									+ opens
									+ ","
									+ clk
									+ ","
									+ emailsent
									+ ","
									+ tbounce
									+ ","
									+ unsub
									+ ","
									+ abouse+"&amp;chco=B8860B,80C65A,008B8B,224499,006400,FF0000&chl=Opens|Clicks|emailsent|TotalBounce|Unsubscriber|AbouseReport\"></div>")));			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();

			JSONObject jaobj = new JSONObject(result);

			String error = jaobj.getString("error");
			System.out.println(error);
			
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
						.setQuestionHtml(	"<h5 style=\"color:#F80000 ;\">You have not selected any Campaign</h5")));
			 
		}
	}

	public void ncampaignStats(TeamchatAPI api, String apikey, String us1) {
		// TODO Auto-generated method stub
		at = apikey;
		us = us1;
		
		System.out.println("length of total is"+length);
		//cid = nid;

		System.out.println(us);
		System.out.println(at);
	//	System.out.println(cid);

		String result = "";
		URL urldemo;
		try {
			urldemo = new URL("https://"+us+".api.mailchimp.com/2.0/campaigns/list/?apikey="+at); 

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
			System.out.println(result);
			JSONObject jaobj = new JSONObject(result);
			System.out.println(result);
			JSONArray a = jaobj.getJSONArray("data");
			
			String len = jaobj.get("total").toString();
			
			 length = Integer.parseInt(len);
			id=new String[length];
			title=new String[length];
			open=new int[length];
			click=new int[length];
			 hardbounce=new int[length] ;
			 softbounce=new int [length];
			 bounce=new int[length];
			 unsubscriber =new int[length];
			abousereport= new int[length];
			 emailsent=new int[length];
			
			for (int i = 0; i<length; i++){
				JSONObject temp = a.getJSONObject(i);
				
				String temp1 = temp.getString("id");
				
				id[i] = temp1;
				
				String temp2 = temp.getString("title");
				title[i] = temp2;
			
				JSONObject sum = (JSONObject) temp.get("summary");
					
			String temp3=sum.get("opens").toString();
			System.out.println("hello");
				open[i]=Integer.parseInt(temp3);
				String temp4=sum.get("clicks").toString();
				click[i]=Integer.parseInt(temp4);
				
				String temp5=sum.get("emails_sent").toString();
				emailsent[i]=Integer.parseInt(temp5);
				
				String temp6=sum.get("abuse_reports").toString();
				abousereport[i]=Integer.parseInt(temp6);
				
				String temp7=sum.get("unsubscribes").toString();
				unsubscriber[i]=Integer.parseInt(temp7);
				
				String temp8=sum.get("hard_bounces").toString();
						hardbounce[i]=Integer.parseInt(temp8);
				
				
				String temp9=sum.get("soft_bounces").toString();
				softbounce[i]=Integer.parseInt(temp9);
				
			
			}
			
			MailchimpBot.title=title;
			MailchimpBot.campid=id;
			
			Webhook.campid=id;
			Webhook.campname=title;
			for(int i=0;i<length;i++)
			{
				System.out.println(" id ="+id[i]);
				System.out.println("title ="+title[i]);
				System.out.println("open ="+open[i]);
				System.out.println("click ="+click[i]);
				System.out.println("abousereport ="+abousereport[i]);
				System.out.println("unsubscriber ="+unsubscriber[i]);
				System.out.println("emailsent ="+emailsent[i]);
				System.out.println("softbounce ="+softbounce[i]);
				System.out.println(" hardbounce ="+hardbounce[i]);
				
			}
			
			
		}
		catch (Exception e) {
			
			 System.out.println(e);
	            
	            
	           api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml(	"<h5 style=\"color:#F80000 ;\">Please select Anyone Campaign name</h5") ));
			
		}
			
			

	}

}


