package com.teamchat.youtube_integration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

public class YoutubeConnect {
	
	YouTube youtube;
	JSONObject json;
	private static String clientId="494950415885-fqv27scptt61fknafej5is92kd1hrbfe.apps.googleusercontent.com";
	private static String clientsecret="1HvoCJaWVFMciAfFyrZ9doSg"; 
	private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  private JsonFactory JSON_FACTORY = new JacksonFactory();
	  public static String uid="";
	  GoogleCredential credentials;
	
	
	public void getaccesstoken(String acode) throws ClientProtocolException, IOException
	{
		 
		String redirecturi ="https://localhost:8443/YoutubeIntegration/YoutubeCallBack";
		
	    HttpPost httppost = new
	    		HttpPost("https://accounts.google.com/o/oauth2/token");
	    HttpClient httpclient=new DefaultHttpClient();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>(5);
		nvps.add(new BasicNameValuePair("code", acode));
		nvps.add(new BasicNameValuePair("client_id", "494950415885-fqv27scptt61fknafej5is92kd1hrbfe.apps.googleusercontent.com"));
		nvps.add(new BasicNameValuePair("client_secret", "1HvoCJaWVFMciAfFyrZ9doSg"));
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
							      .setClientSecrets(clientId, clientsecret)
							      .setJsonFactory(JSON_FACTORY).setTransport(HTTP_TRANSPORT).build()
							      .setRefreshToken(json.getString("refresh_token")).setAccessToken(json.getString("access_token")).setExpiresInSeconds(json.getLong("expires_in"));
							
								        Properties props = new Properties();
								        props.setProperty("access_token", json.getString("access_token"));
								        props.setProperty("token_type", json.getString("token_type"));
								        props.setProperty("refresh_token", json.getString("refresh_token"));
								        props.setProperty("client_id", clientId);
								        props.setProperty("client_secret", clientsecret);
								        System.out.println("/home/intern11/"+uid+".properties");
								        File f = new File("/home/intern11/"+uid+".properties");
								        OutputStream out = new FileOutputStream( f );
								        props.store(out, "This is an optional header comment string");
								 
								        
								      
							      
								
				           	} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				               
				               
	}
	
}
