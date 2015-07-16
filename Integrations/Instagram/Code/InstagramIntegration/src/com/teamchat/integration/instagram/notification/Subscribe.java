package com.teamchat.integration.instagram.notification;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.teamchat.integration.instagram.database.InstaGeography;
import com.teamchat.integration.instagram.database.InstaLocation;
import com.teamchat.integration.instagram.database.InstaSubDB;
import com.teamchat.integration.instagram.properties.InstagramProperty;

public class Subscribe {
	
	String callbackurl="null";
	String client_id="null";
	String client_secret="null";
	String result="";
	
	public Subscribe()
	{
		InstagramProperty ip=new InstagramProperty();
		ip.loadParams();
		client_id=ip.getClientId();
		client_secret=ip.getClientSecret();
		callbackurl=ip.getWebhookUrl();
	}
	
	public String getsubscribelist()
	{
		String geturl="https://api.instagram.com/v1/subscriptions?client_secret="+client_secret+"&client_id="+client_id;
		HttpGet httpget=new HttpGet(geturl);

	    HttpClient httpclient=new DefaultHttpClient();;
		try {
			HttpResponse response=httpclient.execute(httpget);
			System.out.println(response.getStatusLine());
			
			
			BufferedReader in = new BufferedReader(new
					InputStreamReader(response.getEntity().getContent()));
					                StringBuffer sb = new StringBuffer("");
					                String line = "";
					                while ((line = in.readLine()) != null) {
					                    sb.append(line +"\n");
					                    result+=line+"<br>";
					                }
					                in.close();
					                System.out.println(sb);
					                
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return "Subscribed Successfully<br>"+result;
	}
	
	public String subscribeTags(String oid,String id)
	{
		try
		{
			HttpPost httppost = new
		    		HttpPost("https://api.instagram.com/v1/subscriptions/");

		    HttpClient httpclient=new DefaultHttpClient();
			List<NameValuePair> nvps = new ArrayList<NameValuePair>(5);
			nvps.add(new BasicNameValuePair("verify_token", "instabot"));
			nvps.add(new BasicNameValuePair("client_id", client_id));
			nvps.add(new BasicNameValuePair("client_secret", client_secret));
			nvps.add(new BasicNameValuePair("object", "tag"));
			nvps.add(new BasicNameValuePair("aspect", "media"));
			nvps.add(new BasicNameValuePair("object_id", oid));
			nvps.add(new BasicNameValuePair("callback_url", callbackurl));
			UrlEncodedFormEntity sd=new UrlEncodedFormEntity(nvps);
			httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
			httppost.setEntity(sd);
			System.out.println("executing request " + httppost.getRequestLine());
			System.out.println("request body=\n"+EntityUtils.toString(httppost.getEntity()));
	            HttpResponse response = httpclient.execute(httppost);
			System.out.println(response.getStatusLine());
			if(response.getStatusLine().getStatusCode()==200)
			{
				result="Successfully Subscribed";
			
			
			BufferedReader in = new BufferedReader(new
					InputStreamReader(response.getEntity().getContent()));
					                StringBuffer sb = new StringBuffer("");
					                String line = "";
					                while ((line = in.readLine()) != null) {
					                    sb.append(line +"\n");
					                }
					                in.close();
					                System.out.println(sb);

					                JSONObject jobj=new JSONObject(sb.toString());
					                JSONObject jdata=jobj.getJSONObject("data");
					                InstaSubDB isdb=new InstaSubDB();
					                isdb.insert(id, jdata.getString("object"), jdata.getString("object_id"),jdata.getString("id"));    

			}
					              
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public String subscribeLocation(String oid,String id,String locname)
	{
		try
		{
			HttpPost httppost = new
		    		HttpPost("https://api.instagram.com/v1/subscriptions/");

		    HttpClient httpclient=new DefaultHttpClient();
			List<NameValuePair> nvps = new ArrayList<NameValuePair>(5);
			nvps.add(new BasicNameValuePair("verify_token", "instabot"));
			nvps.add(new BasicNameValuePair("client_id", client_id));
			nvps.add(new BasicNameValuePair("client_secret", client_secret));
			nvps.add(new BasicNameValuePair("object", "location"));
			nvps.add(new BasicNameValuePair("aspect", "media"));
			nvps.add(new BasicNameValuePair("object_id", oid));
			nvps.add(new BasicNameValuePair("callback_url", callbackurl));
			UrlEncodedFormEntity sd=new UrlEncodedFormEntity(nvps);
			httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
			httppost.setEntity(sd);
			System.out.println("executing request " + httppost.getRequestLine());
			System.out.println("request body=\n"+EntityUtils.toString(httppost.getEntity()));
	            HttpResponse response = httpclient.execute(httppost);
			System.out.println(response.getStatusLine());
			if(response.getStatusLine().getStatusCode()==200)
			{
				result="Successfully Subscribed";
			
			
			BufferedReader in = new BufferedReader(new
					InputStreamReader(response.getEntity().getContent()));
					                StringBuffer sb = new StringBuffer("");
					                String line = "";
					                while ((line = in.readLine()) != null) {
					                    sb.append(line +"\n");
					                }
					                in.close();
					                System.out.println(sb);

					                JSONObject jobj=new JSONObject(sb.toString());
					                JSONObject jdata=jobj.getJSONObject("data");
					                InstaSubDB isdb=new InstaSubDB();
					                isdb.insert(id, jdata.getString("object"), jdata.getString("object_id"),jdata.getString("id"));
					                InstaLocation il=new InstaLocation();
					                il.insert(jdata.getString("object_id"), locname, jdata.getString("id"));

			}
					              
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	public String subscribeGeo(String id,String lat,String lng,String radius,String geoname)
	{
		try
		{
			HttpPost httppost = new
		    		HttpPost("https://api.instagram.com/v1/subscriptions/");

		    HttpClient httpclient=new DefaultHttpClient();;
			List<NameValuePair> nvps = new ArrayList<NameValuePair>(5);
			nvps.add(new BasicNameValuePair("verify_token", "instabot"));
			nvps.add(new BasicNameValuePair("client_id", client_id));
			nvps.add(new BasicNameValuePair("client_secret", client_secret));
			nvps.add(new BasicNameValuePair("object", "geography"));
			nvps.add(new BasicNameValuePair("aspect", "media"));
			nvps.add(new BasicNameValuePair("lat", lat));
			nvps.add(new BasicNameValuePair("lng", lng));
			nvps.add(new BasicNameValuePair("radius", radius));
			nvps.add(new BasicNameValuePair("callback_url", callbackurl));
			UrlEncodedFormEntity sd=new UrlEncodedFormEntity(nvps);
			httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
			httppost.setEntity(sd);
			System.out.println("executing request " + httppost.getRequestLine());
			System.out.println("request body=\n"+EntityUtils.toString(httppost.getEntity()));
	            HttpResponse response = httpclient.execute(httppost);
			System.out.println(response.getStatusLine());
			if(response.getStatusLine().getStatusCode()==200)
			{
				result="Successfully Subscribed";
			
			
			BufferedReader in = new BufferedReader(new
					InputStreamReader(response.getEntity().getContent()));
					                StringBuffer sb = new StringBuffer("");
					                String line = "";
					                while ((line = in.readLine()) != null) {
					                    sb.append(line +"\n");
//					                    result+=line+"<br>";
					                }
					                in.close();
					                System.out.println(sb);
					                
					                JSONObject jobj=new JSONObject(sb.toString());
					                JSONObject jdata=jobj.getJSONObject("data");
					                InstaSubDB isdb=new InstaSubDB();
					                isdb.insert(id, jdata.getString("object"), jdata.getString("object_id"),jdata.getString("id"));                
					                InstaGeography ig=new InstaGeography();
					                ig.insert(jdata.getString("object_id"), geoname, jdata.getString("id"));
					   
			}
					              
		}
		catch(Exception e)
		{
			e.printStackTrace();
			result="Subscribption failed";
		}
		return result;
	}
	
	

}
