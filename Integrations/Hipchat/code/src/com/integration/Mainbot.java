package com.integration;

import java.util.List;

import org.apache.jasper.tagplugins.jstl.core.ForEach;

import com.github.hipchat.api.HipChat;
import com.github.hipchat.api.Room;
import com.github.hipchat.api.User;
import com.github.hipchat.api.UserId;
import com.github.hipchat.api.messages.Message;
import com.github.hipchat.api.messages.Message.Color;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.org.apache.bcel.internal.classfile.Field;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class Mainbot
{
	static Hipchat_basiccheckbot hb = new Hipchat_basiccheckbot();
	private static String token;
	static HipChat hp;
	static Db_handlerbot db = new Db_handlerbot();
	static String email;

	@OnKeyword("Hipchat")
	public void onhipchat(TeamchatAPI api) throws Exception
	{
		email = api.context().currentSender().getEmail();
		if (db.isAuthorized(email))
		{
			// get the basic info
			hb = db.GetBasicStuff(email);
			hp = new HipChat(hb.getAccess_token());
			api.perform(api.context().currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml("Hi, you may proceed to use your <u>hipchat account</u>. " + "<br />Type <b>Help</b> to know more commands")));
			// welcome message and continue
		} else
		{
			// display the button so the user can begin authentication
			// and tell the user to retype the keyword 'hipchat'
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet().setQuestion("Follow the instructions given below.We need you to get your access token one time").setQuestion(
							"Open your hipchat account.Click on GROUP ADMIN.Click API.Enter your password.Select 'Type' ADMIN and CREATE TOKEN.")));

			Form f = api.objects().form();
			f.addField(api.objects().input().name("token").label("Paste Token here"));
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("Paste your token").setReplyScreen(f).setReplyLabel("Enter").alias("gottoken")));
		}
	}
@OnKeyword("help")
public void onhelp(TeamchatAPI api){
	api.perform(api
			.context()
			.currentRoom()
			.post(new PrimaryChatlet().setQuestion("hipchat-to accesss the account,,,,lists-to get list of all the rooms of your account,,,,,createroom--to create new room,,,,deleteroom--to delete a room,,,,sendmessage--to send a message to a particular room" )));
	       
}
	@OnAlias("gottoken")
	public static void ongottoken(TeamchatAPI api)
	{
		token = api.context().currentReply().getField("token");
		hp = new HipChat(token);
		db.StorageHandler(email, token);

	}

	@OnKeyword("lists")
	public void onlists(TeamchatAPI api)
	{
		List<Room> rooms = hp.listRooms();
		String htmlResponse = "<ul>";
		for (Room room : rooms)
		{
			htmlResponse += "<li>" + room.getName() + "</li>";
		}
		htmlResponse += "</ul>";
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("This is the list of all the rooms" +htmlResponse)));
	}

	@OnKeyword("createroom")
	public void oncreateroom(TeamchatAPI api)
	{
		Form f = api.objects().form();
		f.addField(api.objects().input().name("name").label("Enter your name"));
		f.addField(api.objects().select().name("privates").label("Private or Open").addOption("True").addOption("False"));
		f.addField(api.objects().input().name("topic").label("Enter your topic"));
		f.addField(api.objects().select().name("guests").label("You want to  allow guests").addOption("True").addOption("False"));
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("Enter details for room").setReplyScreen(f).setReplyLabel("Enter").alias("createrooms")));

	}

	@OnAlias("createrooms")
	public void oncreaterooms(TeamchatAPI api) throws Exception
	{
		String name = api.context().currentReply().getField("name");
		String privates = api.context().currentReply().getField("privates");
		String topic = api.context().currentReply().getField("topic");
		String guests = api.context().currentReply().getField("guests");
		boolean p = Boolean.parseBoolean(privates);
		boolean g = Boolean.parseBoolean(guests);
		// String id="2296740";
		List<User> users = hp.listUsers();
		User owner = null;
		for (User user : users)
		{
			if (email.equalsIgnoreCase(user.getEmail()))
			{
				owner = user;
			}
		}
		hp.createRoom(name, owner.getId(), p, topic, g);
	}

	@OnKeyword("deleteroom")
	public void ondeleteroom(TeamchatAPI api)
	{
		List<Room> rooms = hp.listRooms();
		String htmlResponse = "<ul>";
		for (Room room : rooms)
		{
			htmlResponse += "<li>" + room.getName() + "</li>";
		}
		htmlResponse += "</ul>";
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("This is the list of all the rooms" +htmlResponse)));
		Form f = api.objects().form();
		f.addField(api.objects().input().name("name").label("Enter name of room you want to delete"));
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("Enter name of room you want to delte").setReplyScreen(f).setReplyLabel("Enter").alias("delete")));
	}

	@OnAlias("delete")
	public void ondelete(TeamchatAPI api)
	{
		List<Room> rooms = hp.listRooms();
		Room abc = null;
		String name = api.context().currentReply().getField("name");
		for (Room room : rooms)
		{
		if (name.equalsIgnoreCase(room.getName()))
			{
				abc = room;
				hp.deleteRoom(abc.getId());
			}
		}
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("The room has been deleted succesfully")));
	}

	@OnKeyword("sendmessage")
	public void onsendmessage(TeamchatAPI api)
	{
		List<Room> rooms = hp.listRooms();
		String htmlResponse = "<ul>";
		for (Room room : rooms)
		{
			htmlResponse += "<li>" + room.getName() + "</li>";
		}
		htmlResponse += "</ul>";
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("This is the list of all the rooms" +htmlResponse)));
		Form f = api.objects().form();
		f.addField(api.objects().input().name("name").label("Enter name of room you want to send msg"));
		f.addField(api.objects().input().name("msg").label("Enter msg"));
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("Enter name of room you want to sendmsg").setReplyScreen(f).setReplyLabel("Enter").alias("send")));

	}

	@OnAlias("send")
	public void onsend(TeamchatAPI api)
	{
		List<Room> rooms = hp.listRooms();
		List<User> users = hp.listUsers();

		User owner = null;
		Room abc = null;
		String name = api.context().currentReply().getField("name");
		String msg = api.context().currentReply().getField("msg");
		for (Room room : rooms)
		{
			if (name.equalsIgnoreCase(room.getName()))
			{
				for (User user : users)
				{
					if (email.equalsIgnoreCase(user.getEmail()))
					{
						owner = user;
						UserId u = new UserId(owner.getId(), owner.getName());
						abc = room;
						room.sendMessage(msg, u, true, Color.GREEN);
					}

				}
			}
		}
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("Your message has been sent and the message is " +msg)));
	}
}