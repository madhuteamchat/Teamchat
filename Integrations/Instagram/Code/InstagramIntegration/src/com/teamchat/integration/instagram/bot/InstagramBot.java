package com.teamchat.integration.instagram.bot;

import java.net.URLDecoder;
import java.util.ArrayList;

import org.json.JSONException;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.Reply;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.integration.instagram.database.InstaGeography;
import com.teamchat.integration.instagram.database.InstaLocation;
import com.teamchat.integration.instagram.database.InstaSubDB;
import com.teamchat.integration.instagram.database.JDBCConnection;
import com.teamchat.integration.instagram.notification.InstagramURLlink;
import com.teamchat.integration.instagram.notification.Subscribe;
import com.teamchat.integration.instagram.properties.InstagramProperty;
import com.teamchat.integration.instagram.search.GMapAPI;
import com.teamchat.integration.instagram.search.Location;

public class InstagramBot {
	
	ArrayList<String> sidlist=new ArrayList<String>();
	ArrayList<String> oidlist=new ArrayList<String>();
	ArrayList<String> objlist=new ArrayList<String>();
	ArrayList<String> loclist=new ArrayList<String>();
	ArrayList<String> locidlist=new ArrayList<String>();
	ArrayList<String> latlist=new ArrayList<String>();
	ArrayList<String> lnglist=new ArrayList<String>();
	ArrayList<String> placelist=new ArrayList<String>();
	String servlet_url;
	
	public InstagramBot() {
		// TODO Auto-generated constructor stub
		InstagramProperty ip=new InstagramProperty();
		ip.loadParams();
		servlet_url=ip.getServletUrl();
	}

	// Help
//	@OnKeyword("help")
//	public void instagramhelp(TeamchatAPI api) {
//		instagramHelp(api);
//	}
		@OnKeyword("Help")
		public void instagramHelp(TeamchatAPI api) {
			TeamchatPost.tpapi=api;
			api.perform(api.context().currentRoom().post(new PrimaryChatlet()
			.setQuestionHtml("<div align=\"center\"><strong>Instagram Help</strong></div>"
			+"Hey! I am <b>Instagram bot</b>. You can get notifications whenever an image is uploaded "
			+"on Instagram with specific tag or from a specific location or geographical region you subscribed."
			+"<br>Type <b>Connect</b> "
			+ "to connect to your Instagram account."
			+"<br>Type <b>Disconnect</b> to disconnect your Instagram account"
			+"<br>Type <b>Sublist</b> to get your subscription list"
			+"<br>Type <b>Subscribe</b> to get notification"
			+"<br>Type <b>Unsubscribe</b> to stop the notification"
			)));
			
		}

	// Instagram Connect
		
//		@OnKeyword("connect")
//		public void connectinstagram(TeamchatAPI api) throws JSONException {
//			connectInstagram(api);
//		}
		
			@OnKeyword("Connect")
			public void connectInstagram(TeamchatAPI api) throws JSONException {
				String sname=api.context().currentSender().getEmail();
				JDBCConnection db=new JDBCConnection();
				String gc=db.retreive(sname);
				TeamchatPost.tpapi=api;
				if(gc.equals("access_token"))
				{
					String login_href=servlet_url+"?name="+sname;
					InstagramURLlink urlLink=new InstagramURLlink();
					String tc_href=urlLink.createEmbeddedLink(login_href, "Instagram_Bot", "http");
					api.perform(api.context().currentRoom().post(new PrimaryChatlet()
					.setQuestionHtml("<a href=\""+tc_href+"\" target=\"_blank\"> Click here to login Instagram</a>")));
				}
				else
					api.perform(api.context().currentRoom().post( new PrimaryChatlet()
					.setQuestionHtml("You are already connected with Instagram.")));
			}
			
	// Instagram Disconnect
//			@OnKeyword("disconnect")
//			public void disconnectinstagram(TeamchatAPI api) {
//				disconnectInstagram(api);
//			}
			
