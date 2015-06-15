package teamchat;

import java.util.ArrayList;
import java.util.List;

import users.User;
import users.UserMethods;
import yammer.Notifications;
import yammer.UploadedFile;
import yammer.YammerClient;
import yammer.groups.Group;
import yammer.search.Message;
import yammer.search.Search;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

public class YammerBot {
   List<User> users=new ArrayList<User>();
   UserMethods yc=new UserMethods();
   
	@OnKeyword("help")
	public void onYammerEntry(TeamchatAPI api) {
		System.out.println("data entry typed");
		api.perform(
				api.context().currentRoom().registerForEvents().post(
				new TextChatlet("Type 'Post' to post messages on your network<br/>"
						 +      "Type 'Send' to send a private message to a user<br/>"
						 +      "Type 'Search' to search for keywords<br/>"
						 +      "Type 'Notify' to get notifications'<br/>")));
			
	}
	
	@OnKeyword("Notify")
	public void onNotification(TeamchatAPI api) {
		String displayresult="";
		HTMLBuilder ui=new HTMLBuilder();
		List<String> results=new Notifications().getLikedBy();
		displayresult=ui.displayNotifications(results);
		  api.perform(
					api.context().currentRoom().registerForEvents().post(
					new TextChatlet(displayresult))
		  );
	}
	
	@OnKeyword("Post")
	public void onPostEntry(TeamchatAPI api) {
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
	
	@OnKeyword("Search")
	public void onSearchEntry(TeamchatAPI api) {
		api.perform(
				api.context().currentRoom().registerForEvents().post(
				new PrimaryChatlet()
				.setQuestion("Please enter the keyword to be searched")
				.setReplyScreen(api.objects().form()
				.addField(api.objects().input().label("Search Keyword").name("key"))
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
	
	@OnKeyword("Send")
	public void onUsersEntry(TeamchatAPI api) {
		try {
		   yc=new UserMethods();
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
	
	public Field getOptions(TeamchatAPI api,List<User> usrs) {
		Field f=api.objects().select().label("User").name("user");
		for(int i=0;i<usrs.size();i++) {
			f.addOption(usrs.get(i).username);
		}
		return f;
	}
	
	@OnAlias("Send message")
	public void onSend(TeamchatAPI api) {
	  String user = api.context().currentReply().getField("user");
	  String message = api.context().currentReply().getField("message");
	  int responsecode=0;
	  try {
        for(User u:users) {
        	if(u.username.equals(user)) {
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
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  } 
	}
	
	
	
	@OnAlias("Post status")
	public void onPost(TeamchatAPI api) {
		System.out.println("message posted");
	  String message = api.context().currentReply().getField("message");
	  try {
	  YammerClient yc=new YammerClient();
	  int status=yc.postStatus(message);
	  if(status==201) {
		  api.perform(
					api.context().currentRoom().registerForEvents().post(
					new TextChatlet("Posted Successfully")
					)
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
	  String keyword = api.context().currentReply().getField("key");
	  String domain = api.context().currentReply().getField("domain");
	  HTMLBuilder ui=new HTMLBuilder();
	  String displayresult="";
	  try {
		  Search srch=new Search(keyword);
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
				new TextChatlet(displayresult))
	  ); 
	  }
	  catch(Exception e) {
	  e.printStackTrace();
	  } 
	}
	
	public static void main(String[] args) throws Exception {
		TeamchatAPIImpl.fromFile("teamchat1.data")
		.setEmail("botteamchat@gmail.com")
		.setPassword("hellmasterbot")
		.startReceivingEvents(new YammerBot());
		}
}
