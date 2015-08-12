package com.teamchat.integration.linkedin;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.integration.linkedin.utils.Utility;

public class LinkedInBot
{
	boolean sharing = true, admin = false;
	OAuthService service;
	Token requestToken, accessToken;
	OAuthRequest request;
	org.scribe.model.Response response;
	String companyId, updateKey, roomId;
	int totalComments, totalLikes;
	Properties configProps = null;

	@OnKeyword("help")
	public void help(TeamchatAPI api)
	{
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.help)));
	}

	@OnKeyword("connect")
	public void link(TeamchatAPI api)
	{
		String email = api.context().currentSender().getEmail();

		DBHandler db = new DBHandler();
		int acc = db.check(email);

		if (acc == 0)
		{
			roomId = api.context().currentRoom().getId();

			try
			{
				configProps = loadPropertyFromClasspath("linkedin-config.properties", LinkedInBot.class);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			service = new ServiceBuilder().provider(LinkedInApi.withScopes("w_share")).apiKey(configProps.getProperty("apikey").trim()).apiSecret(configProps.getProperty("apisecret").trim())
					.callback(configProps.getProperty("server").trim()).build();

			requestToken = service.getRequestToken();
			String authUrl = service.getAuthorizationUrl(requestToken);
			authUrl = createEmbeddedLink(authUrl, "Authenticate", "http");
			System.out.println(authUrl);

			db.setData(requestToken.getToken(), requestToken.getSecret(), roomId, email);

			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.auth.replace("__auth", authUrl))));
		}
		else
		{
			api.perform(api.context().currentRoom().post(new TextChatlet("already connected")));
		}
	}

	@OnKeyword("profile")
	public void profile(TeamchatAPI api)
	{
		try
		{
			configProps = loadPropertyFromClasspath("linkedin-config.properties", LinkedInBot.class);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String emaila = api.context().currentSender().getEmail();

		DBHandler dba = new DBHandler();
		int acc = dba.check(emaila);

		if (acc == 0)
		{
			api.perform(api.context().currentRoom().post(new TextChatlet("please authenticate first!")));

		}
		else
		{
			service = new ServiceBuilder().provider(LinkedInApi.withScopes("w_share")).apiKey(configProps.getProperty("apikey").trim()).apiSecret(configProps.getProperty("apisecret").trim()).build();

			DBHandler db = new DBHandler();

			String email = api.context().currentSender().getEmail();
			String[] data = db.getAccessData(email);
			Token t = new Token(data[1], data[2]);

			request = new OAuthRequest(Verb.GET, Utility.people1);
			service.signRequest(t, request); // the access token from step 4
			response = request.send();

			System.out.println(response.getBody());

			if (response.getCode() == 200)
			{
				JSONObject j1 = new JSONObject(response.getBody());
				request = new OAuthRequest(Verb.GET, Utility.people2);
				service.signRequest(t, request); // the access token from step 4
				response = request.send();

				System.out.println(response.getBody());

				JSONObject j2 = new JSONObject(response.getBody());
				String html = Utility.shareDocHTML;
				html = html.replace("__link", j1.getJSONObject("siteStandardProfileRequest").getString("url"));
				html = html.replace("__name", j1.getString("firstName") + " " + j1.getString("lastName"));

				html = html.replace("__headline", j1.getString("headline"));
				html = html.replace("__cons", "Connections-" + j2.getInt("numConnections"));
				html = html.replace("__profile", "Open profile in LinkedIn");

				if (j2.length() == 3) html = html.replace("__imgUrl", j2.getString("pictureUrl"));
				else
					html = html.replace("__imgUrl", "https://citizenmed.files.wordpress.com/2011/08/user-icon1.jpg");

				api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(html)));
			}
			else if (response.getCode() == 401)
			{
				api.perform(api.context().currentRoom().post(new TextChatlet("you are not authorized, please authorize first!")));
			}
			else
			{
				api.perform(api.context().currentRoom().post(new TextChatlet("something went wrong, try again!")));
			}
		}
	}

	@OnKeyword("update")
	public void update(TeamchatAPI api)
	{
		String emaila = api.context().currentSender().getEmail();

		DBHandler dba = new DBHandler();
		int acc = dba.check(emaila);

		if (acc == 0)
		{
			api.perform(api.context().currentRoom().post(new TextChatlet("please authenticate first")));

		}
		else
		{
			Form f = api.objects().form();
			f.addField(api.objects().input().label("Share an update").addRegexValidation(".+", "please enter a value").name("comment"));

			api.perform(api.context().currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml("What's on your mind?").setReplyScreen(f).setReplyLabel("Enter").showDetails(true).setDetailsLabel("Shared").alias("updatePost")));
		}
	}

	@OnAlias("updatePost")
	public void updatePost(TeamchatAPI api) throws IOException
	{
		try
		{
			configProps = loadPropertyFromClasspath("linkedin-config.properties", LinkedInBot.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		String email = api.context().currentReply().senderEmail();
		System.out.println(email);

		String comment = api.context().currentReply().getField("comment") == null ? "" : api.context().currentReply().getField("comment");

		DBHandler db = new DBHandler();
		String[] data = db.getAccessData(email);
		Token t = new Token(data[1], data[2]);
		System.out.println(t.toString());

		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();

		json1.put("code", "anyone");

		json.put("comment", comment);
		json.put("visibility", json1);

		service = new ServiceBuilder().provider(LinkedInApi.withScopes("w_share")).apiKey(configProps.getProperty("apikey").trim()).apiSecret(configProps.getProperty("apisecret").trim()).build();
		
		request = new OAuthRequest(Verb.POST, Utility.share);
		request.addHeader("Content-Type", "application/json");
		request.addHeader("x-li-format", "json");
		request.addPayload(json.toString());
		service.signRequest(t, request);
		response = request.send();

		System.out.println("\n\n\n\n" + response.getCode() + "\n\n\n\n");

		JSONObject resp = new JSONObject(response.getBody());

		if (response.getCode() == 201)
		{
			String update = resp.getString("updateUrl");
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.update.replace("__update", update).replace("__title", comment))));
		}
		else
		{
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml("oops! something went wrong, linkedin is not able to process your request<br/>please try again,"
							+ " or contact at <b>support@teamchat.com</b>")));
		}
	}

	@OnKeyword("publish")
	public void publish(TeamchatAPI api)
	{
		String emaila = api.context().currentSender().getEmail();

		DBHandler dba = new DBHandler();
		int acc = dba.check(emaila);

		if (acc == 0)
		{
			api.perform(api.context().currentRoom().post(new TextChatlet("please authenticate first")));

		}
		else
		{
			Form f = api.objects().form();
			f.addField(api.objects().input().label("Title").addRegexValidation(".+", "please enter a value").name("title"));
			f.addField(api.objects().input().label("Description").addRegexValidation(".+", "please enter a value").name("desc"));
			f.addField(api.objects().input().label("Web URL").addRegexValidation(".+", "please enter a value").name("url"));
			f.addField(api.objects().input().label("Image URL (max size: 4mb)").addRegexValidation(".+", "please enter a value").name("imageUrl"));

			api.perform(api.context().currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml("Share information").setReplyScreen(f).setReplyLabel("Enter").showDetails(true).setDetailsLabel("Shared").alias("publishPost")));
		}
	}

	@OnAlias("publishPost")
	public void publishPost(TeamchatAPI api) throws IOException
	{
		try
		{
			configProps = loadPropertyFromClasspath("linkedin-config.properties", LinkedInBot.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		String email = api.context().currentReply().senderEmail();
		System.out.println(email);

		String title = api.context().currentReply().getField("title") == null ? "" : api.context().currentReply().getField("title");
		String desc = api.context().currentReply().getField("desc") == null ? "" : api.context().currentReply().getField("desc");
		String url = api.context().currentReply().getField("url") == null ? "" : api.context().currentReply().getField("url");
		String imgUrl = api.context().currentReply().getField("imageUrl") == null ? "" : api.context().currentReply().getField("imageUrl");

		DBHandler db = new DBHandler();
		String[] data = db.getAccessData(email);
		Token t = new Token(data[1], data[2]);
		System.out.println(t.toString());

		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		JSONObject json2 = new JSONObject();

		json1.put("title", title);
		json1.put("description", desc);
		json1.put("submitted-url", url);
		json1.put("submitted-image-url", imgUrl);

		json2.put("code", "anyone");

		json.put("comment", "");
		json.put("content", json1);
		json.put("visibility", json2);

		service = new ServiceBuilder().provider(LinkedInApi.withScopes("w_share")).apiKey(configProps.getProperty("apikey").trim()).apiSecret(configProps.getProperty("apisecret").trim()).build();
		
		request = new OAuthRequest(Verb.POST, Utility.share);
		request.addHeader("Content-Type", "application/json");
		request.addHeader("x-li-format", "json");
		request.addPayload(json.toString());
		service.signRequest(t, request);
		response = request.send();

		System.out.println("\n\n\n\n" + response.getCode() + "\n\n\n\n");

		JSONObject resp = new JSONObject(response.getBody());

		if (response.getCode() == 201)
		{
			String update = resp.getString("updateUrl");
			api.perform(api.context().currentRoom().post(new TextChatlet(Utility.update.replace("__update", update).replace("__title", title))));
		}
		else
		{
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml("oops! something went wrong, linkedin is not able to process your request<br/>please try again,"
							+ " or contact at <b>support@teamchat.com</b>")));
		}
	}

	@OnKeyword("reset")
	public void reset(TeamchatAPI api)
	{
		String emaila = api.context().currentSender().getEmail();

		DBHandler dba = new DBHandler();
		int acc = dba.check(emaila);

		if (acc == 0)
		{
			api.perform(api.context().currentRoom().post(new TextChatlet("please authenticate first")));
		}
		else
		{
			String email = api.context().currentSender().getEmail();

			DBHandler db = new DBHandler();
			int success = db.remove(email);
			if (success == 1)
			{
				api.perform(api.context().currentRoom().post(new TextChatlet("linkedin access has been removed, to allow send 'help' keyword again")));
			}
			else
			{
				api.perform(api.context().currentRoom().post(new TextChatlet("something went wrong, try again!")));
			}
		}
	}

	public static String createEmbeddedLink(String url, String title, String protocol)
	{
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

	public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException
	{
		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;
	}
}