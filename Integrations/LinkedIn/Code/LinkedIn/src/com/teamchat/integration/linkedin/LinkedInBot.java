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
import com.teamchat.integration.linkedin.utils.Utility;

public class LinkedInBot {
	//private static final boolean DEBUG = true;
	boolean sharing=true, admin=false;
	OAuthService service;
	Token requestToken, accessToken;
	OAuthRequest request;
	org.scribe.model.Response response;
	String companyId, updateKey, roomId;
	int totalComments, totalLikes;
	Properties configProps = null;
	
	@OnKeyword ("help")
	public void help (TeamchatAPI api) {
		String email = api.context().currentSender().getEmail();
		
		DBHandler db = new DBHandler();
		int acc = db.check (email);
		
		if (acc == 0) {
			roomId = api.context().currentRoom().getId();

			try {
				configProps = loadPropertyFromClasspath(
						"linkedin-config.properties", LinkedInBot.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			service = new ServiceBuilder()
					.provider(LinkedInApi.withScopes("w_share"))
					.apiKey(configProps.getProperty("apikey").trim())
					.apiSecret(configProps.getProperty("apisecret").trim())
					.callback(configProps.getProperty("server").trim()).build();

			requestToken = service.getRequestToken();
			String authUrl = service.getAuthorizationUrl(requestToken);
			authUrl = createEmbeddedLink(authUrl, "Authenticate", "http");
			System.out.println(authUrl);
			
			db.setData(requestToken.getToken(), requestToken.getSecret(),
					roomId, email);

			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<a href='"
									+ authUrl
									+ "' target='_blank'>"
									+ "<img src=\"http://www.talentrise.org/wp-content/themes/rise/img/linkedin.png\" "
									+ "style=\"width:100%;height:50px;border:0\">"
									+ "</a>")));
		} else {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<b>Use following keywords -</b><br/><ul>"
									+ "<li>'profile' - Get Profile Information</li>"
									+ "<li>'post' - Post on LinkedIn</li></ol>"
									+ "<li>'reset' - Remove your account from teamchat</li></ul>")));
		}
	}
	
	@OnKeyword ("profile")
	public void profile (TeamchatAPI api) {
		try {
			configProps = loadPropertyFromClasspath("linkedin-config.properties", LinkedInBot.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		service = new ServiceBuilder()
        .provider(LinkedInApi.withScopes("w_share"))
        .apiKey(configProps.getProperty("apikey").trim())
        .apiSecret(configProps.getProperty("apisecret").trim())
        .build();
		
		DBHandler db = new DBHandler();
		
		String email = api.context().currentSender().getEmail();
		String[] data = db.getAccessData(email);
		Token t = new Token (data[1], data[2]);
		
		request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/people/~?format=json");
		service.signRequest(t, request); // the access token from step 4
		response = request.send();

		System.out.println(response.getBody());
		
		if (response.getCode() == 200) {
			JSONObject j1 = new JSONObject(response.getBody());
			request = new OAuthRequest(
					Verb.GET,
					"https://api.linkedin.com/v1/people/~:(id,num-connections,picture-url)?format=json");
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
			
			if (j2.length() == 3)
				html = html.replace("__imgUrl", j2.getString("pictureUrl"));
			else
				html = html.replace("__imgUrl", "https://citizenmed.files.wordpress.com/2011/08/user-icon1.jpg");
			
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml(html)));
		} else if (response.getCode() == 401) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("You are not authorized, please authorize first.")));
		} else {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("Something went wrong, try again!")));
		}
	}
	
	@OnKeyword ("post")
	public void post (TeamchatAPI api) {
		try {
			configProps = loadPropertyFromClasspath("linkedin-config.properties", LinkedInBot.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		service = new ServiceBuilder()
        .provider(LinkedInApi.withScopes("w_share"))
        .apiKey(configProps.getProperty("apikey").trim())
        .apiSecret(configProps.getProperty("apisecret").trim())
        .build();
		
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Title").addRegexValidation("[^()]", "please enter a value").name("title"));
		f.addField(api.objects().input().label("Comment").addRegexValidation("[^()]", "please enter a value").name("com"));
		f.addField(api.objects().input().label("Description").addRegexValidation("[^()]", "please enter a value").name("desc"));
		f.addField(api.objects().input().label("URL").name("url"));
		f.addField(api.objects().input().label("Image URL").name("imageUrl"));
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("Share information")
						.setReplyScreen(f).setReplyLabel("Enter")
						.showDetails(true)
						.setDetailsLabel("Shared")
						.alias("details")));
	}
	
	@OnAlias ("details")
	public void details (TeamchatAPI api) throws IOException {
		String email = api.context().currentReply().senderEmail();
		System.out.println(email);
		
		String com = api.context().currentReply().getField("com") == null ? "" : api.context().currentReply().getField("com");
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
		
		json.put("comment", com);
		json.put("content", json1);
		json.put("visibility", json2);
		
		request = new OAuthRequest(Verb.POST,
				"https://api.linkedin.com/v1/people/~/shares?format=json");
		request.addHeader("Content-Type", "application/json");
		request.addHeader("x-li-format", "json");
		request.addPayload(json.toString());
		service.signRequest(t, request);
		response = request.send();
		
		System.out.println("\n\n\n\n" + response.getCode() + "\n\n\n\n");
		
		JSONObject resp = new JSONObject(response.getBody());
		String update = resp.getString("updateUrl");
		
		if (response.getCode() == 201) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("Posted successfully, Go to linkedin: <a href='" + update + "' target='_blank'>'" + title + "'</a>")));
		} else {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("Something went wrong, try again!")));
		}
	}
	
	@OnKeyword ("reset")
	public void reset (TeamchatAPI api) {
		String email = api.context().currentSender().getEmail();
		
		DBHandler db = new DBHandler();
		int success = db.remove (email);
		if (success == 1) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("LinkedIn access has been removed. To allow send 'help' keyword again")));
		} else {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("Something went wrong, try again!")));
		}
	}
	
	public static String createEmbeddedLink(String url, String title, String protocol) {
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
	
	public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException {
		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;
	}
	
	/*private void log(String data) {
		if(DEBUG) {
			System.out.println(data);
		}
	}*/
}