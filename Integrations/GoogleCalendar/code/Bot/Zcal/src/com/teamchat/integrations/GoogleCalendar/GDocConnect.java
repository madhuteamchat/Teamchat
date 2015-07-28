package com.teamchat.integrations.GoogleCalendar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;//;auth.oauth2.Credential;
//import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.services.drive.Drive;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

public class GDocConnect {
///Drive drive;
public Calendar cal;
public JSONObject json;
public static String client_id;
public static String client_secret; 
private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
private static final JsonFactory JSON_FACTORY = new JacksonFactory();
public static String uid="";
//GoogleCredential credentials;
  
  public void getaccesstoken(String acode,String email) throws ClientProtocolException, IOException
	{
		 
		String redirecturi ="http://interns.teamchat.com:8081/Zcal/GDocCallback";
		 client_id="558193257225-ak2bb44ne3gsqp80ln4cur9nl332c5jp.apps.googleusercontent.com";
		 client_secret="MF2SqJpKYQWBTp0J411wtrml";
	    HttpPost httppost = new
	    		HttpPost("https://accounts.google.com/o/oauth2/token");
	    HttpClient httpclient=new DefaultHttpClient();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>(5);
		nvps.add(new BasicNameValuePair("code", acode));
		nvps.add(new BasicNameValuePair("client_id",client_id ));
		nvps.add(new BasicNameValuePair("client_secret", client_secret));
		nvps.add(new BasicNameValuePair("redirect_uri", redirecturi));
		nvps.add(new BasicNameValuePair("grant_type", "authorization_code"));
		UrlEncodedFormEntity sd=new UrlEncodedFormEntity(nvps,"utf-8");
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
							//	User u=new User(email,json.getString("access_token"),json.getInt("expires_in"));
								
							      System.out.println("BUILD hoga abhi "); 
							      
							      User u=new User();
									Boolean b=u.checkData();
	                                if(json.getLong("expires_in")!=0&&b==true)
	                                {	
									User user=new User(email,json.getString("access_token"),json.getInt("expires_in"));
	                                }
	                                else
	                                {

									User user1=new User(email,json.getInt("expires_in"));
									User user=new User(email,json.getString("access_token"),json.getInt("expires_in"));
									
	                                }  												
							  
							      
							      
							      
							      
							      

							/*	GoogleCredential credentials = new GoogleCredential.Builder()
							      .setClientSecrets(client_id, client_secret)
							      .setJsonFactory(JSON_FACTORY).setTransport(HTTP_TRANSPORT).build()
							    //  .setRefreshToken(json.getString("refresh_token"))
							      .setAccessToken(json.getString("access_token")).setExpiresInSeconds(json.getLong("expires_in"));
							    Calendar c=getCalendar(json.getString("access_token"));  
								System.out.println("BUILD hoga  "); 
								List<Event> items=list(c);
								String html = "";
								for (Event event : items) {
									DateTime start = event.getStart().getDateTime();
									if (start == null) {
										start = event.getStart().getDate();
									}
									html += event.getSummary() + " " + start + "<br/>";
								}
								System.out.println(html);
								        /*Properties props = new Properties();
								        props.setProperty(email, json.getString("access_token"));
								        File f = new File("uid.properties");
								        OutputStream out = new FileOutputStream( f );
								        props.store(out, "This is an optional header comment string");
								        out.close();*/
				           	} catch (Exception e) 
				           	{
								e.printStackTrace();
							}
			               
	}
	 private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";

  public com.google.api.services.calendar.Calendar getCalendar(String accesstoken)
			throws IOException {
		
		 
	  System.out.println("Access Token IS:"+accesstoken);
	GoogleCredential credentials = new GoogleCredential.Builder()
	      .setClientSecrets(client_id, client_secret)
	      .setJsonFactory(JSON_FACTORY).setTransport(HTTP_TRANSPORT).build()
	      .setAccessToken(accesstoken);
		
		Calendar c =new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
		.setApplicationName(APPLICATION_NAME).build();

		return c;
	}
  public static List<Event> list(Calendar calendar) throws IOException {
		// Build a new authorized API client service.getCalendar
		// Note: Do not confuse this class with the
		// com.google.api.services.calendar.model.Calendar class.

		// List the next 10 events from the primary calendar.
		DateTime now = new DateTime(System.currentTimeMillis());
		Events events = calendar.events().list("primary").setMaxResults(10)
				.setTimeMin(now).setOrderBy("startTime").setSingleEvents(true)
				.execute();

		List<Event> items = events.getItems();

		return items;

		// return null;
	}
}

