package com.teamchat.integration.bot;

import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;

import com.teamchat.integration.yammer.UploadedFile;
import com.teamchat.integration.yammer.groups.Group;
import com.teamchat.integration.yammer.search.Message;
import com.teamchat.integration.yammer.users.User;

public class HTMLBuilder {
	
         String displayUsers(List<User> results,String searchword) {
        	 String display="";
        	 if(results.size()==0)
        		 display= "Sorry! No users found by the name "+searchword;
        	 else {
        		 display="<!DOCTYPE html><html><head></head><body>Search results<br />";
        	  for(int i=0;i<results.size();i++) {
        		  display+="Name: <b>"+results.get(i).username+"</b><br />";
        		  display+="Email: <b>"+results.get(i).email+"</b><br />";
        		  display+="Department: <b>"+results.get(i).department+"</b><br />";
        		  display+="Designation: <b>"+results.get(i).designation+"</b><br/>";
        		  display+="<hr />";
        	  }
        	 }
        	 return display+"</body></html>";
         }
         
         String displayGroup(List<Group> results,String searchword) {
        	 String display="";
        	 if(results.size()==0)
        		 display="Sorry! No groups found by the name "+searchword;
        	 else {
        		 display="<!DOCTYPE html><html><head></head><body>Search results<br />";
        	  for(int i=0;i<results.size();i++) {
        		  display+="Name: <b>"+results.get(i).name+"</b><br/>";
        		  display+="Email: <b>"+results.get(i).description+"</b><br/>";
        		  display+="Department: <b>"+results.get(i).url+"</b><br/>";
        		  display+="Members:<br/>";
        		  for(int j=0;j<results.get(i).members.size();j++) {
        		    display+="<b>"+results.get(i).members.get(j)+"</b><br/>";
        		  }
        		  display+="<hr />";
        	  }
        	 }
        	 return display+"</body></html>";
         }
         
         
         String displayUploadedFiles(List<UploadedFile> results,String searchword) {
        	 String display="";
        	 if(results.size()==0)
        		 display="Sorry! No files uploaded by the name "+searchword;
        	 else {
        		 display="<!DOCTYPE html><html><head></head><body>Search results<br />";
        	  for(int i=0;i<results.size();i++) {
        		  display+="Name: <b>"+results.get(i).name+"</b><br />";
        		  display+="File type: <b>"+results.get(i).type+"</b><br />";
        		  display+="Download link: <b>"+results.get(i).downloadurl+"</b><br />";
        		  display+="<hr />";
        	  }
        	 }
        	 return display+"</body></html>";
         }
         
         String displayMessages(List<Message> results,String searchword) {
        	 String display="";
        	 if(results.size()==0)
        		 display="Sorry! No messages for the search word "+searchword;
        	 else {
        		 display="<!DOCTYPE html><html><head></head><body>Search results<br />";
        	  for(int i=0;i<results.size();i++) {
        		  display+=""+results.get(i).body+"<br />";
        		  display+="<hr />";
        	  }
        	 }
        	 return display+"</body></html>";
         }
         
         String displayNotifications(List<String> results) {
        	 String display="";
        	 if(results.size()==0)
        		 display="No notifications <br />";
        	 else {
        		 display="<!DOCTYPE html><html><head></head><body>"; 
        	  for(int i=0;i<results.size();i++) {
        		  display+=""+results.get(i)+"<br />";
        		  display+="<hr />";
        	  }
        	 }
        	 return display+"</body></html>";
         }
         
         public static String createEmbeddedLink(String url, String title, String protocol)
         {
         JSONObject object = new JSONObject();
         JSONObject web = new JSONObject();
         web.put("title", title);
         web.put("cancelBtnName", "Back");
         web.put("minWidth", "200");
         web.put("draggable", "true");
         web.put("newWindow", "true");
         web.put("url", url);
         object.put("web", web);
         System.out.println(object.toString());
         byte[] byteArray = Base64.encodeBase64(object.toString().getBytes());
         String encodedString = new String(byteArray);
         String fUrl = protocol + "://teamchat:data=" + encodedString;

         return fUrl;

         }
}
