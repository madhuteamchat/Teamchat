package com.teamchat.integration.zendesk.servlet;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Properties;

import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.Reply;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class Feedback {

	String ticketId;
	
	public Feedback(String ticketId)
	{
		this.ticketId = ticketId; 
		
	}
	
	public void formfeed()
	{
		String path =
				 "/home/anuj-intern22/Desktop/gup/eclipse/ZendeskBot/data/zendesk-config.properties";
				 Properties configProps = new Properties();
				
				 try {
				 configProps = loadPropertyFileFromDisk(path);
				 } catch (Exception e1) {
				 e1.printStackTrace();
				 }
				
		
		 DBHandler checkData = new DBHandler();
		 String[] roomForm = checkData.getRoomForm(ticketId);
		 
		 TeamchatAPI api = TeamchatAPI.fromFile("teamchat45.data")
				 .setEmail(configProps.getProperty("botemail").trim()).setPassword(configProps.getProperty("botpass").trim()).setEndPoint(configProps.getProperty("endpoint").trim());
		
		 Room r = api.context().byId(roomForm[0]);
		 		
		 Form fo1=api.objects().form();
		 Form fo=api.objects().form();
		 
		 fo.addField(api.objects().select().name("rating").label("Rating:")
					.addOption("good")
					.addOption("bad"));
		 
		 fo.addField(
					api.objects().input().name("rcomment").label("Comment:"));
		 		 
		 PrimaryChatlet prime = new PrimaryChatlet();
		 
		 api.perform(r.post(new PrimaryChatlet().setQuestionHtml("<img src=\"http://cdn.mysitemyway.com/etc-mysitemyway/icons/legacy-previews/icons/matte-blue-and-white-square-icons-symbols-shapes/118265-matte-blue-and-white-square-icon-symbols-shapes-smiley-face1.png\" height=\"200\" width=\"200\">"+"<br /><b>Your issue has been solved!!</b>"+"<br /><b>Thank you for your patience and cooperation.</b>")
					.setReplyScreen(fo1)).post(prime.setQuestionHtml("<h3 style=\"color:black\";><b>Give Us Feedback</b></h3>"+"<br />We value your feedback and product suggestions. While we don't respond to suggestions directly, we do review them. If you'd like to give general feedback about Teamchat support, give your feedback here.")
							.setReplyScreen(fo)
							.setReplyLabel("Enter")));
		 
		 String feedformId = prime.getFormId();
		 
		 Reply rep = api.context().chatletById(feedformId).createReply();
		 
		 String rating = rep.getField("rating");
		 String Rcomment = rep.getField("rcomment");
		 
		 System.out.println(rating);
		 System.out.println(Rcomment);
			 
		 try {
			checkData.stmt.executeUpdate("UPDATE " + checkData.configProps.getProperty("dbname").trim() +"."+checkData.configProps.getProperty("tablename").trim() + " SET rating ='"+ rating +"'"+ ",Rcomment ='"+ Rcomment +"'"+" where ticketid='" + ticketId + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 
		 
		 
	}
	
	public static Properties loadPropertyFileFromDisk(String filePath)
			throws Exception {

		Properties configProp = new Properties();
		FileInputStream fIS = new FileInputStream(filePath);
		configProp.load(fIS);
		fIS.close();
		return configProp;
	}
}
