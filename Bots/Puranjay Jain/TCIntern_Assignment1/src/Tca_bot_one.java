/**
 * 
 */

/**
 * @author intern19
 *
 */

import com.teamchat.client.annotations.*;
import com.teamchat.client.sdk.*;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.*;

public class Tca_bot_one {

	final String admin_email = "puranjay.jain@st.niituniversity.in";
	final String hr_email = "aniruddh.varshney@st.niituniversity.in";
	final String manager_email = "himanshu.rathee@st.niituniversity.in";
	static String area,sales,productcode;
	// exception reporting 1 bot
	@OnKeyword("dataentry")
	public void Exceptionreporting1(TeamchatAPI api) {
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
				.setQuestion("Exception reporting 1")
				.setReplyScreen(
						api.objects()
						.form()
						.addField(
								api.objects()
								.select()
								.name("area")								
								.label("Choose your Area")
								.addOption("Mumbai")
								.addOption("Rest of Maharashtra")
								.addOption("Gujarat")
								.addOption("Goa"))
								.addField(
										api.objects()
										.input()
										.name("sales")
										.label("Enter the Sales Produced:")))
										.setReplyLabel("Send Message")
										.alias("checkfigures")));		
	}

	// check the sales figures
	@OnAlias("checkfigures")
	public void checkfigures(TeamchatAPI api) {
		// get area and sales
		area = api.context().currentReply().getField("area");
		sales = api.context().currentReply().getField("sales");
		// if goa and less than 1000
		if (area.equals("Goa")) {
			if (Integer.parseInt(sales) < 1000) {
				// reason form
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet()
						.setQuestion("Give Reason for lower sales")
						.setReplyScreen(
								api.objects()
								.form()
								.addField(
										api.objects()
										.input()
										.name("reason")
										.label("Reason")))
										.setReplyLabel("Enter")
										.alias("sendreason")));
			}
		}
		// if other than goa
		else {
			if (Integer.parseInt(sales) < 2000) {
				// reason form
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet()
						.setQuestion("Give Reason for lower sales")
						.setReplyScreen(
								api.objects()
								.form()
								.addField(
										api.objects()
										.input()
										.name("reason")
										.label("Reason")))
										.setReplyLabel("Enter")
										.alias("sendreason")));
			}
		}
	}

	// send reason
	@OnAlias("sendreason")
	public void sendreason(TeamchatAPI api) {
		// get reason and send it
		String reason = api.context().currentReply().getField("reason"),
				email = api.context().currentReply().senderEmail(),
				f_reason = "Below threshold sales for " + area +" of Rs." + sales + " reported by " + email + " due to " + reason;
		new Messenger(api, f_reason,"Reason for lower sales", admin_email);
	}

	// exception reporting 2 bot
	@OnKeyword("dataentry1")
	public void Exceptionreporting2(TeamchatAPI api) {
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
				.setQuestion("Exception reporting 1")
				.setReplyScreen(
						api.objects()
						.form()
						.addField(
								api.objects()
								.select()
								.name("area")								
								.label("Choose your Area")
								.addOption("Mumbai")
								.addOption("Rest of Maharashtra")
								.addOption("Gujarat")
								.addOption("Goa"))
								.addField(
										api.objects()
										.input()
										.name("sales")
										.label("Enter the Sales Produced:"))
										.addField(
												api.objects()
												.input()
												.name("productcode")
												.label("Enter the Product Code:")))
												.setReplyLabel("Send Message")
												.alias("checkfigures1")));		
	}

	// check the sales figures
	@OnAlias("checkfigures1")
	public void checkfigures1(TeamchatAPI api) {
		// get area and sales
		area = api.context().currentReply().getField("area");
		sales = api.context().currentReply().getField("sales");
		productcode = api.context().currentReply().getField("productcode");
		// if goa and less than 1000
		if (area.equals("Goa")) {
			if (Integer.parseInt(sales) < 1000) {
				// reason form
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet()
						.setQuestion("Give Reason for lower sales")
						.setReplyScreen(
								api.objects()
								.form()
								.addField(
										api.objects()
										.input()
										.name("reason")
										.label("Reason")))
										.setReplyLabel("Enter")
										.alias("sendreason1")));
			}
		}
		// if other than goa
		else {
			if (Integer.parseInt(sales) < 2000) {
				// reason form
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet()
						.setQuestion("Give Reason for lower sales")
						.setReplyScreen(
								api.objects()
								.form()
								.addField(
										api.objects()
										.input()
										.name("reason")
										.label("Reason")))
										.setReplyLabel("Enter")
										.alias("sendreason1")));
			}
		}
	}

	// send reason
	@OnAlias("sendreason1")
	public void sendreason1(TeamchatAPI api) {
		// get reason and send it
		String reason = api.context().currentReply().getField("reason"),
				email = api.context().currentReply().senderEmail(),
				f_reason = "Below threshold sales for "+productcode+" of Rs."+sales;
		new Messenger(api, f_reason,"Reason for lower sales", new String[]{admin_email,email});
	}

	// delayed message bot
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
								.name("hhmm")
								.label("Enter Scheduled Time (IN HH:mm)"))
								.addField(
										api.objects()
										.input()
										.name("message")
										.label("Enter your message"))
										.addField(
												api.objects()
												.input()
												.name("to")
												.label("Enter the recipient's email id")))
												.setReplyLabel("Send Message").alias("settimer")));		
	}

	@OnAlias("settimer")
	public void SetTimer(TeamchatAPI api){
		String hhmm[] = api.context().currentReply().getField("hhmm")
				.split(":"), message = api.context().currentReply()
				.getField("message"), to = api.context().currentReply()
				.getField("to");
		new Messenger(api, message,"Delayed Message", to, Integer.parseInt(hhmm[0]),
				Integer.parseInt(hhmm[1]), 0, 0);
	}

	@OnKeyword("leavereq")
	public void LeaveReport(TeamchatAPI api) {
		//create a room with 
		Room x = api.context().create().add(admin_email).add(manager_email);
		api.perform(x.post(new PollChatlet()
		.setQuestion("Leave Request Application").alias("lr1")));

	}

	@OnAlias("lr1")
	public void lr1(TeamchatAPI api) {
		String Response1="";
		String Check = "yes";
		Response1 = api.context().currentReply().getField("resp");

		if (Response1.equals(Check)){
			Room r2= api.context().create().add(admin_email)
					.add(hr_email);
			api.perform(r2.post(new PollChatlet().setQuestion(
					"Leave Request Application").alias("lr2")));
		}
		else{
			api.perform(api.context().currentRoom().post(new TextChatlet("Your Application for leave has been Rejected.")));
		}
	}

	@OnAlias("lr2")
	public void lr2(TeamchatAPI api) {
		String Response2="";
		String pollresult = "yes";
		Response2 = api.context().currentReply().getField("resp");
		if (Response2.equals(pollresult)){
			api.perform(api.context().currentRoom().post(new TextChatlet("Your Application for leave has been Accepted.")));
		}
		else{
			api.perform(api.context().currentRoom().post(new TextChatlet("Your Application for leave has been Rejected.")));
		}
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data")
				.setEmail("quity1974@jourrapide.com").setPassword("123456789")
				.startReceivingEvents(new Tca_bot_one());
	}

}