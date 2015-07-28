package com.teamchat.integrations.GoogleCalendar;

import test.gcal.pack.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;//;auth.oauth2.Credential;
//import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.services.drive.Drive;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.mysql.fabric.xmlrpc.base.Array;

//import com.google.api.services.calendar.model.Event;

public class Test {
	/*
	 * public static String bot = "bhushanbot@gmail.com"; public static String
	 * pwd = "qwerty qwerty"; String apikey =
	 * "AIzaSyCuxRZHt5hjH-x-IQQG5MZFldxigM_4_W0";
	 */
	// static Calendar service;
	static String client_id = "558193257225-ak2bb44ne3gsqp80ln4cur9nl332c5jp.apps.googleusercontent.com";
	static String client_secret = "MF2SqJpKYQWBTp0J411wtrml";
	public static TeamchatAPI api1;
	/** Application name. */
	private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";

	/** Directory to store user credentials. */
	// private static final java.io.File DATA_STORE_DIR = new java.io.File(
	// System.getProperty("user.home"),
	// ".credentials6767676767/calendar-api-quickstart");

	/** Global instance of the {@link FileDataStoreFactory}. */
	// private static FileDataStoreFactory DATA_STORE_FACTORY;

	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/** Global instance of the scopes required by this quickstart. */
	/*
	 * private static final List<String> SCOPES = Arrays
	 * .asList(CalendarScopes.CALENDAR_READONLY);
	 */
	// String
	// client_id="558193257225-ak2bb44ne3gsqp80ln4cur9nl332c5jp.apps.googleusercontent.com";
	// String client_secret="MF2SqJpKYQWBTp0J411wtrml";

	/*
	 * public static void main(String args[]) { TeamchatAPI api =
	 * TeamchatAPI.fromFile("teamchat.data") .setEmail("bhushanbot@gmail.com")
	 * // change to your teamchat // registered email id
	 * .setPassword("qwerty qwerty") // change to your teamchat // password
	 * .startReceivingEvents(new Test()); // Wait for other user to // send
	 * message }
	 */

	/*
	 * public Test() { super(); try {
	 * System.out.println("static get calendar service is getting called= > ");
	 * service=getCalendarService(); } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * 
	 * 
	 * try { HTTP_TRANSPORT = new NetHttpTransport(); // DATA_STORE_FACTORY =
	 * new FileDataStoreFactory(DATA_STORE_DIR); } catch (Throwable t) {
	 * t.printStackTrace(); System.exit(1); }
	 * 
	 * 
	 * 
	 * }
	 */

	public com.google.api.services.calendar.Calendar getCalendar(String email)
			throws IOException {

		String accesstoken = null;
		User user = new User(email);
		accesstoken = user.getAccess_token();

		System.out.println("Access Token IS:" + accesstoken);
		GoogleCredential credentials = new GoogleCredential.Builder()
				.setClientSecrets(client_id, client_secret)
				.setJsonFactory(JSON_FACTORY).setTransport(HTTP_TRANSPORT)
				.build().setAccessToken(accesstoken);

		Calendar c = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				credentials).setApplicationName(APPLICATION_NAME).build();

