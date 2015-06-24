package com.teamchat.integration.office365.webhook;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.teamchat.integration.office365.database.JDBCConnection;
import com.teamchat.integration.office365.database.Office365Subscribe;
import com.teamchat.integration.office365.oauth.RenewAccessToken;

public class RenewWebhook {

	public void renewSubscription(String teamchat_id,String sub_id)
	{
		JDBCConnection jdb=new JDBCConnection();
		String access_token=jdb.retreiveAccessToken(teamchat_id);
		if(access_token.equals("access_token"))
		{
			String msg="You have to login";
			new TeamchatPost().postMsg(msg, teamchat_id);
		}
		else
		{
			String url = "https://outlook.office365.com/api/beta/me/subscriptions/"+sub_id+"/renew";
		
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			post.addHeader("Authorization","Bearer "+access_token);
			post.setHeader("Content-Type","application/json");
			
			
			HttpResponse response = null;
			try {
					response = client.execute(post);
				
					int rspcode=response.getStatusLine().getStatusCode();
					System.err.println(rspcode);
					if(rspcode==202)
					{
						System.out.println("Renewed "+teamchat_id);
					}
					else if(rspcode==401)
					{
						String refresh_token=jdb.retreiveRefreshToken(teamchat_id);
						if(new RenewAccessToken().getNewAccessToken(teamchat_id,refresh_token)==200)
						{
							renewSubscription(teamchat_id, sub_id);
						}
						else
						{
							String msg="Your subscription is expired. Please login and "
									+ "subscribe to get notification.";
							Office365Subscribe offsub=new Office365Subscribe();
							offsub.delete(sub_id);
							jdb.delete(teamchat_id);
							new TeamchatPost().postMsg(msg, teamchat_id);
						}
					}
					
			} catch (IOException e) {
			// 	TODO Auto-generated catch block
				e.printStackTrace();
				}
			}
		}
	
}
