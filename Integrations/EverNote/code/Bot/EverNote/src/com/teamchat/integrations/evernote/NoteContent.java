package com.teamchat.integrations.evernote;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
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

public class NoteContent {
	public static String extract(Note fullNote) throws IOException, ParserConfigurationException, SAXException{
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new InputSource(new StringReader(fullNote.getContent())));
        doc.getDocumentElement ().normalize ();
        NodeList info = doc.getElementsByTagName("div");
        Node currNode=info.item(0);
        Element element=(Element)currNode;
        NodeList child=element.getChildNodes();
        String data="";
        if(child.getLength()>0){
        	data=data+((Node)child.item(0)).getNodeValue();
        }
        return data;
	}

	public static Note add(TeamchatAPI api, String nbGuid, String title,
			String fileContent, String tag, NoteStoreClient noteStore) throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException {
		// TODO Auto-generated method stub
		Note note = new Note();
		note.setTitle(title);
		if(nbGuid.length()>0){
			note.setNotebookGuid(nbGuid);
		}
		else{
			Notebook notebook=noteStore.getDefaultNotebook();
			note.setNotebookGuid(notebook.getGuid());
			api.perform(api.context().currentRoom().post(new TextChatlet("Your note is being created in your default notebook named "+notebook.getName())));
		}
	    String content="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
	    		+ "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">"
	    		+ "<en-note>"
	    		+ "<div>"
	    		+ fileContent
	    		+ "</div>"
	    		+ "</en-note>";
	    note.setContent(content);
	    if(tag.length()>0){
	    String tagArray[]=tag.split("-");
	    for(int i=0;i<tagArray.length;i++){
	    note.addToTagNames(tagArray[i]);
	    }
	    }
	    Note createdNote = noteStore.createNote(note);
		return createdNote;
	}
	public static void edit(TeamchatAPI api, String nbGuid, String title,
			String editContent,NoteStoreClient noteStore) throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException{
		
		String tag="";
		List<Notebook> notebooks = noteStore.listNotebooks();
		for (Notebook notebook : notebooks) {
			if(notebook.getGuid().equals(nbGuid)){
				NoteFilter filter = new NoteFilter();
				filter.setNotebookGuid(notebook.getGuid());
				filter.setOrder(NoteSortOrder.CREATED.getValue());
				filter.setAscending(true);
				NoteList noteList = noteStore.findNotes(filter, 0, 100);
				List<Note> notes = noteList.getNotes();
				for (Note note : notes) {
					if(note.getTitle().equals(title)){
						noteStore.deleteNote(note.getGuid());
						try{
						if(note.getTagGuidsSize()>0){
						List<String> tags = note.getTagNames();
						for(int i=0;i<tags.size();i++){
							tag=tag+"-"+tags.get(i);
						}
						tag=tag.substring(1);
						}
						}
						catch(Exception e){
						}
						Note editedNote=NoteContent.add(api, notebook.getGuid(), title, editContent, tag, noteStore);
						String newNoteGuid = editedNote.getGuid();
					    if(!newNoteGuid.isEmpty()){
					    	api.perform(api.context().currentRoom().post(new TextChatlet("Your Note has been edited successfully")));
						}
						
						break;
					}
				}
				break;
			}
		}
	}
}