package com.teamchat.integration.zendesk.servlet;

import java.io.FileInputStream;
import java.util.Properties;

import com.teamchat.client.sdk.Chatlet;
import com.teamchat.client.sdk.Reply;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;

public class updatecomment {

	String comment,ticketId;
	
	public updatecomment(String comment,String ticketId) throws Exception
	{
		this.comment=comment;
		this.ticketId=ticketId;
		
	}
	
	public void updatecommentzendesk()throws Exception
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
		
		

		// System.out.println(roomForm);
		
		 TeamchatAPI api = TeamchatAPI.fromFile("teamchat99.data")
		 .setEmail(configProps.getProperty("botemail").trim()).setPassword(configProps.getProperty("botpass").trim()).setEndPoint(configProps.getProperty("endpoint").trim());
		
		 Room r = api.context().byId(roomForm[0]);
		 Chatlet c = api.context().chatletById(roomForm[1]);
		 Reply rep = c.createReply().addField("upCom", comment);
		
		 System.out.println(rep);
		
		 api.perform(r.reply(rep));
		
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
