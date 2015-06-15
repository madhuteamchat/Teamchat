package com.teamchat.integrations.gotomeeting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.ParseException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class Meeting
{
	String []values=new String[5];
	String date=null,time=null,subject=null;
	String accTok1,groupID1;
	
	public void showOptions(TeamchatAPI api,String teamchatUserEmail)
	{
		
		api.perform(api.context().create().setName("p2p").add(teamchatUserEmail).post(new PrimaryChatlet().setQuestion("Successfully Authenticated with GoToMeeting.")));
		api.perform(api
				.context()
				.create().setName("p2p").add(teamchatUserEmail)
				.post(new PrimaryChatlet().setQuestion("What do you want to do? ")
						.setReplyScreen(api.objects().form().addField(api.objects().select().label("Functions").name("functions").addOption("Instant Meeting").addOption("View All Previous Meetings").addOption("View Meetings by ID").addOption("Schedule a Meeting"))).alias("functions")));

	}
	
	public void instantMeeting(TeamchatAPI api,String accTok,String groupID) throws ClientProtocolException, IOException, JSONException, URISyntaxException
	{
		accTok1=accTok;
		groupID1=groupID;
		String[] values = createMeeting(api, accTok);
		initializeMeeting(api, accTok, values[1]);
		postJoinURL(api, values[0],groupID);
	}

	public String function(TeamchatAPI api)
	{
		return (api.context().currentReply().getField("functions"));
	}
	
	public String[] createMeeting(TeamchatAPI api, String accTok) throws ClientProtocolException, IOException, JSONException
	{
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("https://api.citrixonline.com:443/G2M/rest/meetings");
		post.setHeader("Authorization", accTok);
		JSONObject json = new JSONObject();
		json.put("subject", "Report Meeting");
		json.put("starttime", "2015-06-02T08:54:32.156Z");
		json.put("endtime", "2015-06-02T08:54:32.157Z");
		json.put("passwordrequired", false);
		json.put("conferencecallinfo", "VoIP");
		json.put("timezonekey", "");
		json.put("meetingtype", "immediate");

		StringEntity se = new StringEntity(json.toString());
		post.setEntity(se);
		HttpResponse response = client.execute(post);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuilder sb = new StringBuilder();
		String line = "";
		while ((line = rd.readLine()) != null)
			sb.append(line);
		sb.deleteCharAt(0);
		sb.deleteCharAt(sb.length() - 1);
		String output = sb.toString();
		JSONObject jobject = new JSONObject(output);
		values[0] = (String) jobject.get("joinURL");
		values[1] = jobject.get("meetingid").toString();
		return values;
	}
	
	public void initializeMeeting(TeamchatAPI api, String accTok, String meetingid) throws ClientProtocolException, IOException, URISyntaxException, JSONException
	{
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("https://api.citrixonline.com/G2M/rest/meetings/" + meetingid + "/start");
		request.setHeader("Authorization", accTok);

		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuilder sb = new StringBuilder();
		String line = "";
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString();

		JSONObject jobject = new JSONObject(output);
		String hostURL = (String) jobject.get("hostURL");
		Room p2p = api.context().create().setName("Private").add(api.context().currentReply().senderEmail());

		api.perform(p2p.post(new PrimaryChatlet().setQuestionHtml("<html><body>Organizer " + "<a href=" + hostURL +" target='_blank'"+ ">Click here</a> to start the meeting.</body></html>")));
	}

	public void postJoinURL(TeamchatAPI api, String joinurl, String groupID)
	{
		api.perform(api.context().byId(groupID).post(new PrimaryChatlet().setQuestionHtml("<html><body>" + "New Team Meeting Scheduled for now. " + "Members " + "<a href=" + joinurl +" target='_blank'"+ ">Click here</a> to join the meeting</body></html>")));
	}
	
	public void scheduleMeeting(TeamchatAPI api)
	{
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestion("Please fill the following form to schedule the meeting ")
						.setReplyScreen(api.objects().form().addField(api.objects().input().label("Date (yyyy-mm-dd)").name("date")).addField(api.objects().input().label("Start Time-24 Hour Format (hh:mm:ss)").name("time")).addField(api.objects().input().label("Subject").name("subject"))).alias("schedule")));
	}
	
	public String[] reportSchedule1(TeamchatAPI api, String accTok) throws ClientProtocolException, IOException, JSONException
	{
		date = api.context().currentReply().getField("date");
		time = api.context().currentReply().getField("time");
		subject = api.context().currentReply().getField("subject");
		values[2] = date;
		values[3] = time;
		values[4] = subject;
		values = createMeeting(api, accTok);
		for (String out : values)
			System.out.println(out);
		return values;
	}
	
	public void postSchedule(TeamchatAPI api, String accTok, String[] values,String groupID) throws ClientProtocolException, IOException, URISyntaxException, JSONException, ParseException
	{
		SchedulingOne sch1=new SchedulingOne();
		sch1.setTimer(api, groupID, values, accTok);
		SchedulingTwo sch2=new SchedulingTwo();
		sch2.setTimer(api, groupID, values, accTok);
	}
	
	public void post(TeamchatAPI api) throws ClientProtocolException, IOException, URISyntaxException, JSONException{
		api.perform(api.context().byId(groupID1)
				.post(new PrimaryChatlet().setQuestionHtml("<html><body>" + "Team Meeting scheduled for " + values[2] + " at time " + values[3] + " with subject '" + values[4] + "'. " + "<a href=" + values[0] + ">Click here</a>" + " to start the meeting at scheduled time.</body></html>")));
			api.perform(api.context().create().setName("Private").add(api.context().currentSender().getEmail()).post(new TextChatlet("You can start the meeting at the scheduled date and time.")));
			new Meeting().initializeMeeting(api, accTok1, values[1]);
			api.perform(api.context().currentRoom().post(new TextChatlet("dd")));
		}
	public void viewMeetingsbyID(TeamchatAPI api) throws ClientProtocolException, IOException
	{
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestion("Please give the meeting ID").setReplyScreen(api.objects().form().addField(api.objects().input().label("Meeting ID").name("id2"))

		).alias("meetingid")));

	}
	
	public void viewMeetings(TeamchatAPI api, String accTok) throws ClientProtocolException, IOException, JSONException
	{
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("https://api.citrixonline.com:443/G2M/rest/meetings");
		request.setHeader("Authorization", accTok);
		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		sb.insert(0, "{meetings:");
		sb.insert(sb.length(), "}");
		String output = sb.toString();
		System.out.println(output);
		JSONObject j=new JSONObject(output);
		JSONArray jarray=j.getJSONArray("meetings");
		int length=jarray.length();
		int[] id= new int[length];
		String[] status =new String[length],
				 subject =new String[length];
		
		for(int i=0;i<jarray.length();i++){
			JSONObject j1=jarray.getJSONObject(i);
			id[i]=j1.getInt("meetingid");
			subject[i]=j1.getString("subject");
			status[i]=j1.getString("status");
		}
		
		String head="<html><head><style>table, th, td {" + "border: 1px solid black;" + "border-collapse: collapse;}th, td {" + "padding: 5px;}</style></head><body>" + "<table style='width:100%'>" +"<tr>"+
				"<th>"+"S.No."+"</th>"
				+"<th>"+"Meeting ID"+"</th>"
				+"<th>"+"Subject"+"</th>"
				+"<th>"+"Status"+"</th>"
			+"</tr>";
		
		for(int i=0;i<10;i++)
			head+="<tr>"
					+"<td>"+i+"</td>"
					+"<td>"+id[i]+"</td>"
					+"<td>"+subject[i]+"</td>"
					+"<td>"+status[i]+"</td>"
					+"</tr>";
		head+="</table></body></html>";
	
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(head)));
	
	}
}
