package com.teamchat.integrations.enchant.bot;

import java.util.List;

import enchantapi.Message;
import enchantapi.Ticket;

public class HTMLBuilder {
      String displayTickets(Ticket result) {
    	  String display="";
    	  display="<!DOCTYPE html><html><head></head><body>";
    		display+="<b>Ticket ID: </b>"+result.id+"<br/>";
    		display+="<b>Ticket number: </b>"+result.number+"<br/>";
    		display+="<b>Subject: </b>"+result.subject+"<br/>";
    		display+="<b>State: </b>"+result.state+"<br/>";
    		display+="<b>Customer mail: </b>"+result.customermail+"<br/>";
    		display+="<b>Summary: </b>"+result.summary+"<br/>";
    		display+="<b>Ticket state: </b>"+result.state+"<br/>";
    		display+="<b>Created at: </b>"+result.createdat+"<br/>";
    		display+="<b>Updated at: </b>"+result.updated+"<br/>";
    	  return display+"</body></html>";
      }
      
      String displayMessages(List<Message> messages,String ticketnumber) {
    	  String display="";
    	  display="<!DOCTYPE html><html><head>Messages for Ticket number:"+ticketnumber+"</head><body><br/>";
    	  if(messages.size()==0)
    		  return display+"No messages for this ticket</body></html>";
    	  for(int i=0;i<messages.size();i++){
    		 display+="<b>Message: </b>"+messages.get(i).body+"<br/>";
    		display+="<b>Type: </b>"+messages.get(i).type+"<br/>";
    		display+="<b>Message from: </b>"+messages.get(i).fromname+"<hr/>";
    	  }
    	  return display+"</body></html>";
      }
}
