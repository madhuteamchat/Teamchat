/*package com.teamchat.integrations.trello;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class TrelloVerf {
	public static String authUrl="";
	public static OAuthService service;
	public static Token requestTokenObject;
	static String email;
	static String room_id;
	static String PROTECTED_RESOURCE_URL;
	
	public void getverf(String ver){
		// TODO Auto-generated method stub
		Verifier verifier = new Verifier(ver);
		Token accessToken = service.getAccessToken(requestTokenObject, verifier);
		OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
	    service.signRequest(accessToken, request);
	    Response response = request.send();
	    System.out.println("Response is : "+response.getBody());
	    new ManageDB();
		ManageDB.insert(email,accessToken.getToken());
		new TrelloBot().postMsg("Connected...", room_id);
	}
}
*/