package com.teamchat.integration.office365.oauth;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.teamchat.integration.office365.webhook.TeamchatPost;

public class UserInfo {
	

	public String getUserID(String teamchat_id,String access_token)
	{
		String office365_id="null";
		try
		{
			HttpGet hget=new HttpGet("https://outlook.office365.com/api/beta/me");
			hget.addHeader("Authorization","Bearer "+access_token);
			hget.setHeader("Content-Type","application/json");
		    HttpClient httpclient=new DefaultHttpClient();
		    HttpResponse response =httpclient.execute(hget);
		    System.out.println(response.getStatusLine());
		    if(response.getStatusLine().getStatusCode()==200)
		    {
		    	BufferedReader in = new BufferedReader(new
		    			InputStreamReader(response.getEntity().getContent()));
		    			StringBuffer sb = new StringBuffer("");
		    			String line = "",js="";
		    			while ((line = in.readLine()) != null) {
		    				sb.append(line);
		    				js+=line;
		    			}
		    			in.close();
		    			System.out.println(sb);
		    			JSONObject jobj=new JSONObject(js);
		    			office365_id=jobj.getString("Id");
		    			String msg="Logged in as <strong>"+jobj.getString("DisplayName")+""
		    					+ " ("+office365_id+")</strong>";
		    			System.out.println(msg);
		    			new TeamchatPost().postMsg(msg, teamchat_id);
		    }
		}	
		catch(Exception e)
		{
			e.printStackTrace();
			return "error";
		}

		return office365_id;
	}
	

}
