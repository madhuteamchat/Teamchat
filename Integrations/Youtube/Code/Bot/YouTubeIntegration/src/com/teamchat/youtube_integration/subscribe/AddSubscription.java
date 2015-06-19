package com.teamchat.youtube_integration.subscribe;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionSnippet;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.common.collect.Lists;

public class AddSubscription {
	  private  HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  private  JsonFactory JSON_FACTORY = new JacksonFactory();
	  private  YouTube youtube;
	  String res="";

	 
	  public String subscribe(String channelId,String uid) {

	    // An OAuth 2 access scope that allows for full read/write access.

	      // Authorization.
//	      Credential credential = authorize(scopes);
	    	 Properties props = new Properties();
	    	    InputStream is = null;
	    	 

	    	    try {    // First try loading from the current directory
	    	   
	    	    File f = new File("/home/intern11/"+uid+".properties");
	    	        is = new FileInputStream( f );  	  }
	    catch ( Exception e ) { is = null; }
	    
	    try { 
	    	        if(is==null)
	    	        	return "You have to login";	  
//	    	 is=getClass().getResourceAsStream(uid+".properties");
	    	  
	    	        // Try loading properties from the file (if found)
	    	        props.load( is );
	    	   
	    	GoogleCredential credentials = new GoogleCredential.Builder()
		      .setClientSecrets(props.getProperty("client_id"), props.getProperty("client_secret"))
		      .setJsonFactory(JSON_FACTORY).setTransport(HTTP_TRANSPORT).build()
		      .setRefreshToken(props.getProperty("refresh_token")).setAccessToken(props.getProperty("access_token"));

	      // YouTube object used to make all API requests.
	      youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials).setApplicationName(
	          "youtube-cmdline-addsubscription-sample").build();

	    

	      // We create a resourceId with channel id.
	      ResourceId resourceId = new ResourceId();
	      resourceId.setChannelId(channelId);
	      resourceId.setKind("youtube#channel");

	      // We create a snippet with ResourceId.
	      SubscriptionSnippet snippet = new SubscriptionSnippet();
	      snippet.setResourceId(resourceId);

	      // We create a subscription request with snippet.
	      Subscription subscription = new Subscription();
	      subscription.setSnippet(snippet);

	      /*
	       * The subscription insert command includes: 1. Information we want returned after file is
	       * successfully uploaded. 2. Subscription metadata we want to insert.
	       */
	      YouTube.Subscriptions.Insert subscriptionInsert =
	          youtube.subscriptions().insert("snippet,contentDetails", subscription);

	      // Execute subscription.
	      Subscription returnedSubscription;
	      try
	      {
	      returnedSubscription = subscriptionInsert.execute();
	    }
	      catch(Exception e)
	      {
	    	  return "You have to login";
	      }
	      Thumbnail thumbnail = returnedSubscription.getSnippet().getThumbnails().get("default");
	      res+="<br><p align=\"center\" >Channel Subscribed</p>";
	      res+="<br> -Id : "+returnedSubscription.getId();
	      res+="<br> - Title : "+returnedSubscription.getSnippet().getTitle();
	      res+="<br><img src=\""+thumbnail.getUrl()+"\" height=\"198\" width=\"198\" />";

	    } catch (GoogleJsonResponseException e) {
	      System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
	          + e.getDetails().getMessage());
	      e.printStackTrace();

	    } catch (IOException e) {
	      System.err.println("IOException: " + e.getMessage());
	      e.printStackTrace();
	    } catch (Throwable t) {
	      System.err.println("Throwable: " + t.getMessage());
	      t.printStackTrace();
	    }

//	    System.clearProperty("user8.home");
	    return res;
	  }

	
}
