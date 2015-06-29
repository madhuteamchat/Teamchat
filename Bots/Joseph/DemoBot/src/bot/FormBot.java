package bot;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class FormBot {
	
	
	@OnKeyword("help")
	public void onhelpEntry(TeamchatAPI api) {
		helpchatlet(api);
	}
	
	@OnKeyword("Help")
	public void onHelpEntry(TeamchatAPI api) {
		helpchatlet(api);
	}
	
	@OnKeyword("HElp")
	public void onHElpEntry(TeamchatAPI api) {
		helpchatlet(api);
	}
	
	
	public void helpchatlet(TeamchatAPI api) {
		System.out.println("data entry typed");
		api.perform(
				api.context().currentRoom().registerForEvents().post(
				new TextChatlet("Type 'check' to report routine checks"
						        )));
			
	}
	
	@OnKeyword("check")
	public void onreportEntry(TeamchatAPI api) {
		reportchatlet(api);
	}
	
	@OnKeyword("Check")
	public void onReportEntry(TeamchatAPI api) {
		reportchatlet(api);
	}
	
	@OnKeyword("CHeck")
	public void onREportEntry(TeamchatAPI api) {
		reportchatlet(api);
	}
	

	public void reportchatlet(TeamchatAPI api) {
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
				.showDetails(true)
				.alias("Assign task"))
				);
	}
	
	@OnAlias("Assign task")
	public void onTaskAssigned(TeamchatAPI api) {
		String  useremail=api.context().currentReply().senderEmail();
		String user = api.context().currentReply().getField("user");
		String location = api.context().currentReply().getField("location");
		String check = api.context().currentReply().getField("check");
		String site = api.context().currentReply().getField("site");
		String observation = api.context().currentReply().getField("observation");
		String recommendation = api.context().currentReply().getField("recommendation");
		api.perform(
				api.context().currentRoom().registerForEvents().post(
				new PrimaryChatlet()
				.setQuestionHtml("Assigned by- "+useremail+" Task-"+observation+" Assigned to-"+user+" Type of check-"+check+" Site-"+site+" location-"+location
						         +"")
				.setReplyScreen(api.objects().form()
				.addField(api.objects().select().label("Select option").name("status")
			    .addOption("Open")
			    .addOption("Close")
			    )
			    .addField(api.objects().input().label("Action taken").name("action"))
			    .addField(api.objects().hidden().label("").name("site").value(site))
			    .addField(api.objects().hidden().label("").name("location").value(location))
			    .addField(api.objects().hidden().label("").name("user").value(user))
			    .addField(api.objects().hidden().label("").name("recommendation").value(recommendation))
			    .addField(api.objects().hidden().label("").name("observation").value(observation))
			    .addField(api.objects().hidden().label("").name("sender").value(useremail))
				)
				.showDetails(true)
				.alias("Submit"))
				);
	}
	
	@OnAlias("Submit")
	public void onSubmit(TeamchatAPI api) {
		
		String status = api.context().currentReply().getField("status");
		String sender = api.context().currentReply().getField("sender");
		String action = api.context().currentReply().getField("action");
		String location = api.context().currentReply().getField("location");
		String user = api.context().currentReply().getField("user");
		String observation = api.context().currentReply().getField("observation");
		String recommendation = api.context().currentReply().getField("recommendation");
		api.perform(
				api.context().currentRoom().registerForEvents().post(
				new TextChatlet("Task assigned by- "+sender+" Task assigned to-"+user+" Observation-"+observation+" Recomendation-"+recommendation+" Location-"+location
						       +" Status-"+status+" Action Taken-"+action)
		
				));
	}
	

}
