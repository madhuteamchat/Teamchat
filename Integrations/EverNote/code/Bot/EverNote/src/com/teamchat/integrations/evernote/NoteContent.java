package com.teamchat.integrations.evernote;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.TException;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class NoteContent {
	public static String extract(Note fullNote) throws IOException, ParserConfigurationException, SAXException{
		File file = new File("temp.xml");
        if (!file.exists()) {
				file.createNewFile();
		}
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(fullNote.getContent());
		bw.close();
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse ("temp.xml");
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
}
