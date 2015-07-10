package com.teamchat.integration.instagram.search;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class GMapAPI {
	
	ArrayList<String> latlist=new ArrayList<String>();
	ArrayList<String> lnglist=new ArrayList<String>();
	ArrayList<String> placelist=new ArrayList<String>();
	String result = "";
	
	public String searchlocation(String place)
	{
		try
		{
			String url="https://maps.googleapis.com/maps/api/geocode/json?address="+place+"&key=AIzaSyB3JwLzdzu6jurbKG18YPkW9UCjpDLdtCg";
			
			HttpGet hget=new HttpGet(url);
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
		    				js+=line;
		    				sb.append(line);
		    			}
		    			in.close();
		    			System.out.println(sb);
		    			JSONObject jobj=new JSONObject(js);
		    			JSONArray jdata=jobj.getJSONArray("results");
		    			for(int i=0;i<jdata.length();i++)
		    			{
		    				Double lat=jdata.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
		    				Double lng=jdata.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
		    				latlist.add(lat.toString());
		    				lnglist.add(lng.toString());
		    				placelist.add(jdata.getJSONObject(i).getString("formatted_address"));
		    			}
		    			return "success";
		    }
		    else
		    {
		    	return "error";
		    }
		}	
		catch(Exception e)
		{
			e.printStackTrace();
			return "error";
		}
	}
	
	public ArrayList<String> getlatlist()
	{
		return latlist;
	}
	
	public ArrayList<String> getlnglist()
	{
		return lnglist;
	}
	
	public ArrayList<String> getplacelist()
	{
		return placelist;
	}

}
