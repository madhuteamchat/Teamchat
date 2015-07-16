package com.integration;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.jasper.tagplugins.jstl.core.ForEach;

import com.github.hipchat.api.HipChat;
import com.github.hipchat.api.Room;
import com.github.hipchat.api.User;
import com.github.hipchat.api.UserId;
import com.github.hipchat.api.messages.BaseMessage;
import com.github.hipchat.api.messages.HistoryMessage;
import com.github.hipchat.api.messages.Message;
import com.github.hipchat.api.messages.Message.Color;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hipchat.getwebhook.Deletewebhook;
import com.hipchat.getwebhook.Item;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.sun.org.apache.bcel.internal.classfile.Field;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class Mainbot
{

	public Hipchat_basiccheckbot hb = new Hipchat_basiccheckbot();
	private String token;
	static HipChat hp;
	static Db_handlerbot db = new Db_handlerbot();// Database handler class
	static String email;
	public static String hipchatemail;
	public String notify;
	public String id;
	private Room abc;

	@OnKeyword("start")
	public void onstart(TeamchatAPI api)
	{
		Notifier.api1 = api;
		Form f = api.objects().form();
		f.addField(api.objects().input().name("hipchatemail").label("Enter your hipchat email address."));
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml("Enter you hipchat account email.").setReplyScreen(f).setReplyLabel("Enter").alias("gottoken").alias("starthip")));
	}

	@OnAlias("starthip")
	public void onstarthip(TeamchatAPI api)
	{
		email = api.context().currentSender().getEmail();
		hipchatemail = api.context().currentReply().getField("hipchatemail");
		if (db.isAuthorized(email,hipchatemail))
		{

		} else
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("To get started you need to configure your Hipchat account. Please ENTER keyword hipchat")));
		}
	}

	
	// used to initiate the authentication or if user already authenticated then
	// log into his account automatically
	@OnKeyword("hipchat")
	public void onhipchat(TeamchatAPI api) throws Exception
	{
		
		email = api.context().currentSender().getEmail();
		if(hipchatemail.equalsIgnoreCase(null))
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("To get started you need to tell your Hipchat email. Please ENTER keyword start")));
		}
		if (db.isAuthorized(email,hipchatemail))
		{
			// get the basic info
			hb = db.GetBasicStuff(hipchatemail);
			hp = new HipChat(hb.getAccess_token());
			token = hb.getAccess_token();
			notify = hb.getnotify_token();
			hipchatemail = hb.gethipchatEmail();
			api.perform(api.context().currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml("HI, YOU HAVE SUCCESSFULLY SIGNED INTO YOUR ACCOUNT. You may proceed to use your <u>hipchat account</u>. " + "<br />")));
			// welcome message and continue
		} 
		else
		{
			// give instructions to get the access token from hipchat and paste
			// here just one time
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("Follow the instructions given below.We need you to get your access token one time.")
							.setQuestionHtml(
									"INSTRUCTION-Open your hipchat account.Click on GROUP ADMIN.<br/>Click API.Enter your password.<br/>Select 'Type' ADMIN and CREATE TOKEN.<br/>COPY THAT ACCESS TOKEN AND PASTE HERE.<br/><br/>You need to get your notification token as well to enable notifications for room.<br/>INSTRUCTION-Go To Edit Profile.Click on API access.Enter your password.Select all scopes and create token.Copy that notify token and paste here.")));
			Form f = api.objects().form();

			f.addField(api.objects().input().name("token").label("Paste Access Token here"));
			f.addField(api.objects().input().name("notifytoken").label("Paste Notify Token here"));
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<b>Paste your tokens.</b>").setReplyScreen(f).setReplyLabel("Enter").alias("gottoken")));

		}
	}

	

	@OnKeyword("help")
	// to tell use of all the keywords
	public void onhelp(TeamchatAPI api)
	{
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<b>HEY,I am Hipchat Bot.Type <u>start</u> to begin.</b><br/>To perform differant tasks you need to use certain keywords.<br/><ul><li><u>hipchat-</u>To configure your hipchat account.</li></ul><ul><li><u>viewrooms-</u>To get list of all the rooms of your hipchat account.</li></ul><ul><li><u>createroom-</u>To create a new room.</li></ul><ul><li><u>deleteroom-</u>To delete a room.</li></ul><ul><li><u>sendmessage-</u>To send a message to a particular room</li></ul><ul><li><u>historymessage-</u>To view previous messages </li></ul><ul><li><u>notify-</u>To recieve notification of  messages </li></ul><ul><li><u>stop-</u>To stop notification of messages </li></ul>")));
	}

	@OnAlias("gottoken")
	public void ongottoken(TeamchatAPI api)
	{
		token = api.context().currentReply().getField("token");
		notify = api.context().currentReply().getField("notifytoken");
		hp = new HipChat(token);
		db.StorageHandler(email,token, notify, hipchatemail);
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("You have been LOGGED IN succesfully.")));
	}

	@OnKeyword("viewrooms")
	// displays list of all the rooms for that particular account
	public void onlists(TeamchatAPI api)
	{
		List<Room> rooms = hp.listRooms();
		String htmlResponse = "<ul>";
		for (Room room : rooms)
		{
			htmlResponse += "<li>" + room.getName() + "</li>";
		}
		htmlResponse += "</ul>";
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<b>This is the list of all the rooms</b>" + htmlResponse)));
	}

	@OnKeyword("createroom")
	public void oncreateroom(TeamchatAPI api)
	{
		Form f = api.objects().form();
		f.addField(api.objects().input().name("name").label("Enter ROOM Name"));
		f.addField(api.objects().select().name("privates").label("Private room(TRUE) or Open room(false)").addOption("True").addOption("False"));
		f.addField(api.objects().input().name("topic").label("Enter the Topic"));
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
		List<User> users = hp.listUsers();
		hb = db.GetBasicStuff(hipchatemail);
		String e = hb.gethipchatEmail();
		User owner = null;
		for (User user : users)
		{
			if (e.equalsIgnoreCase(user.getEmail()))
			{
				owner = user;
			} 
		}
		hp.createRoom(name, owner.getId(), p, topic, g);
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<b>The room " + name + " has been created succesfully</b>")));
	}

	@OnKeyword("deleteroom")
	// to delete a room
	public void ondeleteroom(TeamchatAPI api)
	{
		List<Room> rooms = hp.listRooms();
		String htmlResponse = "<ul>";
		Form f = api.objects().form();
		com.teamchat.client.sdk.Field x = api.objects().select().name("name").label("Select name of room you want to delete");

		for (Room room : rooms)
		{
			htmlResponse = room.getName();
			x.addOption(htmlResponse);
		}
		f.addField(x);
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("Enter name of room you want to delete.").setReplyScreen(f).setReplyLabel("Enter").alias("delete")));
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
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<font color='Red'><b>The room " + abc.getName() + " has been successfully deleted</b><font>")));
	}

	@OnKeyword("sendmessage")
	// sends a message which the user specifies to a particular room
	public void onsendmessage(TeamchatAPI api)
	{
		List<Room> rooms = hp.listRooms();
		String htmlResponse = "<ul>";
		Form f = api.objects().form();
		com.teamchat.client.sdk.Field x = api.objects().select().name("name").label("Select name of room you want to send message");

		for (Room room : rooms)
		{
			htmlResponse = room.getName();
			x.addOption(htmlResponse);
		}
		f.addField(x);
		f.addField(api.objects().input().name("msg").label("Enter message"));
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml("<b>Enter name of room you want to send message.</b> ").setReplyScreen(f).setReplyLabel("Enter").alias("send")));

	}

	@OnAlias("send")
	public void onsend(TeamchatAPI api)
	{
		List<Room> rooms = hp.listRooms();
		List<User> users = hp.listUsers();
		hb = db.GetBasicStuff(hipchatemail);
		String e = hb.gethipchatEmail();

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
					if (e.equalsIgnoreCase(user.getEmail()))
					{
						owner = user;
						UserId u = new UserId(owner.getId(), owner.getName());
						abc = room;
						abc.sendMessage(msg, u, true, Color.GREEN);
					} else
					{
						api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<b>You are not ADMIN.So you cant send message.</b>")));
					}
				}
			}
		}
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<b>Your message has been sent and the message is  " + msg + "</b>")));
	}

	@OnKeyword("historymessage")
	public void onhistorymsg(TeamchatAPI api) throws Exception
	{
		List<Room> rooms = hp.listRooms();
		String htmlResponse = "<ul>";
		Form f = api.objects().form();
		com.teamchat.client.sdk.Field x = api.objects().select().name("name").label("Select name of room you want to see history.");

		for (Room room : rooms)
		{
			htmlResponse = room.getName();
			x.addOption(htmlResponse);
		}
		f.addField(x);
		f.addField(api.objects().input().name("d").label("Which date messages you want.FORMAT(yyyy-mm-dd)"));
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml("<b>Select ROOM and DATE for which you want history.</b>").setReplyScreen(f).setReplyLabel("Select").alias("history")));
	}

	@OnAlias("history")
	public void onhistory(TeamchatAPI api) throws ParseException
	{
		List<Room> rooms = hp.listRooms();
		String n = "";
		List<HistoryMessage> his = null;
		String name = api.context().currentReply().getField("name");
		String d = api.context().currentReply().getField("d");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = (Date) sdf.parse(d);
		for (Room room : rooms)
		{
			if (name.equalsIgnoreCase(room.getName()))
			{
				String html = "";
				his = room.getHistory(date);

				for (HistoryMessage historyMessage : his)
				{

					his = room.getHistory(date);
					User n1 = hp.getUser(historyMessage.getSender());
					n = n1.getName();
					html = historyMessage.getText() + "<br/>";
					api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<u>Message sent by-</u>" + n + "<br/>" + html)));
					if (html.equalsIgnoreCase(null))
					{
						api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("There are no messages.")));
					}
				}

			}

		}

	}

	@OnKeyword("notify")
	public void onnotify(TeamchatAPI api)
	{
		List<Room> rooms = hp.listRooms();
		String htmlResponse = "<ul>";
		Form f = api.objects().form();
		com.teamchat.client.sdk.Field x = api.objects().select().name("name").label("Select name of room you want to see notifications.");

		for (Room room : rooms)
		{
			htmlResponse = room.getName();
			x.addOption(htmlResponse);
		}
		f.addField(x);
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml("<b>Select ROOM whose notifications you want</b>").setReplyScreen(f).setReplyLabel("Select").alias("notifyme")));

	}

	@OnAlias("notifyme")
	public void onnotifyme(TeamchatAPI api) throws Exception
	{
		String name = api.context().currentReply().getField("name");

		List<Room> rooms = hp.listRooms();
		for (Room room : rooms)
		{
			if (name.equalsIgnoreCase(room.getName()))
			{
				id = room.getId();
				System.err.println(id);
				api.perform(api.context().currentRoom().post(new TextChatlet("You have successfully subscribed to notifications for the room-" + room.getName() + "")));
			}
		}
		Checkwebhook cw = new Checkwebhook(notify, id);

		// Creates webhook
		if (cw.check())
		{
			OkHttpClient client = new OkHttpClient();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType,
					"{\n    \"url\":\"http://interns.teamchat.com:8080/Hipchat/Hipchat_webhooks\",\n    \"event\":\"room_message\",\n    \"name\":\"Room_notification\"\n}");
			Request request = new Request.Builder().url("http://api.hipchat.com/v2/room/" + id + "/webhook").post(body).addHeader("authorization", "Bearer " + notify + "")
					.addHeader("content-type", "application/json").build();
			Response response = client.newCall(request).execute();
		}
	}

	@OnKeyword("stop")
	public void onstop(TeamchatAPI api) throws IOException
	{
		Checkwebhook cb = new Checkwebhook(notify, id);
		Integer webid = cb.getwebhookid();
		// Deletes webhook
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder().url("http://api.hipchat.com/v2/room/" + id + "/webhook/" + webid + "").delete(null).addHeader("authorization", "Bearer " + notify + "").build();

		Response response = client.newCall(request).execute();

	}
}