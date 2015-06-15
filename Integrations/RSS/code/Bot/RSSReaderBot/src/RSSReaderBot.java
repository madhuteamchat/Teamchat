import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.*;


public class RSSReaderBot {
private static String botID="ultimatekrissh@gmail.com";
private static String botPsswrd="abhishek";
private static TeamchatAPI api;
private static String channelURLs[]=
{"http://static.cricinfo.com/rss/livescores.xml",
"http://feeds.feedburner.com/digit/latest-news",
"http://dynamic.feedsportal.com/pf/555218/http://toi.timesofindia.indiatimes.com/rssfeedstopstories.cms",
"http://feeds.feedburner.com/NDTV-LatestNews",
"http://www.bollywoodhungama.com/rss/release_dates.xml"};
private static String channelNames[]={"Cricket Live Score",
	                                  "Digit Technologies",
	                                  "Times Of India",
	                                  "NDTV News",
	                                  "Movie Release Dates"};
private static Map map=new HashMap();

public static void main(String[] args) {
 api = TeamchatAPI.fromFile("RSSFeed.data")
                             .setEmail(botID) 
                             .setPassword(botPsswrd) 
                             .startReceivingEvents(new RSSReaderBot()); 
}

@OnKeyword("RSS")
public void provideChannels(TeamchatAPI api)
{   
	String HtmlInstructions="<b><u><font color='red'>Type:</font><br/><font color='blue'></u></b>"+
                             "<b>subscribe:</b> To Subscribe or Unsubscribe to Channels<br/>"+
			                 "<b>blogs:</b> To get new blogs<br/>"+
                             "<b>timer:</b> To reset Timer<br/>"+
			                 "<b>unsubscribeAll:</b> To Unsubscribe to RSS";
	
  	api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(HtmlInstructions)
  	                                            )
  	           );
}
@OnKeyword("subscribe")
public void subscribe(TeamchatAPI api)
{   String email=api.context().currentSender().getEmail();
    RSSBot b=(RSSBot)map.get(email);
    if(b==null)
	{RSSBot bot=new RSSBot();
	bot.setup();
	bot.setRoom(api.context().currentRoom().getId());
	map.put(email,bot);
	System.out.println("AB: Bot started");
	}
    postChannels(email,api.context().currentRoom());
}

public void postChannels(String email,Room r)
{   RSSBot b=(RSSBot)map.get(email);
    Boolean sub[]=b.getSubscription();
	Form f=api.objects().form();
	
	for(int i=0;i<channelNames.length;i++)
	{
		Field field=api.objects().select();
		field.label(channelNames[i]).name(channelNames[i]);
		if(sub[i])
		field.addOption("Unsubscribe").addOption("Subscribed");
		else field.addOption("Subscribe").addOption("Not Subscribed");
	    f.addField(field);
	}
	
	  api.perform(r.post(new PrimaryChatlet().setQuestion("View All Channels")
			                                 .setReplyLabel("View")
			                                 .setReplyScreen(f)
			                              .alias("changeSubscription")
			             ));
	
}

@OnAlias("changeSubscription")
public void changeSubscription(TeamchatAPI api)
{   
	String sub[]=new String[channelNames.length];
	for(int i=0;i<channelNames.length;i++)
	{
		sub[i]=api.context().currentReply().getField(channelNames[i]);
	}
	RSSBot bot=(RSSBot)map.get(api.context().currentReply().senderEmail());

	bot.setSubscription(sub);
	if(!bot.isStarted)
		bot.start();
	}

private String getChannelName(String aliases[])
{
	for(String x:aliases)
		if(x.startsWith("channel:"))
			return x.substring("channel:".length());
	return null;
	}

@OnKeyword("unsubscribeAll")
public void unsubscribeAll(TeamchatAPI api)
{
	RSSBot bot=(RSSBot)map.get(api.context().currentSender().getEmail());
	if(bot!=null)
	{
		try{
		bot.subscribed=false;
		bot.stop();
		bot=null;}catch(Exception e){bot=null;}
		api.perform(api.context().currentRoom().post(new PrimaryChatlet()
		                                           .setQuestionHtml("<b><font color='red'>You are Unsubscribed to All RSS Feeds</font></b>")));
		                                           
	    map.put(api.context().currentSender().getEmail(), null);
	}else api.perform(api.context().currentRoom().post(new PrimaryChatlet()
	.setQuestionHtml("<b><font color='green'>You are not Subscribed to any Channels yet."
	           +"<br/>Type subscribe to get Subscribed.</font></b>")
	           ));
	           }
