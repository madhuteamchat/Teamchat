package com.teamchat.integrations.evernote;

import java.io.IOException;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;
import com.evernote.thrift.TException;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class SearchNotes{
	TeamchatAPI api;
	public String query;
	public NoteStoreClient noteStore;
	public SearchNotes(TeamchatAPI api,String query,NoteStoreClient noteStore) throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException, IOException, ParserConfigurationException, SAXException{
		String print="";
		if(query.length()>0){
			this.api=api;
			this.query=query;
			this.noteStore=noteStore;
			NoteFilter filter = new NoteFilter();
		    filter.setWords(query);
		    filter.setOrder(NoteSortOrder.UPDATED.getValue());
		    filter.setAscending(false);

		    NoteList notes = noteStore.findNotes(filter, 0, 200);
		    if(notes.getTotalNotes()==0){
		    	api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("No results to show for the query : "+query)));
		    }
		    else{
		    print=print+"No.of matchings found : " + notes.getTotalNotes()+"<br>";
		    Iterator<Note> iter = notes.getNotesIterator();
		    while (iter.hasNext()) {
		      Note note = iter.next();
		      Note fullNote = noteStore.getNote(note.getGuid(), true, true, false,false);
		      print=print+"Title of the Note : "+fullNote.getTitle()+"<br> Content in the Note is : " +NoteContent.extract(fullNote)+"<br><br>";
		    }//while
		    api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(print)));
		    }
	    }
		else{
			api.perform(api.context().currentRoom().post(new TextChatlet("No results to show as your Search string is empty.")));
		}
    }
}