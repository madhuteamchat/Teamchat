package com.teamchat.integration.zendesk;

import java.io.IOException;
import java.util.Properties;

import org.zendesk.client.v2.Zendesk;
import org.zendesk.client.v2.model.Comment;
import org.zendesk.client.v2.model.Ticket;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class ZendeskBot {
	String roomId, formId, ticketId, requesterId, comment;
	Zendesk zd;
	long tarId;
	DBHandler ticketData = new DBHandler();
	Properties configProps;
	
	@OnKeyword("help")
	public void help(TeamchatAPI api) {
		configProps = new Properties();
		try {
			configProps = loadPropertyFromClasspath("zendesk-config.properties", ZendeskBot.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		zd = new Zendesk.Builder(configProps.getProperty("zenemail")) // Zendesk account id
				.setUsername(configProps.getProperty("zenuser")) // email of zendesk account
				.setPassword(configProps.getProperty("zenpass")).build(); // or setToken("0cf1d94606")
		
		// Setting up Trigger
		// trigger ();

		Form f = api.objects().form();
		f.addField(api.objects().input().label("Subject").name("subject"));
		f.addField(api.objects().input().label("Comment").name("comment"));

		PrimaryChatlet prime = new PrimaryChatlet();
		prime.setQuestion("Enter your query").setReplyScreen(f)
				.setReplyLabel("Enter").setDetailsLabel("Queries")
				.showDetails(true).alias("query");

		api.perform(api.context().currentRoom().post(prime));
	}

	@OnAlias("query")
	public void query(TeamchatAPI api) {
		configProps = new Properties();
		try {
			configProps = loadPropertyFromClasspath("zendesk-config.properties", ZendeskBot.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		zd = new Zendesk.Builder(configProps.getProperty("zenemail")) // Zendesk account id
				.setUsername(configProps.getProperty("zenuser")) // email of zendesk account
				.setPassword(configProps.getProperty("zenpass")).build(); // or setToken("0cf1d94606")

		String sub = api.context().currentReply().getField("subject");
		String com = api.context().currentReply().getField("comment");

		// Setting comment
		Comment c = new Comment();
		c.setBody(com);

		// Creating a new ticket, administrator privilege is required
		Ticket ticket = new Ticket();
		ticket.setSubject(sub);
		ticket.setComment(c);

		ticket = zd.createTicket(ticket);
		ticketId = String.valueOf(ticket.getId());
		requesterId = String.valueOf(ticket.getRequesterId());

		Form f = api.objects().form();
		f.addField(api.objects().input().label("Comment").name("upCom"));

		PrimaryChatlet prime = new PrimaryChatlet();
		prime.setQuestionHtml("<ul><li>Ticket - " + ticketId + "</li><li>Subject - " + sub + "</li><li>Comment - " + com + "</li></ul>")
				.setReplyScreen(f).setReplyLabel("Update")
				.setDetailsLabel("Update").showDetails(true).alias("updateTheTicket");

		api.perform(api.context().currentRoom().post(prime));

		roomId = api.context().currentRoom().getId();
		formId = prime.getFormId();
		comment = com;

		ticketData.setData(roomId, formId, ticketId, requesterId, comment);

		// Saving ticketid with formid for further replies
		// api.data().addField (formId, "RoomID", roomId);
		// api.data().addField (formId, "FormID", formId);
		// api.data().addField (formId, "TicketID", ticketId);
		// api.data().addField (formId, "RequesterID", requesterId);
	}

	@OnAlias("updateTheTicket")
	public void update(TeamchatAPI api) {
		configProps = new Properties();	
		try {
			configProps = loadPropertyFromClasspath("zendesk-config.properties", ZendeskBot.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String rep = api.context().currentReply().senderEmail();
		if (!rep.equals(configProps.getProperty("botemail"))) {
			zd = new Zendesk.Builder(configProps.getProperty("zenemail")) // Zendesk account id
					.setUsername(configProps.getProperty("zenuser")) // email of zendesk account
					.setPassword(configProps.getProperty("zenpass")).build(); // or setToken("0cf1d94606")

			String upCom = api.context().currentReply().getField("upCom");

			// Getting existing ticket id associated with formid
			String[] data = ticketData.getData(api.context().currentReply().getFormId());
			String ticketId = data[2];

			// Creating or getting existing ticket by it id, Only Admin
			Ticket t = zd.getTickets(Long.parseLong(ticketId)).get(0);
			Comment c = new Comment();
			c.setBody(upCom);
			t.setComment(c);

			// updating existing ticket in out database
			zd.updateTicket(t);
		}
	}
	
	public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException {
		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;
	}
}