				@OnKeyword("Disconnect")
				public void disconnectInstagram(TeamchatAPI api) {
//					TeamchatPost.tpapi=api;
					String sname=api.context().currentSender().getEmail();
					 try { 			    	    
			    	JDBCConnection db=new JDBCConnection();
					String gc=db.retreive(sname);
					if(!gc.equals("access_token"))
						db.delete(sname);
			    	        
			    }
			    catch(Exception e)
			    {
			    	e.printStackTrace();
			    }
					api.perform(api.context().currentRoom().post( new PrimaryChatlet()
					.setQuestionHtml("Successfully, Disconnected from your Instagram account.")));
					
				}
		

//				@OnKeyword("subscribe")
//				public void ssubscribe(TeamchatAPI api) throws JSONException {
//					subscribe(api);
//				}
		
		@OnKeyword("Subscribe")
		public void subscribe(TeamchatAPI api) throws JSONException {
			TeamchatPost.tpapi=api;
			String sname=api.context().currentSender().getEmail();
			JDBCConnection db=new JDBCConnection();
			String gc=db.retreive(sname);
			if(gc.equals("access_token"))
			{
				String login_href=servlet_url+"?name="+sname;
				InstagramURLlink urlLink=new InstagramURLlink();
				String tc_href=urlLink.createEmbeddedLink(login_href, "Instagram_Bot", "http");
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml("You have to <a href=\""+tc_href+"\" target=\"_blank\">login</a>")));
			}
			else
			{
				api.perform(
						api.context().currentRoom().post(
								new PrimaryChatlet()
				
					.setQuestion("Subscription Type")
					.setReplyScreen(api.objects().form()
					.addField(api.objects().select().label("Subscribe by").name("substype")
							.addOption("Tags")
							.addOption("Location")
							.addOption("GeographicRegion")
							)
					)
					.alias("aliassubstype")
					));	
			}
		}
		
		@OnAlias("aliassubstype")
		public void aliassubstype(TeamchatAPI api)
		{
			Reply rpl=api.context().currentReply();
			if(rpl.getField("substype").equals("Tags"))
			{
				api.perform(
						api.context().currentRoom().post(
						new PrimaryChatlet()
					
						.setQuestion("Enter the Tag for which you want to subscribe")
						.setReplyScreen(api.objects().form()
						.addField(api.objects().input().label("Tag").name("oid"))
						)
						.alias("aliassubstag")
						));	
			}
			else if(rpl.getField("substype").equals("Location"))
			{
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
							.setQuestionHtml("Enter the Place to search")
							.setReplyScreen(api.objects().form()
									.addField(api.objects().input().label("Address").name("place"))
							)
							.alias("aliaslocplace")));
				/*api.perform(
						api.context().currentRoom().post(
						new PrimaryChatlet()
					
						.setQuestion("Enter the Locaton details for which you want to subscribe")
						.setReplyScreen(api.objects().form()
						.addField(api.objects().input().label("Latitude").name("lat"))
						.addField(api.objects().input().label("Longitude").name("lng"))
						.addField(api.objects().input().label("Radius in meters(max 5000)").name("radius"))
						)
						.alias("aliassubslocation")
						));	*/
			}
			else if(rpl.getField("substype").equals("GeographicRegion"))
			{
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml("Enter the Place to search")
				.setReplyScreen(api.objects().form()
						.addField(api.objects().input().label("Address").name("place"))
				)
				.alias("aliasgeoplace")));
				/*api.perform(
						api.context().currentRoom().post(
						new PrimaryChatlet()
					
						.setQuestion("Enter the Locaton details for which you want to subscribe")
						.setReplyScreen(api.objects().form()
						.addField(api.objects().input().label("Latitude").name("lat"))
						.addField(api.objects().input().label("Longitude").name("lng"))
						.addField(api.objects().input().label("Radius").name("radius"))
						)
						.alias("aliassubsgeo")
						));	*/
			}
		}
		
		@OnAlias("aliassubstag")
		public void aliassubstag(TeamchatAPI api)
		{
			String oid=api.context().currentReply().getField("oid");
			if(oid.matches("^[a-zA-Z0-9_]*$"))
			{
//				System.out.println("valid");
				oid=oid.toLowerCase();
				String id=api.context().currentReply().senderEmail();
				InstaSubDB isdb=new InstaSubDB();
				if(isdb.inInstaSubsDB(id,"tag", oid))
				{
					Subscribe s=new Subscribe();
					api.perform(api.context().currentRoom().post(new PrimaryChatlet()
								.setQuestionHtml(s.subscribeTags(oid, id))));
				}
				else
				{
					api.perform(api.context().currentRoom().post(new PrimaryChatlet()
					.setQuestionHtml("Successfully Subscribed")));
				}
			}
			else
			{
				System.out.println("not valid");
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml("<strong>Please enter a valid tag to subscribe.</strong> "
									+ "(only <b><i>alphanumeric (a-z,A-Z,0-9)</i></b> and <b><i>underscore (\"_\")</i></b> are allowed)")));
			}
		}
		
		
		@OnAlias("aliaslocplace")
		public void aliaslocplace(TeamchatAPI api)
		{
			String place=api.context().currentReply().getField("place");
			place=place.replace(" ", "+");
			GMapAPI g=new GMapAPI();
			String result=g.searchlocation(place);
			if(result.equals("error"))
			{
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
							.setQuestionHtml("Try Again.")));
			}
			else if(result.equals("success"))
			{
				System.out.println(result);
				latlist=g.getlatlist();
				lnglist=g.getlnglist();
				placelist=g.getplacelist();
				if(placelist.size()>0)
				{
					api.perform(api.context().currentRoom().post(new PrimaryChatlet()
					.setQuestionHtml("Select from the list")
					.setReplyScreen(api.objects().form()
						.addField(loclatlngField(api)))
						.alias("aliasloclatlng")
						));
				}
				else
				{
					api.perform(api.context().currentRoom().post(new PrimaryChatlet()
					.setQuestionHtml("Enter a valid place name")));
				}
			}
			else
			{
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml(result)));
			}
		}
		
		public Field loclatlngField(TeamchatAPI api)
		{
			Field f=api.objects().select().label("Select").name("choice");
			for(int i=0;i<placelist.size();i++)
			{
//			      System.out.println("list: "+placelist.get(i));
			      f.addOption(placelist.get(i));
			}
			return f;
		}
		
	/*	@OnAlias("aliasloclatlng")
		public void aliasloclatlng(TeamchatAPI api)
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
				String choice=URLDecoder.decode(rpl.getField("choice"));
				String lat=latlist.get(placelist.indexOf(choice));
				String lng=lnglist.get(placelist.indexOf(choice));
				System.out.println("lat="+lat+" lng"+lng);
			}
		}*/
		
		
