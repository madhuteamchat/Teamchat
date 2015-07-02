package com.teamchat.integrations.twiliobot;

import java.awt.List;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.instance.Call;

public class Callme {
	
	// Find your Account Sid and Token at twilio.com/user/account 
	 //public static final String ACCOUNT_SID = "ACcf2e0860510b6e16528f5cdd60617bea"; 
	 
	 
	 public static void make( String AccountSid,String to,String from,String AuthToken,String ApplicationSid) throws TwilioRestException { 
			TwilioRestClient client = new TwilioRestClient(AccountSid, AuthToken); 
		 
			// Build the parameters 
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(); 
			params.add(new BasicNameValuePair("To", to)); 
			params.add(new BasicNameValuePair("From", from));  
			params.add(new BasicNameValuePair("ApplicationSid",ApplicationSid)); 
			params.add(new BasicNameValuePair("Method", "GET"));  
			params.add(new BasicNameValuePair("FallbackMethod", "GET"));  
			params.add(new BasicNameValuePair("StatusCallbackMethod", "GET"));    
			params.add(new BasicNameValuePair("Record", "false")); 
		 
			CallFactory callFactory = client.getAccount().getCallFactory(); 
			Call call = callFactory.create(params); 
			System.out.println(call.getSid()); 
		 } 

}
