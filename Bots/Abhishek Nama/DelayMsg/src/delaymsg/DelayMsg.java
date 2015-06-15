package delaymsg;

import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class DelayMsg {
	@OnKeyword("delaymsg")
	public void delaymsg(TeamchatAPI api) {
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Schedule Time - HH:MM").name("time"));
		f.addField(api.objects().input().label("Message").name("msg"));
		f.addField(api.objects().input().label("Recipient").name("recip"));
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion("Enter details")
						.setReplyScreen(f).setReplyLabel("Reply")
						.setDetailsLabel("Replies").showDetails(true)
						.alias("msg")));
	}
	
	@OnAlias("msg")
	public void msg(TeamchatAPI api) {
		String time = api.context().currentReply().getField("time");
		int eth = Integer.parseInt(time.substring (0, 2));
		int etm = Integer.parseInt(time.substring(3));
		
		String msg = api.context().currentReply().getField("msg");
		String recip = api.context().currentReply().getField("recip");
		
		Alert a  = new Alert(msg, recip, api);
		Timer timer = new Timer();
		
		LocalTime current = LocalTime.now();
		LocalTime entered = LocalTime.of(eth, etm, 0);
		
		if (current.isAfter(entered)) {
			api.perform(api.context().currentRoom()
					.post(new TextChatlet ("Time has gone")));
		} else {
			int cth = current.getHour();
			int ctm = current.getMinute(); 
			long diff = (eth*60*60*1000+etm*60*1000)-(cth*60*60*1000+ctm*60*1000);
			
			timer.schedule (a, diff);
		}
	}
	
	public static void main(String[] args) {
		TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data")
				.setEmail("playitabhi@gmail.com") // change to your teamchat registered email id
				.setPassword("playit"); // change to your teamchat password
		api.startReceivingEvents(new DelayMsg());
	}
}

class Alert extends TimerTask {
	String msg, recip;
	TeamchatAPI api;
	Alert (String msg, String recip, TeamchatAPI api) {
		this.msg=msg;
		this.recip=recip;
		this.api=api;
	}
	
	public void run () {
		api.perform(api.context().create().add(recip).setName("Alert")
				.post(new TextChatlet (msg)));
	}
}