package com.training.teamchat;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

public class reminder extends TimerTask {
	String rcp,msg;
	 public static final String ACCOUNT_SID = "ACcf2e0860510b6e16528f5cdd60617bea"; 
	 public static final String AUTH_TOKEN = "71ba015af762a2e84c6ce2b936c61b5a"; 
	
	
	  public reminder (String rcp,String msg)
	  {
		  this.rcp=rcp;
		  this.msg=msg;
		  
	  }
	@Override
	public void run() {
		// TODO Auto-generated method stub
		TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN); 
		 List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		 params.add(new BasicNameValuePair("To", rcp)); 
		 params.add(new BasicNameValuePair("From", "+12035990924")); 
		 params.add(new BasicNameValuePair("Body", msg));   
	 
		 MessageFactory messageFactory = client.getAccount().getMessageFactory(); 
		 Message message = null;
		try {
			message = messageFactory.create(params);
		} catch (TwilioRestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 System.out.println(message.getSid()); 
		
	}

}
