package com.teamchat.integrations.wikipedia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.annotations.OnMsg;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class WikiSearch {
	public static String searchKeyword = new String();

	@OnKeyword("wiki")
	public void getSearchKeyword(TeamchatAPI api) throws Exception {
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestion("Enter search keyword")
						.setReplyScreen(
								api.objects()
										.form()
										.addField(
												api.objects().input()
														.label("Search")
														.name("search"))).alias("searchWikipedia")));

	}
	
	@OnMsg
	public void onMsg(TeamchatAPI api) throws IllegalStateException, IOException {
		String message = api.context().currentChatlet().raw();
		search(api, message);
	}
	
	@OnAlias("searchWikipedia")
	public void SearchWikipedia(TeamchatAPI api) throws IllegalStateException, IOException {
		searchKeyword = api.context().currentReply().getField("search");
		search(api, searchKeyword);
	}
	
	public void search(TeamchatAPI api, String searchKeyword) throws IllegalStateException, IOException {
		
		String searchEncoded = URLEncoder.encode(searchKeyword, "UTF-8");
		
		String url = "https://en.wikipedia.org/w/api.php?action=opensearch&search="+searchEncoded+"&format=json";
		
		System.out.println(url);
		
		HttpClient client = HttpClientBuilder.create().build(); 
		
		HttpGet request = new HttpGet(url);

		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString();

		System.out.println("Query executed.");

		JSONArray result = new JSONArray(output);
		
		JSONArray firstResultArray = (JSONArray) result.get(1); 
		String firstResultTitle = (String) firstResultArray.get(0);
		System.out.println(firstResultTitle.toString());
		
		JSONArray details = (JSONArray) result.get(2);
		String detailsOfSearch = (String) details.get(0);
		System.out.println(detailsOfSearch.toString());
		
		JSONArray linkList = (JSONArray) result.get(3);
		String link = (String) linkList.get(0);

		//	String toDisplay = "Read more at:" + link;
		api.performPostInCurrentRoom(new PrimaryChatlet().setQuestionHtml("<h3>"+firstResultTitle+"</h3><br><h5>"+detailsOfSearch+"</h5><br><a href='"+link+"' target='_blank'>Read more</a>"));
	
	}
	
}