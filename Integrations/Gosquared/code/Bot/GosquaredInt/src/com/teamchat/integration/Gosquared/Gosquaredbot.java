package com.teamchat.integration.Gosquared;

import java.util.*;
import java.io.IOException;
import java.text.*;

import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.ReportChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;
import com.teamchat.client.sdk.chatlets.PollChatlet;

public class Gosquaredbot {
	String apikey1, stoken, smail;
	public static TeamchatAPI apig;

	@OnKeyword("help")
	public void help(TeamchatAPI api) {
		apig = api;
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
				.setQuestionHtml("<h3><center>Hey!!, This is Gosquared Bot<center> </h3>"
								+ "<h4>You can track your website,<br> view trends and ongoing trends of your registered website. "
								+ " Use me to perform following functions:</h4>"
								+ "<ul><li><b>configure</b> to configure your Gosquared account  </li> "
								+ "<li><b>now</b> to view present happenings  </li>  "
								+ "<li><b>trend</b> to view present day's trend  </li>"
								+ "<li><b>change</b> to change site token  </li>"
								+ "<li><b>notify</b> to receive notifications from Gosquared  </li>"
								+ "<li><b>scheduleme</b> to get notifications about the trends at specified time  </li>"

				)

				));
	}

	@OnKeyword("configure")
	public void onCreate(TeamchatAPI api) {
		apig = api;
		String email = api.context().currentSender().getEmail();
ManageDB db =new ManageDB();
String att=db.retrieve(email);
if(att==null)
{
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml(
								"Login to your Gosquared account. Go to<a href=\"https://www.gosquared.com/docs/\"> Click Here</a>  Scroll down to Libraries+PLugins and copy api key and site token and paste below. ")
							
						.setReplyScreen(
								api.objects()
										.form()

										.addField(
												api.objects().input()
														.label("Api Key")
														.name("apikey"))
										.addField(
												api.objects().input()
														.label("Site Token")
														.name("sitetoken"))

						).alias("answer1")));
}
else
{
	api.perform(api
			.context()
			.currentRoom()
			.post(new PrimaryChatlet()
			.setQuestionHtml("<font color=\"blue\">You are already configured</font>")));
}
	
	}

	// }
	@OnAlias("answer1")
	public void onData(TeamchatAPI api) throws IOException {
		String apikey = api.context().currentReply().getField("apikey");
		String sitetoken = api.context().currentReply().getField("sitetoken");
		String smail = api.context().currentReply().senderEmail();
		try {
			Currentusers cu = new Currentusers();
			cu.now11(api, apikey, sitetoken);
			ManageDB db = new ManageDB();

			apikey1 = db.retrieve(smail);

			if (apikey1 == null) {
				System.out.println(apikey1);
				db.insert(smail, apikey, sitetoken);
				api.perform(api.context().currentRoom()
						.post(new TextChatlet(" you are configured")));
			} else {
				api.perform(api.context().currentRoom()
						.post(new TextChatlet(" you are configured")));
			}

			// ManageDB db =new ManageDB();

			// apikey1 = db.retrieve(smail);
			// stoken = db.retrieve1(smail);
			// test ts=new test();
			// String schedule=ts.onCreate(api, apikey1, stoken);
			// String dtime = "22:48";
			// String smail1 = api.context().currentReply().senderEmail();
			// String[] dt = dtime.split(":");
			// Timer t =new Timer();
			// DateFormat dateformat = new
			// SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			// Calendar cal = Calendar.getInstance();
			// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dt[0]));
			// cal.set(Calendar.MINUTE, Integer.parseInt(dt[1]));
			// System.out.println(dateformat.format(cal.getTime()));
			// t.schedule(new schedule(api,schedule,smail), cal.getTime());
		} catch (Exception e) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet(
							" Entered values are incorrect please enter the correct details")));
		}

		// ManageDB db =new ManageDB();
		//
		// apikey1 = db.retrieve(smail);
		//
		//
		// if (apikey1 == null)
		// {
		// System.out.println(apikey1);
		// db.insert(smail,apikey,sitetoken);
		// api.perform(api.context().currentRoom().post(new
		// TextChatlet(" you are configured")));
		// }
		// else
		// {
		// api.perform(api.context().currentRoom().post(new
		// TextChatlet(" you are configured")));
		// }

		// ManageDB db =new ManageDB();
		//
		// apikey1 = db.retrieve(smail);
		// stoken = db.retrieve1(smail);
		// test ts=new test();
		// String schedule=ts.onCreate(api, apikey1, stoken);
		// String dtime = "22:08";
		// String smail1 = api.context().currentReply().senderEmail();
		// String[] dt = dtime.split(":");
		// Timer t =new Timer();
		// DateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// Calendar cal = Calendar.getInstance();
		// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dt[0]));
		// cal.set(Calendar.MINUTE, Integer.parseInt(dt[1]));
		// System.out.println(dateformat.format(cal.getTime()));
		// t.schedule(new schedule(api,schedule,smail), cal.getTime());
		//

	}

	@OnKeyword("scheduleme")
	public void onCreate1(TeamchatAPI api) {
		apig = api;

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion(
								"Please enter the time you need to receive the notification")
						.setReplyScreen(
								api.objects()
										.form()
										.addField(
												api.objects()
														.input()
														.label("SCHEDULE TIME IN HH:MM")
														.name("schedule"))

						).alias("scheduling")));
	}

	@OnAlias("scheduling")
	public void onsched(TeamchatAPI api) throws IOException {
		String smail1 = api.context().currentReply().senderEmail();
		ManageDB db = new ManageDB();
		apikey1 = db.retrieve(smail1);
		stoken = db.retrieve1(smail1);
//		System.out.println(apikey1);
//		System.out.println(stoken);
//		System.out.println(smail1);

		// test ts=new test();
		// String schedule=ts.onCreate(api, apikey1, stoken);

		long time;
		String dtime = api.context().currentReply().getField("schedule");
		;
		// String smail1 = api.context().currentReply().senderEmail();
		String[] dt = dtime.split(":");
		Timer t = new Timer();
		//iDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		// SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	      //  System.out.println((cal.getTimeInMillis()) );
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dt[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(dt[1]));
		//System.out.println(dateformat.format(cal.getTime()));
		time = cal.getTimeInMillis() - (5 * 60 * 60 * 1000 + 30 * 60 * 1000);
		System.out.println("Scheduled time in millis "+time);
		Calendar cal1 = Calendar.getInstance();
		System.out.println("Curr time in millis "+System.currentTimeMillis());
	    //System.out.println(("Scheduled time in millis "+cal1.getTimeInMillis()) );
		time=time-System.currentTimeMillis();
		t.schedule(new schedule(api, smail1), time);
	}

	@OnKeyword("change")
	public void onchange(TeamchatAPI api) {
		apig = api;

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion(
								"Please choose from the following for changing the site token")
						.setReplyScreen(
								api.objects()
										.form()
										.addField(
												api.objects()
														.select()
														.label("site token")
														.name("token")
														.addOption(
																"Last entered site token")
														.addOption(
																"New site token"))

						).alias("change")));
	}

	@OnAlias("change")
	public void onchangeData(TeamchatAPI api) throws IOException {
		String smail1 = api.context().currentReply().senderEmail();
		String token = api.context().currentReply().getField("token");

		if (token.equals("New site token")) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestion(
									"Please enter the  changed the site token")
							.setReplyScreen(
									api.objects()
											.form()
											.addField(
													api.objects()
															.input()
															.label("site token")
															.name("token")))
							.alias("change1")));
		} else {

			ManageDB db = new ManageDB();
			apikey1 = db.retrieve(smail1);
			stoken = db.retrieve1(smail1);
			System.out.println(apikey1);
			System.out.println(stoken);

		}
		// test ts=new test();
		// String schedule=ts.onCreate(api, apikey1, stoken);
		// String dtime = "17:00";
		// String smail1 = api.context().currentReply().senderEmail();
		// String[] dt = dtime.split(":");
		// Timer t =new Timer();
		// DateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// Calendar cal = Calendar.getInstance();
		// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dt[0]));
		// cal.set(Calendar.MINUTE, Integer.parseInt(dt[1]));
		// System.out.println(dateformat.format(cal.getTime()));
		// t.schedule(new schedule(api,schedule,smail), cal.getTime());

	}

	@OnAlias("change1")
	public void onchangeData1(TeamchatAPI api) throws IOException {
		String smail1 = api.context().currentReply().senderEmail();
		String stoken = api.context().currentReply().getField("token");

		ManageDB db = new ManageDB();

		apikey1 = db.retrieve(smail1);
		// stoken = db.retrieve1(smail);
		test ts = new test();
		String schedule = null;

		try {
			schedule = ts.onCreate(api, apikey1, stoken);
			db.update(smail1, stoken);
			// String dtime = "17:00";
			// String smail1 = api.context().currentReply().senderEmail();
			// String[] dt = dtime.split(":");
			// Timer t =new Timer();
			// DateFormat dateformat = new
			// SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			// Calendar cal = Calendar.getInstance();
			// cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dt[0]));
			// cal.set(Calendar.MINUTE, Integer.parseInt(dt[1]));
			// System.out.println(dateformat.format(cal.getTime()));
			// t.schedule(new schedule(api,schedule,smail), cal.getTime());
		} catch (Exception e) {
			api.perform(api.context().currentRoom()
					.post(new TextChatlet(" wrong site token")));
			e.printStackTrace();

		}
	}

	@OnKeyword("now")
	public void HelloWorld(TeamchatAPI api) throws IOException {
		apig = api;
		String smail = api.context().currentSender().getEmail();
		ManageDB db = new ManageDB();

		apikey1 = db.retrieve(smail);
		if (apikey1 == null) {
			api.perform(api.context().currentRoom()
					.post(new TextChatlet(" Please  configure your account")));
		}
		stoken = db.retrieve1(smail);
		Now nw = new Now();
		nw.now1(api, apikey1, stoken);
		Test1 tp = new Test1();
		String need = tp.onCreate11(api, apikey1, stoken);
		Currentusers cu = new Currentusers();
		int value = cu.now11(api, apikey1, stoken);
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
				.setQuestionHtml("Hi,here are  some additional details"
						+ need
						+ "<br> No.of current visitors : <font color=\"red\">"
						+ value + "</font>")));

		// api.perform(
		// api.context().currentRoom().post(
		// new
		// TextChatlet("<img style=\"-webkit-user-select: none\" src=\"http://chart.apis.google.com/chart?cht=p3&amp;chs=450x200&amp;chd=t:7,3&amp;chco=80C65A,224499,FF0000&amp;chl=chrome|mozilla|IE|opera|safari\">")
		// ));
	}

	@OnKeyword("trend")
	public void check(TeamchatAPI api) throws IOException {
		apig = api;
		String smail = api.context().currentSender().getEmail();
		ManageDB db = new ManageDB();

		apikey1 = db.retrieve(smail);
		if (apikey1 == null) {
			api.perform(api.context().currentRoom()
					.post(new TextChatlet(" Please  configure your account")));
		}
		stoken = db.retrieve1(smail);
		test ts = new test();
		String schedule = ts.onCreate(api, apikey1, stoken);
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
					.setQuestionHtml("These are the ongoing trends on your website :<br>"
								+ schedule)));

	}

	@OnKeyword("notify")
	public void notify(TeamchatAPI api) {
		String email = api.context().currentSender().getEmail();
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
				
				.setQuestionHtml("please register this URL as your webhook in Gosquare<br>"
								+ "<br/> http://interns.teamchat.com:8083/GosquaredInt/Gosquare?email="
								+ email)));

	}

}
