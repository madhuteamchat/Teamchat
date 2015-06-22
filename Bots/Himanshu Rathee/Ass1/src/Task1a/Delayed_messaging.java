package Task1a;

import com.teamchat.client.sdk.TeamchatAPI;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.*;
import com.teamchat.client.sdk.chatlets.BypassChatletCopy;
import com.teamchat.client.sdk.chatlets.LocationChatlet;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

public class Delayed_messaging {

		@OnKeyword("delaymsg")
		public void DelayMsg(TeamchatAPI api) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
					.setQuestion("Send a Delayed message")
					.setReplyScreen(
							api.objects()
							.form()
							.addField(
									api.objects()
									.input()
									.name("time")
									.label("Enter Scheduled Time (IN HH:mm)"))
									.addField(
											api.objects()
											.input()
											.name("message")
											.label("Enter your message"))
											.addField(
													api.objects()
													.input()
													.name("email")
													.label("Enter the recipient's email id")))
													.setReplyLabel("Send Message").alias("settimer")));
		}
		@OnAlias("settimer")
		public void SetTimer(TeamchatAPI api){
			String time[] = api.context().currentReply().getField("time").split(":"),
					message = api.context().currentReply().getField("message"), 
					email = api.context().currentReply().getField("email");
			new Timemanagement(api, message,"Delayed Message",email, Integer.parseInt(time[0]),
					Integer.parseInt(time[1]), 0, 0);
		}
		public static void main(String[] args) {
			TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data")
					.setEmail("himanshu.rathee@st.niituniversity.in")  
					.setPassword("p@$$word4") 
					.startReceivingEvents(new Delayed_messaging());
				}

}
