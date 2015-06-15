package com.teamchat.integrations.evernote;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.TException;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class ListNotes {
	public String print="";
	public ListNotes(TeamchatAPI api,NoteStoreClient noteStore) throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException, IOException, ParserConfigurationException, SAXException, XPathExpressionException, TransformerFactoryConfigurationError, TransformerException {
		
		List<Notebook> notebooks = noteStore.listNotebooks();
	    for (Notebook notebook : notebooks) {
	      print=print+">>Notebook Name: " + notebook.getName()+"<br>";
	      NoteFilter filter = new NoteFilter();
	      filter.setNotebookGuid(notebook.getGuid());
	      filter.setOrder(NoteSortOrder.CREATED.getValue());
	      filter.setAscending(true);

	      NoteList noteList = noteStore.findNotes(filter, 0, 100);
	      List<Note> notes = noteList.getNotes();
	      for (Note note : notes) {
	    	  Note fullNote = noteStore.getNote(note.getGuid(), true, true, false,false);
		      print=print+"<br>**Title of the Note : "+fullNote.getTitle()+"<br>"+NoteContent.extract(fullNote)+"<br><br>";
		   }
	      print=print+"----Notes present in "+notebook.getName()+" has been shown successfully.----<br><br>";
	    }
	    if(print.length()>0){
	    	api.perform(api.context().currentRoom().post(new TextChatlet(print)));
	    }
	    else{
	    	api.perform(api.context().currentRoom().post(new TextChatlet("Nothing to show. Your NoteStore is empty!")));
	    }
	}
}
