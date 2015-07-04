package com.teamchat.integration.mailchimp;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
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

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;


public class accesstoken {

	
	public String at;
	
	public  String us=null;
	
	public  static String smail;
	public void at(String code)
	{
		 @SuppressWarnings({ "deprecation", "resource" })
		HttpClient httpclient = new DefaultHttpClient();
		 
	        try {
	 
	            HttpPost httpPost = new HttpPost("https://login.mailchimp.com/oauth2/token");
	 
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	
	            nameValuePairs.add(new BasicNameValuePair("grant_type","authorization_code"));
	            nameValuePairs.add(new BasicNameValuePair("client_id",PropertiesFile.getValue("client_id")));
	            nameValuePairs.add(new BasicNameValuePair("code",code));
	            nameValuePairs.add(new BasicNameValuePair("client_secret",PropertiesFile.getValue("client_secret")));
	            nameValuePairs.add(new BasicNameValuePair("redirect_uri",PropertiesFile.getValue("callback")));
	              
	            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
	 
	            System.out.println("executing request " + httpPost.getRequestLine());
	            HttpResponse response = httpclient.execute(httpPost);
	           BufferedReader br=new BufferedReader(new 
	        		InputStreamReader (response.getEntity().getContent())   );
	           
	           StringBuffer sb=new StringBuffer("");
	           String line ="";
	           while((line= br.readLine()) !=null){
	        	   
	        	   sb.append(line);
	        	   
	           }
	           br.close();
	           
	           
	           System.out.println(sb);
	           JSONObject jo=new JSONObject(sb.toString());
	           
	        at=jo.getString("access_token");
	           System.out.println(at);
	          
	           

	           
	           @SuppressWarnings("deprecation")
			HttpClient getclient = new DefaultHttpClient();
	           HttpGet httpget=new HttpGet("https://login.mailchimp.com/oauth2/metadata");
	           httpget.addHeader("Authorization","OAuth "+at);

	            HttpResponse response1 = httpclient.execute(httpget);
	           BufferedReader br1=new BufferedReader(new 
		        		InputStreamReader (response1.getEntity().getContent())   );
		           
		           StringBuffer sb1=new StringBuffer("");
		           String line1 ="";
		           while((line1= br1.readLine()) !=null){
		        	   
		        	   sb1.append(line1);
		        	   
		           }
		           br1.close();
		           
		           
		           System.out.println(sb1);
		           JSONObject j=new JSONObject(sb1.toString());
		            us=j.getString("dc");
		           System.out.println(us);
		          
		           
		         
		          
		           
		           
	           TeamchatAPI api=MailchimpBot.apig;
	           new MailchimpBot().setToken(api,at,us);
	           new Listm().nlistGrowthHistory(api, at, us);
	           
	           new Campaign().ncampaignStats(api, at, us);
	          // ManageDB db=new ManageDB();
	        String act=    ManageDB.retrieve(smail);
	          if(act==null)
	          {
	           ManageDB.insert(smail, at, us);
	           
	          }
	          else
	        	  ManageDB.upadate(smail,at, us);
	           
	           api.perform(api.context().currentRoom().post(new TextChatlet("Login Successful! Please type list or campaign keyword") ));
	           
	           
	           
	           
	            
	        } 
	        catch (Exception e) {
	            System.out.println(e);
	            
	            TeamchatAPI api=MailchimpBot.apig;
		           api.perform(api.context().currentRoom().post(new TextChatlet("<h5 style=\"color:#F80000 ;\">Login UnSuccessful! Please type login Keyword for login</h5") ));
	            
	            
	            //access token =90f37be8e4d08788ccd3197d1603fb1f
	        }
	}
	
}
