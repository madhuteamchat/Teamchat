package com.teamchat.integration.youtube.search;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.teamchat.integration.youtube.bot.TeamchatURLlink;

public class SearchByTopic {
	  private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  private JsonFactory JSON_FACTORY = new JacksonFactory();
	  private long NUMBER_OF_VIDEOS_RETURNED = 5;
	  private long NUMBER_OF_TOPICS_RETURNED = 5;
	  private YouTube youtube;
	  String topic ,topicsId, apiKey,usrchoice="",res="";
	  public String checkNode="";

	    String freebaseId = "";ArrayNode an=null;
	  public SearchByTopic(String apiKey, String topic)
	  {
		  this.topic=topic;
		  this.apiKey=apiKey;
	  }
	  public ArrayNode gettopics() throws IOException
	  {
		      topicsId = getTopicId();
		      checkNode="empty";
		      if(topicsId.length() < 1) {
		        System.out.println("No topic will be applied to your search");
		        checkNode="Topic not found";
//		        return an;
		      }
		      return an;
	  }
	  public String searchByTopic(String queryTerm ) {
	

		    try {
		    	do {
		  	      System.out.print("Choose the number of the Freebase Node: "+queryTerm);
		  	    } while (!isValidIntegerSelection(queryTerm, an.size()));
		  
		  	    // Returns Topic id needed for YouTube Search.
		  	    JsonNode node = an.get(Integer.parseInt(queryTerm));
		  	    topicsId = node.get("mid").asText();

	     
	      youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
	          public void initialize(HttpRequest request) throws IOException {}})
	        .setApplicationName("youtube-cmdline-search-sample")
	        .build();

	      YouTube.Search.List search = youtube.search().list("id,snippet");
	 
	      search.setKey(apiKey);
	      search.setQ(queryTerm);
	      if(topicsId.length() > 0) {
	        search.setTopicId(topicsId);
	      }

	      search.setType("video");
	  
	      search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
	      search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
	      SearchListResponse searchResponse = search.execute();

	      List<SearchResult> searchResultList = searchResponse.getItems();

	      if(searchResultList != null) {
	        prettyPrint(searchResultList.iterator(), queryTerm, topicsId);
	      } else {
	        System.out.println("There were no results for your query.");
	        res="There were no results for your query.";
	      }
	    } catch (GoogleJsonResponseException e) {
	      System.err.println("There was a service error: " + e.getDetails().getCode() +
	          " : " + e.getDetails().getMessage());
	      e.printStackTrace();
	    } catch (IOException e) {
	      System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
	      e.printStackTrace();
	    }
		    return res;
	  }


	  /**
	   * The Java Freebase client library does not include search functionality, so we created a call
	   * directly via URL.  We use jackson functionality to put the JSON response into a POJO (Plain
	   * Old Java Object).  The additional classes to create the object from JSON were created based on
	   * the JSON response to make it easier to get the values we need.  For more info on jackson
	   * classes, please search on the term.
	   */
	  private String getTopicId() throws IOException {

	    String topicsId = "";

	    String topicQuery = topic;

	    HttpClient httpclient = new DefaultHttpClient();
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("query", topicQuery));
	    params.add(new BasicNameValuePair("limit", Long.toString(NUMBER_OF_TOPICS_RETURNED)));

	    String serviceURL = "https://www.googleapis.com/freebase/v1/search";
	    String url = serviceURL + "?" + URLEncodedUtils.format(params, "UTF-8");

	    HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
	    HttpEntity entity = httpResponse.getEntity();

	    if (entity != null) {
	        InputStream instream = entity.getContent();
	        try {
	          /*
	           * Converts JSON to a Tree.  I could have specified extra classes and done an exact map
	           * from JSON to POJO, but I was trying to keep the sample within one Java file.  If the
	           * .get() function calls here and in getUserChoice() aren't your cup of tea, feel free
	           * to create those classes and use them with the mapper.readValue() function.
	           */
	          ObjectMapper mapper = new ObjectMapper();
	          JsonNode rootNode = mapper.readValue(instream, JsonNode.class);

	          // Check that the response is valid.
	          if(rootNode.get("status").asText().equals("200 OK")) {
	            // I know the "result" field contains the list of results I need.
	            ArrayNode arrayNodeResults = (ArrayNode) rootNode.get("result");
	            // Only place we set the topicsId for a valid selection in this function.
	            topicsId = getUserChoice(arrayNodeResults);
	          }
	        } finally {
	          instream.close();
	        }
	    }
	    return topicsId;
	  }

	
	  private String getUserChoice(ArrayNode freebaseResults) throws IOException {

	    if(freebaseResults.size() < 1) {
	      return freebaseId;
	    }

	    for(int i = 0; i < freebaseResults.size(); i++) {
	      JsonNode node = freebaseResults.get(i);
	      System.out.print(" " + i + " = " + node.get("name").asText());
	      usrchoice+=" " + i + " = " + node.get("name").asText();
	      if(node.get("notable") != null) {
	        System.out.print(" (" + node.get("notable").get("name").asText() + ")");
	        usrchoice+=" (" + node.get("notable").get("name").asText() + ")        ";
	      }
	      System.out.println("");
	      an=freebaseResults;
	      freebaseId="notnull";
	    }

	    return freebaseId;
	  }

	  
	  public boolean isValidIntegerSelection(String input, int max) {
	    if (input.length() > 9)
	      return false;

	    boolean validNumber = false;
	    // Only accepts positive numbers of up to 9 numbers.
	    Pattern intsOnly = Pattern.compile("^\\d{1,9}$");
	    Matcher makeMatch = intsOnly.matcher(input);

	    if(makeMatch.find()){
	      int number = Integer.parseInt(makeMatch.group());
	      if((number >= 0) && (number < max)) {
	        validNumber = true;
	      }
	    }
	    return validNumber;
	  }

	
	  private void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query, String topicsId) {

	    res+="<br>";
	    if(!iteratorSearchResults.hasNext()) {
	      res="<br>There aren't any results for your query.";
	    }

	    while(iteratorSearchResults.hasNext()) {

	      SearchResult singleVideo = iteratorSearchResults.next();
	      ResourceId rId = singleVideo.getId();

	      // Double checks the kind is video.
	      if(rId.getKind().equals("youtube#video")) {
	        Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().get("default");

	        TeamchatURLlink urlLink = new TeamchatURLlink();
	        String url="https://www.youtube.com/watch?v="+rId.getVideoId();
	        String urlEncoded = urlLink.createEmbeddedLink(url,"Youtube Bot","http");
	        
	        res+="<div align=\"center\" ><br><h4> "+singleVideo.getSnippet().getTitle()+"</h4>";
	        res+="<br> Video Id : "+rId.getVideoId();
//	        res+="<br><img src=\""+thumbnail.getUrl()+"\" height=\"278\" width=\"419\"/>";
	        res+="<br> link : <a href=\""+urlEncoded+"\" target=\"_blank\">"+url+"</a>";	        
//	        res+="<br><object width=\"425\" height=\"350\">";
//	        res+="<br><param name=\"movie\" value=\"https://www.youtube.com/v/"+rId.getVideoId()+"\" />";
//	        res+="<embed src=\"https://www.youtube.com/v/"+rId.getVideoId()+"\" type=\"application/x-shockwave-flash\" width=\"425\" height=\"350\" />";
	        res+="</div>";
	      }
	    }
	  }
}
