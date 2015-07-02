package com.teamchat.integration.Gosquared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONObject;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;


public class Test1 {
	
	
	String need;
	
	
	public String onCreate11(TeamchatAPI api, String apikey1, String stoken) throws IOException {
//		ArrayList<String> a = new ArrayList<String>();
//		ArrayList<String> b = new ArrayList<String>();
//		ArrayList<Integer> c = new ArrayList<Integer>();
//		ArrayList<Integer> d = new ArrayList<Integer>();
//		ArrayList<Integer> e = new ArrayList<Integer>();
//	
		String result = "";
		URL urldemo = new URL(
				"https://api.gosquared.com/now/v3/sources?api_key="+apikey1+"&site_token="+stoken);
		URLConnection yc;
		yc = urldemo.openConnection(); // AK65YLGDU2HMH6GY&site_token=GSN-621790-C
		BufferedReader in = new BufferedReader(new InputStreamReader(
				yc.getInputStream()));
		String inputline;
		while ((inputline = in.readLine()) != null) {
			result += inputline;

			System.out.println(inputline + "\n");
		}
		in.close();
	
	//https://api.gosquared.com/now/v3/sources?api_key=AK65YLGDU2HMH6GY&site_token=GSN-621790-C
		JSONObject ja = new JSONObject(result);
	   int other = ja.getInt("direct");
	  
	   JSONObject jobj=ja.getJSONObject("site");
	   int sitevisitors=jobj.getInt("visitors");
	   
	   JSONObject jobj1=ja.getJSONObject("social");
	   int socialvisitors=jobj1.getInt("visitors");
	   
	   JSONObject jobj2=ja.getJSONObject("organic");
	   int searchvisitors=jobj2.getInt("visitors");
//	   String value=socialvisitors+","+other+","+sitevisitors+","+searchvisitors;
//	   String val1="Social|Other|Site|Search";
	   
	   need = "<center><h3> Sources<h3></center> <ul> <li> Social :"+ socialvisitors+"</li> <li> Direct :"+ other+"</li> <li> Site :"+ sitevisitors+"</li> <li> Search :"+ searchvisitors+"</li> </ul>";
//	   api.perform(
//      		 api.context().currentRoom().post(
//      		 new TextChatlet("<center><h3> Sources</h3></center> <ul> <li> Social :"+ socialvisitors+"</li> <li> Direct :"+ other+"</li> <li> Site :"+ sitevisitors+"</li> <li> Search :"+ searchvisitors+"</li> </ul>"
//      		 		                     )
//      		 ));
	return need;
	   

}


}
