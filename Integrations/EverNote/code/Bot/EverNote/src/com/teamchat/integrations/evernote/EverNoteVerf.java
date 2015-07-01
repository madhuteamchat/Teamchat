package com.teamchat.integrations.evernote;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.evernote.auth.EvernoteService;

public class EverNoteVerf {

	public static OAuthService service;
	public static Token requestTokenObject;
	static final EvernoteService EVERNOTE_SERVICE = EvernoteService.PRODUCTION;
	static String mail;
	static String room_id;
	
	public void getverf(String ver) {

		// TODO Auto-generated method stub
		Verifier verifier = new Verifier(ver);
		Token accessToken = service
				.getAccessToken(requestTokenObject, verifier);
		ManageDB.insert(mail, accessToken.getToken());
		new EverNote().postMsg("Connected...", room_id);

	}
}
