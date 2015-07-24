package com.teamchat.integrations.Gmail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class GmailBot {
	public static final String client_id = "70059097404-9hvham50i6s4cvba0u9dpsk82eoe5t6f.apps.googleusercontent.com";
	public static final String client_secret = "s71rFRtHuj8MrUE3oXkdsvHc";
	public static final String redirect_uri1 = "http://interns.teamchat.com:8083/GmailIntegration/Servlet";
	public static String accesstoken;
	private static final HttpTransport ht = new NetHttpTransport();
	private static final JsonFactory jf = new JacksonFactory();

	@OnKeyword("help")
	public void Help(TeamchatAPI api) {
		Servlet.api = api;
		String intro = "<b>Hey, this is Gmail Bot!</b><br><img src=http://www.iconarchive.com/download/i98301/dakirby309/simply-styled/Gmail.ico alt=gmail.png style=width:90px;height:90px;><br><i>You can use me to get your unread emails and reply to them through Teamchat.<br>Use the following keywords.</i><table style=width:100%><tr><th>Keywords</th><th>Function</th></tr><tr><td>getmail</td><td>Get unread emails from your Gmail inbox</td></tr><tr><td>help</td><td>Log in to Gmail</td></tr>";
		String msg = "<a href=https://accounts.google.com/o/oauth2/auth?scope=https://mail.google.com/&redirect_uri="
				+ redirect_uri1
				+ "&response_type=code&client_id="
				+ client_id
				+ " target=_blank>Click here to login</a>";
		api.performPostInCurrentRoom(new PrimaryChatlet()
				.setQuestionHtml(intro));
		api.performPostInCurrentRoom(new PrimaryChatlet().setQuestionHtml(msg));
	}

	public static Credential authorize() throws IOException {
		GoogleCredential credentials = new GoogleCredential.Builder()
				.setClientSecrets(client_id, client_secret).setJsonFactory(jf)
				.setTransport(ht).build().setAccessToken(accesstoken);
		return credentials;
	}

	public static Gmail getGmailService(Credential credential)
			throws IOException {
		return new Gmail.Builder(ht, jf, credential).setApplicationName(
				"Teamchat").build();
	}

	@OnKeyword("getmail")
	public void getMail(TeamchatAPI api) throws IOException {
		Gmail service = getGmailService(authorize());
		String user = "me";
		ListMessagesResponse listResponse = service.users().messages()
				.list(user).setQ("label:unread").execute();
		List<Message> messages = listResponse.getMessages();
		for (Message message : messages) {
			Message msg = service.users().messages().get(user, message.getId())
					.execute();
			JSONObject j = new JSONObject(msg.toString());
			JSONObject j1 = j.getJSONObject("payload");
			JSONArray j2 = j1.getJSONArray("headers");
			int i = 0, flag1 = 0, flag2 = 0, flag3 = 0, flag4 = 0, flag5 = 0;
			for (i = 0; i < j2.length(); i++) {
				JSONObject temp = j2.getJSONObject(i);
				String temp1 = temp.getString("name");
				if (temp1.equals("From"))
					flag1 = i;
				if (temp1.equals("Subject"))
					flag2 = i;
				if (temp1.equals("Date"))
					flag3 = i;
				if (temp1.equals("Return-Path"))
					flag4 = i;
				if (temp1.equals("Delivered-To"))
					flag5 = i;
			}

			String from = j2.getJSONObject(flag1).getString("value");
			String subject = j2.getJSONObject(flag2).getString("value");
			String date = j2.getJSONObject(flag3).getString("value");
			String return_path = j2.getJSONObject(flag4).getString("value");
			String to = j2.getJSONObject(flag5).getString("value");
			System.out.println(from + " " + subject);
			Form f = api.objects().form();
			f.addField(api.objects().input().label("Message Body")
					.name("replybody"));
			f.addField(api.objects().hidden().label("").name("subject")
					.value(subject));
			f.addField(api.objects().hidden().label("").name("return_path")
					.value(return_path));
			f.addField(api.objects().hidden().label("").name("to").value(to));
			api.performPostInCurrentRoom(new PrimaryChatlet()
					.setQuestionHtml(
							date + "<h4><b>From: " + from
									+ "</b></h4><h5><b>Subject: " + subject
									+ "</b></h5><br>" + msg.getSnippet())
					.setReplyScreen(f).setReplyLabel("Reply").showDetails(true)
					.alias("reply2mail"));
		}
	}

	@OnAlias("reply2mail")
	public void Reply2Mail(TeamchatAPI api) throws IOException,
			MessagingException {
		String replybody = api.context().currentReply().getField("replybody");
		String subject = api.context().currentReply().getField("subject");
		String return_path = api.context().currentReply()
				.getField("return_path");
		String to = api.context().currentReply().getField("to");
		System.out.println(replybody + subject + return_path + to);
		Gmail service = getGmailService(authorize());
		String user = "me";
		MimeMessage email = createEmail(return_path, to, "Re: " + subject,
				replybody);
		Message message = createMessageWithEmail(email);
		message = service.users().messages().send(user, message).execute();
		api.performPostInCurrentRoom(new PrimaryChatlet()
				.setQuestionHtml("Reply Successful"));
	}

	public static MimeMessage createEmail(String to, String from,
			String subject, String bodyText) throws MessagingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);
		// InternetAddress tAddress = new InternetAddress(to);
		// InternetAddress fAddress = new InternetAddress(from);

		email.setFrom(new InternetAddress(from));
		email.addRecipient(javax.mail.Message.RecipientType.TO,
				new InternetAddress(to));
		email.setSubject(subject);
		email.setText(bodyText);
		return email;
	}

	public static Message createMessageWithEmail(MimeMessage email)
			throws MessagingException, IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		email.writeTo(bytes);
		String encodedEmail = Base64.encodeBase64URLSafeString(bytes
				.toByteArray());
		Message message = new Message();
		message.setRaw(encodedEmail);
		return message;
	}
}