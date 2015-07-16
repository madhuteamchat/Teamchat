package com.teamchat.integration.youtube.playlist;


import java.io.IOException;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.google.api.services.youtube.model.PlaylistStatus;
import com.google.api.services.youtube.model.ResourceId;
import com.teamchat.integration.youtube.database.JDBCConnection;

public class PlaylistUpdates {
	  private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  private JsonFactory JSON_FACTORY = new JacksonFactory();
	  private YouTube youtube;
	  String res="";
	  String client_id,client_secret;

	public PlaylistUpdates(String client_id,String client_secret)
	{
		this.client_id=client_id;
		this.client_secret=client_secret;
	}
	  
	  public String updatePlaylist( String pltitle,String pldes,int vno,String[] vid,String uid) {

	    // General read/write scope for YouTube APIs.

	      // Authorization.
//	      Credential credential = authorize(scopes);
//	    	 Properties props = new Properties();
//	    	    InputStream is = null;
	    	 

	    	/*    try {    // First try loading from the current directory
	    	   
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
	      youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credentials)
	        .setApplicationName("Teamchat")
	        .build();

	      // Creates a new playlist in the authorized user's channel.
	      String playlistId = insertPlaylist(pltitle,pldes);

	      // If a valid playlist was created, adds a new playlistitem with a video to that playlist.
	      for(int i=0;i<vno;i++)
	      {
	    	  if(vid[i]!=null)
	    		  insertPlaylistItem(playlistId, vid[i]);
	      }

	    } catch (GoogleJsonResponseException e) {
	      System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
	      e.printStackTrace();
	    } catch (IOException e) {
	      System.err.println("IOException: " + e.getMessage());
	      e.printStackTrace();
	    } catch (Throwable t) {
	      System.err.println("Throwable: " + t.getMessage());
	      t.printStackTrace();
	    }
	    return res;
	  }

	  /**
	   * Creates YouTube Playlist and adds it to the authorized account.
	   */
	  private String insertPlaylist(String pltitle,String pldes) throws IOException {

	    /*
	     * We need to first create the parts of the Playlist before the playlist itself.  Here we are
	     * creating the PlaylistSnippet and adding the required data.
	     */
	    PlaylistSnippet playlistSnippet = new PlaylistSnippet();
	    playlistSnippet.setTitle(pltitle);
	    playlistSnippet.setDescription(pldes);

	    // Here we set the privacy status (required).
	    PlaylistStatus playlistStatus = new PlaylistStatus();
	    playlistStatus.setPrivacyStatus("private");

	    /*
	     * Now that we have all the required objects, we can create the Playlist itself and assign the
	     * snippet and status objects from above.
	     */
	    Playlist youTubePlaylist = new Playlist();
	    youTubePlaylist.setSnippet(playlistSnippet);
	    youTubePlaylist.setStatus(playlistStatus);

	    /*
	     * This is the object that will actually do the insert request and return the result.  The
	     * first argument tells the API what to return when a successful insert has been executed.  In
	     * this case, we want the snippet and contentDetails info.  The second argument is the playlist
	     * we wish to insert.
	     */
	    YouTube.Playlists.Insert playlistInsertCommand =
	        youtube.playlists().insert("snippet,status", youTubePlaylist);
	    Playlist playlistInserted ;
	    try{
	    playlistInserted = playlistInsertCommand.execute();
	  }
      catch(Exception e)
      {
    	  return "You have to login";
      }

	    res+="<br>New Playlist name: " + playlistInserted.getSnippet().getTitle();
	    res+="<br> - Privacy : " + playlistInserted.getStatus().getPrivacyStatus();
	    res+="<br> - Description : " + playlistInserted.getSnippet().getDescription();
	    res+="<br> - Posted : " + playlistInserted.getSnippet().getPublishedAt();
	    res+="<br> - Channel : " + playlistInserted.getSnippet().getChannelId() + "<br>";
	    return playlistInserted.getId();

	  }

	  /**
	   * Creates YouTube PlaylistItem with specified video id and adds it to the specified playlist id
	   * for the authorized account.
	   *
	   * @param playlistId assign to newly created playlistitem
	   * @param videoId YouTube video id to add to playlistitem
	   */
	  private String insertPlaylistItem(String playlistId, String videoId) throws IOException {

	    /*
	     * The Resource type (video,playlist,channel) needs to be set along with the resource id. In
	     * this case, we are setting the resource to a video id, since that makes sense for this
	     * playlist.
	     */
	    ResourceId resourceId = new ResourceId();
	    resourceId.setKind("youtube#video");
	    resourceId.setVideoId(videoId);

	    /*
	     * Here we set all the information required for the snippet section.  We also assign the
	     * resource id from above to the snippet object.
	     */
	    PlaylistItemSnippet playlistItemSnippet = new PlaylistItemSnippet();
//	    playlistItemSnippet.setTitle(vtitle);
	    playlistItemSnippet.setPlaylistId(playlistId);
	    playlistItemSnippet.setResourceId(resourceId);

	    /*
	     * Now that we have all the required objects, we can create the PlaylistItem itself and assign
	     * the snippet object from above.
	     */
	    PlaylistItem playlistItem = new PlaylistItem();
	    playlistItem.setSnippet(playlistItemSnippet);

	    /*
	     * This is the object that will actually do the insert request and return the result.  The
	     * first argument tells the API what to return when a successful insert has been executed.  In
	     * this case, we want the snippet and contentDetails info.  The second argument is the
	     * playlistitem we wish to insert.
	     */
	    YouTube.PlaylistItems.Insert playlistItemsInsertCommand =
	        youtube.playlistItems().insert("snippet,contentDetails", playlistItem);
	    PlaylistItem returnedPlaylistItem = playlistItemsInsertCommand.execute();

	    res+="<br>New PlaylistItem name: " + returnedPlaylistItem.getSnippet().getTitle();
	    res+="<br>  - Video id : " + returnedPlaylistItem.getSnippet().getResourceId().getVideoId();
	    res+="<br>  - Posted : " + returnedPlaylistItem.getSnippet().getPublishedAt();
	    res+="<br>  - Channel : " + returnedPlaylistItem.getSnippet().getChannelId()+"<br>";
	    return returnedPlaylistItem.getId();

	  }
}
