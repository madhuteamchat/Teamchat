package com.teamchat.integration.gdoc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.teamchat.integration.database.User;

/**
 * 
 * @author Mallika Gogoi
 *
 */
public class GDocConnect {
Drive drive;
JSONObject json;
static String client_id;
static String client_secret; 
static String redirecturi;
private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
private JsonFactory JSON_FACTORY = new JacksonFactory();
public static String uid="";
GoogleCredential credentials;
Properties configPropertyFile;

public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException
{

	Properties prop = new Properties();
	prop.load(type.getClassLoader().getResourceAsStream(fileName));
	return prop;

}
  
  public void getaccesstoken(String acode,String email) throws ClientProtocolException, IOException
	{
	  try {
		  configPropertyFile = loadPropertyFromClasspath("gdrive.properties", GDocConnect.class);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	  client_id=configPropertyFile.getProperty("client_id");
	  client_secret=configPropertyFile.getProperty("client_secret");
	  redirecturi=configPropertyFile.getProperty("redirecturi");
	//	String redirecturi ="http://interns.teamchat.com:8084/GDrive/GDocCallback";
	//	String client_id="981497132714-mh39fapeju1av8vt1kq8pbhco34itju1.apps.googleusercontent.com";
	//	String client_secret="HTVEauW_gvjXBeSQgejqylFI";
	    HttpPost httppost = new
	    		HttpPost("https://accounts.google.com/o/oauth2/token");
	    HttpClient httpclient=HttpClientBuilder.create().build();
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
								credentials = new GoogleCredential.Builder()
							      .setClientSecrets(client_id, client_secret)
							      .setJsonFactory(JSON_FACTORY).setTransport(HTTP_TRANSPORT).build()
							      .setAccessToken(json.getString("access_token").toString())
							      .setExpiresInSeconds(json.getLong("expires_in"));
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
						
				           	} catch (Exception e) 
				           	{
								e.printStackTrace();
							}			               
	}	
}