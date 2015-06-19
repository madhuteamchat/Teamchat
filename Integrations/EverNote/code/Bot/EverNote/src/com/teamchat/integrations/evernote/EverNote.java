package com.teamchat.integrations.evernote;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.NoteSortOrder;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.SharedNotebook;
import com.evernote.edam.type.SharedNotebookPrivilegeLevel;
import com.evernote.thrift.TException;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

public class EverNote {
		
	static final String consumerKey="botbegins";
	static final String consumerSecret="1344399f32a08272";
	static final EvernoteService EVERNOTE_SERVICE = EvernoteService.SANDBOX;
	public static NoteStoreClient noteStore;
	public static UserStoreClient userStore;
	String nbGuidArray[]=null,nbArray[]=null,nArray[]=null,nGuidArray[]=null,nBook,noteName;
	private String task;
	TeamchatAPI api;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TeamchatAPI api = TeamchatAPIImpl.fromFile("teamchat.data")
				.setEmail("botbegins@gmail.com")  //change to your teamchat registered email id
				.setPassword("botbeginsit"); //change to your teamchat password
				api.startReceivingEvents(new EverNote()); //Wait for other user to send message
	}
	
	@OnKeyword("connect")
	public void connect(TeamchatAPI api) throws EDAMUserException, EDAMSystemException, TException, IOException{
		String mail=api.context().currentSender().getEmail();
		String temp=null;
		temp=PropertyFile.getProperty(mail);
		if(temp == null){
			String print= "<a href=\"http://localhost:8080/EverNote/EverNoteServlet?name="+mail+"\" target=\"_blank\">Login to EverNote</a>";
			api.perform(api.context().currentRoom().post(new TextChatlet(print)));
			while(temp != null){
				temp=PropertyFile.getProperty(mail);
				}
			
		}
		else{
			//api.perform(api.context().currentRoom().post(new TextChatlet("Token is there")));
		}
		EvernoteAuth evernoteAuth = new EvernoteAuth(EvernoteService.SANDBOX, temp);
		ClientFactory factory = new ClientFactory(evernoteAuth);
		userStore = factory.createUserStoreClient();
		noteStore = factory.createNoteStoreClient();
		boolean versionOk = userStore.checkVersion("Evernote Evernotebot (Java)",
			       com.evernote.edam.userstore.Constants.EDAM_VERSION_MAJOR,
			       com.evernote.edam.userstore.Constants.EDAM_VERSION_MINOR);
		if (!versionOk) {
				      System.err.println("Incompatible Evernote client protocol version");
				      System.err.println("Incompatible Evernote client protocol version");
						
				      System.exit(1);
		}
		api.perform(api.context().currentRoom().post(new TextChatlet("Connected...")));
	}
	
	@OnKeyword("help")
	public void help(TeamchatAPI api){
		String print= "Type \"connect\" to connect your Evernote.<br>Type \"myevernote\" to start your EverNote and select the task you need.";
		api.perform(api.context().currentRoom().post(new TextChatlet(print)));
	}
	
	@OnKeyword("myevernote")
	public void myEverNote(TeamchatAPI api) throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException{
		
		new AddReminders(api,noteStore);
		api.perform(api.context().currentRoom().post(
				new PrimaryChatlet()
				.setQuestion("Select your task")
				.setReplyScreen
				(
				api.objects().form()
				.addField(api.objects().select().label("Task").name("task")
				.addOption("Create NoteBook")
				.addOption("Create Note")
				.addOption("List NoteBooks")
				.addOption("List Notes")
				.addOption("Search Notes")
				.addOption("List Reminders")
				.addOption("Edit Note")
				.addOption("Share NoteBook")
				)
				)
				.alias("taskToDo")
		));
		
	}
	@OnAlias("taskToDo")
	public void taskToDo(TeamchatAPI api){
		task=(api.context().currentReply().getField("task"));
		try {			
			if(task.equals("Share NoteBook")){
				String nb="";
		    	String nbGuid="";
		    	List<Notebook> notebooks = noteStore.listNotebooks();
		    	if(notebooks.size()<1){
		    		api.perform(api.context().currentRoom().post(new TextChatlet("No Notebooks to edit")));
		    	}
		    	else
		    	{
		    		for (Notebook notebook : notebooks) {
		    			nb=nb+":"+notebook.getName();
		    			nbGuid=nbGuid+":"+notebook.getGuid();
		    		}
		    		nb=nb.substring(1);
		    		nbGuid=nbGuid.substring(1);
		    		nbArray=nb.split(":");
		    		nbGuidArray=nbGuid.split(":");
		    		String label1="Notebook";
		    		String name1="notebook";
		    		api.perform(
		    				api.context().currentRoom().post(
		    						new PrimaryChatlet()
		    						.setQuestion("Select the NoteBook to share.")
		    						.setReplyScreen
		    						(
		    								api.objects().form()
		    								.addField(getOptions(api,label1,name1,nbArray))
		    								.addField(api.objects().input().label("E-mail").name("mailid"))
		    						)
		    						.alias("share")
					));
			}
			}
			else if(task.equals("List NoteBooks")){
				String nb="";
				nb=nb+"<ol>";
				List<Notebook> notebooks = noteStore.listNotebooks();
		    	if(notebooks.size()>0){
		    		for (Notebook notebook : notebooks) {
		    			nb=nb+"<li>"+notebook.getName()+"</li>";
		    			//i++;
		    		}
		    		nb=nb+"</ol>";
		    		api.perform(api.context().currentRoom().post(new TextChatlet(nb)));
		    	}
		    	else{
		    		api.perform(api.context().currentRoom().post(new TextChatlet("No Notebooks to show.")));
		    	}
		    }
			else if(task.equals("Edit Note")){
				String nb="";
		    	String nbGuid="";
		    	List<Notebook> notebooks = noteStore.listNotebooks();
		    	if(notebooks.size()<1){
		    		api.perform(api.context().currentRoom().post(new TextChatlet("No Notebooks to edit")));
		    	}
		    	else
		    	{
		    		for (Notebook notebook : notebooks) {
		    			nb=nb+":"+notebook.getName();
		    			nbGuid=nbGuid+":"+notebook.getGuid();
		    		}
		    		nb=nb.substring(1);
		    		nbGuid=nbGuid.substring(1);
		    		nbArray=nb.split(":");
		    		nbGuidArray=nbGuid.split(":");
		    		String label1="Notebook";
		    		String name1="notebook";
		    		api.perform(
		    				api.context().currentRoom().post(
		    						new PrimaryChatlet()
		    						.setQuestion("Select the NoteBook")
		    						.setReplyScreen
		    						(
		    								api.objects().form()
		    								.addField(getOptions(api,label1,name1,nbArray))
		    						)
		    						.alias("edit")
					));
			}
			}
			else if(task.equals("List Reminders")){
				 listReminders(api); 
			}
			else if(task.equals("Create NoteBook")){
				api.perform(api.context().currentRoom().post(
					  new PrimaryChatlet()
					  .setQuestion("Enter the name of the notebook")
					  .setReplyScreen(api.objects().form()
							  .addField(api.objects().input().label("NoteBook Name").name("nbname")))
							  .alias("notebook")));
			}
		    else if(task.equals("List Notes")){			  
				listNotes(api);
		    }
		    else if(task.equals("Create Note")){
		    	String nb="";
		    	String nbGuid="";
		    	List<Notebook> notebooks = noteStore.listNotebooks();
		    	if(notebooks.size()>0){
		    	for (Notebook notebook : notebooks) {
		    		nb=nb+":"+notebook.getName();
		    		nbGuid=nbGuid+":"+notebook.getGuid();
		    	}
		    	nb=nb.substring(1);
		    	nbGuid=nbGuid.substring(1);
		    	nbArray=nb.split(":");
		    	nbGuidArray=nbGuid.split(":");
		    	String label1="Notebook";
		    	String name1="notebook";
		    	api.perform(
						api.context().currentRoom().post(
						new PrimaryChatlet()
						.setQuestion("Fill the details to create note")
						.setReplyScreen
						(
						api.objects().form()
						.addField(getOptions(api,label1,name1,nbArray))
						.addField(api.objects().input().label("Title").name("str"))
						.addField(api.objects().input().label("Content").name("content"))
						.addField(api.objects().input().label("Tag").name("tag"))
						)
						.alias("create")
						));
		    }
		    else{
		    		api.perform(api.context().currentRoom().post(new TextChatlet("No notebooks. First create a notebook then you can create note.")));
		    }
		    }
		    else if(task.equals("Search Notes")){
		    	api.perform(
						api.context().currentRoom().post(
						new PrimaryChatlet()
						.setQuestion("Enter the keyword to search. To search in tags append \"tag:\" before your keyword.")
						.setReplyScreen
						(
						api.objects().form()
						.addField(api.objects().input().label("Query String").name("query"))
						)
						.alias("searchit")
						));  
		    }
		    else{
		    	api.perform(api.context().currentRoom().post(new TextChatlet("Please select a valid option.")));
		    }
		 }//try
		 catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	@OnAlias("share")
	public void shareNoteBook(TeamchatAPI api) throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException{
		nBook=api.context().currentReply().getField("notebook");
		String mailId=api.context().currentReply().getField("mailid");
		SharedNotebook sharedNotebook = new SharedNotebook();
		String mail[]=mailId.split(",");
		List<Notebook> notebooks = noteStore.listNotebooks();
		for (Notebook notebook : notebooks) {
			if(notebook.getName().equals(nBook)){
				sharedNotebook.setNotebookGuid(notebook.getGuid());
				sharedNotebook.setAllowPreview(true);
				sharedNotebook.setPrivilege(SharedNotebookPrivilegeLevel.MODIFY_NOTEBOOK_PLUS_ACTIVITY);
				for(int i=0;i<mail.length;i++){
					sharedNotebook.setEmail(mail[i]);
				}
				sharedNotebook = noteStore.createSharedNotebook(sharedNotebook); 
				}
			break;
		}
	}
	@OnAlias("edit")
	public void selectNoteToEdit(TeamchatAPI api) throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException{
		nBook=api.context().currentReply().getField("notebook");
		if(nBook.length()>0){
		List<Notebook> notebooks = noteStore.listNotebooks();
		String[] title=new String[100];
		for (Notebook notebook : notebooks) {
			if(notebook.getName().equals(nBook)){  
	    		int i=0;
	    		NoteFilter filter = new NoteFilter();
	    		filter.setNotebookGuid(notebook.getGuid());
	    		filter.setOrder(NoteSortOrder.CREATED.getValue());
	    		filter.setAscending(true);
	    		NoteList noteList = noteStore.findNotes(filter, 0, 200);
	    		List<Note> notes = noteList.getNotes();
	    		if(notes.size()>0){
	    			for (Note note : notes) {
	    				Note fullNote = noteStore.getNote(note.getGuid(), true, true, false,false);
	    				title[i]=fullNote.getTitle();
	    				i++;
	    			}
	    			String label1="Note";
	    			String name1="note";
	    			api.perform(
	    				api.context().currentRoom().post(
	    				new PrimaryChatlet()
	    				.setQuestion("Select the note to be modified")
	    				.setReplyScreen
	    				(
	    						api.objects().form()
	    						.addField(getOptions(api,label1,name1,title))
	    				)
	    				.alias("edit1")
	    				)
	    			);
		    }
	    	else{
	    		api.perform(api.context().currentRoom().post(new TextChatlet("There are no Notes to edit in this notebook")));	
	    	}
		}
	 }
	}
		else{
			api.perform(api.context().currentRoom().post(new TextChatlet("No Notebook is selected")));
		}
	}
	@OnAlias("edit1")
	public void showContentToEdit(TeamchatAPI api) throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException, IOException, ParserConfigurationException, SAXException{
		noteName=api.context().currentReply().getField("note");
		if(noteName.length()>0){
		String data="";
		List<Notebook> notebooks = noteStore.listNotebooks();
	    for (Notebook notebook : notebooks) {
	    	if(notebook.getName().equals(nBook)){
	    	NoteFilter filter = new NoteFilter();
		      filter.setNotebookGuid(notebook.getGuid());
		      filter.setOrder(NoteSortOrder.CREATED.getValue());
		      filter.setAscending(true);

		      NoteList noteList = noteStore.findNotes(filter, 0, 200);
		      List<Note> notes = noteList.getNotes();
		      for (Note note : notes) {
		    	  Note fullNote = noteStore.getNote(note.getGuid(), true, true, false,false);
		    	  if(fullNote.getTitle().equals(noteName)){
		    		  
		    		  data=data+NoteContent.extract(fullNote);
		    		  break;
		    	  }
		      }
		      String print="Your Current content is : <br>"+data+"<br><br>Enter your modified conent";
			  api.perform(
						api.context().currentRoom().post(
						new PrimaryChatlet()
						.setQuestion(print)
						.setReplyScreen
						(
						api.objects().form()
						.addField(api.objects().input().label("Content").name("content"))
						)
						.alias("edit2")
						)
			  );
		      break;
	    	}
	    }
	    }
		else{
			api.perform(api.context().currentRoom().post(new TextChatlet("Please select a note to edit.")));
		}
	}
	@OnAlias("edit2")
	public void editNote(TeamchatAPI api) throws EDAMUserException, EDAMSystemException, EDAMNotFoundException, TException{
		String content=api.context().currentReply().getField("content");
		String nbGuid = null;
		String tag="";
		String newNoteGuid;
		for(int i=0;i<nbArray.length;i++){
			if(nBook.equals(nbArray[i])){
				nbGuid=nbGuidArray[i];
			}
		}
		Note createdNote=NoteContent.add(api,nbGuid,noteName,content,tag,noteStore);
		newNoteGuid = createdNote.getGuid();
	    if(!newNoteGuid.isEmpty()){
	    	api.perform(api.context().currentRoom().post(new TextChatlet("Your Note has been edited successfully")));
		}
	}
	private void listReminders(TeamchatAPI api) throws EDAMUserException, EDAMSystemException, TException, EDAMNotFoundException{
		// TODO Auto-generated method stub
		List<Notebook> notebooks = noteStore.listNotebooks();
		String print="";
		for (Notebook notebook : notebooks) {
			NoteFilter filter = new NoteFilter();
		    filter.setNotebookGuid(notebook.getGuid());
		    filter.setOrder(NoteSortOrder.CREATED.getValue());
		    filter.setAscending(true);
			NoteList noteList = noteStore.findNotes(filter, 0, 100);
		    List<Note> notes = noteList.getNotes();
		    for (Note note : notes) {
		    	  Note fullNote = noteStore.getNote(note.getGuid(), true, true, false,false);
		    	  long time=fullNote.getAttributes().getReminderTime();
		    	  Date d=new Date();
		    	  d.setTime(time);
		    	  DateFormat df=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		    	  if(time!=0){
		    		  print=print+"<br>NoteBook : "+notebook.getName()
		    				     +"<br>Title : "+fullNote.getTitle()
		    				     +"<br>Time : "+df.format(d)
		    				     +"<br>";
		    	  }
		    }
		}
		if(print.length()>0){
			api.perform(api.context().currentRoom().post(new TextChatlet(print)));
		}
		else{
			api.perform(api.context().currentRoom().post(new TextChatlet("No reminders to display.")));
		}
	}
	private Field getOptions(TeamchatAPI api,String label1,String name1,String[] nbArray) {
		// TODO Auto-generated method stub
		Field f=api.objects().select().label(label1).name(name1);
		for(int i=0;i<nbArray.length;i++){
			f.addOption(nbArray[i]);
		}
		return f;
	}
	@OnAlias("notebook")
	public void  createNoteBook(TeamchatAPI api) throws EDAMUserException, EDAMSystemException, TException{
		String nbName=api.context().currentReply().getField("nbname");
		if(nbName.length()>0){
			new CreateNoteBook(api,nbName,noteStore);
		}
		else{
			api.perform(api.context().currentRoom().post(new TextChatlet("No name has been entered.")));
		}
	}
	
	@OnAlias("create")
	public void createNote(TeamchatAPI api) throws EDAMSystemException, EDAMNotFoundException, TException, EDAMUserException{
		String fileContent=api.context().currentReply().getField("content");
		if(fileContent.isEmpty()){
			fileContent="";
		}
		String title=(api.context().currentReply().getField("str"));
		if(title.isEmpty()){
			title="Untitled";
		}
		String tag=(api.context().currentReply().getField("tag"));
		if(tag.isEmpty()){
			tag="";
		}
		String nbName=(api.context().currentReply().getField("notebook"));
		String nbGuid = "";
		int i;
		for(i=0;i<nbArray.length;i++){
			if(nbName.equals(nbArray[i])){
				nbGuid=nbGuid+nbGuidArray[i];
				break;
			}
		}
			new CreateNote(api,nbGuid,title,fileContent,tag,noteStore);
	}
	
	public void listNotes(TeamchatAPI api) throws Exception {
	    new ListNotes(api,noteStore);
	}
	
	@OnAlias("searchit")
	public void searchNotes(TeamchatAPI api) throws Exception {
		String query= (api.context().currentReply().getField("query"));
		new SearchNotes(api,query,noteStore);
	}
}