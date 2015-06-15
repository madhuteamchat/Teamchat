package linkit;

import java.io.IOException;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
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
	String email, id, updateKey;
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
		
		service = new ServiceBuilder()
        .provider(LinkedInApi.withScopes("w_share"))
        .apiKey("754z46slfke0xm")
        .apiSecret("Jm46YNdPTb2toxoY")
        .build();

		requestToken = service.getRequestToken();
		String authUrl = service.getAuthorizationUrl(requestToken);
								
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Enter Pin").name("pin"));
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("Click "
								+ "<a href='" + authUrl + "' target='_blank'>here</a> to authorize. Enter the pin obtained")
						.setReplyScreen(f).setReplyLabel("Enter")
						.alias("pin")));
	}
	
	@OnAlias("pin")
	public void access(TeamchatAPI api) {
		Verifier v = new Verifier(api.context().currentReply().getField("pin"));
		accessToken = service.getAccessToken(requestToken, v);
		
		api.data().addField (email, "Token", accessToken.getToken());
		api.data().addField (email, "Secret", accessToken.getSecret());
		
		request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/people/~?format=json");
		service.signRequest(accessToken, request); // the access token from step 4
		response = request.send();
		
		JSONObject j1 = new JSONObject (response.getBody());
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new TextChatlet("Hello! " + j1.getString ("firstName") + " " + j1.getString ("lastName") + "<br/>Successfully Connected")));
		
	}
	
	@OnKeyword ("profile")
	public void profile (TeamchatAPI api) {
		service = new ServiceBuilder()
        .provider(LinkedInApi.withScopes("w_share"))
        .apiKey("754z46slfke0xm")
        .apiSecret("Jm46YNdPTb2toxoY")
        .build();
		
		email = api.context().currentSender().getEmail();
		Token t = new Token (api.data().getField(email, "Token"), api.data().getField(email, "Secret"));
		
		request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/people/~?format=json");
		service.signRequest(t, request); // the access token from step 4
		response = request.send();
		
		JSONObject j1 = new JSONObject (response.getBody());
		
		request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/people/~:(id,num-connections,picture-url)?format=json");
		service.signRequest(t, request); // the access token from step 4
		response = request.send();
		
		JSONObject j2 = new JSONObject (response.getBody());
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("Name - " + j1.getString ("firstName") + " " + j1.getString ("lastName") + "<br/>"
								+ "Headline - " + j1.getString("headline") + "<br/>"
								+ "Number Of Connections - " + j2.getInt("numConnections"))));
	}
	
	@OnKeyword ("share")
	public void share (TeamchatAPI api) {
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
		
		String email = api.context().currentSender().getEmail();
		Token t = new Token (api.data().getField(email, "Token"), api.data().getField(email, "Secret"));
		
		JSONObject j = new JSONObject ("{\"comment\":" + com + ",\"content\":{\"title\":" + title + ",\"description\":" + desc + ",\"submitted-url\":" + url + ",\"submitted-image-url\":" + imgUrl + "},\"visibility\":{\"code\":\"anyone\"}}");
		
		request = new OAuthRequest(Verb.POST, "https://api.linkedin.com/v1/people/~/shares?format=json");
		request.addHeader("Content-Type", "application/json");
		request.addHeader("x-li-format", "json");
		request.addPayload(j.toString());
		service.signRequest(t, request);
		response = request.send();
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
		id = api.context().currentReply().getField("id");
		
		service = new ServiceBuilder()
        .provider(LinkedInApi.withScopes("rw_company_admin"))
        .apiKey("754z46slfke0xm")
        .apiSecret("Jm46YNdPTb2toxoY")
        .build();
		
		Token t = new Token (api.data().getField(email, "Token"), api.data().getField(email, "Secret"));
		
		request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/companies/" + id + "/is-company-share-enabled?format=json");
		service.signRequest(t, request);
		response = request.send();
		sharing = Boolean.parseBoolean(response.getBody());
		System.out.println(sharing);
		
		request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/companies/" + id + "/relation-to-viewer/is-company-share-enabled?format=json");
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
		
		request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/companies/"+ id +":(id,name,ticker,description)?format=json");
		service.signRequest(t, request);
		response = request.send();
		JSONObject profile = new JSONObject (response.getBody());
		
		request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/companies/" + id + "/num-followers?format=json");
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
		id = api.context().currentReply().getField("id");
		
		service = new ServiceBuilder()
        .provider(LinkedInApi.withScopes("rw_company_admin"))
        .apiKey("754z46slfke0xm")
        .apiSecret("Jm46YNdPTb2toxoY")
        .build();
		
		Token t = new Token (api.data().getField(email, "Token"), api.data().getField(email, "Secret"));
		
		request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/companies/"+ id +"/updates?format=json");
		request.addQuerystringParameter("count", "1");
		service.signRequest(t, request);
		response = request.send();
		JSONObject recent = new JSONObject(response.getBody());
		
		boolean comment = recent.getJSONArray("values").getJSONObject(0).getBoolean("isCommentable");
		boolean like = recent.getJSONArray("values").getJSONObject(0).getBoolean("isLikable");
		String update = recent.getJSONArray("values").getJSONObject(0).getJSONObject("updateContent").getJSONObject("companyStatusUpdate")
				.getJSONObject("share").getString ("comment");
		
		updateKey = recent.getJSONArray("values").getJSONObject(0).getString("updateKey");
		
		if (comment) {
			request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/companies/" + id + "/updates/key=" + updateKey + "/update-comments?format=json");
			service.signRequest(t, request);
			response = request.send();
			JSONObject j = new JSONObject (response.getBody());
			totalComments = j.getInt("_total");
		}
		if (like) {
			request = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/companies/" + id + "/updates/key=" + updateKey + "/likes?format=json");
			service.signRequest(t, request);
			response = request.send();
			JSONObject j = new JSONObject (response.getBody());
			totalLikes = j.getInt("_total");
		}
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("Recent Update -<br/><br/><ul><li>" + update + "</li><li>Total Comments - " + totalComments + "</li>" +
								"<li>Total Likes - " + totalLikes + "</li></ul>")));
	}
	
	public static void main(String[] args) {
		TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data")
				.setEmail("playitabhi@gmail.com")
				.setPassword("playit");
		api.startReceivingEvents(new LinkIt());
	}
}

/*
String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" +
            	"<share> \n" +
            		"<comment>" + com + "</comment> \n" +
            		"<content> \n" +
            			"<title>" + title + "</title> \n" +
            			"<description>" + desc + "</description> \n" +476f3684-3158-48fe-8082-b3adebfc83ea
            			"<submitted-url>" + url + "</submitted-url> \n" +
            			"<submitted-image-url>" + imgUrl + "</submitted-image-url> \n" +
            		"</content> \n" +
            		"<visibility> \n" +
            			"<code>anyone</code> \n" +
            		"</visibility> \n" +
            	 "</share> \n";
*/