package com.teamchat.youtube_integration.channelbulletin;


import java.io.File;
import java.util.Calendar;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Activity;
import com.google.api.services.youtube.model.ActivityContentDetails;
import com.google.api.services.youtube.model.ActivityContentDetails.Bulletin;
import com.google.api.services.youtube.model.ActivitySnippet;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.ResourceId;
import com.google.common.collect.Lists;

public class ChannelBulletin {
	  private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  private JsonFactory JSON_FACTORY = new JacksonFactory();
	  private YouTube youtube;
	  private String VIDEO_ID = "";
	  String res="";

	
	  private Credential authorize(List<String> scopes) throws Exception {

	    // Load client secrets.
	    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
	        JSON_FACTORY, ChannelBulletin.class.getResourceAsStream("/client_secrets.json"));

	
	    // Set up file credential store.
	    FileCredentialStore credentialStore = new FileCredentialStore(
	        new File(System.getProperty("user8.home"), ".credentials/youtube-api.json"),
	        JSON_FACTORY);

	    // Set up authorization code flow.
	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes).setCredentialStore(credentialStore)
	        .build();

	    // Build the local server and bind it to port 8080
	    LocalServerReceiver localReceiver = new LocalServerReceiver.Builder().setPort(8080).build();

	    // Authorize.
	    return new AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user");
	  }

	
	  public String postvideo(String videoid,String description) {

	    // Scope required to upload to YouTube.
		  VIDEO_ID=videoid;
	    List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");

	    try {
	      // Authorization.
	      Credential credential = authorize(scopes);

	      // YouTube object used to make all API requests.
	      youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
	          "youtube-cmdline-channelbulletin-sample").build();
	   
	      YouTube.Channels.List channelRequest = youtube.channels().list("contentDetails");
	      channelRequest.setMine("true");
	      /*
	       * Limits the results to only the data we need making your app more efficient.
	       */
	      channelRequest.setFields("items/contentDetails");
	      ChannelListResponse channelResult = channelRequest.execute();

	      /*
	       * Gets the list of channels associated with the user.
	       */
	      List<Channel> channelsList = channelResult.getItems();

	      if (channelsList != null) {
	        // Gets user's default channel id (first channel in list).
	        String channelId = channelsList.get(0).getId();
	  
	        ActivitySnippet snippet = new ActivitySnippet();
	        snippet.setChannelId(channelId);
	        Calendar cal = Calendar.getInstance();
	        snippet.setDescription(description);

	   
	        ResourceId resource = new ResourceId();
	        resource.setKind("youtube#video");
	        resource.setVideoId(VIDEO_ID);

	        Bulletin bulletin = new Bulletin();
	        bulletin.setResourceId(resource);

	        // We construct the ActivityContentDetails now that we have the Bulletin.
	        ActivityContentDetails contentDetails = new ActivityContentDetails();
	        contentDetails.setBulletin(bulletin);

	        /*
	         * Finally, we construct the activity we will write to YouTube via the API. We set the
	         * snippet (covers description and channel we are posting to) and the content details
	         * (covers video id and type).
	         */
	        Activity activity = new Activity();
	        activity.setSnippet(snippet);
	        activity.setContentDetails(contentDetails);

	        /*
	         * We specify the parts (contentDetails and snippet) we will write to YouTube. Those also
	         * cover the parts that are returned.
	         */
	        YouTube.Activities.Insert insertActivities =
	            youtube.activities().insert("contentDetails,snippet", activity);
	        // This returns the Activity that was added to the user's YouTube channel.
	        Activity newActivityInserted = insertActivities.execute();

	        if (newActivityInserted != null) {
	          
	          res+="<br>New Activity inserted of type " + newActivityInserted.getSnippet().getType();
	          res+="<br> - Video id "+ newActivityInserted.getContentDetails().getBulletin().getResourceId().getVideoId();
	          res+="<br> - Description: " + newActivityInserted.getSnippet().getDescription();
	          res+="<br> - Posted on " + newActivityInserted.getSnippet().getPublishedAt();
	          
	        } else {
	          res+="<br>Activity failed.";
	        }

	      } else {
	        res+="No channels are assigned to this user.";
	      }
	    } catch (GoogleJsonResponseException e) {
	      e.printStackTrace();
	      System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
	          + e.getDetails().getMessage());

	    } catch (Throwable t) {
	      t.printStackTrace();
	    }
		  return res;
	  }
}
