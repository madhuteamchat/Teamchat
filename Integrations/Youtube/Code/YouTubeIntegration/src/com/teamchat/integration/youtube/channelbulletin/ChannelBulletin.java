package com.teamchat.integration.youtube.channelbulletin;


import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
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
import com.teamchat.integration.youtube.bot.TeamchatURLlink;
import com.teamchat.integration.youtube.database.JDBCConnection;

public class ChannelBulletin {
	  private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  private JsonFactory JSON_FACTORY = new JacksonFactory();
	  private YouTube youtube;
	  private String VIDEO_ID = "";
	  String res="";
	  String client_id,client_secret;

	public ChannelBulletin(String client_id,String client_secret)
	{
		this.client_id=client_id;
		this.client_secret=client_secret;
	}
	  
	
	  public String postvideo(String videoid,String description,String uid) {

	    // Scope required to upload to YouTube.
		  VIDEO_ID=videoid;

	      // Authorization.
//	      Credential credential = authorize(scopes);
//	    	 Properties props = new Properties();
//	    	    InputStream is = null;
	    	 

	    	  /*  try {    // First try loading from the current directory
	    	   
	    	    File f = new File("uid.properties");
	    	        is = new FileInputStream( f );  	  }
	    catch ( Exception e ) { is = null; }*/
	    
	    try { 
//	    	        if(is==null)
//	    	        	return "You have to login";  
//	    	 is=getClass().getResourceAsStream(uid+".properties");
	    	  
	    	        // Try loading properties from the file (if found)
//	    	        props.load( is );
	    	
	    	JDBCConnection db=new JDBCConnection();
			String[] gc=db.retreive(uid);
	    	   
	    	GoogleCredential credentials = new GoogleCredential.Builder()
		      .setClientSecrets(client_id, client_secret)
		      .setJsonFactory(JSON_FACTORY).setTransport(HTTP_TRANSPORT).build()
		      .setRefreshToken(gc[1]).setAccessToken(gc[0]);

	      // YouTube object used to make all API requests.
	      youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials).setApplicationName(
	          "Teamchat").build();
	   
	      YouTube.Channels.List channelRequest = youtube.channels().list("contentDetails");
	      channelRequest.setMine("true");
	      /*
	       * Limits the results to only the data we need making your app more efficient.
	       */
	      ChannelListResponse channelResult;
	      channelRequest.setFields("items/contentDetails");
	      try{
	      channelResult = channelRequest.execute();
	    }
	      catch(Exception e)
	      {
	    	  return "You have to login";
	      }

	      /*
	       * Gets the list of channels associated with the user.
	       */
	      List<Channel> channelsList = channelResult.getItems();

	      if (channelsList != null) {
	        // Gets user's default channel id (first channel in list).
	        String channelId = channelsList.get(0).getId();
	  
	        ActivitySnippet snippet = new ActivitySnippet();
	        snippet.setChannelId(channelId);
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
	          res+="<br> - Video id : "+ newActivityInserted.getContentDetails().getBulletin().getResourceId().getVideoId();
	          res+="<br> - Description : " + newActivityInserted.getSnippet().getDescription();
	          res+="<br> - Posted on : " + newActivityInserted.getSnippet().getPublishedAt();
	          
	          TeamchatURLlink urlLink = new TeamchatURLlink();
	          String url="https://www.youtube.com/watch?v="+newActivityInserted.getContentDetails().getBulletin().getResourceId().getVideoId();
	          String urlEncoded = urlLink.createEmbeddedLink(url,"Youtube Bot","http");
	          res+="<br> - Link : "+urlEncoded;
	          
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
