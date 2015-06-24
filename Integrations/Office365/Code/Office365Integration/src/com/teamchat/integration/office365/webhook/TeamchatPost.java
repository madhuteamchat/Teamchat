package com.teamchat.integration.office365.webhook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.integration.office365.database.Office365Subscribe;
import com.teamchat.integration.office365.subscription.CalenderNotification;
import com.teamchat.integration.office365.subscription.MailNotification;

public class TeamchatPost {
	
	public static TeamchatAPI tpapi;
	
	public void postMsg(String msg,String sname)
	{
		if(msg.length()>1)
		{

			System.out.println("=======================Posting Msg============================");
			Room r=tpapi.context().create().setName("Office365")
						.add(sname);
			tpapi.perform(r.post(new PrimaryChatlet()
					.setQuestionHtml(msg)));
		}
	}
	
	
	public void postjsonMsg(String msg)
	{
		try 
		{
			String result="";
			JSONObject jobj=new JSONObject(msg);
			JSONArray value=jobj.getJSONArray("value");
			for(int i=0;i<value.length();i++)
			{
				if(value.getJSONObject(i).getString("ChangeType").equals("Acknowledgment"))
				{
					break;
				}
				if(value.getJSONObject(i).getString("@odata.type").equals("#Microsoft.OutlookServices.Notification"))
				{
					JSONObject vobj=value.getJSONObject(i);
					String sub_id=vobj.getString("SubscriptionId");
					Office365Subscribe offsub=new Office365Subscribe();
					String teamchat_id=offsub.retreiveTeamchatID(sub_id);
					if(!teamchat_id.equals("teamchat_id"))
					{
						JSONObject jentity=vobj.getJSONObject("Entity");
						String sub_type=jentity.getString("@odata.type");
						String url=jentity.getString("@odata.id");
						String change_type=vobj.getString("ChangeType");

						System.out.println("===============================checking subtype");
						if(change_type.equals("Created"))
						{
							if(sub_type.equals("#Microsoft.OutlookServices.Message"))
							{
								result=new MailNotification().notifyCreate(url, teamchat_id);
							}
							else if(sub_type.equals("#Microsoft.OutlookServices.Event"))
							{
								result=new CalenderNotification().notifyCreate(url, teamchat_id);
							}
						}
						else if(change_type.equals("Updated"))
						{
							if(sub_type.equals("#Microsoft.OutlookServices.Message"))
							{
//								result=new MailNotification().notifyCreate(url, teamchat_id);
							}
							else if(sub_type.equals("#Microsoft.OutlookServices.Event"))
							{
								result=new CalenderNotification().notifyCreate(url, teamchat_id);
							}
						}
						postMsg(result, teamchat_id);
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
