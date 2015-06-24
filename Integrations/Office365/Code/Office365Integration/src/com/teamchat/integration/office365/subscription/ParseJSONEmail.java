package com.teamchat.integration.office365.subscription;

import org.json.JSONObject;

import com.teamchat.integration.office365.bot.TeamchatURLlink;

public class ParseJSONEmail {
  
	
	String createNotification(String JSONString, String offid){
		System.out.println("About to parse ");
	  String message="";
	  JSONObject obj=new JSONObject(JSONString);
	  if(!obj.has("DateTimeCreated"))
	  {
		  return message;
	  }
	  String datecreated=obj.getString("DateTimeCreated");
	  datecreated=formatTime(datecreated);
	  String subject=obj.getString("Subject");
	  JSONObject senderobj=obj.getJSONObject("Sender");
	  JSONObject emailobj=senderobj.getJSONObject("EmailAddress");
	  String weblink=obj.getString("WebLink");
	  String teamchatlink=new TeamchatURLlink().createEmbeddedLink(weblink, "Office365", "http");
	  String email=emailobj.getString("Address");
	  if(email.equals(offid)) {
		  return message;
	  }
	  message="<!DOCTYPE html><html><head></head><body>";
	  message+="<div style='' > "
				+ "<center> "
				+ "<div style=' margin-top:5px; color :#159ceb;font-family: monospace;'>";
	  message+="<div style='padding:2px;font-weight:bold'>";
	  message+="You have a mail from "+email+" at "+datecreated+"</div>";
	  message+="<div style='padding:2px;font-weight:bold'>";
	  message+="Subject:"+subject+"</div>";
	  message+= "<a href='"+teamchatlink+"' target='_blank' style='text-decoration: none;'>"
				+ "<div style='background-color:rgba(21, 156, 235, 0.49);padding:2px;color :white;'>Click here to view event</div>"
			+ "</a>"
					+ "</div>"
				+ "</center>"
			+ "</div>";
		System.out.println("=============================MailJson");
	  return message+"</body></html>";
  }
	
	String formatTime(String time){
        String timeformat="";
        String timearray[]=time.split("T");
        timeformat+=timearray[1].split("Z")[0]+"GMT";   
        timeformat+=" "+timearray[0];
        return timeformat;
    }
  
  String deleteNotification(String JSONString){
	  String message="<!DOCTYPE html><html><head></head><body>";
	 message+="Message deleted";
	  return message+"</body></html>";
  }
  
  String updateNotification(String JSONString){
	  String message="";
	  JSONObject obj=new JSONObject(JSONString);
	  String datecreated=obj.getString("DateTimeCreated");
	  String subject=obj.getString("Subject");
	  JSONObject senderobj=obj.getJSONObject("Sender");
	  JSONObject emailobj=senderobj.getJSONObject("EmailAddress");
	  String email=emailobj.getString("Address");
	  message="<!DOCTYPE html><html><head></head><body>";
	  message+="You have a mail from "+email+" at "+datecreated+"<br/>";
	  message+="Subject:"+subject;
	  return message+"</body></html>";
  }
}