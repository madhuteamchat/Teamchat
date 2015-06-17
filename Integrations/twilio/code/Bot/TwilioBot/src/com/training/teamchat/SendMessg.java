package com.training.teamchat;
import java.util.*; 

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.DeserializerFactory.Config;

import com.twilio.sdk.*; 
import com.twilio.sdk.resource.factory.*; 
import com.twilio.sdk.resource.instance.*; 
import com.twilio.sdk.resource.list.*; 
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.ReportChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;
import com.teamchat.client.sdk.chatlets.PollChatlet;
	
	 
	public  class SendMessg { 
	 // Find your Account Sid and Token at twilio.com/user/account 
	 public static final String ACCOUNT_SID = "ACcf2e0860510b6e16528f5cdd60617bea"; 
	 public static final String AUTH_TOKEN = "71ba015af762a2e84c6ce2b936c61b5a"; 
	 TeamchatAPI api2;
	 public int value(String to ,String msg) throws TwilioRestException { 
		TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN); 
 //Object gh = client.getAccount(ACCOUNT_SID);
    //System.out.println(gh);
		 // Build the parameters 
		 List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		 params.add(new BasicNameValuePair("To", to)); 
		 params.add(new BasicNameValuePair("From", "+12035990924")); 
		 params.add(new BasicNameValuePair("Body", msg));   
	 
		 MessageFactory messageFactory = client.getAccount().getMessageFactory(); 
		 Message message = messageFactory.create(params); 
		 String receive = message.getSid();
		 System.out.println(message.getSid());
		 if(receive.equalsIgnoreCase(null))
		 {
				return 0;			
		 }
		 else
		 {
			
			 return 1;
		 }
		
		 
	 } 
	 
	}


