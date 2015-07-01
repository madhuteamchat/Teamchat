/*package com.teamchat.integrations.evernote;

import java.io.IOException;

import org.scribe.model.Token;

import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.thrift.TException;

public class EverNoteConnect {
	static String temp;
	static String room_id;

	public void getStarted(Token accessToken) throws EDAMUserException, EDAMSystemException, TException, IOException{
		new ManageDB();
		ManageDB.insert(temp,accessToken.getToken());
		new EverNote().postMsg("Connected...", room_id);
	}
}
*/