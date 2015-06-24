package com.teamchat.integration.youtube.bot;


import java.net.URLDecoder;
import java.util.ArrayList;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.json.JSONException;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.Reply;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.integration.youtube.channelbulletin.ChannelBulletin;
import com.teamchat.integration.youtube.connect.YoutubeConnect;
import com.teamchat.integration.youtube.database.JDBCConnection;
import com.teamchat.integration.youtube.playlist.PlaylistUpdates;
import com.teamchat.integration.youtube.properties.YoutubeProperty;
import com.teamchat.integration.youtube.search.SearchByKeyword;
import com.teamchat.integration.youtube.search.SearchByTopic;
import com.teamchat.integration.youtube.search.SearchChannel;
import com.teamchat.integration.youtube.servlet.TeamchatPost;
import com.teamchat.integration.youtube.subscribe.AddSubscription;
import com.teamchat.integration.youtube.upload.MyUploads;

public class YouTubeBot {
	
	
	SearchByTopic sbt=null;
	ArrayList<String> lan;
	String apikey,client_id,client_secret,servlet_url,upload_url;
	
	String pltitle,pldes;
	Integer vno;
	
	public YouTubeBot()
	{
		YoutubeProperty yp=new YoutubeProperty();
		yp.loadParams();
		apikey=yp.getApiKey();
		client_id=yp.getClientId();
		client_secret=yp.getClientSecret();
		servlet_url=yp.getServletUrl();
		upload_url=yp.getUploadUrl();
	}

	@OnKeyword("Help")
	public void youtubeHelp(TeamchatAPI api) {
		
		api.perform(api.context().currentRoom().post(new PrimaryChatlet()
		.setQuestionHtml("<div align=\"center\">Youtube Help</div>"
		+"Hey! I am <b>YouTube bot</b>. You can upload video, search videos, subscribe channels, create playlist and post videos in your channel."
		+"<br>Type <b>Connect</b> "
		+ "to connect to your Youtube account."
		+"<br>Type <b>Disconnect</b> to sign out your youtube account"
		+"<br>Type <b>searchbykeyword</b> to search videos related to the keyword"
		+"<br>Type <b>searchbytopic</b> to search videos under a category"
		+"<br>Type <b>searchchannel</b> to search channel related to the keyword"
		+"<br>Type <b>subscribechannel</b> to subscribe a channel using channel id"
		+"<br>Type <b>channelbulletin</b> to post a video in your channel by providing video id"
		+"<br>Type <b>createplaylist</b> to ceate a playlist by providing video id"
		+"<br>Type <b>upload</b> to upload a video into your channel by providing the location"
		+"<br>Type <b>myuploads</b> to reterive the details of the video uploaded by you")));
		
	}
	
