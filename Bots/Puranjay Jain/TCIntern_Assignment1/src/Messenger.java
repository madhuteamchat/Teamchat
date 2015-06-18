import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.*;

public class Messenger extends TimerTask  {
	TeamchatAPI api;   
	String msg,to,gname,mto [];
	int hh,mm,ss,ms;
	
	//timed message
	public Messenger(TeamchatAPI api,String msg,String gname,String to,int hh,int mm,int ss,int ms){
		this.msg = msg;
		this.to = to;
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
		
		Timer time = new Timer(); // Instantiate Timer Object
		
		time.schedule(new Messenger(api,msg,gname,to), calendar.getTime(), 1000 * 60 * 60 * 24);
	}
	
	//send message
	public Messenger(TeamchatAPI api,String msg,String gname,String to){
		this.msg = msg;
		this.to = to;
		this.api = api;
		this.gname = gname;
		//create a room and send message
		Room x = api.context().create().setName(gname).add(to);
		api.perform(x.post(new PrimaryChatlet().setQuestion(msg)));
	}
	
	//send Message to multi people in one group
	public Messenger(TeamchatAPI api,String msg,String gname,String mto []){
		this.msg = msg;
		this.mto = mto;
		this.api = api;
		this.gname = gname;
		//create a room and send message
		Room x = api.context().create().setName(gname);
		for (int i = 0; i < mto.length; i++) {			
			x.add(mto[i]);
		}
		api.perform(x.post(new PrimaryChatlet().setQuestion(msg)));
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}