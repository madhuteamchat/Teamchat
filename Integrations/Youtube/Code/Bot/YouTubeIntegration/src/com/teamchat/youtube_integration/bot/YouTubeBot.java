package com.teamchat.youtube_integration.bot;


import java.net.URLDecoder;
import java.util.ArrayList;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.Reply;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.youtube_integration.channelbulletin.ChannelBulletin;
import com.teamchat.youtube_integration.connect.YoutubeConnect;
import com.teamchat.youtube_integration.playlist.PlaylistUpdates;
import com.teamchat.youtube_integration.search.SearchByKeyword;
import com.teamchat.youtube_integration.search.SearchByTopic;
import com.teamchat.youtube_integration.search.SearchChannel;
import com.teamchat.youtube_integration.subscribe.AddSubscription;
import com.teamchat.youtube_integration.upload.MyUploads;
import com.teamchat.youtube_integration.upload.UploadVideo;

public class YouTubeBot {
	
	String apikey="AIzaSyCuxRZHt5hjH-x-IQQG5MZFldxigM_4_W0";
	SearchByTopic sbt=null;
	ArrayList<String> lan,chId,chTitle;
	/*
	 copy and paste client_secrets.json in src folder
	 where the clientid and client secret is stored
	 youtube account used here is email=teambott2@gmail.com and password=gupshup8
	 */
	String pltitle,pldes;
	Integer vno;
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		 TeamchatAPI api = TeamchatAPI.fromFile("teamchat2.data")
				    .setEmail("teambott2@gmail.com") //change to your teamchat registered email id
				    .setPassword("gupshup") //change to your teamchat password
				    .startReceivingEvents(new YouTubeBot()); //Wait for other user to send message
	}
	
// Help
	@OnKeyword("youtubehelp")
	public void youtubeHelp(TeamchatAPI api) {
		
		api.perform(api.context().currentRoom().post( 
				new TextChatlet("<br><div align=\"center\">Youtube Help</div>"
						+"<br>searchbykeyword &nbsp; - &nbsp; To search videos related to the keyword"
						+"<br>searchbytopic &nbsp; - &nbsp; To search videos under a category"
						+"<br>searchchannel &nbsp; - &nbsp; To search channel related to the keyword"
						+"<br>subscribechannel &nbsp; - &nbsp; To subscribe a channel using channel id"
						+"<br>channelbulletin &nbsp; - &nbsp; To post a video in your channel by providing video id"
						+"<br>createplaylist &nbsp; - &nbsp; To ceate a playlist by providing video id"
						+"<br>uploadvideo &nbsp; - &nbsp; To upload a video into your channel by providing the location"
						+"<br>myuploads &nbsp; - &nbsp; Reterive the details of the video uploaded by you"
						)));
		
	}

// Youtube Connect
	
		@OnKeyword("connectyoutube")
		public void connectYoutube(TeamchatAPI api) {
			YoutubeConnect ytc=new YoutubeConnect();
			ytc.youtubeLogin();
			api.perform(api.context().currentRoom().post( new TextChatlet("Successfully, Connected to your Youtube account.")));
			
		}
		
// Youtube Disconnect
		
			@OnKeyword("disconnectyoutube")
			public void disconnectYoutube(TeamchatAPI api) {
				YoutubeConnect ytc=new YoutubeConnect();
				ytc.youtubeLogout();
				api.perform(api.context().currentRoom().post( new TextChatlet("Successfully, Disconnected from your Youtube account.")));
				
			}
	
