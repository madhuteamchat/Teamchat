package com.teamchat.integration.instagram.oauth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.teamchat.integration.instagram.bot.TeamchatPost;
import com.teamchat.integration.instagram.database.JDBCConnection;

public class InstagramToken {

	
	public static String uid="";
	
	String scopes="wl.basic+wl.offline_access+wl.signin+wl.skydrive+wl.skydrive_update";
	String redirecturi="http://interns.teamchat.com:8086/InstagramIntegration/InstagramCallBack";
	String client_id="858afa5bc35e419c82d02ad8c58d037e";
	String client_secret="f4dde757b8074f7893ce7fb4a5010ccc";
	
	
	public void getaccesstoken(String acode)
	{
		 try { 
			 String uid=InstagramToken.uid;
		
	    HttpPost httppost = new
	    		HttpPost("https://api.instagram.com/oauth/access_token");
	    HttpClient httpclient=new DefaultHttpClient();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>(5);
		nvps.add(new BasicNameValuePair("code", acode));
		nvps.add(new BasicNameValuePair("client_id", client_id));
		nvps.add(new BasicNameValuePair("client_secret", client_secret));
		nvps.add(new BasicNameValuePair("redirect_uri", redirecturi));
		nvps.add(new BasicNameValuePair("grant_type", "authorization_code"));
		UrlEncodedFormEntity sd=new UrlEncodedFormEntity(nvps);
		httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		httppost.setEntity(sd);
		System.out.println("executing request " + httppost.getRequestLine());
		System.out.println("request body=\n"+EntityUtils.toString(httppost.getEntity()));
            HttpResponse response = httpclient.execute(httppost);
		System.out.println(response.getStatusLine());
		
		
		BufferedReader in = new BufferedReader(new
				InputStreamReader(response.getEntity().getContent()));
				                StringBuffer sb = new StringBuffer("");
				                String line = "";
				                while ((line = in.readLine()) != null) {
				                    sb.append(line);
				                }
				                in.close();
				                System.out.println(sb);
				              
				               JSONObject json = new JSONObject(sb.toString());
								System.out.println("Access Token="+json.getString("access_token"));
								
//								Properties props = new Properties();
//						        props.setProperty("access_token", json.getString("access_token"));
//						        File f = new File("/home/intern11/uid.properties");
//						        OutputStream out = new FileOutputStream( f );
//						        props.store(out, "This is an optional header comment string");
								
								 JDBCConnection db=new JDBCConnection();
							        db.insert(uid, json.getString("access_token"));
							        JSONObject juser=json.getJSONObject("user");
							        String usrname=juser.getString("username");
							        String fullname=juser.getString("full_name");
							        String dp=juser.getString("profile_picture");
							        String msg="<div align=\"center\"><h4>Logged in as</h4><br>"
							        			+"<img src=\""+dp+"\" height=\"120\" width=\"120\" /><br><h4>"
							        			+fullname+" ("+usrname+")</h4><div>";
//							        System.out.println("@@@@@@@@@@@ uid="+uid);
							        new TeamchatPost().postMsg(msg, uid);
							     
								
				           	} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				               
				               
	}
}
