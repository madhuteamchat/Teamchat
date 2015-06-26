package bot;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class FormBot {
	
	
	@OnKeyword("help")
	public void onhelpEntry(TeamchatAPI api) {
		System.out.println("data entry typed");
		api.perform(
				api.context().currentRoom().registerForEvents().post(
				new TextChatlet("Type 'report' to report an issue"
						        )));
			
	}
	
	
	@OnKeyword("report")
	public void ondemoEntry(TeamchatAPI api) {
		api.perform(
				api.context().currentRoom().registerForEvents().post(
				new PrimaryChatlet()
				.setQuestion("Select a user and assign him a task")
				.setReplyScreen(api.objects().form()
				.addField(api.objects().select().label("Type of check").name("check")
							    .addOption("Mock drill")
							    .addOption("Surprise check")
							    .addOption("Night round")
							    )
			    .addField(api.objects().input().label("Site Name").name("site"))
				.addField(api.objects().select().label("Location").name("location")
							    .addOption("Corporate office")
							    .addOption("Manufacturing site")
							    .addOption("B-Hall")
							    .addOption("Laboratory")
							    )
			    .addField(api.objects().input().label("Observation").name("observation"))
			    .addField(api.objects().input().label("Recommendation").name("recommendation"))
				.addField(api.objects().select().label("Select user").name("user")
			    .addOption("Bhavini")
			    .addOption("Savio")
			    .addOption("Madhusudhan")
			    .addOption("Anubhav")
			    .addOption("Vikram")
			    )
				)
				.alias("Assign task"))
				);
	}
	
	@OnAlias("Assign task")
	public void onTaskAssigned(TeamchatAPI api) {
		String user = api.context().currentReply().getField("user");
		String location = api.context().currentReply().getField("location");
		String task = api.context().currentReply().getField("check");
		String observation = api.context().currentReply().getField("observation");
		String recommendation = api.context().currentReply().getField("recommendation");
		api.perform(
				api.context().currentRoom().registerForEvents().post(
				new PrimaryChatlet()
				.setQuestion("<b>"+user+"</b> assigned task <b>"+observation+"</b><br/><b> Recommendation: </b><br/>"+recommendation+" <b>Site:</b> "+task+" <br/><b>location:</b> "+location)
				.setReplyScreen(api.objects().form()
				.addField(api.objects().select().label("Select option").name("status")
			    .addOption("Open")
			    .addOption("Close")
			    )
			    .addField(api.objects().hidden().label("").name("location").value(task))
			    .addField(api.objects().hidden().label("").name("user").value(user))
			    .addField(api.objects().hidden().label("").name("recommendation").value(recommendation))
			    .addField(api.objects().hidden().label("").name("observation").value(observation))
				)
				.alias("Submit"))
				);
	}
	
	@OnAlias("Submit")
	public void onSubmit(TeamchatAPI api) {
		
		String status = api.context().currentReply().getField("status");
		String location = api.context().currentReply().getField("location");
		String user = api.context().currentReply().getField("user");
		String observation = api.context().currentReply().getField("observation");
		String recommendation = api.context().currentReply().getField("recommendation");
		api.perform(
				api.context().currentRoom().registerForEvents().post(
				new TextChatlet("<b>Person: </b>"+user+" <b><br />Observation: </b>"+observation+" <br /><b>Recomendation: </b> "+recommendation+" <br /><b>Location:</b> "+location
						       +"<br /> <b>Status:</b> "+status)
		
				));
	}
	

}