@OnKeyword("timer")
public void timer(TeamchatAPI api)
{  RSSBot bot=(RSSBot)map.get(api.context().currentSender().getEmail());
if(bot!=null)
{
	Form f=api.objects().form();
    Field field=api.objects().select().label("Hours").name("hours");
    for(int i=1;i<=12;i++)
    	field.addOption(i+"-hours");
    f.addField(field);
    api.perform(api.context().currentRoom().post(new PrimaryChatlet()
                                                      .setQuestionHtml("<b><font color='green'>Select the delay hours</font></b>")
                                                      .setReplyLabel("Select")
                                                      .setReplyScreen(f)
                                                      .alias("setTime")));
}  
else api.perform(api.context().currentRoom().post(new PrimaryChatlet()
.setQuestionHtml("<b><font color='green'>You are not Subscribed to any Channels yet."
		           +"<br/>Type subscribe to get Subscribed.</font></b>")
		           )
);                                               
	}
@OnAlias("setTime")
public void setTimer(TeamchatAPI api)
{
	RSSBot bot=(RSSBot)map.get(api.context().currentSender().getEmail());
	if(bot!=null)
	{
		int x=Integer.parseInt(api.context().currentReply().getField("hours"));
		bot.setTime(x);
		}
    }
@OnKeyword("blogs")
public void viewBlog(TeamchatAPI api)
{RSSBot bot=(RSSBot)map.get(api.context().currentSender().getEmail());
if(bot!=null)
{bot.post(api);
	}
else{
	String html="<b><font color='red'>You are not Subscribed to any Channels.<br/>Type subscribe to get subscribed</font></b>";
	api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(html)));
}
}


class RSSBot extends Thread{
	int delayHrs=1;
	long delayMillis=delayHrs*60*60*1000;
	long currentTime=0;
	int hrs=0;
	Channel Channels[];
	Date d=new Date();
	Boolean subscribed=true;
	double lastTime;
	String roomID;
	Boolean isStarted=false;
	public void run()
	{
	 isStarted=true;
	 currentTime=d.getTime();	
	 post(api);
	try{
		
	    while(subscribed)
		{
			Thread.sleep(60*60*1000);
		    hrs++;
		    if(hrs>=delayHrs&&subscribed)
		    {  
		      System.out.println("Checking\n\n\n");
		      post(api);
		       }
		}
		
		}catch(Exception e)
		{
			System.out.println("Exception in thread");
		}
	}
	public void post(TeamchatAPI api)
	{
		for(int i=0;i<Channels.length;i++)
	    {
	    	if(Channels[i].getSubscription())
	    	{
	    		Channels[i].getFeeds();
	    	Channels[i].postFeeds(api,roomID);
	    	Channels[i].feeds=null;	
	    	}
	    }
	     hrs=0;
	}
	public void setup()
	{
		Channels=new Channel[channelURLs.length];
		for(int i=0;i<channelURLs.length;i++)
		{
		  Channels[i]=new Channel(channelURLs[i]);
		  Channels[i].setName(channelNames[i]);
		}
		
		       //ADD CUSTOM CHANNELS HERE
	}
	
	public void setSubscription(String sub[])
	{
		for(int i=0;i<sub.length;i++)
		{  if(sub[i].compareTo("Unsubscribe")==0
		      || sub[i].compareTo("Not Subscribed")==0)
		  { Channels[i].unsubscribe();
		    System.out.println("Unsubscribed to: "+Channels[i].getName());
		  }
		else if(sub[i].compareTo("Subscribe")==0
		      || sub[i].compareTo("Subscribed")==0){
			Channels[i].subscribe();
		System.out.println("Subscribed to: "+Channels[i].getName());
		  
		}
		}
	}
	public void setTime(int x)
	{
		delayHrs=x;
		api.perform(api.context().byId(roomID).post(new TextChatlet("Timer Successfully Set")));
		
	}
	
    public void setRoom(String r)
    {
    	roomID=r;
    }
    
	public Boolean[] getSubscription()
	{  Boolean sub[]=new Boolean[Channels.length];
	for(int i=0;i<Channels.length;i++)
	{
		sub[i]=Channels[i].getSubscription();
	}
		return sub;
	}
}
}


