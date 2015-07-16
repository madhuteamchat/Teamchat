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

import com.teamchat.integration.instagram.database.JDBCConnection;
import com.teamchat.integration.instagram.notification.InstagramURLlink;


public class Location {

	ArrayList<String> loclist=new ArrayList<String>();
	ArrayList<String> locidlist=new ArrayList<String>();
	String result = "";
	public static String locprev="";
	public String searchlocation(String id,String lat,String lng,String radius)
	{
		try
		{
			JDBCConnection jdb=new JDBCConnection();
			String access_token=jdb.retreive(id);
			if(access_token.equals("access_token"))
				return "error";
			HttpGet hget=new HttpGet("https://api.instagram.com/v1/locations/search?lat="+lat+"&lng="+lng+"&distance="+radius+"&access_token="+access_token);
		    HttpClient httpclient=new DefaultHttpClient();
		    HttpResponse response =httpclient.execute(hget);
		    System.out.println(response.getStatusLine());
		    if(response.getStatusLine().getStatusCode()==429)
		    {
		    	return "The maximum number of requests per hour has been exceeded. Try again later.";
		    }
		    else if(response.getStatusLine().getStatusCode()==200)
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
//		    			System.out.println(sb);
		    			JSONObject jobj=new JSONObject(js);
		    			JSONArray jdata=jobj.getJSONArray("data");
		    			for(int i=0;i<jdata.length();i++)
		    			{
		    				locidlist.add(jdata.getJSONObject(i).getString("id"));
		    				loclist.add(jdata.getJSONObject(i).getString("name"));
		    			}
		    			return "success";
		    }
		    else
		    {
		    	jdb.delete(id);
		    	return "error";
		    }
		}	
		catch(Exception e)
		{
			e.printStackTrace();
			return "error";
		}
	}
	
	public ArrayList<String> getloclist()
	{
		return loclist;
	}
	
	public ArrayList<String> getlocidlist()
	{
		return locidlist;
	}
	
	public String recentLocation(String lid,String access_token)
	{
		String result="";
		try
		{
			HttpGet hget=new HttpGet("https://api.instagram.com/v1/locations/"+lid+"/media/recent?count=1&access_token="+access_token);
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
		    			JSONArray jdata=jobj.getJSONArray("data");
		    			for(int i=0;i<jdata.length();i++)
		    			{
		    				if(jdata.getJSONObject(i).getString("type").equals("image"))
		    				{
		    					JSONObject iobj=jdata.getJSONObject(i).getJSONObject("images");
		    					result="<div align=\"center\"><h4>"+jdata.getJSONObject(i).getJSONObject("caption").getString("text")+"</h4>";
		    					if(iobj.has("thumbnail"))
		    					{
		    						JSONObject imgobj=iobj.getJSONObject("thumbnail");
			    					String imgurl=imgobj.getString("url");
			    					int width=imgobj.getInt("width");
			    					int height=imgobj.getInt("height");
			    					result+="<img src=\""+imgurl+"\" height=\""+height+"\" width=\""+width+"\" />";
		    					}
		    					
		    					
		    					else if(iobj.has("low_resolution"))
		    					{
		    						JSONObject imgobj=iobj.getJSONObject("low_resolution");
			    					String imgurl=imgobj.getString("url");
			    					int width=imgobj.getInt("width");
			    					int height=imgobj.getInt("height");
			    					result+="<img src=\""+imgurl+"\" height=\""+height+"\" width=\""+width+"\" />";
		    					}
		    					else if(iobj.has("standard_resolution"))
		    					{
		    						JSONObject imgobj=iobj.getJSONObject("standard_resolution");
			    					String imgurl=imgobj.getString("url");
			    					int width=imgobj.getInt("width");
			    					int height=imgobj.getInt("height");
			    					result+="<img src=\""+imgurl+"\" height=\""+height+"\" width=\""+width+"\" />";
		    					}
		    					result+="</div>";
		    					String media_id=jdata.getJSONObject(i).getString("id");
		    					if(media_id.equals(Location.locprev))
		    					{
		    						return "dup";
		    					}
		    					Location.locprev=media_id;
		    					JSONObject from=jdata.getJSONObject(i).getJSONObject("caption").getJSONObject("from");
		    					result+="<strong>Uploaded by:</strong> "+from.getString("full_name")+" ("+from.getString("username")+")<br>";
		    					if(jdata.getJSONObject(i).has("location"))
		    					{
		    						JSONObject check=jdata.getJSONObject(i).getJSONObject("location");
		    						if(check.has("name"))
		    							result+="<strong>Location: </strong>"+check.getString("name")+"<br>";
		    					}
		    					
		    					String insta_href=jdata.getJSONObject(i).getString("link");
		    					InstagramURLlink urlLink=new InstagramURLlink();
		    					String tc_href=urlLink.createEmbeddedLink(insta_href, "Instagram_Bot", "http");
		    					result+="<strong>link:</strong> <a href=\""+tc_href+"\" target=\"_blank\">"+insta_href+"</a>";
		    					
		    				}
		    			}
		    }
		}	
		catch(Exception e)
		{
			e.printStackTrace();
			return "error";
		}

		return result;
	}
	
}
