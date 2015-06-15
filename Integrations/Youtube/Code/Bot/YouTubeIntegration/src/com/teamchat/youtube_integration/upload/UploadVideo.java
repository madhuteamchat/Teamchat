package com.teamchat.youtube_integration.upload;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.google.common.collect.Lists;


public class UploadVideo {
	  private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  private JsonFactory JSON_FACTORY = new JacksonFactory();
	  private YouTube youtube;

	  /* Global instance of the format used for the video being uploaded (MIME type). */
	  private String VIDEO_FILE_FORMAT = "video/*";
	  String res="";

	  private Credential authorize(List<String> scopes) throws Exception {

	    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
	        JSON_FACTORY, UploadVideo.class.getResourceAsStream("/client_secrets.json"));


	    FileCredentialStore credentialStore = new FileCredentialStore(
	        new File(System.getProperty("user2.home"), ".credentials/youtube-api.json"),
	        JSON_FACTORY);

	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes).setCredentialStore(credentialStore)
	        .build();

	    LocalServerReceiver localReceiver = new LocalServerReceiver.Builder().setPort(8080).build();

	    // Authorize.
	    return new AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user");
	  }

	
	  public String uploadVideo(String location,String vtitle, String vdescription,String[] vtags) {

	    // Scope required to upload to YouTube.
	    List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.upload");

	    try {
	      // Authorization.
	      Credential credential = authorize(scopes);

	      // YouTube object used to make all API requests.
	      youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
	          "youtube-cmdline-uploadvideo-sample").build();

	      // We get the user selected local video file to upload.
	      File videoFile = new File(location);
	      System.out.println("You chose " + videoFile + " to upload.");

	      // Add extra information to the video before uploading.
	      Video videoObjectDefiningMetadata = new Video();

	  
	      VideoStatus status = new VideoStatus();
	      status.setPrivacyStatus("public");
	      videoObjectDefiningMetadata.setStatus(status);
	      VideoSnippet snippet = new VideoSnippet();

	
	      snippet.setTitle(vtitle);
	      snippet.setDescription(vdescription);

	      // Set your keywords.
	      List<String> tags = new ArrayList<String>();
	      for(int i=0;i<vtags.length;i++)
	      tags.add(vtags[i]);
	      snippet.setTags(tags);

	      // Set completed snippet to the video object.
	      videoObjectDefiningMetadata.setSnippet(snippet);

	      InputStreamContent mediaContent = new InputStreamContent(
	          VIDEO_FILE_FORMAT, new BufferedInputStream(new FileInputStream(videoFile)));
	      mediaContent.setLength(videoFile.length());

	      /*
	       * The upload command includes: 1. Information we want returned after file is successfully
	       * uploaded. 2. Metadata we want associated with the uploaded video. 3. Video file itself.
	       */
	      YouTube.Videos.Insert videoInsert = youtube.videos()
	          .insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent);

	      // Set the upload type and add event listener.
	      MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();

	  
	      uploader.setDirectUploadEnabled(false);

	      MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
	        public void progressChanged(MediaHttpUploader uploader) throws IOException {
	          switch (uploader.getUploadState()) {
	            case INITIATION_STARTED:
	              System.out.println("Initiation Started");
	              break;
	            case INITIATION_COMPLETE:
	              System.out.println("Initiation Completed");
	              break;
	            case MEDIA_IN_PROGRESS:
	              System.out.println("Upload in progress");
	              System.out.println("Upload percentage: " + uploader.getProgress());
	              break;
	            case MEDIA_COMPLETE:
	              System.out.println("Upload Completed!");
	              break;
	            case NOT_STARTED:
	              System.out.println("Upload Not Started!");
	              break;
	          }
	        }
	      };
	      uploader.setProgressListener(progressListener);

	      // Execute upload.
	      Video returnedVideo = videoInsert.execute();

	      res+="<br>Uploaded Successfully";
	      res+="<br>  - Id: " + returnedVideo.getId();
	      res+="<br>  - Title: " + returnedVideo.getSnippet().getTitle();
	      res+="<br>  - Tags: " + returnedVideo.getSnippet().getTags();
	      res+="<br>  - Privacy Status: " + returnedVideo.getStatus().getPrivacyStatus();

	    } catch (GoogleJsonResponseException e) {
	      System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
	          + e.getDetails().getMessage());
	      e.printStackTrace();
	    } catch (IOException e) {
	      System.err.println("IOException: " + e.getMessage());
	      res+="Upload Failed<br> Enter the location correctly.";
	      e.printStackTrace();
	    } catch (Throwable t) {
	      System.err.println("Throwable: " + t.getMessage());
	      t.printStackTrace();
	    }
	    return res;
	  }

}