// Search By Keyword
	
	@OnKeyword("searchbykeyword")
	public void searchbykeyword(TeamchatAPI api) {
		api.perform(
				api.context().currentRoom().post(
				new PrimaryChatlet()
			
				.setQuestion("Enter the Keyword to Search")
				.setReplyScreen(api.objects().form()
				.addField(api.objects().input().label("Keyword").name("keyword"))
				.addField(api.objects().input().label("Max Result").name("maxresult"))
				)
				.alias("aliassearchbykeyword")
				));	
	}
	
	@OnAlias("aliassearchbykeyword")
	public void aliassearchbykeyword(TeamchatAPI api)
	{
		
		Reply rpl=api.context().currentReply();
		String keyword=rpl.getField("keyword");
		String maxresult=rpl.getField("maxresult");
		SearchByKeyword sbk=new SearchByKeyword();
		try {
			api.perform(api.context().currentRoom().post( new TextChatlet(sbk.searchByKeyword(apikey,keyword,Integer.parseInt(maxresult)))));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
// Search Channel
	
		@OnKeyword("searchchannel")
		public void searchChannel(TeamchatAPI api) {
			api.perform(
					api.context().currentRoom().post(
					new PrimaryChatlet()
				
					.setQuestion("Enter the Keyword to Search")
					.setReplyScreen(api.objects().form()
					.addField(api.objects().input().label("Channel Name").name("cname"))
					
					)
					.alias("getchannel")
					));	
		}
		
		@OnAlias("getchannel")
		public void getchannel(TeamchatAPI api)
		{
			
			Reply rpl=api.context().currentReply();
			String cname=rpl.getField("cname");
			SearchChannel sc=new SearchChannel();
			try {
				api.perform(api.context().currentRoom().post( new TextChatlet(sc.channelSearch(apikey,cname))));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
// Search By Topic
	
		@OnKeyword("searchbytopic")
		public void searchbytopic(TeamchatAPI api) {
			api.perform(
					api.context().currentRoom().post(
					new PrimaryChatlet()
				
					.setQuestion("Enter the Topic")
					.setReplyScreen(api.objects().form()
					.addField(api.objects().input().label("Topic").name("topic"))
					)
					.alias("aliassearchbytopic")
					));	
		}
		
		@OnAlias("aliassearchbytopic")
		public void aliassearchbytopic(TeamchatAPI api)
		{
			
			Reply rpl=api.context().currentReply();
			String topic=rpl.getField("topic");
			sbt=new SearchByTopic(apikey,topic);
			
			try {
				ArrayNode an=sbt.gettopics();
				if(!sbt.checkNode.equals("empty"))
				{
					api.perform(api.context().currentRoom().post( new TextChatlet(sbt.checkNode)));
				}
				else
				{
					api.perform(api.context().currentRoom().post(
						new PrimaryChatlet()
						.setQuestion("Choose the Topic")
						.setReplyScreen(api.objects().form().addField(usrField(api, an)))
						.alias("topicselected"))
						);
				}
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		public Field usrField(TeamchatAPI api,ArrayNode an)
		{
			Field f=api.objects().select().label("Topic").name("choice");
			lan=new ArrayList<String>();
			for(int i=0;i<an.size();i++)
			{
				  JsonNode node = an.get(i);
				  String name=node.get("name").asText();
			      if(node.get("notable") != null) 
			        name+=" (" + node.get("notable").get("name").asText() + ")";
				  lan.add(name);
			      f.addOption(name);
			}
			return f;
		}
		
		@OnAlias("topicselected")
		public void topicSelected(TeamchatAPI api)
		{
			
			Reply rpl=api.context().currentReply();
			Integer choice=lan.indexOf(rpl.getField("choice"));
			try {
				api.perform(api.context().currentRoom().post( new TextChatlet(sbt.searchByTopic(choice.toString()))));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
// Subscribe channel
		
		@OnKeyword("subscribechannel")
		public void subscribeChannel(TeamchatAPI api) {
			api.perform(
					api.context().currentRoom().post(
					new PrimaryChatlet()
				
					.setQuestion("Enter the ChannelId or ChannelName")
					.setReplyScreen(api.objects().form()
					.addField(api.objects().input().label("ChannelId").name("channelId"))
					.addField(api.objects().input().label("(or) ChannelName").name("keyword"))
					)
					.alias("aliassubscrib")
					));	
		}
		
		@OnAlias("aliassubscrib")
		public void aliasSubscrib(TeamchatAPI api)
		{
			
			Reply rpl=api.context().currentReply();
			String chKeyword=rpl.getField("keyword");
			if(chKeyword.isEmpty())
			{
				String channelId=rpl.getField("channelId");
				AddSubscription sub=new AddSubscription();
				try {
						api.perform(api.context().currentRoom().post( new TextChatlet(sub.subscribe(channelId))));
				} catch (Exception e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				SearchChannel sc=new SearchChannel();
				try {
					String result=sc.channelSearch(apikey,chKeyword);
					if(!sc.cTitle.isEmpty())
					{
						chTitle=sc.cTitle;
						chId=sc.cId;
						api.perform(api.context().currentRoom().post(
								new PrimaryChatlet()
								.setQuestion("Choose the Channel")
								.setReplyScreen(api.objects().form().addField(channelField(api,chTitle)))
								.alias("channelselected"))
								);
					}
					else
					{
						api.perform(api.context().currentRoom().post( new TextChatlet(result)));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		public Field channelField(TeamchatAPI api,ArrayList<String> chtitle)
		{
			Field f=api.objects().select().label("Channel").name("choice");
			for(int i=0;i<chtitle.size();i++)
			{
			      f.addOption(chtitle.get(i));
			}
			return f;
		}
		
		@OnAlias("channelselected")
		public void channelSelected(TeamchatAPI api)
		{
			
			Reply rpl=api.context().currentReply();
			String chChoice=URLDecoder.decode(rpl.getField("choice"));
			String channelId=chId.get(chTitle.indexOf(chChoice));
			AddSubscription sub=new AddSubscription();
				try {
						api.perform(api.context().currentRoom().post( new TextChatlet(sub.subscribe(channelId))));
				} catch (Exception e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
// Uploaded video
		
				@OnKeyword("myuploads")
				public void myUploads(TeamchatAPI api) {
					MyUploads mu=new MyUploads();
					try {
						api.perform(api.context().currentRoom().post( new TextChatlet(mu.myUpload())));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}				
// Creating Playlist
				
				@OnKeyword("createplaylist")
				public void createPlaylist(TeamchatAPI api) {
					
					api.perform(
							api.context().currentRoom().post(
							new PrimaryChatlet()
						
							.setQuestion("Enter the Playlist Details ")
							.setReplyScreen(api.objects().form()
							.addField(api.objects().input().label("Playlist Title").name("pltitle"))
							.addField(api.objects().input().label("Playlist Description").name("pldes"))
							.addField(api.objects().input().label("No. of Videos to be added").name("vno"))
							)
							.alias("videolist")
							));	
				}
				
				@OnAlias("videolist")
				public void videolist(TeamchatAPI api)
				{
					
					Reply rpl=api.context().currentReply();
					pltitle=rpl.getField("pltitle");
					pldes=rpl.getField("pldes");
					vno=Integer.parseInt(rpl.getField("vno"));
					PlaylistUpdates plu=new PlaylistUpdates();
					try {
						api.perform(
								api.context().currentRoom().post(
										new PrimaryChatlet()
										.setQuestion("Enter the Details")
										.setReplyScreen(ninputField(api))
										.alias("playlist")));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				public Form ninputField(TeamchatAPI api)
				{
					Form f=api.objects().form();
					for(int i=0;i<vno;i++)
					{
					    f.addField(api.objects().input().label("Video"+(i+1)+" ID").name("v"+(i+1)+"id"));
					}
					return f;
				}
				
				@OnAlias("playlist")
				public void playlist(TeamchatAPI api)
				{
					String[] vid=new String[vno];
					Reply rpl=api.context().currentReply();
					for(int i=0;i<vno;i++)
					{
						vid[i]=rpl.getField("v"+(i+1)+"id");
					}
					PlaylistUpdates plu=new PlaylistUpdates();
					try {
						api.perform(api.context().currentRoom().post( new TextChatlet(plu.updatePlaylist(pltitle, pldes,vno, vid))));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				
// Channel Bulletin
				
				@OnKeyword("channelbulletin")
				public void channelBulletin(TeamchatAPI api) {
					api.perform(
							api.context().currentRoom().post(
							new PrimaryChatlet()
						
							.setQuestion("Enter the Details")
							.setReplyScreen(api.objects().form()
							.addField(api.objects().input().label("Video ID ").name("videoid"))
							.addField(api.objects().input().label("Description ").name("description"))
							)
							.alias("getvideoid")
							));	
				}
				
				@OnAlias("getvideoid")
				public void getVideoId(TeamchatAPI api)
				{
					
					Reply rpl=api.context().currentReply();
					String videoid=rpl.getField("videoid");
					String description=rpl.getField("description");
					ChannelBulletin cb=new ChannelBulletin();
					try {
						api.perform(api.context().currentRoom().post( new TextChatlet(cb.postvideo(videoid,description))));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
// Uploading Video
				
				@OnKeyword("uploadvideo")
				public void uploadVideo(TeamchatAPI api) {
					api.perform(
							api.context().currentRoom().post(
							new PrimaryChatlet()
						
							.setQuestion("Enter the Details")
							.setReplyScreen(api.objects().form()
							.addField(api.objects().input().label("Location (example : /home/user/video/samplevideo.mp4) ").name("location"))
							.addField(api.objects().input().label("Video Title ").name("vtitle"))
							.addField(api.objects().input().label("Description ").name("vdescription"))
							.addField(api.objects().input().label("Tags(separated by \"-\")").name("vtags"))
							)
							.alias("getlocation")
							));	
				}
				
				@OnAlias("getlocation")
				public void getLocation(TeamchatAPI api)
				{
					
					Reply rpl=api.context().currentReply();
					String location=rpl.getField("location");
					String vtitle=rpl.getField("vtitle");
					String vdescription=rpl.getField("vdescription");
					String[] vtags=rpl.getField("vtags").split("-");
					UploadVideo uv=new UploadVideo();
					try {
						api.perform(api.context().currentRoom().post( new TextChatlet(uv.uploadVideo(location,vtitle,vdescription,vtags))));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
		
}
