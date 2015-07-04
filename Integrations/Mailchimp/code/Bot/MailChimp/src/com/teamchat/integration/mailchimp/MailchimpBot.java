package com.teamchat.integration.mailchimp;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class MailchimpBot {

	public static String at = null;
	public static String smail = null;
	
	public static String us = null;
	public static TeamchatAPI apig;

	public static String name[];
	public static String title[];
	public static String lid[];
	public static String campid[];

	public void setToken(TeamchatAPI api, String token, String us1) {
		at = token;
		us = us1;
		smail = api.context().currentSender().getEmail();
		accesstoken.smail = smail;
		System.out.println(at);
		System.out.println(us);
		System.out.println(us);
		// ManageDB.delete(token);
	}

	

	@OnKeyword("help")
	public void oncreate2(TeamchatAPI api)

	{

		apig = api;
		smail = api.context().currentSender().getEmail();
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("Hi!! This Mailchimp Bot , You can Use Me with the Following Keywords  "

								+ "<h4> <center>List Of Keywords :</center> </h4>"

								+ "<center><h4 style=\"color:#159CEB;\">Please Enter Correct Keyword</h4></center>"
								+ "<br/><ul><li><b>login:</b> to  connect with your mailchimp acoount.</li>"
								+ "<br/><li><b>campaign:</b> Report bounces, clicks detail of a campaign.</li>"
								+ "<br/><li><b>list:</b> Report for list.</li>"
								+ "<br/><li><b>notify :</b>Get List  Notification of Your MailChimp Account.</li>"
								+"<br><li><b>logout:</b>Disconnect Your Account.</li></ul>"

						)));
	}

	@OnKeyword("login")
	public void login(TeamchatAPI api) {
		apig = api;

		at = ManageDB.retrieve(smail);
		if (at == null) {
			String url = "https://login.mailchimp.com/oauth2/authorize?response_type=code&client_id="
					+ PropertiesFile.getValue("client_id")
					+ "&redirect_uri="
					+ PropertiesFile.getValue("callback");
			String embed_url = createEmbeddedLink(url, "logging in...", "https");
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<h5>Please Click On below link To Connect your mailchimp Account </h5>"

									+ "<center><a href="
									+ embed_url
									+ ">click to login</a><center>")));

		} else {

			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<h5>You are  Already connected with Your Mailchimp Account Please type help keyword </h5>")));

		}
	}
   
	@OnKeyword("logout")
	public void logout(TeamchatAPI api)
	{
		smail = api.context().currentSender().getEmail();
	
		at = ManageDB.retrieve(smail);

		if (at == null) {
			
			
			String url = "https://login.mailchimp.com/oauth2/authorize?response_type=code&client_id="
					+ PropertiesFile.getValue("client_id")
					+ "&redirect_uri="
					+ PropertiesFile.getValue("callback");
			String embed_url = createEmbeddedLink(url, "logging in...", "https");
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<h5>Please  first login  with your mailchimp Account  </h5>"

									+ "<center><a href="
									+ embed_url
									+ ">click to login</a><center>")));
		}else
			
		ManageDB.remove(smail);
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<h5> You are sucessfully logout")));
		
		
	}
	
	@OnKeyword("notify")
	public void notify(TeamchatAPI api) {

		smail = api.context().currentSender().getEmail();

		at = ManageDB.retrieve(smail);

		if (at == null) {
			
			
			String url = "https://login.mailchimp.com/oauth2/authorize?response_type=code&client_id="
					+ PropertiesFile.getValue("client_id")
					+ "&redirect_uri="
					+ PropertiesFile.getValue("callback");
			String embed_url = createEmbeddedLink(url, "logging in...", "https");
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<h5>Please  first login  with your mailchimp Account  </h5>"

									+ "<center><a href="
									+ embed_url
									+ ">click to login</a><center>")));
			
			

		} else {

			us = ManageDB.retrieve1(smail);

			new Listm().nlistGrowthHistory(api, at, us);

			String label1 = "Name of list", name1 = "nlist";
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml(
									"Please Select the  List for Notifications")
							.setReplyLabel("select")
							.setReplyScreen(
									api.objects()
											.form()
											.addField(
													getOptions2(api, label1,
															name1, name)))
							.alias("webhooklistid")));

			
		}
	}

	private Field getOptions2(TeamchatAPI api, String label1, String name1,
			String[] nbArray) {
		// TODO Auto-generated method stub
		Field f = api.objects().select().label(label1).name(name1);
		for (int i = 0; i < nbArray.length; i++) {
			f.addOption(nbArray[i]);
		}
		return f;
	}

	@OnAlias("webhooklistid")
	public void webhooklistid(TeamchatAPI api) {

		
		String temp = api.context().currentReply().getField("nlist");
		String tempid = null;
		for (int i = 0; i < name.length; i++) {

			if (temp.equals(name[i])) {
				tempid = lid[i];
			}
		}

		System.out.println("list id =" + tempid);

		Listm l1 = new Listm();

		l1.webhookadd(api, at, us, smail, tempid);
	}

	@OnKeyword("list")
	public void listm(TeamchatAPI api) {

		smail = api.context().currentSender().getEmail();
		apig = api;

		
		at = ManageDB.retrieve(smail);

		if (at == null) {
			String url = "https://login.mailchimp.com/oauth2/authorize?response_type=code&client_id="
					+ PropertiesFile.getValue("client_id")
					+ "&redirect_uri="
					+ PropertiesFile.getValue("callback");
			String embed_url = createEmbeddedLink(url, "logging in...", "https");
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<h5>Please  first login  with your mailchimp Account  </h5>"

									+ "<center><a href="
									+ embed_url
									+ ">click to login</a><center>")));
			

		}

		else {

			us = ManageDB.retrieve1(smail);

			new Listm().nlistGrowthHistory(api, at, us);

			String label1 = "Name of list", name1 = "nlist";
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("Select the  List")
							.setReplyLabel("select")
							.setReplyScreen(
									api.objects()
											.form()
											.addField(
													getOptions(api, label1,
															name1, name)))
							.alias("listid")));

		}

	}

	private Field getOptions(TeamchatAPI api, String label1, String name1,
			String[] nbArray) {
		// TODO Auto-generated method stub
		Field f = api.objects().select().label(label1).name(name1);
		for (int i = 0; i < nbArray.length; i++) {
			f.addOption(nbArray[i]);
		}
		return f;
	}

	@OnAlias("listid")
	public void listid(TeamchatAPI api) {
		String temp = api.context().currentReply().getField("nlist");
		String tempid = null;
		for (int i = 0; i < name.length; i++) {

			if (temp.equals(name[i])) {
				tempid = lid[i];
			}
		}

		System.out.println("list id =" + tempid);

		Listm l1 = new Listm();
		l1.listrepotrs(api, tempid);
	}

	@OnKeyword("campaign")
	public void camp(TeamchatAPI api) {
		smail = api.context().currentSender().getEmail();

		// ManageDB db=new ManageDB();
		at = ManageDB.retrieve(smail);

		if (at == null) {
			String url = "https://login.mailchimp.com/oauth2/authorize?response_type=code&client_id="
					+ PropertiesFile.getValue("client_id")
					+ "&redirect_uri="
					+ PropertiesFile.getValue("callback");
			String embed_url = createEmbeddedLink(url, "logging in...", "https");
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<h5>lease  first login  with your mailchimp Account  </h5>"

									+ "<center><a href="
									+ embed_url
									+ ">click to login</a><center>")));
			
		}

		else {

			System.out.println("Snder mail" + smail);
			us = ManageDB.retrieve1(smail);

			new Campaign().ncampaignStats(api, at, us);

			String label1 = "Name of Campaign", name1 = "ncamp";
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("Select the Campaign ")
							.setReplyLabel("Select")
							.setReplyScreen(
									api.objects()
											.form()
											.addField(
													getOptions1(api, label1,
															name1, title)))
							.alias("cmpid")));
		}

	}

	private Field getOptions1(TeamchatAPI api, String label1, String name1,
			String[] nbArray) {
		// TODO Auto-generated method stub
		Field f = api.objects().select().label(label1).name(name1);
		for (int i = 0; i < nbArray.length; i++) {
			f.addOption(nbArray[i]);
		}
		return f;
	}

	@OnAlias("cmpid")
	public void cmpid(TeamchatAPI api) {
		String temp = api.context().currentReply().getField("ncamp");
		String tempid = null;
		for (int i = 0; i < title.length; i++) {

			if (temp.equals(title[i])) {
				tempid = campid[i];
			}
		}

		System.out.println("camp id =" + tempid);
		Campaign cn = new Campaign();
		cn.campaignStats(api, at, us, tempid);
	}

	public static String createEmbeddedLink(String url, String title,
			String protocol) {
		JSONObject object = new JSONObject();
		JSONObject web = new JSONObject();
		web.put("title", title);
		web.put("cancelBtnName", "Back");
		web.put("minWidth", "200");
		web.put("draggable", "true");
		web.put("newWindow", "true");
		web.put("url", url);
		object.put("web", web);
		System.out.println(object.toString());
		byte[] byteArray = Base64.encodeBase64(object.toString().getBytes());
		String encodedString = new String(byteArray);
		String fUrl = protocol + "://teamchat:data=" + encodedString;

		return fUrl;

	}

}
