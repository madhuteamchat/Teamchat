package com.teamchat.youtube_integration.upload;


import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.common.collect.Lists;

public class MyUploads {
	  private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  private JsonFactory JSON_FACTORY = new JacksonFactory();
	  private YouTube youtube;
	  String res="";
	  
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

	
	  public String myUpload() {

	    // Scope required to upload to YouTube.
	    List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");

	    try {
	      // Authorization.
	      Credential credential = authorize(scopes);
	      
	      // YouTube object used to make all API requests.
	      youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
	          "youtube-cmdline-myuploads-sample").build();

	      YouTube.Channels.List channelRequest = youtube.channels().list("contentDetails");
	      channelRequest.setMine("true");
	      /*
	       * Limits the results to only the data we need which makes things more efficient.
	       */
	      channelRequest.setFields("items/contentDetails,nextPageToken,pageInfo");
	      ChannelListResponse channelResult = channelRequest.execute();

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
