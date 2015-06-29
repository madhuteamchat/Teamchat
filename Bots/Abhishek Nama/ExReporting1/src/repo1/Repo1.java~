package repo1;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;


public class Repo1 {
	String area;
	double sales;
	
	@OnKeyword("dataentry")
	public void dataentry(TeamchatAPI api) {
		Form f = api.objects().form();
		f.addField(api.objects().select().addOption("Mumbai").addOption("Rest of Maharashtra").addOption("Gujarat").addOption("Goa").label("Choose Area").name("area"));
		f.addField(api.objects().input().label("Sales").name("sales"));
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion("Enter your area and sales details")
						.setReplyScreen(f).setReplyLabel("Reply")
						.setDetailsLabel("Replies").showDetails(true)
						.alias("entry")));
	}
	
	@OnAlias("entry")
	public void entry(TeamchatAPI api) {
		area = api.context().currentReply().getField("area");
		sales = Double.parseDouble(api.context().currentReply().getField("sales"));
		if ((sales<1000 && area.equals("Goa") || sales<2000 && !area.equals("Goa"))) {
			Form f = api.objects().form();
			f.addField(api.objects().input().label("Reason").name("reason"));
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestion("Enter reson for low sales")
							.setReplyScreen(f).setReplyLabel("Reply")
							.setDetailsLabel("Replies").showDetails(true)
							.alias("send")));
		}
	}
	
	@OnAlias("send")
	public void send(TeamchatAPI api) {
		String reason = api.context().currentReply().getField("reason");
		String email = api.context().currentReply().senderEmail();
		
		api.perform(api.context().create().add("abhisheknama@outlook.com").setName("Report").post(new TextChatlet ("Below threshold sales for "
				+ area + " of " + sales + " reported by " + email + " due to reson " + reason)));
	}
	
	public static void main(String[] args) {
		TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data")
				.setEmail("playitabhi@gmail.com") // change to your teamchat registered email id
				.setPassword("playit"); // change to your teamchat password
		api.startReceivingEvents(new Repo1());
	}
}