//		@OnAlias("aliassubslocation")
		@OnAlias("aliasloclatlng")
		public void aliassubslocation(TeamchatAPI api)
		{
			Reply rpl=api.context().currentReply();
			String ch="",lat="",lng="",radius="5000";
			ch=rpl.getField("choice");
			if(ch.length()<1)
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml("Try Again")));
			else
			{
				String choice=URLDecoder.decode(rpl.getField("choice"));
				lat=latlist.get(placelist.indexOf(choice));
				lng=lnglist.get(placelist.indexOf(choice));
//				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@lat="+lat+" lng="+lng);
				String id=api.context().currentReply().senderEmail();
				Location l=new Location();
				String result=l.searchlocation(id, lat, lng, radius);
				if(result.equals("error"))
				{
					api.perform(api.context().currentRoom().post(new PrimaryChatlet()
							.setQuestionHtml("Try Again.")));
				}
				else
				{
					locidlist=l.getlocidlist();
					loclist=l.getloclist();
					api.perform(api.context().currentRoom().post(new PrimaryChatlet()
							.setQuestionHtml("Select the location from the list to subscribe")
							.setReplyScreen(api.objects().form()
									.addField(searchlocField(api)))
									.alias("aliaslocsearch")
							));
				}
			}			
		}
		
		public Field searchlocField(TeamchatAPI api)
		{
			Field f=api.objects().select().label("Select").name("choice");
			for(int i=0;i<locidlist.size();i++)
			{
			      f.addOption(loclist.get(i));
//			      System.out.println("%%%%%%"+loclist.get(i));
			}
			return f;
		}
		
		@OnAlias("aliaslocsearch")
		public void aliaslocsearch(TeamchatAPI api)
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
				String choice=URLDecoder.decode(rpl.getField("choice"));
				String locid=locidlist.get(loclist.indexOf(choice));
				InstaSubDB isdb=new InstaSubDB();
				if(isdb.inInstaSubsDB(sname,"location", locid))
				{
					Subscribe s=new Subscribe();
					api.perform(api.context().currentRoom().post(new PrimaryChatlet()
								.setQuestionHtml(s.subscribeLocation(locid, sname, choice))));
				}
				else
				{
					api.perform(api.context().currentRoom().post(new PrimaryChatlet()
					.setQuestionHtml("Successfully Subscribed")));
				}
			}
		}
		
		
		
		
		@OnAlias("aliasgeoplace")
		public void aliasgeoplace(TeamchatAPI api)
		{
			String place=api.context().currentReply().getField("place");
			place=place.replace(" ", "+");
			GMapAPI g=new GMapAPI();
			String result=g.searchlocation(place);
			if(result.equals("error"))
			{
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
							.setQuestionHtml("Try Again.")));
			}
			else if(result.equals("success"))
			{
				System.out.println(result);
				latlist=g.getlatlist();
				lnglist=g.getlnglist();
				placelist=g.getplacelist();
				if(placelist.size()>0)
				{
					
					api.perform(api.context().currentRoom().post(new PrimaryChatlet()
						.setQuestionHtml("Select from the list")
						.setReplyScreen(api.objects().form()
								.addField(geolatlngField(api)))
						.alias("aliasgeolatlng")
						));
				}
				else
				{
					api.perform(api.context().currentRoom().post(new PrimaryChatlet()
					.setQuestionHtml("Enter a valid place name")));
				}
			}
			else
			{
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml(result)));
			}
		}
		
		public Field geolatlngField(TeamchatAPI api)
		{
			Field f=api.objects().select().label("Select").name("choice");
			for(int i=0;i<placelist.size();i++)
			{
//			      System.out.println("list: "+placelist.get(i));
			      f.addOption(placelist.get(i));
			}
			return f;
		}
		
	/*	@OnAlias("aliasgeolatlng")
		public void aliasgeolatlng(TeamchatAPI api)
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
				String choice=URLDecoder.decode(rpl.getField("choice"));
				String lat=latlist.get(placelist.indexOf(choice));
				String lng=lnglist.get(placelist.indexOf(choice));
				System.out.println("lat="+lat+" lng"+lng);
			}
		}*/
				
		
		
