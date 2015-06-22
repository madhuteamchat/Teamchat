package Task1a;

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

public class Exception_reporting_1 {
    public static String Admin="rathee.coolhimanshu@gmail.com";
	public static String area;
	public static String sales;
	@OnKeyword("dataentry")
	public void onCreateKeyword(TeamchatAPI api) {
		Form f = api.objects().form();
			f.addField(api.objects().select().name("area").label("Area").addOption("Mumbai").addOption("Rest of Maharashtra").addOption("Gujrat").addOption("Goa"));
			f.addField(api.objects().input().name("sales").label("Sales"));
		api.perform(
				api.context().currentRoom().post(
						new PrimaryChatlet().setQuestion("**DATA ENTRY FORM**")
						.setReplyScreen(f)
						.setReplyLabel("CLICK")
						.alias("check")));
	}
	@OnAlias("check")
	public void oncheck(TeamchatAPI api) {
		Form g = api.objects().form();
		g.addField(api.objects().input().name("reason").label("Reason"));
		 area = api.context().currentReply().getField("area");
		sales = api.context().currentReply().getField("sales");
		 int value = Integer.parseInt(sales);
		if(area.equals("Goa")){
			if (value<=1000) {
					api.perform(
						api.context().currentRoom().post(
								new PrimaryChatlet().setQuestion("You have low sales!!!!!!! \nState REASON for low sales")
								.setReplyScreen(g)
								.setReplyLabel("type")
								.alias("display")));
					}
		}
		if(area.equals("Gujrat")||area.equals("Rest of Maharashtra")||area.equals("Mumbai"))	
		if(value<=2000){
				api.perform(
						api.context().currentRoom().post(
								new PrimaryChatlet().setQuestion("You have low sales!!!!!!! \nState REASON for low sales")
								.setReplyScreen(g)
								.setReplyLabel("type")
								.alias("display")));
			}
		
		}	
	@OnAlias("display")
	public void ondisplay(TeamchatAPI api){
				String sender = api.context().currentReply().senderEmail();
		String reason = api.context().currentReply().getField("reason");
		Room x = api.context().create().setName("Data Entry").add(Admin);
		api.perform(x.post(new PrimaryChatlet().setQuestion("Below threshold sales for " +area+" of " +sales+ " reported by " +sender+ " due to " +reason)));
	}
	private boolean strcmp(boolean b) {
		// TODO Auto-generated method stub
		return false;
	}
	public static void main(String[] args) {
		TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data")
				.setEmail("himanshu.rathee@st.niituniversity.in")  
				.setPassword("p@$$word4") 
				.startReceivingEvents(new Exception_reporting_1());
			}
	}
