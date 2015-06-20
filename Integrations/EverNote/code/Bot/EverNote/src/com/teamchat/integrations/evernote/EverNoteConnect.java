package com.teamchat.integrations.evernote;

import java.io.IOException;

import org.scribe.model.Token;

import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.thrift.TException;

public class EverNoteConnect {
	//public static UserStoreClient userStore;
	//public static NoteStoreClient noteStore;
	//static final EvernoteService EVERNOTE_SERVICE = EvernoteService.SANDBOX;
	static String temp;

	public void getStarted(Token accessToken) throws EDAMUserException, EDAMSystemException, TException, IOException{
		
		new PropertyFile();
		PropertyFile.setProperty(temp,accessToken.getToken());
		
	}
}
