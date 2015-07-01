package com.teamchat.integrations.evernote;

import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.TException;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class CreateNoteBook {
	public CreateNoteBook(TeamchatAPI api,String nbName,NoteStoreClient noteStore) throws EDAMSystemException, TException{
		// TODO Auto-generated constructor stub
		Notebook noteBook = new Notebook();
		noteBook.setName(nbName);
		try {
			noteStore.createNotebook(noteBook);
			api.perform(api.context().currentRoom().post(new TextChatlet("NoteBook created")));
		} catch (EDAMUserException e) {
			// TODO Auto-generated catch block
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("NoteBook with name "+nbName+" already exists.\nTry different name.")));
			e.printStackTrace();
		}		
		}
}
