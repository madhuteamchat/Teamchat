package com.teamchat.integration.Gosquared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
public class Now {
	int str;
	public   void now1 (TeamchatAPI api,String apikey1,String stoken) throws IOException
	{ ArrayList<Integer> a = new ArrayList<Integer>();
	ArrayList<String> b = new ArrayList<String>();
		String result="";
		URL urldemo = new URL("https://api.gosquared.com/now/v3/aggregateStats?api_key="+apikey1+"&site_token="+stoken); 
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
        JSONArray ja=new JSONArray(result);
        
        for(int i=0;i<ja.length();i++)
        {
        JSONObject jaobj=ja.getJSONObject(i);
        System.out.println(jaobj.getString("type"));
        str=(jaobj.getInt("cardinality"));
        JSONArray list =jaobj.getJSONArray("list");
        
        for(int j=0;j<list.length();j++)
        {
        JSONObject list1=list.getJSONObject(j);
        System.out.println(list1.getString("key"));
        System.out.println(list1.getInt("value"));
       
        
        
        if(i==0){
        a.add(list1.getInt("value"));
        b.add(list1.getString("key"));}
        }
//        System.out.println(jaobj.getInt("cardinality"));
        
        
        }
        
        
        if (str==0)
        {
     	   api.perform(
           		 api.context().currentRoom().post(
           		 new PrimaryChatlet()
           		 .setQuestionHtml("<font color=\"red\">NO VISITORS</font>")));
        }
        else{
        String str=b.get(0)+"="+a.get(0);
        String val1=b.get(0);
        String val=a.get(0).toString();
        for(int i=1;i<a.size();i++)
        {
        	val=val+","+a.get(i).toString();
        }
        
        for(int i=1;i<b.size();i++)
        {
        	val1=val1+"|"+b.get(i);
        }
        for(int i=1;i<a.size();i++)
        {
        	 str = str+"<br>"+ b.get(i)+"="+a.get(i);
        }
        //System.out.println(str);
        
        
        api.perform(
        		 api.context().currentRoom().post(
        		 new PrimaryChatlet()
        		 .setQuestionHtml("List of Browser used by users <br>"+ str+"<br>"+"<div style=\"overflow:scroll;overflow-y:scroll;overflow-x:scroll;\">   <img style=\"-webkit-user-select: none\" src=\"http://chart.apis.google.com/chart?cht=p3&amp;chs=450x200&amp;chd=t:"+val+"&amp;chco=80C65A,224499,FF0000&amp;chl="+val1+"\"></div>")
        		 ));
        }
        
	}

}
