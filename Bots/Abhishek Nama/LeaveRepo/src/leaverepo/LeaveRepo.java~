package leaverepo;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PollChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class LeaveRepo {
	String name, email, id;
	
	@OnKeyword("leavereq")
	public void leavereq (TeamchatAPI api) {
		name = api.context().currentSender().getName();
		email = api.context().currentSender().getEmail();
		Room r= api.context().currentRoom();
		id = r.getId();
		
		api.perform(r.post(new TextChatlet("Mr. "+ name + ", your request has been sent, wait for approval")));
		
		api.perform(api.context().create().add("abhisheknama@outlook.com").setName("Report")
				.post(new PollChatlet().setQuestion("Mr. " + name + " Email: " + email + ", requested for leave?").alias("manager")));
	}
	
	@OnAlias("manager")
	public void manager(TeamchatAPI api) {
		String resp = api.context().currentReply().getField("resp");
		
		if (resp.equals("yes")) {
			api.perform(api.context().byId(id)
					.post(new TextChatlet("Your leave approved")));
		}
		else {
			api.perform(api.context().create().add("abhisheknama@outlook.com").setName("Report")
					.post(new PollChatlet().setQuestion("Mr. " + name + " Email: " + email + ", requested for leave?").alias("HR")));
		}
	}
	
	@OnAlias("HR")
	public void hr(TeamchatAPI api) {
		String resp = api.context().currentReply().getField("resp");
		
		if (resp.equals("yes")) {
			api.perform(api.context().byId(id)
					.post(new TextChatlet("Your leave approved")));
		}
		else {
			api.perform(api.context().byId(id)
					.post(new TextChatlet("Your leave not approved")));
		}
	}
	
	public static void main(String[] args) {
		TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data")
				.setEmail("playitabhi@gmail.com") // change to your teamchat registered email id
				.setPassword("playit"); // change to your teamchat password
		api.startReceivingEvents(new LeaveRepo());
	}
}
