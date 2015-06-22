package Task1a;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.*;

public class Timemanagement extends TimerTask  {
	TeamchatAPI api;   
	String msg,email,gname,memail [];
	int hh,mm,ss,ms;
	

	public Timemanagement(TeamchatAPI api,String msg,String gname,String email,int hh,int mm,int ss,int ms){
		this.msg = msg;
		this.email = email;
		this.api = api;
		this.hh = hh;
		this.mm = mm;
		this.ss = ss;
		this.ms = ms;
		this.gname = gname;
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hh);
		calendar.set(Calendar.MINUTE, mm);
		calendar.set(Calendar.SECOND, ss);
		calendar.set(Calendar.MILLISECOND, ms);
		
		Timer time = new Timer(); 
		
		time.schedule(new Timemanagement(api,msg,gname,email), calendar.getTime(), 1000 * 60 * 60 * 24);
	}
	

	public Timemanagement(TeamchatAPI api,String msg,String gname,String email){
		this.msg = msg;
		this.email = email;
		this.api = api;
		this.gname = gname;
		//create a room and send message
		Room x = api.context().create().setName(gname).add(email);
		api.perform(x.post(new PrimaryChatlet().setQuestion(msg)));
	}
	

	public Timemanagement(TeamchatAPI api,String msg,String gname,String memail []){
		this.msg = msg;
		this.memail = memail;
		this.api = api;
		this.gname = gname;
		//create a room and send message
		Room x = api.context().create().setName(gname);
		for (int i = 0; i < memail.length; i++) {			
			x.add(memail[i]);
		}
		api.perform(x.post(new PrimaryChatlet().setQuestion(msg)));
	}

	@Override
	public void run() {
		// emailDO Auemail-generated method stub
		
	}
}
