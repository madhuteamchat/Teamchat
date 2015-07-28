package test.gcal.pack;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.integrations.GoogleCalendar.Test;

public class Calendar1 {
	// int count=0;
	String name[];
	Calendar a = Calendar.getInstance();

	TeamchatAPI api1;
	TimerTask1 tak;

	public Calendar1() {
		api1 = Test.api1;
	}

	/*
	 * TimerTask task = new TimerTask() { int hrs,min; String summ;
	 * 
	 * @Override public void run() {
	 * 
	 * System.out.println("Message on time!!!");
	 * api1.perform(api1.context().currentRoom() .post(new
	 * PrimaryChatlet().setQuestion("Reminder for Event"+ ""+" "+
	 * "starting at"+"")));
	 * 
	 * assign(); count++; } };
	 */

	public void testCal(int hours, int minutes, int j, String mydate,
			String summ) {
		
		
		/*
		 * a.set(Calendar.MINUTE, minutes); a.set(Calendar.HOUR, hours);
		 * //System.out.println(a.getTime());
		 * 
		 * Date d = new Date(a.getTimeInMillis()); //a.add(a.MINUTE, -10);
		 * 
		 * System.out.println(a.getTimeInMillis()); Timer timer = new Timer();
		 * System.out.println("Timer begin"); timer.schedule(task, d);
		 * System.out.println("Task set "+j + "\n\n");
		 */
		// name=summ;
		DateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	//	d.setTimeZone(Timezone.);
		java.util.Date date1 = null;
        java.util.Date dates=null;
        java.util.Date temp=new java.util.Date();
		try {
			date1 = d.parse(mydate + " " + hours + ":" + minutes);
		dates=new Date(date1.getTime()-(60*10000)-(19800000));
		} catch (ParseException e) {
			// TODO Auto-generated catch block		18000000+1800000
			e.printStackTrace();
		}
		System.out.println("Time of this comp "+temp.getTime()+" "+dates.getTime());
		
		tak = new TimerTask1(hours, minutes, summ);
		tak.api1 = api1;
		Calendar c = d.getCalendar();
		System.out.println(c.getTime());
	//	c.add(c.MINUTE, -10);			19800000
		Timer timer = new Timer();

		System.out.println("Okay.");

		timer.schedule(tak, dates);

	}

	/*
	 * public void assign() {
	 * 
	 * 
	 * api1.perform(api1.context().currentRoom() .post(new
	 * PrimaryChatlet().setQuestion("Reminder for Event"+ name[count]+" "+
	 * "starting at"+" yaha TIME aaega")));
	 * 
	 * 
	 * }
	 */

}

class TimerTask1 extends TimerTask {

	public static TeamchatAPI api1;

	int hrs, min;
	String summ;

	TimerTask1(int hrs, int min, String summ) {
		this.hrs = hrs;
		this.min = min;
		this.summ = summ;

	}

	@Override
	public void run() {
		String html2 = "";
		System.out.println("Message on time!!!");
		html2=
				"<h3><b><font color='#159CEB'>Reminder!  </font></b></h3>"
						+ "<font color='red'><b><h4>" +summ+ "</font>"
						+ " starting at " + " "
						+"<font color='#8A2BE2'>"+ hrs+":"
						+ min+"</font></h4></b>";
		
		
		api1.perform(api1
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(html2)));

		// assign();
		// count++;
	}
}