//		@OnAlias("aliassubsgeo")
		@OnAlias("aliasgeolatlng")
		public void aliassubsgeo(TeamchatAPI api)
		{
			Reply rpl=api.context().currentReply();
			String ch="",lat="",lng="",radius="5000";
			ch=rpl.getField("choice");
			if(ch.length()<1)
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml("Try Again")));
			else
			{
				String choice=URLDecoder.decode(rpl.getField("choice"));
				lat=latlist.get(placelist.indexOf(choice));
				lng=lnglist.get(placelist.indexOf(choice));
				String id=api.context().currentReply().senderEmail();
				Subscribe s=new Subscribe();
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
							.setQuestionHtml(s.subscribeGeo(id,lat,lng,radius,choice))));

			}
		}
		
//		@OnKeyword("sublist")
//		public void subscribelist(TeamchatAPI api) throws JSONException
//		{
//			subscribeList(api);
//		}
		
		@OnKeyword("Sublist")
		public void subscribeList(TeamchatAPI api) throws JSONException
		{
			TeamchatPost.tpapi=api;
			String sname=api.context().currentSender().getEmail();
			JDBCConnection db=new JDBCConnection();
			String gc=db.retreive(sname);
			if(gc.equals("access_token"))
			{
				String login_href=servlet_url+"?name="+sname;
				InstagramURLlink urlLink=new InstagramURLlink();
				String tc_href=urlLink.createEmbeddedLink(login_href, "Instagram_Bot", "http");
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml("You have to <a href=\""+tc_href+"\" target=\"_blank\">login</a>")));
			}
			else
			{
//				Subscribe s=new Subscribe();
//				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
//							.setQuestionHtml(s.getsubscribelist())));
				InstaSubDB isdb=new InstaSubDB();
				isdb.retreiveSubsList(sname);
				ArrayList<String> sidlist=isdb.sidList();
				ArrayList<String> oidlist=isdb.oidList();
				ArrayList<String> objlist=isdb.objList();
				String msg="";
				for(int i=0;i<sidlist.size();i++)
				{
					if(objlist.get(i).equals("location"))
					{
						InstaLocation il=new InstaLocation();
						String locname=il.retreive(oidlist.get(i));
//						msg+=objlist.get(i)+"-"+oidlist.get(i)+"-"+locname+"<br>";
						msg+=objlist.get(i)+"-"+locname+"<br>";
					}
					else if(objlist.get(i).equals("geography"))
					{
						InstaGeography ig=new InstaGeography();
						String geoname=ig.retreive(oidlist.get(i));
//						msg+=objlist.get(i)+"-"+oidlist.get(i)+"-"+geoname+"<br>";
						msg+=objlist.get(i)+"-"+geoname+"<br>";
					}
					else
						msg+=objlist.get(i)+"-"+oidlist.get(i)+"<br>";
				}
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml(msg)));
			}
		}
		
