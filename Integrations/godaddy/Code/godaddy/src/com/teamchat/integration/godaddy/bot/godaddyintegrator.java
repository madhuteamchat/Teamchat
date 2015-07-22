package com.teamchat.integration.godaddy.bot;
/*
 * *
 * @author:Anuj Arora
 */
import java.io.IOException;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class godaddyintegrator {
	//getting the availability from the godaddy server
public String getavailability(String domain) throws IOException
{
	    String err = "Error";
		OkHttpClient client = new OkHttpClient();
    //"sso-key {API_KEY}{API_SECRET}"
	
	String API_KEY = "dKDJb9vzBUEt_Avsbi4v2hf96VqAgiD8ADo";
	
	String API_SECRET = "DuTSUzdf3NAFLqK8XHske7";
	
	Request request = new Request.Builder()
	  .url("https://api.godaddy.com/v1/domains/available?domain="+domain.trim())
	  .get()
	  .addHeader("authorization", "sso-key "+API_KEY+":"+API_SECRET)
	  .build();

	Response response = client.newCall(request).execute();
		
	 if (!response.isSuccessful())
	 {
		 return err;
	 }
	 else
	return response.body().string();
	
	}

}
