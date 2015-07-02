package com.teamchat.integration.Gosquared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class test  {
	String	 sech;
	
	

	
	public String onCreate(TeamchatAPI api,String apikey1,String stoken) throws IOException {
		ArrayList<String> a = new ArrayList<String>();
		ArrayList<String> b = new ArrayList<String>();
		ArrayList<Integer> c = new ArrayList<Integer>();
		ArrayList<Integer> d = new ArrayList<Integer>();
		ArrayList<Integer> e = new ArrayList<Integer>();
		ArrayList<Integer> f = new ArrayList<Integer>();
		ArrayList<Integer> g = new ArrayList<Integer>();
	
		String result = "";
		URL urldemo = new URL(
				"https://api.gosquared.com/trends/v2/aggregate?api_key="+apikey1+"&site_token="+stoken);
		URLConnection yc;
		yc = urldemo.openConnection(); // AK65YLGDU2HMH6GY&site_token=GSN-621790-C
		 BufferedReader in = new BufferedReader(new InputStreamReader( yc.getInputStream())); 
		String inputline;
		while ((inputline = in.readLine()) != null) {
			result += inputline;
//https://api.gosquared.com/trends/v2/aggregate?api_key=AK65YLGDU2HMH6GY&site_token=GSN-621790-A
			System.out.println(inputline + "\n");
		}
		in.close();

		JSONObject ja = new JSONObject(result);
		JSONArray list = ja.getJSONArray("list");
		for (int i = 0; i < list.length(); i++) {
			a.add(list.getJSONObject(i).getString("from"));
			b.add(list.getJSONObject(i).getString("to"));
			JSONObject met = list.getJSONObject(i).getJSONObject("metrics");
			if (met.has("day_uniques")) {
				c.add(met.getInt("day_uniques"));
				d.add(met.getInt("visits"));
				e.add(met.getInt("engaged_time_per_visit"));
				f.add(met.getInt("new_visits"));
				g.add(met.getInt("returning_visits"));
				DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				String date = dateformat.format(cal.getTime()) + "T00:00:00+05:30";
		System.out.println(date);
				for (int j = 0; j < a.size(); j++) {
					if (date.equals(a.get(j))) {
						int newvisit =f.get(i);
						int returnvisit=g.get(i);
						int day = c.get(j);
						int visit = d.get(j);
						int eng = e.get(j);
						 sech="<ul> <li> Number of new visitors :"+newvisit+"</li>"+"<li> Number of returning visitors :"+returnvisit+"</li>"+"<li> Number of day uniques :"+day+"</li>"+"<li> Number of visit : "+visit+"</li>"+"<li> Engage time/visit : "+eng+"</li> </ul>";
						// api.perform(api.context().currentRoom().post(new TextChatlet("<ul> <li> Number of new visitors :"+newvisit+"</li>"+"<li> Number of returning visitors :"+returnvisit+"</li>"+"<li> Number of bounces :"+bounce+"</li>"+"<li> Number of visit : "+visit+"</li>"+"<li> Engage time/visit : "+eng+"</li> </ul>")));
			}
				}
			
				
				}
		
			else 
			{   if(i==0) 
				sech ="no visitors";
				// api.perform(api.context().currentRoom().post(new TextChatlet(" no visitors")));
				  break;
			}

			
		
		
	}
		return sech;
}
}