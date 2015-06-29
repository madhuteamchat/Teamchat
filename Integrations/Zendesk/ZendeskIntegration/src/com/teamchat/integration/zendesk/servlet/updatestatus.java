package com.teamchat.integration.zendesk.servlet;

import java.io.FileInputStream;
import java.util.Properties;

import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class updatestatus {

	String ticketId, status;

	public updatestatus(String ticketId, String status) throws Exception {
		this.ticketId = ticketId;
		this.status = status;

	}

	public void update()throws Exception
	{
		 DBHandler datacng = new DBHandler();
		 
		datacng.stmt.executeUpdate("UPDATE " + datacng.configProps.getProperty("dbname").trim() +"."+datacng.configProps.getProperty("tablename").trim() + " SET TktStatus ='"+ status +"'"+" where ticketId='" + ticketId + "'");
	   	
	
		String path =
				 "/home/anuj-intern22/Desktop/gup/eclipse/ZendeskBot/data/zendesk-config.properties";
				 Properties configProps = new Properties();
				
				 try {
				 configProps = loadPropertyFileFromDisk(path);
				 } catch (Exception e1) {
				 e1.printStackTrace();
				 }
				
				 String[] roomForm = datacng.getRoomForm(ticketId);
				 
				 TeamchatAPI api = TeamchatAPI.fromFile("teamchat45.data")
						 .setEmail(configProps.getProperty("botemail").trim()).setPassword(configProps.getProperty("botpass").trim()).setEndPoint(configProps.getProperty("endpoint").trim());
				
				 Room r = api.context().byId(roomForm[0]);
				 		
				 Form fo1=api.objects().form();
				 
				 api.perform(r.post(new PrimaryChatlet().setQuestionHtml("<img src=\"http://cdn.mysitemyway.com/etc-mysitemyway/icons/legacy-previews/icons/matte-blue-and-white-square-icons-symbols-shapes/118265-matte-blue-and-white-square-icon-symbols-shapes-smiley-face1.png\" height=\"200\" width=\"200\">"+"<h4><b>Ticket: <a1 style=\"color:#359FD8\";>"+ ticketId +"</a1></b></h4>"+"<br /><b>Your issue has been solved!!</b>"+"<br /><b>Thank you for your patience and cooperation.</b>"+"<br /><b>We value your opinion and welcome your feedback.Type \"tcfeed\" for giving your feedback.</b>")
							.setReplyScreen(fo1)));
		
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
