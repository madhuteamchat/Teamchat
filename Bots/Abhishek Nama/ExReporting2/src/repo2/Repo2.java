package repo2;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class Repo2 {
	@OnKeyword("productentry")
	public void productentry(TeamchatAPI api) {
		Form f = api.objects().form();
		f.addField(api.objects().select().addOption("Mumbai").addOption("Rest of Maharashtra")
				.addOption("Gujarat").addOption("Goa").label("Choose Area").name("area"));
		f.addField(api.objects().input().label("Product Code").name("code"));
		f.addField(api.objects().input().label("Sales").name("sales"));
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion("Enter your area, product code and sales details")
						.setReplyScreen(f).setReplyLabel￼("Reply")
						.setDetailsLabel("Replies").showDetails(true)
						.alias("prod")));
	}
	
	@OnAlias("prod")
	public void prod(TeamchatAPI api) {
		String email = api.context().currentReply().senderEmail();
		String area = api.context().currentReply().getField("area");
		int code = Integer.parseInt(api.context().currentReply().getField("code"));
		double sales = Double.parseDouble(api.context().currentReply().getField("sales"));
		if ((sales<1000 && area.equals("Goa") || sales<2000 && !area.equals("Goa"))) {
			api.perform(api.context().create().add(email).add("abhisheknama@outlook.com")
				.setName("Report").post(new TextChatlet ("Below threshold sales for " + code + " of " + sales)));
		}
	}
	
	public static void main(String[] args) {
		TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data")
				.setEmail("email") // change to your teamchat registered email id
				.setPassword("pass"); // change to your teamchat password
		api.startReceivingEvents(new Repo2());
	}
}
