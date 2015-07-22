package com.teamchat.integration;
import java.awt.image.ConvolveOp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

@SuppressWarnings("deprecation")
public class TodoistIntegration
{
	public static final String bot = "priyankarshiyal@gmail.com";
    public static final String password = "king@123";
    
    public final  String USER_AGENT = "Google Chrome/43.0.2357.81";
    
    public String emailid="";
    public String Password="";
    
    public static String token = "";
    
    
    public  Map<String, Integer> ProjectNameToProjectIdMap = new HashMap<String, Integer>();
    public  Map<String, TaskDetails> TaskNameToTaskDetailsMap = new HashMap<String, TaskDetails>();

   
   // public static int projectId ;
    @OnKeyword("login")
    public void onCreate(TeamchatAPI api) {

        api.perform(

                api.context().currentRoom().post(

                        new PrimaryChatlet()
                            .setQuestion("Enter Login Details.")
                            .setReplyScreen(api.objects().form()                                    

                            .addField(api.objects().input().label("EmailId").name("username"))

                            .addField(api.objects().input().label("Password").name("password")))

                            .alias("loginAlias")

                            ));

    }
    
    
    @OnAlias("loginAlias")
    public String getAccessTokenNEW(TeamchatAPI api) throws IOException,JSONException
	{
        emailid = api.context().currentReply().getField("username");

        Password = api.context().currentReply().getField("password");
         
        String urlforlogin = "https://todoist.com/API/v6/login?email=" + emailid + "&password=" + Password;
        
        
        @SuppressWarnings("resource")
		HttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
        HttpPost httppost = new HttpPost(urlforlogin);
        
        httppost.setHeader("Content-type", "application/json");

        InputStream inputStream = null;
        String result = null;
        try {
            HttpResponse response = httpclient.execute(httppost);  
            
            int statusCode = response.getStatusLine().getStatusCode();
            
            System.out.println("Status Code Is:" + statusCode);
            
            if(200 == statusCode){
            	
            	api.perform(

                        api.context().currentRoom().post(

                                new PrimaryChatlet()
                               
                                    .setQuestion("Login Successfully.") )); 
            	
                            	
            	HttpEntity entity = response.getEntity();

                inputStream = entity.getContent();
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
        
        		result = sb.toString();
        		reader.close();
        		
        		JSONObject jobject = new JSONObject(result);
        		token = (String)jobject.get("api_token");
        		System.out.println("Access Token Is: "+token);
        		
        		
        		
            	SyncProjectsFromTODOISTServer(api);
            	
            	SyncTasksFromTODOISTServer(api);
            	                
            }
            else{
            	api.perform(

                        api.context().currentRoom().post(

                                new PrimaryChatlet()
                               
                                    .setQuestionHtml("<html><body><p>Your UserName And Pasword Is Invalid ."
                                    		
                                    		+ "</p>"
                                    		+"<p>Please Try Again. </p></body></html></br>")
                                    
                                    .setReplyScreen(api.objects().form()                                    

                                    .addField(api.objects().input().label("EmailId").name("username"))

                                    .addField(api.objects().input().label("Password").name("password")))

                                    .alias("loginAlias")

                                    ));               
            }
            
            
        } catch (Exception e) { 
            // Oops
        }
        finally {
            try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
        }
        		
		return "";
			
	}      
    