		return c;
	}

	@OnKeyword("help")
	public void helpMethod(TeamchatAPI api) {
		api1 = api;

		String html = "<center><img src='data:image/png;base64,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' "
				+ "style='width:230px;height:90px;'></center>"
				+ "<br>"

				+ "<center><p><font color='#0000CD'><b><h4>Get Your Calendar Reminders!!!</h4></b></font></p></center>"
				+ "<center><font color='red'><b> connect</b></font>      To sign in to connectGDocyour Google Calendar.</center>"
				+ "<center><font color='red'><b>calendar</b></font>       To get list of upcoming events.</center>";

		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(html)));

	}

	@OnKeyword("connect")
	public void connectGDoc(TeamchatAPI api) {
		api1 = api;

		String sname = api.context().currentSender().getEmail();
		sname = sname.replace('@', '_');
		sname = sname.replace('.', '_');
		String emailid = api.context().currentSender().getEmail();
		api.perform(api
				.context()
				.currentRoom()
				.post(new TextChatlet(
						"<a href='http://interns.teamchat.com:8081/Zcal/GDocBotServlet?client_id="
								+ client_id + "&client_secret=" + client_secret
								+ "&email=" + emailid + "&name=" + sname
								+ "' target='_blank'> LOGIN</a>")));

	}

	@OnKeyword("Connect")
	public void Start_Setup1(TeamchatAPI api) {

		connectGDoc(api);
	}

	/*
	 * @OnKeyword("calendar") public void calendar(TeamchatAPI api) { api1 =
	 * api; api.perform(api .context() .currentRoom() .post(new PrimaryChatlet()
	 * .setQuestion("Select Your Option:") .setReplyScreen( api.objects()
	 * .form() .addField( api.objects() .select() .label("Select Your Option")
	 * .name("menu") .addOption( "List Upcoming Events"))) .alias("opt"))); }
	 */

	public static List<Event> list(Calendar calendar) throws IOException {
		// Build a new authorized API client service.getCalendar
		// Note: Do not confuse this class with the
		// com.google.api.services.calendar.model.Calendar class.

		// List the next 10 events from the primary calendar.
		DateTime now = new DateTime(System.currentTimeMillis());
		Events events = calendar.events().list("primary").setMaxResults(10)
				.setTimeMin(now).setOrderBy("startTime").setSingleEvents(true)
				.execute();

		List<Event> items = events.getItems();

		return items;

		// return null;
	}

	@OnKeyword("calendar")
	public void opt(TeamchatAPI api) throws IOException {
		api1 = api;
		System.out.println(api.context().currentSender().getEmail());
		System.err.println("getCalendar is called now");

		Calendar calendar = getCalendar(api.context().currentSender()
				.getEmail());

		// String opt = api.context().currentReply().getField("menu");

		final TeamchatAPI myapi = api;
		// if (opt.equals("List Upcoming Events"))

		onListReceived(myapi, list(calendar));

	}

	String summ[] = new String[50];
	String star[] = new String[50];
	int s = 0;
	String dated[] = new String[50];
	String timed[] = new String[50];
	int hr = 0;
	int min = 0;
	String mydate = null, evename = null;

	private void onListReceived(TeamchatAPI api, List<Event> items) {

		if (items.size() == 0) {
			System.out.println("No upcoming events found.");
		} else {
			System.out.println("Upcoming events");
			for (Event event : items) {
				DateTime start = event.getStart().getDateTime();
				if (start == null) {
					start = event.getStart().getDate();
				}
				System.out.printf("%s (%s)\n", event.getSummary(), start);
				summ[s] = event.getSummary();
				star[s] = start.toString();
				s++;

			}
			System.err.println(s);
			System.err.println(Arrays.toString(summ)); // Names of events
			System.err.println(Arrays.toString(star));

		}

		for (int i = 0; i < s; i++) {

			dated[i] = star[i].substring(0, 10);
			timed[i] = star[i].substring(11, 19);
		}
		System.out.println(Arrays.toString(dated)); // DATE of upcoming event
		System.out.println(Arrays.toString(timed)); // TIME of corressponding
													// event

		for (int j = 0; j < s; j++) {
			hr = Integer.parseInt(timed[j].substring(0, 2));
			min = Integer.parseInt(timed[j].substring(3, 5));
			mydate = (dated[j].substring(0, 10)); // mydate is in yyyy-mm-dd
													// format
			// evename=summ[j];
			Calendar1 a = new Calendar1();
			System.out.println("Time:" + hr + ": " + min + "Date:" + " "
					+ mydate);
			a.testCal(hr, min, j, mydate, summ[j]);
			// a.checkdate(mydate,hr,min);
		}

		String html = "<ul>";
		int i = 0;
		for (Event event : items) {
			DateTime start = event.getStart().getDateTime();
			if (start == null) {
				start = event.getStart().getDate();
			}
			html += "<li><h3><b><font color='#159CEB'>"
					+ event.getSummary()
					+ "</font></b></h3>"
					+ "<h4><font color='black'>"
					+ start.toString().replace("T", "          ")
							.substring(0, 25) + "</font></h4>" + "</li>";

			html += "</ul>";
			if (i % 5 == 0) {
				api.perform(api.context().currentRoom()
						.post(new PrimaryChatlet().setQuestionHtml(html)));
				html = "";
			}
			if (!html.equalsIgnoreCase(""))
				api.perform(api.context().currentRoom()
						.post(new PrimaryChatlet().setQuestionHtml(html)));
		}

	}

}