//		@OnKeyword("unsubscribe")
//		public void unssubscribe(TeamchatAPI api) throws JSONException
//		{
//			unsubscribe(api);
//		}
		
		@OnKeyword("Unsubscribe")
		public void unsubscribe(TeamchatAPI api) throws JSONException
		{
//			TeamchatPost.tpapi=api;
			String sname=api.context().currentSender().getEmail();
			JDBCConnection db=new JDBCConnection();
			String gc=db.retreive(sname);
			if(gc.equals("access_token"))
			{
				String login_href=servlet_url+"?name="+sname;
				InstagramURLlink urlLink=new InstagramURLlink();
				String tc_href=urlLink.createEmbeddedLink(login_href, "Instagram_Bot", "http");
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml("You have to <a href=\""+tc_href+"\" target=\"_blank\">login</a>")));
			}
			else
			{
				InstaSubDB isdb=new InstaSubDB();
				isdb.retreiveSubsList(sname);
				sidlist=isdb.sidList();
				oidlist=isdb.oidList();
				objlist=isdb.objList();
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
				.setQuestionHtml("Select from the list")
				.setReplyScreen(api.objects().form()
						.addField(unsubField(api)))
				.alias("aliasunsubb")
						));
			}
		}
		
		public Field unsubField(TeamchatAPI api)
		{
			Field f=api.objects().select().label("Select").name("choice");
			for(int i=0;i<sidlist.size();i++)
			{
				if(objlist.get(i).equals("location"))
				{
					InstaLocation il=new InstaLocation();
					String locname=il.retreive(oidlist.get(i));
//					locname=locname.replace('-', ' ');         Assuming locname won't have "-"
					f.addOption(locname+"-"+oidlist.get(i));
				}
				else if(objlist.get(i).equals("geography"))
				{
					InstaGeography ig=new InstaGeography();
					String geoname=ig.retreive(oidlist.get(i));
//					geoname=geoname.replace('-', ' ');        Assuming geoname won't have "-"
					f.addOption(geoname+"-"+oidlist.get(i));
				}
				else
			      f.addOption(objlist.get(i)+"-"+oidlist.get(i));
			}
			return f;
		}
		
		@OnAlias("aliasunsubb")
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
				String choice=URLDecoder.decode(rpl.getField("choice"));
				String[] split=choice.split("-");
				String sid=sidlist.get(oidlist.indexOf(split[1]));
				InstaSubDB isdb=new InstaSubDB();
				isdb.delete(sname, sid);
				api.perform(api.context().currentRoom().post(new PrimaryChatlet()
						.setQuestionHtml("Successfully Unsubscribed")));
				
			}
		}
	
}
