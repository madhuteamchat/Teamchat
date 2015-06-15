package com.teamchat.youtube_integration.connect;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.json.Property;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.common.collect.Lists;
import com.teamchat.youtube_integration.upload.MyUploads;

public class YoutubeConnect {
	
	
	  private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  private JsonFactory JSON_FACTORY = new JacksonFactory();
	  
	  
	  private Credential authorize(List<String> scopes) throws Exception {

		    // Load client secrets.
		    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
		        JSON_FACTORY, MyUploads.class.getResourceAsStream("/client_secrets.json"));


		    // Set up file credential store.
		    FileCredentialStore credentialStore = new FileCredentialStore(
		        new File(System.getProperty("user8.home"), ".credentials/youtube-api.json"),
		        JSON_FACTORY);

		    // Set up authorization code flow.
		    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
		        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes).setCredentialStore(credentialStore)
		        .build();

		    // Build the local server and bind it to port 9000
		    LocalServerReceiver localReceiver = new LocalServerReceiver.Builder().setPort(8080).build();

		    // Authorize.
		    return new AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user8");
		  }
	  
	  public void youtubeLogin()
	  {
		  youtubeLogout();
		  List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");

		    
		      // Authorization.
		      try {
				Credential credential = authorize(scopes);
				YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
				          "youtube-teamchat-integration").build();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
	  }
	  
	  public void youtubeLogout()
	  {
		  File f=new File(System.getProperty("user8.home"), ".credentials/youtube-api.json");
		  if(f.exists())
		  {
			  f.delete();
		  }
	  }
}
