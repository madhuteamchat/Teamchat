package teamchat;

import java.util.List;

import users.User;
import yammer.UploadedFile;
import yammer.groups.Group;
import yammer.search.Message;

public class HTMLBuilder {
	
         String displayUsers(List<User> results,String searchword) {
        	 String display="";
        	 if(results.size()==0)
        		 display="Sorry! No users found by the name <b>"+searchword+"</b><br />";
        	 else {
        		 display="Search results<br />";
        	  for(int i=0;i<results.size();i++) {
        		  display+="Name: <b>"+results.get(i).username+"</b><br />";
        		  display+="Email: <b>"+results.get(i).email+"</b><br />";
        		  display+="Department: <b>"+results.get(i).department+"</b><br />";
        		  display+="Designation: <b>"+results.get(i).designation+"</b><br/>";
        		  display+="<hr />";
        	  }
        	 }
        	 return display;
         }
         
         String displayGroup(List<Group> results,String searchword) {
        	 String display="";
        	 if(results.size()==0)
        		 display="Sorry! No groups found by the name <b>"+searchword+"</b><br/>";
        	 else {
        		 display="Search results<br/>";
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
        	 return display;
         }
         
         
         String displayUploadedFiles(List<UploadedFile> results,String searchword) {
        	 String display="";
        	 if(results.size()==0)
        		 display="Sorry! No files uploaded by the name <b>"+searchword+"</b><br />";
        	 else {
        		 display="Search results<br />";
        	  for(int i=0;i<results.size();i++) {
        		  display+="Name: <b>"+results.get(i).name+"</b><br />";
        		  display+="File type: <b>"+results.get(i).type+"</b><br />";
        		  display+="Download link: <b>"+results.get(i).downloadurl+"</b><br />";
        		  display+="<hr />";
        	  }
        	 }
        	 return display;
         }
         
         String displayMessages(List<Message> results,String searchword) {
        	 String display="";
        	 if(results.size()==0)
        		 display="Sorry! No messages for the search word <b>"+searchword+"</b><br />";
        	 else {
        		 display="Search results<br />";
        	  for(int i=0;i<results.size();i++) {
        		  display+=""+results.get(i).body+"<br />";
        		  display+="<hr />";
        	  }
        	 }
        	 return display;
         }
         
         String displayNotifications(List<String> results) {
        	 String display="";
        	 if(results.size()==0)
        		 display="No notifications <br />";
        	 else {
        	  for(int i=0;i<results.size();i++) {
        		  display+=""+results.get(i)+"<br />";
        		  display+="<hr />";
        	  }
        	 }
        	 return display;
         }
}
