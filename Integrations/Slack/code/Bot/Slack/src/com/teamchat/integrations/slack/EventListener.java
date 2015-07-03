//INTEGRATION: SLACK

/*
	Thanks to uulink for the simple-slack-api
 */

package com.teamchat.integrations.slack;

import java.awt.TextArea;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackChannelArchived;
import com.ullink.slack.simpleslackapi.events.SlackChannelCreated;
import com.ullink.slack.simpleslackapi.events.SlackChannelDeleted;
import com.ullink.slack.simpleslackapi.events.SlackChannelRenamed;
import com.ullink.slack.simpleslackapi.events.SlackChannelUnarchived;
import com.ullink.slack.simpleslackapi.events.SlackGroupJoined;
import com.ullink.slack.simpleslackapi.events.SlackMessageDeleted;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.events.SlackMessageUpdated;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelArchivedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelCreatedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelDeletedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelRenamedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackChannelUnarchivedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackGroupJoinedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessageDeletedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import com.ullink.slack.simpleslackapi.listeners.SlackMessageUpdatedListener;

public class EventListener {

	public static TeamchatAPI api1; //assigned as soon as keyword 'slack' in entered by user

	public static void listenForEvents(String token) throws Exception {
		SlackSession session = SlackSessionFactory
				.createWebSocketSlackSession(token); //predefined Websocket Session creator class
		session.connect();

		session.addMessagePostedListener(new SlackMessagePostedListener() {

			@Override
			public void onEvent(SlackMessagePosted event, SlackSession session) {
				System.out.println("Message posted in Slack!");
				String j = event.toString();
				String messagestart = j.substring(32);
				System.out.println(messagestart);
				String a[] = messagestart.split("'");
				System.out.println(a[1]);
				String actualMessage = a[1];
				System.out.println(a[17]);
				String messageInChannel = a[17];			//Manipulating JSON-esque responses
				if (actualMessage.contains(">")) {
					String b[] = actualMessage.split(">");
					String m = b[0];
					String m2 = b[1];
					String m1 = m.substring(12);
					String finalMessage = m1 + m2;
					api1.performPostInCurrentRoom(new PrimaryChatlet()
							.setQuestionHtml("<b>'"+finalMessage+"'/b>" + " " + "<i>'"
									+ messageInChannel + "'</i>" + " in Slack."));
				} else {
					api1.performPostInCurrentRoom(new PrimaryChatlet()
							.setQuestionHtml("Message"+" " +"<i>'"+actualMessage+"'</i>" + " " +"has been posted in channel"+" " +"<i>"+messageInChannel +"</i>"+ " " +"in Slack"));
				}
			}
		});

		session.addchannelCreatedListener(new SlackChannelCreatedListener() {

			@Override
			public void onEvent(SlackChannelCreated event, SlackSession session) {
				System.out.println("Channel created!");
			/*	api1.performPostInCurrentRoom(new TextChatlet(
						"A channel has been created in Slack."));
			*/}
		});

		session.addchannelArchivedListener(new SlackChannelArchivedListener() {

			@Override
			public void onEvent(SlackChannelArchived event, SlackSession session) {
				System.out.println("A channel has been archived.");
			/*	api1.performPostInCurrentRoom(new TextChatlet(
						"A channel has been archived in Slack."));
			*/}
		});

		session.addchannelDeletedListener(new SlackChannelDeletedListener() {

			@Override
			public void onEvent(SlackChannelDeleted event, SlackSession session) {
				System.out.println("A Channel has been deleted.");
			/*	api1.performPostInCurrentRoom(new TextChatlet(
						"A Channel has been deleted in Slack."));
			*/}
		});

		session.addChannelRenamedListener(new SlackChannelRenamedListener() {

			@Override
			public void onEvent(SlackChannelRenamed event, SlackSession session) {
				System.out.println("A Channel has been renamed.");
			/*	api1.performPostInCurrentRoom(new TextChatlet(
						"A Channel has been renamed in Slack."));
			*/}
		});

		session.addChannelUnarchivedListener(new SlackChannelUnarchivedListener() {

			@Override
			public void onEvent(SlackChannelUnarchived event,
					SlackSession session) {
				System.out.println("A Channel has been unarchived.");
			/*	api1.performPostInCurrentRoom(new TextChatlet(
						"A Channel has been unarchived in Slack."));
			*/}
		});

		session.addGroupJoinedListener(new SlackGroupJoinedListener() {

			@Override
			public void onEvent(SlackGroupJoined event, SlackSession session) {
				System.out.println("A group has been joined.");
				api1.performPostInCurrentRoom(new PrimaryChatlet()
						.setQuestion("You have joined a new group in Slack."));
			}
		});

		session.addMessageDeletedListener(new SlackMessageDeletedListener() {

			@Override
			public void onEvent(SlackMessageDeleted event, SlackSession session) {
				System.out.println("A message has been deleted.");
				api1.performPostInCurrentRoom(new PrimaryChatlet().setQuestion
						("A message has been deleted in Slack."));
			}
		});

		session.addMessageUpdatedListener(new SlackMessageUpdatedListener() {

			@Override
			public void onEvent(SlackMessageUpdated event, SlackSession session) {
				System.out.println("A message has been updated.");
				api1.performPostInCurrentRoom(new PrimaryChatlet().setQuestion(
						"A message has been updated in Slack."));
			}
		});

	}

}
