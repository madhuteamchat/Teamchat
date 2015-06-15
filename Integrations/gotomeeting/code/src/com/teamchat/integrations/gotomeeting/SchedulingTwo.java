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

import org.json.JSONException;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class SchedulingTwo
{
	public void setTimer(final TeamchatAPI api, final String groupID, final String[] values,final String accTok) throws ParseException
	{
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				api.perform(api.context().byId(groupID).post(new PrimaryChatlet()
				                                     .setQuestionHtml("<html><body>REMINDER: Your scheduled meeting with "
				                                     		+ "Meeting ID "+values[1]+" is in 10 Minutes."
				                                     		+ "<a href="+values[0]+" target='_blank'> Click here</a> To join.</body></html>"
				                                     		)));
				
				try
				{
					api.perform(api.context().currentRoom().post(new PrimaryChatlet()
                    .setQuestionHtml("<html><body>REMINDER: Your scheduled meeting with Meeting ID: "+values[1]+" is in 10 Minutes."
                    		)));

					new Meeting().initializeMeeting(api, accTok,values[1]);
				} catch (IOException | URISyntaxException | JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		DateFormat d= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date1 = d.parse(values[2]+" "+ values[3]);
		Calendar c=d.getCalendar();
		System.out.println(c.getTime());
		c.add(c.MINUTE, -10);
		String [] str=c.getTime().toString().split(" ");
		String time=c.get(Calendar.YEAR)+"-"+((int)c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DATE)+" "+str[3];
		Date date2=d.parse(time);
		Timer timer = new Timer();
		timer.schedule(task, date2);

	}

}