    public int SyncProjectsFromTODOISTServer(TeamchatAPI api)
    {
    	
    	api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestion("Start Sync For Project Deatils From Todo Server...")));
    	
    	String syncProjectUrl =	"https://todoist.com/API/v6/sync?token="
    			+ token
    			+ "&seq_no=0&seq_no_global=0&resource_types=[%22projects%22]";
        
        
        @SuppressWarnings("resource")
		HttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
        HttpPost httppost = new HttpPost(syncProjectUrl);
        
        httppost.setHeader("Content-type", "application/json");

        InputStream inputStream = null;
        String result = null;
        try {
            	HttpResponse response = httpclient.execute(httppost);  
            
  
            	HttpEntity entity = response.getEntity();

                inputStream = entity.getContent();
                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
        
        		result = sb.toString();
        		reader.close();
        		
        		JSONObject thedata = new JSONObject(result);
        		
        		
        		JSONArray jsonArray = (JSONArray) thedata.get("Projects");
        		
        		int count = jsonArray.length(); // get totalCount of all jsonObjects	
        		
        		if(0 != count)
        		{
        	
        		try {
        		
        			
        			for(int i=0 ; i< count; i++){   // iterate through jsonArray 
        				JSONObject jsonObject = jsonArray.getJSONObject(i);  // get jsonObject @ i position 
        				//System.out.println("jsonObject " + i + ": " + jsonObject);
        				
        			
                		String projectName = (String)jsonObject.get("name");
                	    Integer projectId = new Integer((int)jsonObject.get("id"));
                	    
                	     
                	     ProjectNameToProjectIdMap.put(projectName, projectId);
                		
                		
                		api.perform(api
        						.context()
        						.currentRoom()
        						.post(new PrimaryChatlet().setQuestion("Project Id=" + projectId
        								+","+ "Project Name=" + projectName)));

        			}
        		} catch (JSONException e) {
        			e.printStackTrace();
        		}
        		
                 
        		}
        		else
        		{	
        			api.perform(api
    						.context()
    						.currentRoom()
    						.post(new PrimaryChatlet().setQuestion("No Projects Found!!")));
        		}
        } catch (Exception e) { 
            // Oops
        	e.printStackTrace();
        }
        finally {
            try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
        }
        
        
        api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestion("End Sync For Project Deatils From Todo Server...")));
            
    	return 0;
    }
    
    
    public int SyncTasksFromTODOISTServer(TeamchatAPI api)
    {
    	
    	
    	api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestion("Start Sync For Tasks Deatils From Todo Server...")));
    	
    	String syncProjectUrl =	"https://todoist.com/API/v6/sync?token="
    			+ token
    			+ "&seq_no=0&seq_no_global=0&resource_types=[%22items%22]";
        
        
        @SuppressWarnings("resource")
		HttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
        HttpPost httppost = new HttpPost(syncProjectUrl);
        
        httppost.setHeader("Content-type", "application/json");

        InputStream inputStream = null;
        String result = null;
        try {
            	HttpResponse response = httpclient.execute(httppost);  
            
  
            	HttpEntity entity = response.getEntity();

                inputStream = entity.getContent();
                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
        
        		result = sb.toString();
        		reader.close();
        		
        		JSONObject thedata = new JSONObject(result);
        		
        		
        		JSONArray jsonArray = (JSONArray) thedata.get("Items");
        		
        		int count = jsonArray.length(); // get totalCount of all jsonObjects	
        		
        		if(0 != count)
        		{
        	
        		try {
        		
        			
        			for(int i=0 ; i< count; i++){   // iterate through jsonArray 
        				JSONObject jsonObject = jsonArray.getJSONObject(i);  // get jsonObject @ i position 
        				//System.out.println("jsonObject " + i + ": " + jsonObject);
        				
        			
                		String taskName = (String)jsonObject.get("content");
                		Integer  taskId = new Integer((int)jsonObject.get("id"));
                		Integer	projectId = new Integer((int)jsonObject.get("project_id"));
                		
                		
                		
                		TaskDetails objTaskDetails = new TaskDetails();
                		objTaskDetails.setTaskId(taskId);
                		objTaskDetails.setProjectId(projectId);
                		objTaskDetails.setTaskName(taskName);
                		
                		TaskNameToTaskDetailsMap.put(taskName, objTaskDetails);
                		
                		
                		api.perform(api
        						.context()
        						.currentRoom()
        						.post(new PrimaryChatlet().setQuestion("Task Name Is="+taskName +","+ "Task Id=" + taskId +","+ "Project Id="+projectId)));

        			}
        		} catch (JSONException e) {
        			e.printStackTrace();
        		}
        		
                 
        		}
        		else
        		{	
        			api.perform(api
    						.context()
    						.currentRoom()
    						.post(new PrimaryChatlet().setQuestion("No Task Found!!")));
        		}
        } catch (Exception e) { 
            // Oops
        	e.printStackTrace();
        }
        finally {
            try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
        }
        
        
        api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestion("End Sync For Task Deatils From Todo Server...")));
            
    	return 0;
    }

    

    @OnKeyword("addproject")
    public int AddNewProjectToTODOISTServer(TeamchatAPI api)
    {
    	
        api.perform(

                api.context().currentRoom().post(

                        new PrimaryChatlet()
                        .setQuestion("Enter Project Details.")
                            .setReplyScreen(api.objects().form()                                    

                            .addField(api.objects().input().label("Project Name").name("projectname")))
                            .alias("addprojectalias")

                            ));
    	return 0;
    }
    
    
    @OnAlias("addprojectalias")
    public void AddNewProjectToTODOISTServerAlias(TeamchatAPI api) throws IOException,JSONException
	{
      
    	String projectNameFromUI = api.context().currentReply().getField("projectname");     
        UUID uuid = UUID.randomUUID();
        	
    	
    	String url = "https://todoist.com/API/v6/sync?token="
   			+ token
    			+"&commands=[{%22type%22:%22project_add%22,%22temp_id%22:%224ff1e388-5ca6-453a-b0e8-662ebf373b6a%22,%22"
    			+ "uuid%22:%22"
    			+ uuid
    			+ "%22,%22"
    			+ "args%22:{%22name%22:%22"
    			+ projectNameFromUI
    			+ "%22}}]";
    	 
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		
		in.close();
		
		JSONObject thedata = new JSONObject(response.toString());
		
		
		JSONObject jsonObjectForSyncStatus = (JSONObject) thedata.get("SyncStatus");
		
		String OkStatus = (String) jsonObjectForSyncStatus.get(uuid.toString());
		
		System.out.println("Project add status is:"+ OkStatus);
		
		
		JSONObject jsonObjectForTempIdMapping = (JSONObject) thedata.get("TempIdMapping");
		
		Integer projectId = new Integer((int)jsonObjectForTempIdMapping.get("4ff1e388-5ca6-453a-b0e8-662ebf373b6a"));
		
		System.out.println("Project id is:"+ projectId);
		
		if(0 == OkStatus.compareTo("ok"))
		{
			
			ProjectNameToProjectIdMap.put(projectNameFromUI, projectId);
			
			api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestion("Project Added SuccessFully, Project Id=" + projectId
						+","+ "Project Name=" + projectNameFromUI)));

		}
		else
		{
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet().setQuestion("Add Project Failed. Project Name=" + projectNameFromUI)));

			
		}
		
		
	}
    
   
    @OnKeyword("updateproject")
    public int UpdateProjectToTODOISTServer(TeamchatAPI api)
    {
    	Form form=api.objects().form();
    	Field field=api.objects().select().label("Project Name").name("projectname");
    	 for (Map.Entry<String, Integer> entry : ProjectNameToProjectIdMap.entrySet()) {
             String key=entry.getKey();
            field.addOption(key);
    	 }
    	form.addField(field);
        api.perform(

                api.context().currentRoom().post(
                		
                        new PrimaryChatlet()
                        .setQuestion("Enter Project Name.")
                        .setQuestion("Updated Project Name Is.")
                            .setReplyScreen(form  
                            .addField(api.objects().input().label("UpdateProjectName").name("updateprojectname")))
                            .alias("updateprojectalias")

                            ));
    	 
    	return 0;
    }
    
    
    @OnAlias("updateprojectalias")
    public void UpdateProjectToTODOISTServerAlias(TeamchatAPI api) throws IOException,JSONException
	{
    	      
    	String projectNameFromUI = api.context().currentReply().getField("projectname");
    	String updateProjectNameFromUI = api.context().currentReply().getField("updateprojectname");
    	Integer projectId = ProjectNameToProjectIdMap.get(projectNameFromUI);
    	
        UUID uuid = UUID.randomUUID();
        System.out.println("UUID Is:"+uuid);	
    	

        String url = "https://todoist.com/API/v6/sync?token="
        		+ token
        		+ "&commands=[{%22type%22:%20%22project_update%22,%20%22uuid%22:%20%22"
        		+ uuid
        		+ "%22,%20%22args%22:%20{%22id%22:%20"
        		+ projectId
        		+ ",%20%22name%22:%22"
        		+ updateProjectNameFromUI
        		+ "%22,%22indent%22:%202}}]";
 
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		
		in.close();
		
		JSONObject thedata = new JSONObject(response.toString());
		
		
		JSONObject jsonObjectForSyncStatus = (JSONObject) thedata.get("SyncStatus");
		
		String OkStatus = (String) jsonObjectForSyncStatus.get(uuid.toString());
		
		System.out.println("Project update status is:"+ OkStatus);
		
			
		if(0 == OkStatus.compareTo("ok"))
		{
			
			ProjectNameToProjectIdMap.put(updateProjectNameFromUI, projectId);
			
			api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestion("Project Updated SuccessFully, Project Id=" + projectId
						+","+ "Project Name=" + updateProjectNameFromUI)));

		}
		else
		{
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet().setQuestion("Update Project Failed. Project Name=" + projectNameFromUI)));

			
		}
	
		
	}
    
    
    
    @OnKeyword("deleteproject")
    public int DeleteProjectToTODOISTServer(TeamchatAPI api)
    {
    	Form form=api.objects().form();
    	Field field=api.objects().select().label("Project Name").name("projectname");
    	 for (Map.Entry<String, Integer> entry : ProjectNameToProjectIdMap.entrySet()) {
             String key=entry.getKey();
            field.addOption(key);
    	 }
    	form.addField(field);
        api.perform(

                api.context().currentRoom().post(

                        new PrimaryChatlet()
                        .setQuestion("Enter Project Name.")
                        
                            .setReplyScreen(form)                                    

                  
                            .alias("deleteprojectalias")

                            ));
    	return 0;
    }
    
    
    @OnAlias("deleteprojectalias")
    public void DeleteProjectToTODOISTServerAlias(TeamchatAPI api) throws IOException,JSONException
	{
    	      
    	String projectNameFromUI = api.context().currentReply().getField("projectname");
    	
    	Integer projectId = ProjectNameToProjectIdMap.get(projectNameFromUI);
    	
        UUID uuid = UUID.randomUUID();
        	
    	        	
    	
        String url  = "https://todoist.com/API/v6/sync?token="
        		+ token
        		+ "&commands=[{%22type%22:%20%22project_delete%22,%20%22uuid%22:%20%22"
        		+ uuid
        		+ "%22,%20%22args%22:%20{%22ids%22:%20["
        		+ projectId
        		+ "]}}]";
    	 

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		
		in.close();
		
		JSONObject thedata = new JSONObject(response.toString());
		
		
		JSONObject jsonObjectForSyncStatus = (JSONObject) thedata.get("SyncStatus");
		
		//String OkStatus = (String) jsonObjectForSyncStatus.get(uuid);
		
		//System.out.println("Project update status is:"+ OkStatus);
		
			
		//if(0 == OkStatus.compareTo("ok"))
		//{
			
			//ProjectNameToProjectIdMap.put(projectNameFromUI, projectId);
			
			api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestion("Project Deleted SuccessFully, Project Id=" + projectId
						+","+ "Project Name=" + projectNameFromUI)));

		//}
		//else
		//{
			//api.perform(api
				//	.context()
					//.currentRoom()
					//.post(new PrimaryChatlet().setQuestion("Delete Project Failed. Project Name=" + projectNameFromUI)));

			
		//}
	
		
	}

    
    @OnKeyword("addtask")
    public int AddTaskInProjectToTODOISTServer(TeamchatAPI api)
    {
    	Form form=api.objects().form();
    	Field field=api.objects().select().label("Project Name").name("projectname");
    	 for (Map.Entry<String, Integer> entry : ProjectNameToProjectIdMap.entrySet()) {
             String key=entry.getKey();
            field.addOption(key);
    	 }
    	form.addField(field);
        api.perform(

                api.context().currentRoom().post(

                        new PrimaryChatlet()
                        .setQuestion("Enter Project Name.")
                        .setQuestion("Enter Task Details.")
                            .setReplyScreen(form                                   
                            .addField(api.objects().input().label("TaskName").name("taskname")))
                            .alias("addtaskalias")

                            ));
    	return 0;
    }
    
    
    @OnAlias("addtaskalias")

    public void AddTaskInProjectToTODOISTServerAlias(TeamchatAPI api) throws IOException,JSONException
	{
    	
    	String ProjectNameFromUI = api.context().currentReply().getField("projectname");
    	String taskNameFromUI = api.context().currentReply().getField("taskname");
    	
    	Integer projectId = ProjectNameToProjectIdMap.get(ProjectNameFromUI);
    

        UUID uuid = UUID.randomUUID();
        	
        
        String url = "https://todoist.com/API/v6/sync?token="
        		+ token
        		+ "&commands=[{%22type%22:%20%22item_add%22,%20%22temp_id%22:%20%2243f7ed23-a038-46b5-b2c9-4abda9097ffa%22,%20%22uuid%22:%20%22"
        		+ uuid
        		+ "%22,%20%22args%22:%20{%22content%22:%20%22"
        		+ taskNameFromUI
        		+ "%22,%20%22project_id%22:%20"
        		+ projectId
        		+ "}}]";
    	
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		
		in.close();
		
		JSONObject thedata = new JSONObject(response.toString());
		
		
		JSONObject jsonObjectForSyncStatus = (JSONObject) thedata.get("SyncStatus");
		
		String OkStatus = (String) jsonObjectForSyncStatus.get(uuid.toString());
		
		System.out.println("Task add status is:"+ OkStatus);
		
		
		JSONObject jsonObjectForTempIdMapping = (JSONObject) thedata.get("TempIdMapping");
		
		int TaskId = (int)jsonObjectForTempIdMapping.get("43f7ed23-a038-46b5-b2c9-4abda9097ffa");
		
		System.out.println("TaskId is:"+ TaskId);
		
		if(0 == OkStatus.compareTo("ok"))
		{
			TaskDetails objTaskDetails = new TaskDetails();
			objTaskDetails.setTaskId(TaskId);
			objTaskDetails.setProjectId(projectId);
			objTaskDetails.setTaskName(taskNameFromUI);
			
			TaskNameToTaskDetailsMap.put(taskNameFromUI, objTaskDetails);
			
			api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestion("Task Added SuccessFully, Project Id=" + projectId
						+","+ "Task Name=" + taskNameFromUI+","+ "Task Id="+TaskId)));

		}
		else
		{
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet().setQuestion("Add Task Failed. Project Name=" + taskNameFromUI)));

			
		}
    	
		
}
		
    
    @OnKeyword("deletetask")
    public int DeleteTaskInProjectToTODOISTServer(TeamchatAPI api)
    {
    	Form form=api.objects().form();
    	Field field=api.objects().select().label("Task Name").name("taskname");
    	 for (Map.Entry<String, TaskDetails> entry : TaskNameToTaskDetailsMap.entrySet()) {
             String key=entry.getKey();
            field.addOption(key);
    	 }
    	form.addField(field);
    	
    	
        api.perform(

                api.context().currentRoom().post(

                        new PrimaryChatlet()
                        
                        .setQuestion("Enter Task Details.")
                            .setReplyScreen(form )
                            .alias("deletetaskalias")

                            ));
    	return 0;
    }
    
    
    @OnAlias("deletetaskalias")
    public void DeleteTaskInProjectToTODOISTServerAlias(TeamchatAPI api) throws IOException,JSONException
	{
    	String taskNameFromUI = api.context().currentReply().getField("taskname");
    	TaskDetails objTaskDetails = TaskNameToTaskDetailsMap.get(taskNameFromUI);
    	Integer taskid = objTaskDetails.getTaskId();
    	
        System.out.println("TaskId Is:"+taskid);

        UUID uuid = UUID.randomUUID();
        	
        
      String url = "https://todoist.com/API/v6/sync?token="
      		+ token
      		+ "&commands=[{%22type%22:%20%22item_delete%22,%20%22uuid%22:%20%22"
      	    + uuid
      		+ "%22,%20%22args%22:%20{%22ids%22:%20["
      		+ taskid
      		+ "]}}]";
        
    	
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		
		in.close();
		
		JSONObject thedata = new JSONObject(response.toString());
		
		
		JSONObject jsonObjectForSyncStatus = (JSONObject) thedata.get("SyncStatus");
		JSONObject jsonObjectForUUID = (JSONObject) jsonObjectForSyncStatus.get(uuid.toString());
		
		String OkStatus = (String) jsonObjectForUUID.get(taskid.toString());
		
		System.out.println("Task delete status is:"+ OkStatus);
		
	
		if(0 == OkStatus.compareTo("ok"))
		{
			TaskNameToTaskDetailsMap.remove(taskNameFromUI);
			
			api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestion("Task Deleted SuccessFully," + "Task Name=" + taskNameFromUI+","+ "Task Id="+taskid)));

		}
		else
		{
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet().setQuestion("Deleted Task Failed. Task Name=" + taskNameFromUI)));

			
		}
    		
}
    //https://todoist.com/API/v6/sync?token=3b89e6cc65f03e590752a9d704ceb507cb032c54&commands=[{%22type%22:%20%22item_update%22,%20%22uuid%22:%20%22217d16a7-0c88-46e0-9eb5-cde6c72477c8%22,%20%22args%22:%20{%22id%22:%208155383,%20%22content%22:%22priyanka%22,%22priority%22:%202}}]	
   
    
    
    @OnKeyword("updatetask")
    public int UpdateTaskInProjectToTODOISTServer(TeamchatAPI api)
    {
    	Form form = api.objects().form();
    	Field field = api.objects().select().label("Task Name").name("taskname");
    	for (Map.Entry<String,TaskDetails > entry : TaskNameToTaskDetailsMap.entrySet()) {
            String key=entry.getKey();
           field.addOption(key);
   	}
    	form.addField(field);
        api.perform(

                api.context().currentRoom().post(

                        new PrimaryChatlet()
                        .setQuestion("Update Task.")
                        //.setQuestion("Enter New Task Name.")
                            .setReplyScreen(form                                   
                            
                            .addField(api.objects().input().label("NewTaskName").name("newtaskname")))
                            .alias("updatetaskalias")

                            ));
    	return 0;
    }
    
    
    @OnAlias("updatetaskalias")
    public void UpdateTaskInProjectToTODOISTServerAlias(TeamchatAPI api) throws IOException,JSONException
	{
    	String oldTaskNameFromUI = api.context().currentReply().getField("taskname");
    	String newTaskNameFromUI = api.context().currentReply().getField("newtaskname");
    	
    	TaskDetails objTaskDetails = TaskNameToTaskDetailsMap.get(oldTaskNameFromUI);
    	Integer taskid = objTaskDetails.getTaskId();
    	
        System.out.println("TaskId Is:"+taskid);

        UUID uuid = UUID.randomUUID();
        	
        
      String url = "https://todoist.com/API/v6/sync?token="
      		+ token
      		+ "&commands=[{%22type%22:%20%22item_update%22,%20%22uuid%22:%20%22"
      		+ uuid
      		+ "%22,%20%22args%22:%20{%22id%22:%20"
      		+ taskid
      		+ ",%20%22content%22:%22"
      		+ newTaskNameFromUI
      		+ "%22,%22priority%22:%202}}]";
        
    	
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		
		in.close();
		
		JSONObject thedata = new JSONObject(response.toString());
		
		
		JSONObject jsonObjectForSyncStatus = (JSONObject) thedata.get("SyncStatus");
	 
        String OkStatus = (String) jsonObjectForSyncStatus.get(uuid.toString());
		
		System.out.println("Task Update status is:"+ OkStatus);
		
	
		if(0 == OkStatus.compareTo("ok"))
		{
			//update task detail memory
			TaskNameToTaskDetailsMap.remove(oldTaskNameFromUI);
			objTaskDetails.setTaskName(newTaskNameFromUI);
			TaskNameToTaskDetailsMap.put(newTaskNameFromUI, objTaskDetails);
			
			api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestion("Task Updated SuccessFully," + "Task Name=" + newTaskNameFromUI+","+ "Task Id="+taskid)));

		}
		else
		{
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet().setQuestion("Update Task Failed. Task Name=" + newTaskNameFromUI)));

			
		}
    		
}
    
    
    @OnKeyword("taskmovefromoneprojecttoanother")
    public int TaskMoveFromOneProjectToAnotherProjectToTODOISTServer(TeamchatAPI api)
    {
    	api.perform(

                api.context().currentRoom().post(

                        new PrimaryChatlet()
                        .setQuestion("Task Move Fom One Project To Another Project.")
                        
                            .setReplyScreen(api.objects().form()                               
                            .addField(api.objects().input().label("ProjectNameFromMoveTask").name("projectnamefrommovetask"))
                            .addField(api.objects().input().label("TaskName").name("taskname"))
                            .addField(api.objects().input().label("ProjectNameToAddTask").name("projectnameforaddtask")))
                            .alias("taskmovefromoneprojecttoanotheralias")

                            ));
    	return 0;
    }
    
    
    @OnAlias("taskmovefromoneprojecttoanotheralias")
    public void TaskMoveFromOneProjectToAnotherProjectToTODOISTServerAlias(TeamchatAPI api) throws IOException,JSONException
	{
    	String TaskNameFromUI = api.context().currentReply().getField("taskname");
    	String ProjectNameFromUIToMoveTask = api.context().currentReply().getField("projectnamefrommovetask");
    	String ProjectNameFromUIToAddtask = api.context().currentReply().getField("projectnametoaddtask");
    	
        int ProjectIdForMovetask = ProjectNameToProjectIdMap.get(ProjectNameFromUIToMoveTask);
    	int ProjectIdForAddTask = ProjectNameToProjectIdMap.get(ProjectNameFromUIToAddtask);
    	
    	TaskDetails objTaskDetails = TaskNameToTaskDetailsMap.get(TaskNameFromUI);
    	Integer taskid = objTaskDetails.getTaskId();
    	
        System.out.println("TaskId Is:"+taskid);
        System.out.println("ProjectIdOfMoveTask Is:"+ProjectIdForMovetask);
        System.out.println("ProjectIdOfAddTask Is:"+ProjectIdForAddTask);
        

        UUID uuid = UUID.randomUUID();
        	  
  String url = "https://todoist.com/API/v6/sync?token="
  		+ token
  		+ "&commands=[{%22type%22:%20%22item_move%22,%20%22uuid%22:%20%22"
  		+ uuid
  		+ "%22,%20%22args%22:%20{%22project_items%22:%20{%22"
  		+ ProjectIdForMovetask
  		+ "%22:%20["
  		+ taskid
  		+ "]},%20%22to_project%22:%20"
  		+ ProjectIdForAddTask
  		+ "}}]";
    	
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		
		in.close();
		
		JSONObject thedata = new JSONObject(response.toString());
		
		
		JSONObject jsonObjectForSyncStatus = (JSONObject) thedata.get("SyncStatus");
		
		JSONObject jsonObjectFortaskIdStatus = (JSONObject) jsonObjectForSyncStatus.get(uuid.toString());
	 
        String OkStatus = (String) jsonObjectFortaskIdStatus.get(taskid.toString());
		
		System.out.println("Task Update status is:"+ OkStatus);
		
	
		if(0 == OkStatus.compareTo("ok"))
		{
			TaskNameToTaskDetailsMap.remove(TaskNameFromUI);
			ProjectNameToProjectIdMap.put(TaskNameFromUI, ProjectIdForAddTask);
			
			
			api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestion("Task Moved SuccessFully," + "Task Name=" + TaskNameFromUI+","+ "Task Id="+taskid)));

		}
		else
		{
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet().setQuestion("Move Task Failed. Task Name=" + TaskNameFromUI)));

			
		}
    		
}
}