		@OnKeyword("Connect")
		public void Connect(TeamchatAPI api) throws JSONException {
			String sname=api.context().currentSender().getEmail();
			JDBCConnection db=new JDBCConnection();
			String[] gc=db.retreive(sname);
			TeamchatPost.tpapi=api;
			String rid=api.context().currentRoom().getId();
			if(gc[1].equals("refresh_token"))
			{
				TeamchatURLlink urlLink = new TeamchatURLlink();
				String url=servlet_url+"?rid="+rid+"&client_id="+client_id+"&client_secret="+client_secret+"&name="+sname;
				String urlEncoded = urlLink.createEmbeddedLink(url,"Youtube Bot","http");
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml("<a href=\""+urlEncoded+"\" target=\"_blank\"> Click here to login Youtube</a>")));
			}
			else
				api.perform(api.context().currentRoom().post( new PrimaryChatlet()
				.setQuestionHtml("You are already connected with Youtube.")));
		}
		
			@OnKeyword("disconnect")
			public void disconnect(TeamchatAPI api) {
				String sname=api.context().currentSender().getEmail();
				
				YoutubeConnect ytc=new YoutubeConnect();
				ytc.youtubeLogout(sname);
				api.perform(api.context().currentRoom().post( new PrimaryChatlet()
				.setQuestionHtml("Successfully, Disconnected from your Youtube account.")));
				
			}
	

	
	@OnKeyword("searchbykeyword")
	public void searchbykeyword(TeamchatAPI api) {
		api.perform(api.context().currentRoom().post(new PrimaryChatlet()			
				.setQuestionHtml("Enter the Keyword to Search")
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
			api.perform(api.context().currentRoom().post( new PrimaryChatlet()
			.setQuestionHtml(sbk.searchByKeyword(apikey,keyword,Integer.parseInt(maxresult)))));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

	
		@OnKeyword("searchchannel")
		public void searchChannel(TeamchatAPI api) {
			api.perform(
					api.context().currentRoom().post(
					new PrimaryChatlet()				
					.setQuestionHtml("Enter the Keyword to Search")
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
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml(sc.channelSearch(apikey,cname))));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	

		
		@OnKeyword("searchbytopic")
		public void searchbytopic(TeamchatAPI api) {
			api.perform(
					api.context().currentRoom().post(
					new PrimaryChatlet()				
					.setQuestionHtml("Enter the Topic")
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
					api.perform(api.context().currentRoom().post( new PrimaryChatlet()
					.setQuestionHtml(sbt.checkNode)));
				}
				else
				{
					api.perform(api.context().currentRoom().post(
						new PrimaryChatlet()
						.setQuestionHtml("Choose the Topic")
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
			String ch="";
			ch=rpl.getField("choice");
			if(ch.length()<1)
				api.perform(api.context().currentRoom().post( new PrimaryChatlet()
				.setQuestionHtml("Try Again")));
			else
			{
				Integer choice=lan.indexOf(ch);
				try {
					api.perform(api.context().currentRoom().post( new PrimaryChatlet()
					.setQuestionHtml(sbt.searchByTopic(choice.toString()))));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		
		@OnKeyword("subscribechannel")
		public void subscribeChannel(TeamchatAPI api) {
			api.perform(api.context().currentRoom().post(new PrimaryChatlet()				
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
			String chKeyword="";
			chKeyword=rpl.getField("keyword");
			if(chKeyword.isEmpty())
			{
				String channelId=rpl.getField("channelId");
				String sname=rpl.senderEmail();
				AddSubscription sub=new AddSubscription(client_id,client_secret);
				try {
						api.perform(api.context().currentRoom().post( new PrimaryChatlet()
						.setQuestionHtml(sub.subscribe(channelId,sname))));
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
						ArrayList<String> chTitle=sc.cTitle;
						ArrayList<String> chId=sc.cId;
						api.perform(api.context().currentRoom().post(
								new PrimaryChatlet()
								.setQuestion("Choose the Channel")
								.setReplyScreen(api.objects().form().addField(channelField(api,chTitle,chId)))
								.alias("channelselected"))
								);
					}
					else
					{
						api.perform(api.context().currentRoom().post( new PrimaryChatlet()
						.setQuestionHtml(result)));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		public Field channelField(TeamchatAPI api,ArrayList<String> chtitle,ArrayList<String> chId)
		{
			Field f=api.objects().select().label("Channel").name("choice");
			for(int i=0;i<chtitle.size();i++)
			{
			      f.addOption(chtitle.get(i)+":"+chId.get(i));
			}
			return f;
		}
		
		@OnAlias("channelselected")
		public void channelSelected(TeamchatAPI api)
		{
			
			Reply rpl=api.context().currentReply();
			String sname=rpl.senderEmail();
			String ch="";
			ch=rpl.getField("choice");
			if(ch.length()<1)
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml("Try Again")));
			else
			{
				String chChoice=URLDecoder.decode(rpl.getField("choice"));
				String[] chspilt=chChoice.split(":");
				String channelId=chspilt[1];
				AddSubscription sub=new AddSubscription(client_id,client_secret);
				try {
						api.perform(api.context().currentRoom().post(new PrimaryChatlet()
						.setQuestionHtml(sub.subscribe(channelId,sname))));
				} catch (Exception e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
				@OnKeyword("myuploads")
				public void myUploads(TeamchatAPI api) {
					MyUploads mu=new MyUploads(client_id,client_secret);
					String sname=api.context().currentSender().getEmail();
					try {
						api.perform(api.context().currentRoom().post(new PrimaryChatlet()
						.setQuestionHtml(mu.myUpload(sname))));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}	
				
				@OnKeyword("createplaylist")
				public void createPlaylist(TeamchatAPI api) {
					
					api.perform(api.context().currentRoom().post(new PrimaryChatlet()						
							.setQuestionHtml("Enter the Playlist Details ")
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
					try {
						api.perform(
								api.context().currentRoom().post(
										new PrimaryChatlet()
										.setQuestionHtml("Enter the Details")
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
					String sname=rpl.senderEmail();
					for(int i=0;i<vno;i++)
					{
						vid[i]=rpl.getField("v"+(i+1)+"id");
					}
					PlaylistUpdates plu=new PlaylistUpdates(client_id,client_secret);
					try {
						api.perform(api.context().currentRoom().post( new PrimaryChatlet()
						.setQuestionHtml(plu.updatePlaylist(pltitle, pldes,vno, vid,sname))));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				

				
				@OnKeyword("channelbulletin")
				public void channelBulletin(TeamchatAPI api) {
					api.perform(api.context().currentRoom().post(new PrimaryChatlet()						
							.setQuestionHtml("Enter the Details")
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
					String sname=rpl.senderEmail();
					String videoid=rpl.getField("videoid");
					String description=rpl.getField("description");
					ChannelBulletin cb=new ChannelBulletin(client_id,client_secret);
					try {
						api.perform(api.context().currentRoom().post(new PrimaryChatlet()
						.setQuestionHtml(cb.postvideo(videoid,description,sname))));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}

				
				@OnKeyword("upload")
				public void uploadVideo(TeamchatAPI api) throws JSONException {

					TeamchatURLlink urlLink = new TeamchatURLlink();
					String url=upload_url;
					String urlEncoded = urlLink.createEmbeddedLink(url,"YouTube Bot","http");
					api.perform(api.context().currentRoom().post(new PrimaryChatlet()
								.setQuestionHtml("<a href=\""+urlEncoded+"\" target=\"_blank\"> Upload</a>")
								));
				}

		
}
