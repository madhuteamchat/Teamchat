package com.teamchat.integration.office365.subscription;

import org.json.JSONObject;

import com.teamchat.integration.office365.bot.TeamchatURLlink;

public class ParseJSONCalendar {
	
	String createNotification(String JSONString){
		  String message="";
		  JSONObject obj=new JSONObject(JSONString);
		  if(!obj.has("DateTimeCreated"))
		  {
			  return message;
		  }
		  String datecreated=obj.getString("DateTimeCreated");
		  datecreated=formatTime(datecreated);
		  String subject=obj.getString("Subject");
		  String link=obj.getString("WebLink");
		  String teamchatlink=new TeamchatURLlink().createEmbeddedLink(link, "Office365", "http");
		  JSONObject senderobj=obj.getJSONObject("Organizer");
		  JSONObject emailobj=senderobj.getJSONObject("EmailAddress");
		  String email=emailobj.getString("Address");
		  String start=obj.getString("Start");
		  String end=obj.getString("End");
		  message="<!DOCTYPE html><html><head></head><body>";
		  message+="<div style='' > "
					+ "<center> "
					+ "<div style=' margin-top:5px; color :#159ceb;font-family: monospace;'>";
		  message+="<div style='padding:2px;font-weight:bold'>";
		  message+="You have a event created by "+email+" at "+datecreated+"</div>";
		  message+="<div style='padding:2px;font-weight:bold'>";
		  message+="Subject: "+subject+"</div>";
		  message+="<div style='padding:2px;font-weight:bold'>";
		  message+="Start time:"+formatTime(start)+"</div>";
		  message+="<div style='padding:2px;font-weight:bold'>";
		  message+="End time:<b>"+formatTime(end)+"</div>";
		  message+= "<a href='"+teamchatlink+"' target='_blank' style='text-decoration: none;'>"
			+ "<div style='background-color:rgba(21, 156, 235, 0.49);padding:2px;color :white;'>Click here to view event</div>"
		+ "</a>"
				+ "</div>"
			+ "</center>"
		+ "</div>";
		  return message+"</body></html>";
	  }
	
	String formatTime(String time){
        String timeformat="";
        String timearray[]=time.split("T");
        timeformat+=timearray[1].split("Z")[0];
        if(timeformat.contains("."))
        	timeformat=timeformat.split("\\.")[0];
        timeformat+="GMT";
        timeformat+=" "+timearray[0];
        return timeformat;
    }
	
	String updateNotification(String JSONString){
		  String message="";
		  JSONObject obj=new JSONObject(JSONString);
		  String datecreated=obj.getString("DateTimeCreated");
		  String subject=obj.getString("Subject");
		  String link=obj.getString("WebLink");
		  String teamchatlink=new TeamchatURLlink().createEmbeddedLink(link, "Office365", "http");
		  JSONObject senderobj=obj.getJSONObject("Organizer");
		  JSONObject emailobj=senderobj.getJSONObject("EmailAddress");
		  String email=emailobj.getString("Address");
		  String start=obj.getString("Start");
		  String end=obj.getString("End");
		  message="<!DOCTYPE html><html><head></head><body>";
		  message+="<div style='' > "
					+ "<center> "
					+ "<div style=' margin-top:5px; color :#159ceb;font-family: monospace;'>";
		  message+="<div style='padding:2px;font-weight:bold'>";
		  message+="You have a event created by "+email+" at "+datecreated+"</div>";
		  message+="<div style='padding:2px;font-weight:bold'>";
		  message+="Subject: "+subject+"</div>";
		  message+="<div style='padding:2px;font-weight:bold'>";
		  message+="Start time:"+formatTime(start)+"</div>";
		  message+="<div style='padding:2px;font-weight:bold'>";
		  message+="End time:<b>"+formatTime(end)+"</div>";
		  message+= "<a href='"+teamchatlink+"' target='_blank' style='text-decoration: none;'>"
			+ "<div style='background-color:rgba(21, 156, 235, 0.49);padding:2px;color :white;'>Click here to view event</div>"
		+ "</a>"
				+ "</div>"
			+ "</center>"
		+ "</div>";
		  return message+"</body></html>";
	  }
}