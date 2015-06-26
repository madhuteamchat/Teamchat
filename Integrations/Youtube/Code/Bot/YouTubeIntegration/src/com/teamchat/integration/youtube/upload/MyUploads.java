package com.teamchat.integration.youtube.upload;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;

public class MyUploads {
	  private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  private JsonFactory JSON_FACTORY = new JacksonFactory();
	  private YouTube youtube;
	  String res="";
	  String client_id,client_secret;

	public MyUploads(String client_id,String client_secret)
	{
		this.client_id=client_id;
		this.client_secret=client_secret;
	}

	
	  public String myUpload(String uid) {

	    // Scope required to upload to YouTube.

	      // Authorization.
//	      Credential credential = authorize(scopes);
	    	 Properties props = new Properties();
	    	    InputStream is = null;
	    	 

	    	    try {    // First try loading from the current directory
	    	   
	    	    File f = new File("uid.properties");
	    	        is = new FileInputStream( f );  	  }
	    catch ( Exception e ) { is = null; }
	    
	    try { 
	    	        if(is==null)
	    	        	return "You have to login";
//	    	 is=getClass().getResourceAsStream(uid+".properties");
	    	  
	    	        // Try loading properties from the file (if found)
	    	        props.load( is );
	    	        System.out.println("myuploads....."+props.getProperty(uid));
	    	GoogleCredential credentials = new GoogleCredential.Builder()
		      .setClientSecrets(client_id, client_secret)
		      .setJsonFactory(JSON_FACTORY).setTransport(HTTP_TRANSPORT).build()
		      .setRefreshToken(props.getProperty(uid));
	      
	      // YouTube object used to make all API requests.
	      youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials).setApplicationName(
	          "Teamchat").build();

	      YouTube.Channels.List channelRequest = youtube.channels().list("contentDetails");
	      channelRequest.setMine("true");
	      /*
	       * Limits the results to only the data we need which makes things more efficient.
	       */
	      ChannelListResponse channelResult;
	      channelRequest.setFields("items/contentDetails,nextPageToken,pageInfo");
	      try{
	      channelResult = channelRequest.execute();
	      }
	      catch(Exception e)
	      {
	    	  return "You have to login";
	      }

	      /*
	       * Gets the list of channels associated with the user. This sample only pulls the uploaded
	       * videos for the first channel (default channel for user).
	       */
	      List<Channel> channelsList = channelResult.getItems();

	      if (channelsList != null) {
	        // Gets user's default channel id (first channel in list).
	        String uploadPlaylistId =
	            channelsList.get(0).getContentDetails().getRelatedPlaylists().getUploads();

	        // List to store all PlaylistItem items associated with the uploadPlaylistId.
	        List<PlaylistItem> playlistItemList = new ArrayList<PlaylistItem>();

	  
	        YouTube.PlaylistItems.List playlistItemRequest =
	            youtube.playlistItems().list("id,contentDetails,snippet");
	        playlistItemRequest.setPlaylistId(uploadPlaylistId);

	        // This limits the results to only the data we need and makes things more efficient.
	        playlistItemRequest.setFields(
	            "items(contentDetails/videoId,snippet/title,snippet/publishedAt),nextPageToken,pageInfo");

	        String nextToken = "";

	        // Loops over all search page results returned for the uploadPlaylistId.
	        do {
	          playlistItemRequest.setPageToken(nextToken);
	          PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();

	          playlistItemList.addAll(playlistItemResult.getItems());

	          nextToken = playlistItemResult.getNextPageToken();
	        } while (nextToken != null);

	        // Prints results.
	        prettyPrint(playlistItemList.size(), playlistItemList.iterator());
	      }

	    } catch (GoogleJsonResponseException e) {
	      e.printStackTrace();
	      System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
	          + e.getDetails().getMessage());

	    } catch (Throwable t) {
	      t.printStackTrace();
	    }
//	    System.clearProperty("user8.home");
	    return res;
	  }

	
	  private void prettyPrint(int size, Iterator<PlaylistItem> playlistEntries) {
	    res+="<br><br>Total Videos Uploaded: " + size+"<br>";
	    while (playlistEntries.hasNext()) {
	      PlaylistItem playlistItem = playlistEntries.next();	      
	      res+="<br> Video Name  = " + playlistItem.getSnippet().getTitle();
	      res+="<br> Video Id    = " + playlistItem.getContentDetails().getVideoId();
	      res+="<br> Upload Date = " +playlistItem.getSnippet().getPublishedAt();
	      res+="<br>";
	    }
	  }
}
