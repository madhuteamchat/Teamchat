package com.teamchat.integrations.evernote;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.TException;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class NoteContent2 {
	public static String extract(Note fullNote) throws SAXException, IOException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, XPathExpressionException{
		String fileContent=fullNote.getContent();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(fileContent)));
        XPath xPath = XPathFactory.newInstance().newXPath();
	    Node result = (Node)xPath.evaluate("en-note/div", doc, XPathConstants.NODE);
	    //System.out.println(nodeToString(result));
        return nodeToString(result);
	}
	private static String nodeToString(Node node) throws TransformerFactoryConfigurationError, TransformerException{
		    StringWriter buf = new StringWriter();
		    Transformer xform = TransformerFactory.newInstance().newTransformer();
		    xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		    xform.transform(new DOMSource(node), new StreamResult(buf));
		    return(buf.toString());
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