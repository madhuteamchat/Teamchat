package com.teamchat.integration.bot;

import java.util.ArrayList;
import java.util.List;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.integration.factory.PropertyFile;
import com.teamchat.integration.yammer.Notifications;
import com.teamchat.integration.yammer.UploadedFile;
import com.teamchat.integration.yammer.PostStatus;
import com.teamchat.integration.yammer.YammerConnection;
import com.teamchat.integration.yammer.groups.Group;
import com.teamchat.integration.yammer.search.Message;
import com.teamchat.integration.yammer.search.Search;
import com.teamchat.integration.yammer.users.User;
import com.teamchat.integration.yammer.users.UserMethods;

public class YammerBot {
   List<User> users=new ArrayList<User>();
   UserMethods yc=null;
   String useremail="";
   YammerConnection yammerconnect;
   
   public YammerBot(){
	   try{
		yammerconnect=new YammerConnection();
	   }
	   catch(Exception e){
		   e.printStackTrace();
	   }
   }
     
	@OnKeyword("help")
	public void onhelpEntry(TeamchatAPI api) {
		System.out.println("testing now");
		api.perform(
				api.context().currentRoom().registerForEvents().post(
				new PrimaryChatlet()
				.setQuestionHtml("<!DOCTYPE html><html><head></head><body>Hi! I am the <b>Yammer</b> bot of teamchat! You can use me to post and send messgaes "
						 +       "from your Yammer account. Also, you can get notifications and search from people, messages,groups "         
						 +       "and files on your Yammer network.<br/>"
						 +       "Type <b>'login'</b> to log into your Yammer network.<br/>"
						 +       "Type <b>'post'</b> to post messages on your network.<br/>"
						 +       "Type <b>'send'</b> to send a private message to a user.<br/>"
						 +       "Type <b>'search'</b> to search for keywords.<br/>"
						 +       "Type <b>'notify'</b> to get notifications.<br/></body></html>")));
			
	}
	
	
	@OnKeyword("login")
	public void onloginEntry(TeamchatAPI api) {
		String hostname=PropertyFile.getProperty("host");
		String port=PropertyFile.getProperty("port");
		useremail=api.context().currentSender().getEmail();
		String url="http://"+hostname+":"+port+"/Yammer/yammerlogin?email="+useremail;
		String embed_url=HTMLBuilder.createEmbeddedLink(url, "Login", "http");
		api.perform(
				api.context().currentRoom().registerForEvents().post(
				new PrimaryChatlet()
				.setQuestionHtml("<!DOCTYPE html><html><head></head><body>Log into Yammer and then enter the keywords again.<br/><a href="+embed_url+">Login to Yammer (Please open in a seperate tab)</a>"
						        +"</html></body>"
						))
	  );
	}
	
	
	
	
	@OnKeyword("notify")
	public void onNotification(TeamchatAPI api) {
		String displayresult="";
		useremail=api.context().currentSender().getEmail();
		try{
		System.out.println("User is "+useremail);
		if(yammerconnect.getAuthToken(useremail)== null)
		{     
			onloginEntry(api); 
		}
		else {
		HTMLBuilder ui=new HTMLBuilder();
		List<String> results=new Notifications(useremail).getLikedBy();
		displayresult=ui.displayNotifications(results);
		  api.perform(
					api.context().currentRoom().registerForEvents().post(
					new PrimaryChatlet()
					.setQuestionHtml(displayresult))
		  );
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@OnKeyword("post")
	public void onPostEntry(TeamchatAPI api) {
		useremail=api.context().currentSender().getEmail();
		try{
		System.out.println("User is "+useremail);
		if(yammerconnect.getAuthToken(useremail)== null)
		{
			onloginEntry(api);
		}
		else
		api.perform(
				api.context().currentRoom().registerForEvents().post(
				new PrimaryChatlet()
				.setQuestion("Please enter the message to be posted")
				.setReplyScreen(api.objects().form()
				.addField(api.objects().input().label("Message").name("message"))
				)
				.alias("Post status"))
				);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@OnKeyword("search")
	public void onSearchEntry(TeamchatAPI api) {
		useremail=api.context().currentSender().getEmail();
		System.out.println("User is "+useremail);
		if(yammerconnect.getAuthToken(useremail)== null)
		{
			onloginEntry(api);
		}
		else
		  api.perform(
				api.context().currentRoom().registerForEvents().post(
				new PrimaryChatlet()
				.setQuestion("Please enter the keyword to be searched")
				.setReplyScreen(api.objects().form()
				.addField(api.objects().input().label("Search Keyword").name("keyvalue"))
				.addField(api.objects().select().label("Search").name("domain")
			    .addOption("Search users")
			    .addOption("Search groups")
			    .addOption("Search messages")
			    .addOption("Search uploaded files on your network")
			    )
				)
				.alias("Search keyword"))
				);
	}
	
	@OnKeyword("send")
	public void onUsersEntry(TeamchatAPI api) {
		useremail=api.context().currentSender().getEmail();
		System.out.println("User is "+useremail);
		if(yammerconnect.getAuthToken(useremail)== null)
		{
			onloginEntry(api);
		}
		else {
		useremail=api.context().currentSender().getEmail();
		try {
		   yc=new UserMethods(useremail);
		  users=yc.getUsers();
			api.perform(
					api.context().currentRoom().registerForEvents().post(
					new PrimaryChatlet()
					.setQuestion("Please choose a sender from your network")
					.setReplyScreen(api.objects().form()
					.addField(getOptions(api,users))
					.addField(api.objects().input().label("Message").name("message"))
					)
					.alias("Send message"))
					);
	      
	}
	catch(Exception e) {
		e.printStackTrace();
	}
		}
	}
	
	public Field getOptions(TeamchatAPI api,List<User> usrs) {
		Field f=api.objects().select().label("User").name("user");
		for(int i=0;i<usrs.size();i++) {
			f.addOption(usrs.get(i).username);
		}
		return f;
	}
	
	@OnAlias("Send message")
	public void onSend(TeamchatAPI api) {
	  useremail=api.context().currentReply().senderEmail();
	  String user = api.context().currentReply().getField("user");
	  String message = api.context().currentReply().getField("message");
	  int responsecode=0;
	  try {
        for(User u:users) {
        	if(u.username.equals(user)) {
        		u.setAuthtoken(useremail);
        		responsecode=u.sendMessage(message);
        		break;
        	}
	  }
      if(responsecode==201) {
	  api.perform(
				api.context().currentRoom().registerForEvents().post(
				new TextChatlet("Message sent"))
	  ); 
      }
      else if(responsecode==401) {
		  api.perform(
					api.context().currentRoom().registerForEvents().post(
		            new TextChatlet("Authentication failure. Please try logging again."))
		            );
	  }
	  else
		  api.perform(
					api.context().currentRoom().registerForEvents().post(
					new TextChatlet("Message could not be sent! Status returned "+responsecode)
					)
	  	);  
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  } 
	}
	
	
	
	@OnAlias("Post status")
	public void onPost(TeamchatAPI api) {
		System.out.println("message posted");
	  useremail=api.context().currentReply().senderEmail();
	  String message = api.context().currentReply().getField("message");
	  try {
	  PostStatus yc=new PostStatus(useremail);
	  int status=yc.postStatus(message);
	  if(status==201) {
		  api.perform(
					api.context().currentRoom().registerForEvents().post(
					new TextChatlet("Posted Successfully")
					)
	  	); 
	  }
	  else if(status==401) {
		  api.perform(
					api.context().currentRoom().registerForEvents().post(
		            new TextChatlet("Authentication failure. Please try logging again."))
		            );
	  }
	  else
		  api.perform(
					api.context().currentRoom().registerForEvents().post(
					new TextChatlet("Post could not be posted! Status returned "+status)
					)
	  	);  

	    yc.close();
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  } 
	}
	
	@OnAlias("Search keyword")
	public void onSearch(TeamchatAPI api) {
	  useremail=api.context().currentReply().senderEmail();
	  String keyword = api.context().currentReply().getField("keyvalue");
	  String domain = api.context().currentReply().getField("domain");
	  HTMLBuilder ui=new HTMLBuilder();
	  String displayresult="";
	  try {
		  Search srch=new Search(keyword,useremail);
        if(domain.equals("Search users")) {
        	List<User> results=srch.searchUsers();
        	displayresult=ui.displayUsers(results,keyword);
        }
        if(domain.equals("Search groups")) {
        	List<Group> results=srch.searchGroups();
        	displayresult=ui.displayGroup(results,keyword);
        }
        if(domain.equals("Search messages")) {
        	List<Message> results=srch.searchMessages();
        	displayresult=ui.displayMessages(results,keyword);
        }
        if(domain.equals("Search uploaded files on your network")) {
        	List<UploadedFile> results=srch.searchUploadedFiles();
        	displayresult=ui.displayUploadedFiles(results,keyword);
        }
	  api.perform(
				api.context().currentRoom().registerForEvents().post(
				new PrimaryChatlet().setQuestionHtml(displayresult))
	  ); 
	  }
	  catch(Exception e) {
	  e.printStackTrace();
	  } 
	}
	
}
