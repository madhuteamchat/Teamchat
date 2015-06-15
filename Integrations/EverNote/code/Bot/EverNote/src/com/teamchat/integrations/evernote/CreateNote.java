package com.teamchat.integrations.evernote;

import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.thrift.TException;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class CreateNote {
	private String newNoteGuid;
		
	public CreateNote(TeamchatAPI api,String nbGuid,String title,String fileContent,String tag,NoteStoreClient noteStore) throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException {
		// TODO Auto-generated constructor stub
		
		Note createdNote=NoteContent.add(api,nbGuid,title,fileContent,tag,noteStore);
		newNoteGuid = createdNote.getGuid();
	    if(!newNoteGuid.isEmpty()){
	    	api.perform(api.context().currentRoom().post(new TextChatlet("Your Note has been created successfully")));
		}
	}

}
