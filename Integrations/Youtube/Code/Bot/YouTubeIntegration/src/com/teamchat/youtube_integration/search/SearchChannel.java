package com.teamchat.youtube_integration.search;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

public class SearchChannel {
	private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  private JsonFactory JSON_FACTORY = new JacksonFactory();
	  private long NUMBER_OF_CHANNELS_RETURNED = 5;
	  private YouTube youtube;
	  public ArrayList<String> cTitle=new ArrayList<String>();
	  public ArrayList<String> cId=new ArrayList<String>();
	  String result="";
	  

	
	public String channelSearch(String apiKey, String queryTerm) throws Exception {
		
		youtube = new YouTube.Builder(HTTP_TRANSPORT,JSON_FACTORY,null).build();

	      YouTube.Search.List search = youtube.search().list("id,snippet");
	      search.setKey(apiKey);
	      search.setQ(queryTerm);		
	      search.setType("channel");
	      search.setFields("items(id/kind,id/channelId,snippet/title,snippet/thumbnails/default/url)");
	      search.setMaxResults(NUMBER_OF_CHANNELS_RETURNED);
	      SearchListResponse searchResponse = search.execute();
	      
	      List<SearchResult> searchResultList = searchResponse.getItems();

	      if (searchResultList != null) {
	        result=prettyPrint(searchResultList.iterator(), queryTerm);
	      }
		return result;

	}
	
	private String prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {
		String res="";
	    
	    if (!iteratorSearchResults.hasNext()) {
	      System.out.println(" There aren't any results for your query.");
	      res+="<br>There aren't any results for your query.";
	    }

	    while (iteratorSearchResults.hasNext()) {

	      SearchResult singleChannel = iteratorSearchResults.next();
	      ResourceId rId = singleChannel.getId();

	      // Double checks the kind is video.
	      if (rId.getKind().equals("youtube#channel")) {
	        Thumbnail thumbnail = singleChannel.getSnippet().getThumbnails().get("default");
	        res+="<div align=\"center\" ><br><h4> "+singleChannel.getSnippet().getTitle()+"</h4>";
	        res+="<br> Channel Id : "+rId.getChannelId();
	        res+="<br><img src=\""+thumbnail.getUrl()+"\" height=\"198\" width=\"198\" />";
	        res+="<br><br></div>";
	        cTitle.add(singleChannel.getSnippet().getTitle());
	        cId.add(rId.getChannelId());
	      }
	    }
	    return res;
	  }
}
