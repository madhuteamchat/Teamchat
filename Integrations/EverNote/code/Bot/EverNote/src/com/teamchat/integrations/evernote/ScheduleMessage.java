package com.teamchat.integrations.evernote;

import java.io.IOException;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.evernote.edam.type.Note;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class ScheduleMessage extends TimerTask{
	TeamchatAPI api;
	Note note;

	public ScheduleMessage(TeamchatAPI api, Note note) {
		// TODO Auto-generated constructor stub
		this.api=api;
		this.note=note;
	}
	@Override
	public void run(){
		try {
			if(note.isSetTitle()){
			api.perform(api.context().currentRoom().post(new TextChatlet("Reminder..." +
			  		"<br>Title : "+note.getTitle()+
			  		"<br>"+NoteContent.extract(note))));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}