package com.teamchat.integration.instagram.bot;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.integration.instagram.database.InstaSubDB;
import com.teamchat.integration.instagram.database.JDBCConnection;
import com.teamchat.integration.instagram.search.Geography;
import com.teamchat.integration.instagram.search.Location;
import com.teamchat.integration.instagram.search.Tags;

public class TeamchatPost {
	
	public static TeamchatAPI tpapi;
	
	public void postMsg(String msg,String sname)
	{
		Room r=tpapi.context().create().setName("Instagram")
				.add(sname);
		tpapi.perform(r.post(new PrimaryChatlet()
		.setQuestionHtml(msg)));
	}
	
	/*public void postMsg(String msg)
	{
		System.out.println(msg);
		tpapi.perform(tpapi.context().currentRoom().post(new PrimaryChatlet()
					.setQuestionHtml(msg)));
	}*/
	
	public void sndMsg(ArrayList<String> uidlist,String msg)
	   {
			if(!msg.equals(""))
			{
				for(int i=0;i<uidlist.size();i++)
				{
					JDBCConnection db=new JDBCConnection();
					String gc=db.retreive(uidlist.get(i));
					if((!gc.equals("access_token")))
					{
						Room r=tpapi.context().create().setName("Instagram")
								.add(uidlist.get(i));
						tpapi.perform(r.post(new PrimaryChatlet()
							.setQuestionHtml(msg)));
					}
				}
			}
	   }
	
	public void postjsonMsg(String msg)
	{
		try 
		{
			InstaSubDB isdb=new InstaSubDB();
			JSONArray jarr=new JSONArray(msg);
			for(int i=0;i<jarr.length();i++)
			{
				JSONObject jobj;
			
				jobj = jarr.getJSONObject(i);
			
				if(jobj.getString("object").equals("tag"))
				{
					Long ss=jobj.getLong("subscription_id");
					ArrayList<String> uidlist=isdb.retreiveUidList(ss.toString());
					for(int j=0;j<uidlist.size();j++)
					{
						JDBCConnection jd=new JDBCConnection();
						String at=jd.retreive(uidlist.get(j));
						if(at.equals("access_token"))
							break;
						String result=new Tags().recentTag(jobj.getString("object_id"), at);
						if(result.equals("dup"))
						{
							break;
						}
						else if(!result.equals("error"))
						{
							sndMsg(uidlist, result);
							break;
						}
					}
				}
				else if(jobj.getString("object").equals("geography"))
				{
					Long ss=jobj.getLong("subscription_id");
					ArrayList<String> uidlist=isdb.retreiveUidList(ss.toString());
					for(int j=0;j<uidlist.size();j++)
					{
						JDBCConnection jd=new JDBCConnection();
						String at=jd.retreive(uidlist.get(j));
						if(at.equals("access_token"))
							break;
						String result=new Geography().recentUpdate(jobj.getString("object_id"));
						if(result.equals("dup"))
						{
							break;
						}
						else if(!result.equals("error"))
						{
							sndMsg(uidlist, result);
							break;
						}						
					}
				}
				else if(jobj.getString("object").equals("location"))
				{
					Long ss=jobj.getLong("subscription_id");
					ArrayList<String> uidlist=isdb.retreiveUidList(ss.toString());
					for(int j=0;j<uidlist.size();j++)
					{
						JDBCConnection jd=new JDBCConnection();
						String at=jd.retreive(uidlist.get(j));
						if(at.equals("access_token"))
							break;
						String result=new Location().recentLocation(jobj.getString("object_id"), at);
						if(result.equals("dup"))
						{
							break;
						}
						else if(!result.equals("error"))
						{
							sndMsg(uidlist, result);
							break;
						}
					}
				}
			}
			
//			tpapi.perform(tpapi.context().currentRoom().post(new PrimaryChatlet()
//						.setQuestionHtml(msg)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
