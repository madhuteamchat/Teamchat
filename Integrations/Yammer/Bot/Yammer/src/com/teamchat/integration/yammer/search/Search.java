package com.teamchat.integration.yammer.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.integration.yammer.UploadedFile;
import com.teamchat.integration.yammer.YammerConnection;
import com.teamchat.integration.yammer.groups.Group;
import com.teamchat.integration.yammer.users.User;

public class Search {
	
	  private CloseableHttpClient httpclient=null;
	   private String authtoken="";
	   private HttpGet httpget;
	   private HttpResponse response;
	   private JSONObject countobject;
	   private JSONObject json; 
	  private YammerConnection yc;
	  private StringBuffer sb;
	  
	   public Search(String search,String email) {
		   try{
		    yc=new YammerConnection();
	        httpclient=yc.getHttpClient();
	        authtoken=yc.getAuthToken(email);
	        List<NameValuePair> params = new LinkedList<NameValuePair>();
	        params.add(new BasicNameValuePair("search",search));
	        String paramString = URLEncodedUtils.format(params, "utf-8");
	        httpget = new HttpGet("https://www.yammer.com/api/v1/search.json?"+paramString);
	        httpget.addHeader("Authorization", "Bearer " +authtoken);
	        response = httpclient.execute(httpget);
	        BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	        sb = new StringBuffer("");
	        String line = "";
	        while ((line = in.readLine()) != null) {
	            sb.append(line +"\n");
	        }
	        in.close();
	        System.out.println(sb);
	        json = new JSONObject(sb.toString());
        	countobject=json.getJSONObject("count");
		   }
		   catch(Exception e) {
			   e.printStackTrace();
		   }
	   }
	   
	public  List<User> searchUsers() throws IOException {
        List<User> users=new ArrayList<User>();
        try {
        	int count=countobject.getInt("users");
        
       		if(count != 0) {
        			JSONArray userobjectarray=json.getJSONArray("users");
        			for(int i=0;i<userobjectarray.length();i++){
        				JSONObject userobject = userobjectarray.getJSONObject(i);
        				String name=userobject.getString("full_name");
        				String email=userobject.getString("email");
        				String department=userobject.getString("department");
        				String designation=userobject.getString("job_title");
        				users.add(new User(name,email,department,designation));
        			} 	
       		  }
            yc.close();
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
    	
        return users;
	}
	
	public  List<Message> searchMessages() throws IOException {
        List<Message> results=new ArrayList<Message>();
        try {	
        	int count=countobject.getInt("messages");
       		if(count != 0) {
       		 	JSONObject msgobject=json.getJSONObject("messages");
            	JSONArray childJSONarray = msgobject.getJSONArray("messages");
            	for (int i = 0; i < childJSONarray.length(); i++) { 
             	    JSONObject childobject = childJSONarray.getJSONObject(i);
             	    JSONObject body = childobject.getJSONObject("body");
             	    int id=childobject.getInt("id");
             	    String text=body.getString("plain");
             	    System.out.println("id: "+id+" text: "+text);
             	     results.add(new Message(id,text));//stores the search results
             	}
        	} 	
            yc.close();
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        return results;
	}
	
	public  List<Group> searchGroups() throws IOException {
        List<Group> results=new ArrayList<Group>();
        try {	
        	int count=countobject.getInt("groups");
       		if(count != 0) {
       			JSONArray childJSONarray=json.getJSONArray("groups");
            	for (int i = 0; i < childJSONarray.length(); i++) { 
             	    JSONObject childobject = childJSONarray.getJSONObject(i);
             	    String name = childobject.getString("full_name");
             	    String url = childobject.getString("url");
             	    int id = childobject.getInt("id"); 
             	    String description = "Not available";
             	    if(childobject.has("description"))
             	    	description=childobject.getString("description");  
             	   JSONArray userarray=json.getJSONArray("users");
             	  List<String> users=new ArrayList<String>();
             	  for (int k = 0; k < userarray.length(); k++) { 
             		 JSONObject user=userarray.getJSONObject(i);
             		users.add(user.getString("full_name"));
             	  }
             	  results.add(new Group(name,id,users,description,url));
        	 } 	
       		}
            yc.close();
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        return results;
	}
	
	public  List<UploadedFile> searchUploadedFiles() throws IOException {
        List<UploadedFile> results=new ArrayList<UploadedFile>();
        try {	
        	int count=countobject.getInt("uploaded_files");
       		if(count != 0) {
       			JSONArray childJSONarray=json.getJSONArray("uploaded_files");
            	for (int i = 0; i < childJSONarray.length(); i++) { 
             	    JSONObject childobject = childJSONarray.getJSONObject(i);
             	    String name = childobject.getString("original_name");
             	    String doctype= childobject.getString("content_class");
             	    String url = childobject.getString("download_url"); 
             	    results.add(new UploadedFile(name,url,doctype));
        	 } 	
       		}
            yc.close();
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        System.out.println(results.size());
        return results;
	}
	
	public static void main(String[] args) throws Exception {
		/*Search sr=new Search("Yammer");
		sr.searchMessages();*/
	}
}
