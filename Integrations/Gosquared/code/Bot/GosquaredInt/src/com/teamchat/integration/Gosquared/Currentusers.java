package com.teamchat.integration.Gosquared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import com.teamchat.client.sdk.TeamchatAPI;

public class Currentusers {

	public   int now11 (TeamchatAPI api,String apikey1,String stoken) throws IOException
	{
	
	String result="";
	URL urldemo = new URL("https://api.gosquared.com/now/v3/concurrents?api_key="+apikey1+"&site_token="+stoken); 
    URLConnection yc; 
    yc = urldemo.openConnection(); //AK65YLGDU2HMH6GY&site_token=GSN-621790-C
    BufferedReader in = new BufferedReader(new InputStreamReader( yc.getInputStream())); 
    String inputline;
	while ((inputline = in.readLine()) != null) 
	{
		result+=inputline;

        System.out.println(inputline+"\n");
	}
    in.close(); 
    JSONObject jobj2 = new JSONObject(result);
    int visitor= jobj2.getInt("visitors");
	return visitor;
	
	
	
	
}
	
}
	

