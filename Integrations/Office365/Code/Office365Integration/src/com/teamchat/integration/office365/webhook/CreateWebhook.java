package com.teamchat.integration.office365.webhook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.teamchat.integration.office365.database.JDBCConnection;
import com.teamchat.integration.office365.database.Office365Subscribe;
import com.teamchat.integration.office365.oauth.RenewAccessToken;

public class CreateWebhook {
	
	public void subscribe(String teamchat_id,String sub_type)
	{
		JDBCConnection jdb=new JDBCConnection();
		String access_token=jdb.retreiveAccessToken(teamchat_id);
		String office365_id=jdb.retreiveOffice365ID(teamchat_id);
		if(access_token.equals("access_token"))
		{
			String msg="You have to login";
			new TeamchatPost().postMsg(msg, teamchat_id);
		}
		else
		{
			String url = "https://outlook.office365.com/api/beta/me/subscriptions";
			String resrc="";
			if(sub_type.equals("Calendar"))
			{
				resrc="https://outlook.office365.com/api/beta/me/events";
			}
			else
			{
				resrc="https://outlook.office365.com/api/beta/me/messages";
			}
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			post.addHeader("Authorization","Bearer "+access_token);
			post.setHeader("Content-Type","application/json");
			
			JSONObject jobj=new JSONObject();
			jobj.put("@odata.type", "#Microsoft.OutlookServices.PushSubscription");
			jobj.put("CallbackURL", "https://prod-solutions4.teamchat.com/UrlRedirect/redirect");
			jobj.put("ResourceURL", resrc);
			jobj.put("ClientState", "352e7364-ed52-4756-aea2-25689244d5ee");
			jobj.put("ChangeType", "Created, Updated, Deleted");
			String jsonstring=jobj.toString();
			StringEntity entity = new StringEntity(jsonstring,"UTF-8");
			entity.setContentType("application/json");
			post.setEntity(entity);
			HttpResponse response = null;
			try {
					response = client.execute(post);
				
					int rspcode=response.getStatusLine().getStatusCode();
					System.err.println(rspcode);
					if(rspcode==201)
					{
						String inputline="";
						String result="";
						BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
						while ((inputline = in.readLine()) != null)
							result+=inputline;
						in.close();
						System.out.println(result); 
						JSONObject jsub=new JSONObject(result);
						String sub_id=jsub.getString("Id");
						Office365Subscribe offsub=new Office365Subscribe();
						offsub.insert(sub_id, teamchat_id, office365_id, sub_type);
						String msg=sub_type+" Subscribed Successfully";
						new TeamchatPost().postMsg(msg, teamchat_id);
					}
					else if(rspcode==401)
					{
						String refresh_token=jdb.retreiveRefreshToken(teamchat_id);
						if(new RenewAccessToken().getNewAccessToken(teamchat_id,refresh_token)==200)
						{
							subscribe(teamchat_id,sub_type);
						}
						else
						{
							String msg="Login and Try Again";
							new TeamchatPost().postMsg(msg, teamchat_id);
						}
					}
					else
					{
						String msg="Subscription failed. Try Again";
						new TeamchatPost().postMsg(msg, teamchat_id);
					}
					
			} catch (IOException e) {
			// 	TODO Auto-generated catch block
				e.printStackTrace();
				}
			}
		}
	
	public void unSubscribe(String teamchat_id,String sub_type)
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
			Office365Subscribe offsub=new Office365Subscribe();
			String sub_id=offsub.retreiveSubID(teamchat_id,sub_type);
			String url = "https://outlook.office365.com/api/beta/me/subscriptions('"+sub_id+"')";
		
			HttpClient client = new DefaultHttpClient();
			HttpDelete hdel = new HttpDelete(url);
			hdel.addHeader("Authorization","Bearer "+access_token);
			hdel.setHeader("Content-Type","application/json");
			
			
			HttpResponse response = null;
			try {
					response = client.execute(hdel);
				
					int rspcode=response.getStatusLine().getStatusCode();
					System.err.println(rspcode);
					if(rspcode==204)
					{
						System.out.println("Unsubscribed "+teamchat_id);
						offsub=new Office365Subscribe();
						offsub.delete(sub_id);
						String msg="Unsubscribed "+sub_type+" Successfully";
						new TeamchatPost().postMsg(msg, teamchat_id);
					}
					else if(rspcode==401)
					{
						String refresh_token=jdb.retreiveRefreshToken(teamchat_id);
						if(new RenewAccessToken().getNewAccessToken(teamchat_id,refresh_token)==200)
						{
							unSubscribe(teamchat_id, sub_type);
						}
						else
						{
							String msg="Login and Try again";
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
