package com.Maintenance;

import java.util.*;
import java.util.Map.Entry;

import com.tc.sol.server.util.sms.SMSUtil; //For sending SMS
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PollChatlet;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.RichmediaChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class mainmaintenance {
	public static final String bot = "himanshu.rathee@st.niituniversity.in";// bot
																			// email
																			// name
	public static final String password = "p@$$word4";// botpassword
	public static final String admin = "rathee.coolhimanshu@gmail.com";// admin
																		// email
	public static final String electrician = "9671847176",
			plumber = "8828090019", carpenter = "9680000880",
			cleaner = "9671847176";// Numbers

	// secret code generator
	public int secretcode() { // of
		Random code = new Random();
		int value = 0;
		value = code.nextInt(9999);
		return value;
	}

	// pending or completed
	public String completed(Boolean status) {
		if (status) {
			return "Completed";
		}
		return "Pending";
	}

	public int secret = secretcode();

	@OnKeyword("maintenance")
	// displays form for filling complaint
	public void onroomservice(TeamchatAPI api) throws Exception {
		String sender = api.context().currentSender().getEmail();
		String image = "<html><body><img src=\"";
		String url = "http://calliopeairconditioning.com.au/wp-content/uploads/2014/02/Icon-CommercialMaintenance-CalliopeAirCon.png\"";
		String size = "style=\"";
		String width = "width:204px;height:128px;>\"";
		String end = "</body></html>";
		Form f = api.objects().form();
		f.addField(api.objects().select().name("nameofworker")
				.label("Person to contact").addOption("Electrician")
				.addOption("Plumber").addOption("Carpenter")
				.addOption("Cleaner"));
		f.addField(api.objects().input().name("name").label("Your Name"));
		f.addField(api.objects().input().name("roomno").label("Your Room no."));
		f.addField(api.objects().input().name("problem")
				.label("Description of Problem (not more than 100 words)"));
		Room x = api.context().create().setName("Maintenance").add(admin)
				.add(sender);
		api.perform(x
				.post(new PrimaryChatlet()
						.setQuestionHtml(
								"<font color='Red'><h3><b>WELCOME TO MAINTAINTIA</b></h3></font>"
										+ image
										+ url
										+ size
										+ width
										+ "s the pic"
										+ end
										+ "<br/><font color='Blue'<b><strong>**Need Maintenance.You are at the right place.Fill the form given.**</strong></b></font>")
						.setReplyScreen(f).setReplyLabel("CLICK").alias("post")));
	}

	@OnKeyword("status")
	// displays form for filling complaint
	public void getroomdata(TeamchatAPI api) throws Exception {
		Room x = api.context().create().setName("Status of Complaints")
				.add(admin);
		ArrayList<Customer> customers = new Db_handler().Get();
		String htmlResponse = "<table><tr><th>#</th><th>Name</th><th>Problem</th><th>Status</th></tr>";
		for (Customer cust : customers) {
			htmlResponse += "<tr>";
			htmlResponse += "<td>" + cust.getId() + "</td>";
			htmlResponse += "<td>" + cust.getName() + "</td>";
			htmlResponse += "<td>" + cust.getProblem() + "</td>";
			htmlResponse += "<td>" + completed(cust.getStatus()) + "</td>";
			htmlResponse += "</tr>";
		}
		htmlResponse += "</table>";
		api.perform(x.post(new PrimaryChatlet().setQuestionHtml(htmlResponse)));
	}

	@OnAlias("post")
	// Displays number of the worker whom you want to contact
	public void onpost(TeamchatAPI api) throws Exception {
		SendingSMS send = new SendingSMS();
		System.out.println("\n THE SECRET CODE IS " + secret);
		String nameofworker = api.context().currentReply()
				.getField("nameofworker");
		String email = api.context().currentReply().senderEmail();
		String name = api.context().currentReply().getField("name");
		String roomno = api.context().currentReply().getField("roomno");
		String problem = api.context().currentReply().getField("problem");
		String api_c = api.context().currentRoom().getId();
		new Db_handler().Store(email, name, nameofworker,
				Integer.parseInt(roomno), problem, false, api_c);
		if (nameofworker.equalsIgnoreCase("Electrician")) {
			send.sendmessage(electrician, name, roomno, problem, secret);
			String image = "<html><body><img src=\"";
			String url = "http://previews.123rf.com/images/limbi007/limbi0071210/limbi007121000045/15800957-Orange-cartoon-character-as-electrician-with-toolbox-and-house-White-background--Stock-Photo.jpg\"";
			String size = "style=\"";
			String width = "width:204px;height:128px;>\"";
			String end = "</body></html>";
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<font color='Red'><strong><i>Your request is filed.Please wait for sometime or else contact our electricain on  "
									+ electrician
									+ ".\n You will get a secret code from the worker.Please use that to confirm whether you are satisfied with the work or not.</i></strong></font>"
									+ image
									+ url
									+ size
									+ width
									+ "s the pic"
									+ end)));
		} else if (nameofworker.equalsIgnoreCase("Plumber")) {
			send.sendmessage(plumber, name, roomno, problem, secret);
			String image = "<html><body><img src=\"";
			String url = "http://images.fineartamerica.com/images-medium-large-5/plumber-repair-faucet-tap-cartoon-aloysius-patrimonio.jpg\"";
			String size = "style=\"";
			String width = "width:204px;height:128px;>\"";
			String end = "</body></html>";
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<font color='Red'><strong><i>Your request is filed.Please wait for sometime or else contact our plumber on  "
									+ plumber
									+ ".\n You will get a secret code from the worker.Please use that to confirm whether you are satisfied with the work or not.</i></strong></font>"
									+ image
									+ url
									+ size
									+ width
									+ " s the pic"
									+ end)));
		} else if (nameofworker.equalsIgnoreCase("Carpenter")) {
			send.sendmessage(carpenter, name, roomno, problem, secret);
			String image = "<html><body><img src=\"";
			String url = "http://www.puzzlepuzzles.com/imatjes/bob-worked-as-a-carpenter_4b67023f8c857-p.jpg\"";
			String size = "style=\"";
			String width = "width:204px;height:128px;>\"";
			String end = "</body></html>";
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<font color='Red'><strong><i>Your request is filed.Please wait for sometime or else contact our carpenter on  "
									+ carpenter
									+ ".\n You will get a secret code from the worker.Please use that to confirm whether you are satisfied with the work or not.</i></strong></font>"
									+ image
									+ url
									+ size
									+ width
									+ " s the pic"
									+ end)));
		} else if (nameofworker.equalsIgnoreCase("Cleaner")) {
			send.sendmessage(cleaner, name, roomno, problem, secret);
			String image = "<html><body><img src=\"";
			String url = "http://thumbs.dreamstime.com/z/cartoon-cleaner-16133033.jpg\"";
			String size = "style=\"";
			String width = "width:204px;height:128px;>\"";
			String end = "</body></html>";
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<font color='Blue'><strong><i>Your request is filed.Please wait for sometime or else contact our cleaner on  "
									+ cleaner
									+ ".\n You will get a secret code from the worker.Please use that to confirm whether you are satisfied with the work or not.</i></strong></font>"
									+ image
									+ url
									+ size
									+ width
									+ " s the pic"
									+ end)));
		}
		Form g = api.objects().form();
		g.addField(api.objects().input().name("code")
				.label("Enter the secret code"));
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml(
								"<font color='Blue'<em><b>**Enter the secret code  OR 12345 for filing complaint**</b></em></font>")
						.setReplyScreen(g).setReplyLabel("CLICK").alias("code")));
	}

	@OnAlias("code")
	// Asks for acknowledgment if work is done or not
	public void oncode(TeamchatAPI api) throws Exception {
		System.out.println("\n Code is here also" + secret);
		String code = api.context().currentReply().getField("code");
		int value = Integer.parseInt(code);
		if (value == 12345) {
			Form g = api.objects().form();
			g.addField(api.objects().input().name("namec").label("Your Name"));
			g.addField(api.objects().input().name("msg")
					.label("File your complaint"));
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml(
									"<font color=Blue><i>**File your complaint**</i><font>")
							.setReplyScreen(g).setReplyLabel("CLICK")));
		} else if (secret == value) {
			api.data().getField("", "");
			api.perform(api
					.context()
					.currentRoom()
					.post(new PollChatlet()
							.setQuestionHtml("<font color=Blue><i>The code matches.PLEASE REVIEW THE WORK DONE.</i><font>")));
			// update work status
			new Db_handler().UpdateStatus(api.context().currentRoom().getId(),
					true);
		} else {
			Form g = api.objects().form();
			g.addField(api.objects().input().name("roomid")
					.label("Enter the secret code"));
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml(
									"<font color=Blue><b>Incorret code.Enter the code again</b><font>")
							.setReplyScreen(g).setReplyLabel("CLICK")
							.alias("code")));
		}
	}

	// @OnKeyword("complaintdetails")
	// public void ondata(TeamchatAPI api){
	// String namec=api.context().currentReply().getField("namec");
	// String msg=api.context().currentReply().getField("msg");
	//
	// api.data().addField("Complaint data", "Name", namec);
	// api.data().addField("Complaint data", "Complaint", msg);
	//
	// String person = api.data().getField("Complaint data", "Name");
	// String result = api.data().getField("Complaint data", "Complaint");
	//
	// api.perform(api.context().currentRoom().post(new
	// PrimaryChatlet().setQuestion("NAME:-" +person
	// + " COMPLAINT " + " " + result)));
	// }
	public static void main(String[] args)// main function
	{
		TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data").setEmail(bot)
				.setPassword(password)
				.startReceivingEvents(new mainmaintenance());
	}
}