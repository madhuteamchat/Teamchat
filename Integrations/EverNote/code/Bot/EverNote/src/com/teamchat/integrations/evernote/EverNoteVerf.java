package com.teamchat.integrations.evernote;

import java.io.IOException;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.evernote.auth.EvernoteService;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.thrift.TException;

public class EverNoteVerf {
	
	public static String authUrl="";
	public static OAuthService service;
	public static Token requestTokenObject;
	static final EvernoteService EVERNOTE_SERVICE = EvernoteService.PRODUCTION;
	
	
	public void getverf(String ver) throws EDAMUserException, EDAMSystemException, TException, IOException{
		// TODO Auto-generated method stub
		Verifier verifier = new Verifier(ver);
		Token accessToken = service.getAccessToken(requestTokenObject, verifier);
	    new EverNoteConnect().getStarted(accessToken);
	}
}
