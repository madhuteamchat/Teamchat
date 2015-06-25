package com.teamchat.integration.youtube.search;


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

public class SearchByKeyword {
	  private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	  private JsonFactory JSON_FACTORY = new JacksonFactory();
	  private long NUMBER_OF_VIDEOS_RETURNED = 25;
	  private YouTube youtube;
	  String result="";
	  

	
	public String searchByKeyword(String apiKey, String queryTerm, long maxreturn) throws Exception {
		
		youtube = new YouTube.Builder(HTTP_TRANSPORT,JSON_FACTORY,null).build();
		NUMBER_OF_VIDEOS_RETURNED=maxreturn;
	      YouTube.Search.List search = youtube.search().list("id,snippet");
	      search.setKey(apiKey);
	      search.setQ(queryTerm);		
	      search.setType("video");
	      search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
	      search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
	      SearchListResponse searchResponse = search.execute();
	      
	      List<SearchResult> searchResultList = searchResponse.getItems();

	      if (searchResultList != null) {
	        result=prettyPrint(searchResultList.iterator(), queryTerm);
	      }
		return result;

	}
	
	private String prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {
		String res="";
//	    res+="<br>";
//	    res+="<br> &nbsp; First &nbsp; "+NUMBER_OF_VIDEOS_RETURNED+" videos for search on \" "+query+" \".";
//	    res+="<br>";
	    if (!iteratorSearchResults.hasNext()) {
	      System.out.println(" There aren't any results for your query.");
	      res+="<br>There aren't any results for your query.";
	    }

	    while (iteratorSearchResults.hasNext()) {

	      SearchResult singleVideo = iteratorSearchResults.next();
	      ResourceId rId = singleVideo.getId();

	      // Double checks the kind is video.
	      if (rId.getKind().equals("youtube#video")) {
	        Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().get("default");

	        res+="<div align=\"center\" ><br><h4> "+singleVideo.getSnippet().getTitle()+"</h4>";
	        res+="<br> Video Id : "+rId.getVideoId();
	        res+="<br><img src=\""+thumbnail.getUrl()+"\"/>";  //"\" height=\"278\" width=\"419\"/>";
	        res+="<br> link : https://www.youtube.com/watch?v="+rId.getVideoId();
//	        res+="<br><object width=\"425\" height=\"350\">";
//	        res+="<br><param name=\"movie\" value=\"https://www.youtube.com/v/"+rId.getVideoId()+"\" />";
//	        res+="<embed src=\"https://www.youtube.com/v/"+rId.getVideoId()+"\" type=\"application/x-shockwave-flash\" width=\"425\" height=\"350\" />";
	        res+="<br><br>";
	      }
	    }
	    return res;
	  }
}
