package com.teamchat.integration.youtube.servlet;

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

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.teamchat.integration.youtube.database.JDBCConnection;
import com.teamchat.integration.youtube.properties.YoutubeProperty;

public class YoutubeConnect {
	
	YouTube youtube;
	JSONObject json;
	static String client_id;
	static String client_secret; 
	static String rid;
	String redirecturi;
	private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  private JsonFactory JSON_FACTORY = new JacksonFactory();
	  public static String uid="";
	  GoogleCredential credentials;
	  
	  public YoutubeConnect() {
		// TODO Auto-generated constructor stub
		  YoutubeProperty yp=new YoutubeProperty();
		  yp.loadParams();
		  client_id=yp.getClientId();
		  client_secret=yp.getClientSecret();
		  redirecturi=yp.getRedirectUrl();
	}
	
	
	public void getaccesstoken(String acode) throws ClientProtocolException, IOException
	{
		 
		
	    HttpPost httppost = new
	    		HttpPost("https://accounts.google.com/o/oauth2/token");
	    HttpClient httpclient=new DefaultHttpClient();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>(5);
		nvps.add(new BasicNameValuePair("code", acode));
		nvps.add(new BasicNameValuePair("client_id",client_id ));
		nvps.add(new BasicNameValuePair("client_secret", client_secret));
		nvps.add(new BasicNameValuePair("redirect_uri", redirecturi));
		nvps.add(new BasicNameValuePair("grant_type", "authorization_code"));
		UrlEncodedFormEntity sd=new UrlEncodedFormEntity(nvps,HTTP.DEF_CONTENT_CHARSET);
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
								credentials = new GoogleCredential.Builder()
							      .setClientSecrets(client_id, client_secret)
							      .setJsonFactory(JSON_FACTORY).setTransport(HTTP_TRANSPORT).build()
							      .setRefreshToken(json.getString("refresh_token")).setAccessToken(json.getString("access_token")).setExpiresInSeconds(json.getLong("expires_in"));
							
//								        Properties props = new Properties();
//								        props.setProperty(uid, json.getString("refresh_token"));
//								        File f = new File("uid.properties");
//								        OutputStream out = new FileOutputStream( f );
//								        props.store(out, "This is an optional header comment string");
								 
								        JDBCConnection db=new JDBCConnection();
								        db.insert(uid, json.getString("access_token"), json.getString("refresh_token"));
								        new TeamchatPost().postMsg("Successfully connected to Youtube!!", rid);
								      
							      
								
				           	} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				               
				               
	}
	
}
