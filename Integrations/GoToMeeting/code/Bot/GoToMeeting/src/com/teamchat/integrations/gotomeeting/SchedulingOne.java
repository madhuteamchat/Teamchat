package com.teamchat.integrations.gotomeeting;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.integrations.gotomeeting.Meeting;

public class SchedulingOne
{
	public void setTimer(final TeamchatAPI api, final String groupID, final String[] values,final String accTok) throws ParseException
	{
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				api.perform(api.context().byId(groupID).post(new PrimaryChatlet()
				                                     .setQuestion("New Team Meeting Scheduled on "+values[2]+""
				                                     		+ " at "+values[3]+" with subject '"
				                                     		+values[4]+"'. Meeting ID: "+values[1])));
				api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestion("Meeting Scheduled on "+values[2]+""
				                                     		+ " at "+values[3]+" with subject '"
				                                     		+values[4]+"'. Meeting ID: "+values[1])));
			}
		};

		DateFormat d= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date1 = d.parse(values[2]+" "+ values[3]);
		Calendar c=d.getCalendar();
		System.out.println(c.getTime());
		c.add(c.HOUR, -2);
		String [] str=c.getTime().toString().split(" ");
		String time=c.get(Calendar.YEAR)+"-"+((int)c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DATE)+" "+str[3];
		Date date2=d.parse(time);
		Timer timer = new Timer();
		timer.schedule(task, date2);

	}

}
