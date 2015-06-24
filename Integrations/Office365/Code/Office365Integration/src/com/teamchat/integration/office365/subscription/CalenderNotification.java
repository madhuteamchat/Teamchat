package com.teamchat.integration.office365.subscription;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.teamchat.integration.office365.database.JDBCConnection;
import com.teamchat.integration.office365.oauth.RenewAccessToken;

public class CalenderNotification {

	public String notifyCreate(String url,String teamchat_id) {
		String message="";
		try {
			JDBCConnection jdb=new JDBCConnection();
			String access_token=jdb.retreiveAccessToken(teamchat_id);
//			URL urldemo = new URL(url);
//		    HttpURLConnection officeapi = (HttpURLConnection)urldemo.openConnection();
//		    officeapi.setRequestProperty ("Authorization", "Bearer "+access_token);
//		    officeapi.setRequestMethod("GET");
//		    int statuscode=officeapi.getResponseCode();
			HttpClient client = new DefaultHttpClient();
			HttpGet hget = new HttpGet(url);
			hget.addHeader("Authorization","Bearer "+access_token);
			HttpResponse response = client.execute(hget);
		    if(response.getStatusLine().getStatusCode()==401) {
		    	//access token need to be renewed
		    	String refresh_token=jdb.retreiveRefreshToken(teamchat_id);
				if(new RenewAccessToken().getNewAccessToken(teamchat_id,refresh_token)==200)
				{
					message=notifyCreate(url, teamchat_id);
				}
				else
				{
					message="You have received calendar notifications. Please log into your Office365 account to view your notifications.";
					
				}
		    	System.out.println("Auth failure");
		    	/*call renew token function*/
		    }
		    else {
		    	String result=new HttpConnection().getBodyOfResponse(response);
		    	message=new ParseJSONCalendar().createNotification(result);
		    } 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
	
}
