package com.teamchat.integration.office365.oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.teamchat.integration.office365.database.JDBCConnection;
import com.teamchat.integration.office365.property.Office365Property;

public class RenewAccessToken {
	
	JSONObject json;
	static String client_id;
	static String client_secret; 
	static String rid;
	String redirecturi;
	
	 public RenewAccessToken() {
			// TODO Auto-generated constructor stub
			  Office365Property op=new Office365Property();
			  op.loadParams();
			  client_id=op.getClientId();
			  client_secret=op.getClientSecret();
			  redirecturi=op.getRedirectUrl();
		}
	
	 public int getNewAccessToken(String teamchat_id,String refresh_token) throws ClientProtocolException, IOException
		{
			 
			
		    HttpPost httppost = new
		    		HttpPost("https://login.microsoftonline.com/common/oauth2/token");
		    HttpClient httpclient=new DefaultHttpClient();
			List<NameValuePair> nvps = new ArrayList<NameValuePair>(5);
			nvps.add(new BasicNameValuePair("refresh_token", refresh_token));
			nvps.add(new BasicNameValuePair("client_id",client_id ));
			nvps.add(new BasicNameValuePair("client_secret", client_secret));
			nvps.add(new BasicNameValuePair("redirect_uri", redirecturi));
			nvps.add(new BasicNameValuePair("grant_type", "refresh_token"));
			nvps.add(new BasicNameValuePair("resource", "https://outlook.office365.com/"));
			UrlEncodedFormEntity sd=new UrlEncodedFormEntity(nvps,HTTP.DEF_CONTENT_CHARSET);
			httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
			httppost.setEntity(sd);
			System.out.println("executing request " + httppost.getRequestLine());
			System.out.println("request body=\n"+EntityUtils.toString(httppost.getEntity()));
	            HttpResponse response = httpclient.execute(httppost);
			System.out.println(response.getStatusLine());
			
			int resp_code=response.getStatusLine().getStatusCode();
			if(resp_code==200)
			{
			
			BufferedReader in = new BufferedReader(new
					InputStreamReader(response.getEntity().getContent()));
					                StringBuffer sb = new StringBuffer("");
					                String line = "";
					                while ((line = in.readLine()) != null) {
					                    sb.append(line +"\n");
					                }
					                in.close();
					                System.out.println(sb);
					               try {
									json = new JSONObject(sb.toString());
									System.out.println("Access Token="+json.getString("access_token"));
									System.out.println("Token Type="+json.getString("token_type"));
									System.out.println("Expires in="+json.getInt("expires_in"));
									System.out.println("Refresh Token="+json.getString("refresh_token"));
									String access_token=json.getString("access_token");
									refresh_token=json.getString("refresh_token");
									JDBCConnection db=new JDBCConnection();
									db.update(teamchat_id, access_token, refresh_token);      
								      
									
					           	} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								     return resp_code;
						}
			return resp_code;           
					               
		}

}
