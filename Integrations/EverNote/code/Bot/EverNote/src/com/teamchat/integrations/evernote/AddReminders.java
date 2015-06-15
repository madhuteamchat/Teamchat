package com.teamchat.integrations.evernote;

import java.util.Date;
import java.util.List;
import java.util.Timer;

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

public class AddReminders {

	public AddReminders(TeamchatAPI api,NoteStoreClient noteStore) throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException {
		// TODO Auto-generated constructor stub
		List<Notebook> notebooks = noteStore.listNotebooks();
		for (Notebook notebook : notebooks) {
			NoteFilter filter = new NoteFilter();
		    filter.setNotebookGuid(notebook.getGuid());
		    filter.setOrder(NoteSortOrder.CREATED.getValue());
		    filter.setAscending(true);
			NoteList noteList = noteStore.findNotes(filter, 0, 200);
		    List<Note> notes = noteList.getNotes();
		    
		    for (Note note : notes) {
		    	  Note fullNote = noteStore.getNote(note.getGuid(), true, true, false,false);
		    	  Date d=new Date();
		    	  long currTime=d.getTime();
		    	  long time=fullNote.getAttributes().getReminderTime();
		    	  if(time>0 && currTime<time){
		    	  Date exectime= new Date(time);
		    	  ScheduleMessage sch=new ScheduleMessage(api,fullNote);
				  Timer timer=new Timer();
				  timer.schedule(sch,exectime);
		    	  }
		    }
		}
	}
}
