package com.teamchat.integration.linkedin.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.json.*;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class LinkIt {
	boolean sharing=true, admin=false;
	OAuthService service;
	Token requestToken, accessToken;
	OAuthRequest request;
	org.scribe.model.Response response;
	String email, companyId, updateKey, roomId;
	int totalComments, totalLikes;
	
	@OnKeyword ("help")
	public void help (TeamchatAPI api) {
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<ol><li>'link' - Connect to LinkedIn</li>"
								+ "<li>'profile' - Get Profile Information</li>"
								+ "<li>'share' - Post on LinkedIn</li>"
								+ "<li>'company' - Add and get company details</li>"
								+ "<li>'recent' - Get company's recent update</li></ol>")));
	}
	
	@OnKeyword("link")
	public void link(TeamchatAPI api) {
		email = api.context().currentSender().getEmail();
		roomId = api.context().currentRoom().getId();
		
		service = new ServiceBuilder()
        .provider(LinkedInApi.withScopes("w_share"))
        .apiKey("754z46slfke0xm")
        .apiSecret("Jm46YNdPTb2toxoY")
        .callback("http://localhost:8080/LinkedInIntegration/Linking")
        .build();
		
		requestToken = service.getRequestToken();
		String authUrl = service.getAuthorizationUrl(requestToken);
		
		DBHandler db = new DBHandler ();
		db.setData(requestToken.getToken(), requestToken.getSecret(), roomId, email);
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml("<a href='" + authUrl + "' target='_blank'>"
						+ "<img src=\"http://www.talentrise.org/wp-content/themes/rise/img/linkedin.png\" "
						+ "style=\"width:378px;height:50px;border:0\">"
						+ "</a>")));
		
	}
	
	@OnKeyword ("profile")
	public void profile (TeamchatAPI api) {
		service = new ServiceBuilder()
        .provider(LinkedInApi.withScopes("w_share"))
        .apiKey("754z46slfke0xm")
        .apiSecret("Jm46YNdPTb2toxoY")
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
						.setQuestionHtml("<table border=0 width=378px><tr><td rowspan=4><img src='" + j2.getString("pictureUrl") + "'></img>"
								+ "</td></tr><tr><td>" + j1.getString ("firstName") + " " + j1.getString ("lastName") + "</td></tr>"
								+ "<tr><td>\"" + j1.getString("headline") + "\"</td></tr>"
								+ "<tr><td>Connections-" + j2.getInt("numConnections") + "</td></tr></table>")));
		else
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<table border=0 width=378px><tr><td rowspan=4><img src='" + "" + "'></img>"
									+ "</td></tr><tr><td>Name - " + j1.getString ("firstName") + " " + j1.getString ("lastName") + "</td></tr>"
									+ "<tr><td>Headline - \"" + j1.getString("headline") + "\"</td></tr>"
									+ "<tr><td>Connections-" + j2.getInt("numConnections") + "</td></tr></table>")));
	}
	
	@OnKeyword ("share")
	public void share (TeamchatAPI api) {
		email = api.context().currentSender().getEmail();
		service = new ServiceBuilder()
        .provider(LinkedInApi.withScopes("w_share"))
        .apiKey("754z46slfke0xm")
        .apiSecret("Jm46YNdPTb2toxoY")
        .build();
		
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Comment").name("com"));
		f.addField(api.objects().input().label("Title").name("title"));
		f.addField(api.objects().input().label("Description").name("desc"));
		f.addField(api.objects().input().label("URL").name("url"));
		f.addField(api.objects().input().label("Image URL").name("imgUrl"));
		
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
		String imgUrl = api.context().currentReply().getField("imgUrl");
		
		if (com.equals("")|| com==null) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet ("All fields are required")));
		}
		else if (title.equals("") || title==null) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet ("All fields are required")));
		}
		else if (desc.equals("") || desc==null) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet ("All fields are required")));
		}
		else if (url.equals("") || url==null) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet ("All fields are required")));
		}
		else if (imgUrl.equals("") || imgUrl==null) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet ("All fields are required")));
		}
		else {		
			DBHandler db = new DBHandler();
			String[] data = db.getAccessData(email);
			Token t = new Token (data[1], data[2]);
		
			JSONObject j = new JSONObject ("{\"comment\":" + com + ",\"content\":{\"title\":" + title + ",\"description\":" + desc + ",\"submitted-url\":" + url + ",\"submitted-image-url\":" + imgUrl + "},\"visibility\":{\"code\":\"anyone\"}}");
		
			request = new OAuthRequest(Verb.POST, "https://api.linkedin.com/v1/people/~/shares?format=json");
			request.addHeader("Content-Type", "application/json");
			request.addHeader("x-li-format", "json");
			request.addPayload(j.toString());
			service.signRequest(t, request);
			response = request.send();
		}
	}
	
	@OnKeyword ("company")
	public void company (TeamchatAPI api) {
		email = api.context().currentSender().getEmail();
		
		Form f = api.objects().form();
		f.addField(api.objects().input().label("ID").name("id"));
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("Enter Company ID")
						.setReplyScreen(f).setReplyLabel("ID")
						.alias("comp")));
	}
	
	@OnAlias ("comp")
	public void comp (TeamchatAPI api) {
		companyId = api.context().currentReply().getField("id");
		
		service = new ServiceBuilder()
        .provider(LinkedInApi.withScopes("rw_company_admin"))
        .apiKey("754z46slfke0xm")
        .apiSecret("Jm46YNdPTb2toxoY")
        .build();
		
		DBHandler db = new DBHandler();
		String[] data = db.getAccessData(email);
		Token t = new Token (data[1], data[2]);
		
		request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/companies/" + companyId + "/is-company-share-enabled?format=json");
		service.signRequest(t, request);
		response = request.send();
		if (response.getCode() == 400) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet ("Invalid input")));
		} else {
			sharing = Boolean.parseBoolean(response.getBody());
			System.out.println(sharing);
		
			request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/companies/" + companyId + "/relation-to-viewer/is-company-share-enabled?format=json");
			service.signRequest(t, request);
			response = request.send();
			admin = Boolean.parseBoolean(response.getBody());
			System.out.println(admin);
		
			request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/companies?format=json&is-company-admin=true");
			service.signRequest(t, request);
			response = request.send();
			if (response.isSuccessful()) {
				JSONObject list = new JSONObject (response.getBody());
				System.out.println(list);
			}
		
			request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/companies/"+ companyId +":(id,name,ticker,description)?format=json");
			service.signRequest(t, request);
			response = request.send();
			JSONObject profile = new JSONObject (response.getBody());
		
			request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/companies/" + companyId + "/num-followers?format=json");
			service.signRequest(t, request);
			response = request.send();
			String followers = response.getBody();
		
			StringBuffer out = new StringBuffer ("<ul><li>Company Name - " + profile.getString("name") + "</li>");
			out.append(admin?"<li>Hello admin</li>":"<li>You are not admin</li>");
			out.append(sharing?"<li>Sharing is enabled</li>":"<li>Sharing is not enabled</li>");
			out.append("<li>Description - " + profile.getString("description") + "</li>"
					+ "<li>Followers - " + followers + "</li></ul>");
		
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
					.setQuestionHtml(out.toString())));
		}
	}
	
	@OnKeyword ("recent")
	public void updates (TeamchatAPI api) {
		email = api.context().currentSender().getEmail();
		
		Form f = api.objects().form();
		f.addField(api.objects().input().label("ID").name("id"));
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("Enter Company ID")
						.setReplyScreen(f).setReplyLabel("ID")
						.alias("comUpdates")));
	}
	
	@OnAlias ("comUpdates")
	public void comUpdates (TeamchatAPI api) {
		companyId = api.context().currentReply().getField("id");
		
		service = new ServiceBuilder()
        .provider(LinkedInApi.withScopes("rw_company_admin"))
        .apiKey("754z46slfke0xm")
        .apiSecret("Jm46YNdPTb2toxoY")
        .build();

		DBHandler db = new DBHandler();
		String[] data = db.getAccessData(email);
		Token t = new Token (data[1], data[2]);
		
		request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/companies/"+ companyId +"/updates?format=json");
		request.addQuerystringParameter("count", "1");
		service.signRequest(t, request);
		response = request.send();
		if (response.getCode() == 400)
			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet ("Invalid input")));
		else {
			JSONObject recent = new JSONObject(response.getBody());
		
			boolean comment = recent.getJSONArray("values").getJSONObject(0).getBoolean("isCommentable");
			boolean like = recent.getJSONArray("values").getJSONObject(0).getBoolean("isLikable");
			String update = recent.getJSONArray("values").getJSONObject(0).getJSONObject("updateContent").getJSONObject("companyStatusUpdate")
					.getJSONObject("share").getString ("comment");
		
			updateKey = recent.getJSONArray("values").getJSONObject(0).getString("updateKey");
		
			if (comment) {
				request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/companies/" + companyId + "/updates/key=" + updateKey + "/update-comments?format=json");
				service.signRequest(t, request);
				response = request.send();
				JSONObject j = new JSONObject (response.getBody());
				totalComments = j.getInt("_total");
			}
			if (like) {
				request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/companies/" + companyId + "/updates/key=" + updateKey + "/likes?format=json");
				service.signRequest(t, request);
				response = request.send();
				JSONObject j = new JSONObject (response.getBody());
				totalLikes = j.getInt("_total");
			}
		
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
					.setQuestionHtml("<table border=2><tr><td colspan=2 align=center><b>Recent Update</b></td></tr>"
							+ "<tr><td>Update</td><td>" + update + "</td></tr>"
							+ "<tr><td>Total Comments</td><td>" + totalComments + "</td></tr>"
							+ "<tr><td>Total Likes</td><td>" + totalLikes + "</td></tr></table>")));
		}
	}
	
	public static void main(String[] args) {
		String path = "/data/solutions-config/workflow-data/zendesk/zendesk-config.properties";
		Properties configProps = new Properties();
		
		try {
			configProps = loadPropertyFileFromDisk(path);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data")
				.setEmail(configProps.getProperty("botemail").trim())
				.setPassword(configProps.getProperty("botpass").trim());
		api.startReceivingEvents(new LinkIt());
	}
	
	public static Properties loadPropertyFileFromDisk(String filePath) throws Exception
	{
		String os = System.getProperty("os.name");
		if (os.toLowerCase().contains("windows"))
		{
			filePath = "d:" + filePath;

		}
		Properties configProp = new Properties();
		FileInputStream fIS = new FileInputStream(filePath);
		configProp.load(fIS);
		fIS.close();
		return configProp;
	}
}
