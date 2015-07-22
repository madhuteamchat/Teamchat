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

public class LinkedInBot {
	boolean sharing=true, admin=false;
	OAuthService service;
	Token requestToken, accessToken;
	OAuthRequest request;
	org.scribe.model.Response response;
	String email, companyId, updateKey, roomId;
	int totalComments, totalLikes;
	Properties configProps = null;
	
	@OnKeyword ("help")
	public void help (TeamchatAPI api) {
		email = api.context().currentSender().getEmail();
		roomId = api.context().currentRoom().getId();
		
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
        .callback(configProps.getProperty("server").trim())
        .build();
		
		requestToken = service.getRequestToken();
		String authUrl = service.getAuthorizationUrl(requestToken);
		authUrl = createEmbeddedLink(authUrl, "Authenticate", "http");
		System.out.println(authUrl);
		
		DBHandler db = new DBHandler ();
		db.setData(requestToken.getToken(), requestToken.getSecret(), roomId, email);
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("Enter Following Keywords -<br/><ol>"
								+ "<li><a href='" + authUrl + "' target='_blank'>'link'</a> - Click or enter keyword to authenticate Teamchat</li>"
								+ "<li>'profile' - Get Profile Information</li>"
								+ "<li>'post' - Post on LinkedIn</li></ol>"
								//+ "<li>'company' - Add and get company details</li>"
								//+ "<li>'recent' - Get company's recent update</li></ol>"
								+ "<b color=red>please authenticate first for using other functionalities</b>")));
	}

	@OnKeyword("link")
	public void link(TeamchatAPI api) {
		email = api.context().currentSender().getEmail();
		roomId = api.context().currentRoom().getId();
		
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
        .callback(configProps.getProperty("server").trim())
        .build();
		
		requestToken = service.getRequestToken();
		String authUrl = service.getAuthorizationUrl(requestToken);
		authUrl = createEmbeddedLink(authUrl, "Authenticate", "http");
		System.out.println(authUrl);
		
		DBHandler db = new DBHandler ();
		db.setData(requestToken.getToken(), requestToken.getSecret(), roomId, email);
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml("<a href='" + authUrl + "' target='_blank'>"
						+ "<img src=\"http://www.talentrise.org/wp-content/themes/rise/img/linkedin.png\" "
						+ "style=\"width:100%;height:50px;border:0\">"
						+ "</a>")));
		
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
		
		email = api.context().currentSender().getEmail();
		String[] data = db.getAccessData(email);
		Token t = new Token (data[1], data[2]);
		
		request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/people/~?format=json");
		service.signRequest(t, request); // the access token from step 4
		response = request.send();
		
		JSONObject j1 = new JSONObject (response.getBody());
		
		request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/people/~:(id,num-connections,picture-url)?format=json");
		service.signRequest(t, request); // the access token from step 4
		response = request.send();
		
		JSONObject j2 = new JSONObject (response.getBody());
		System.out.println(j2.toString());
		
		if (j2.length() == 3)	
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<table border=0 width=100%><tr><td align=center><img src='" + j2.getString("pictureUrl") + "'></img>"
								+ "</td></tr><tr><td align=center>" + j1.getString ("firstName") + " " + j1.getString ("lastName") + "</td></tr>"
								+ "<tr><td align=center>\'" + j1.getString("headline") + "\'</td></tr>"
								+ "<tr><td align=center>Connections-" + j2.getInt("numConnections") + "</td></tr></table>")));
		else
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<table border=0 width=100%><tr><td align=center><img src='" + "" + "'></img>"
									+ "</td></tr><tr><td align=center>Name - " + j1.getString ("firstName") + " " + j1.getString ("lastName") + "</td></tr>"
									+ "<tr><td align=center>\'" + j1.getString("headline") + "\'</td></tr>"
									+ "<tr><td align=center>Connections-" + j2.getInt("numConnections") + "</td></tr></table>")));
	}
	
	@OnKeyword ("post")
	public void post (TeamchatAPI api) {
		try {
			configProps = loadPropertyFromClasspath("linkedin-config.properties", LinkedInBot.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		email = api.context().currentSender().getEmail();
		service = new ServiceBuilder()
        .provider(LinkedInApi.withScopes("w_share"))
        .apiKey(configProps.getProperty("apikey").trim())
        .apiSecret(configProps.getProperty("apisecret").trim())
        .build();
		
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Title").addRegexValidation("[^()]", "This field cannot be left blank").name("title"));
		f.addField(api.objects().input().label("Comment").addRegexValidation("[^()]", "This field cannot be left blank").name("com"));
		f.addField(api.objects().input().label("Description").addRegexValidation("[^()]", "This field cannot be left blank").name("desc"));
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
		String com = api.context().currentReply().getField("com");
		String title = api.context().currentReply().getField("title");
		String desc = api.context().currentReply().getField("desc");
		String url = api.context().currentReply().getField("url");
		String imgUrl = api.context().currentReply().getField("imageUrl");
		
		DBHandler db = new DBHandler();
		String[] data = db.getAccessData(email);
		Token t = new Token(data[1], data[2]);

		JSONObject j = new JSONObject("{\"comment\":" + com
				+ ",\"content\":{\"title\":" + title + ",\"description\":"
				+ desc + ",\"submitted-url\":" + url
				+ ",\"submitted-image-url\":" + imgUrl
				+ "},\"visibility\":{\"code\":\"anyone\"}}");

		request = new OAuthRequest(Verb.POST,
				"https://api.linkedin.com/v1/people/~/shares?format=json");
		request.addHeader("Content-Type", "application/json");
		request.addHeader("x-li-format", "json");
		request.addPayload(j.toString());
		service.signRequest(t, request);
		response = request.send();
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
}