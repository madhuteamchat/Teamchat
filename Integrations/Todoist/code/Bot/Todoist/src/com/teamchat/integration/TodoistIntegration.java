package com.teamchat.integration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

@SuppressWarnings("deprecation")
public class TodoistIntegration {

	public static Authentication 	objAuthentication = null;
	

	public final String USER_AGENT = "Google Chrome/43.0.2357.81";
	
	public static String token ="";

    private	static TeamchatAPI api;
   

	public static Map<String, Integer> ProjectNameToProjectIdMap = new HashMap<String, Integer>();
	public static Map<String, TaskDetails> TaskNameToTaskDetailsMap = new HashMap<String, TaskDetails>();

	public static int projectId;
	
	public static DatabaseHandler db = null;
	
	
	@OnKeyword("help")
	public void onhelp(TeamchatAPI api){
		
		api.perform(api.context().currentRoom().post(new PrimaryChatlet()
		.setQuestionHtml("<html><body>"
				+"<b>Hey, This Is Todoist Bot</center></b><br></br>"
				+ "<p>Now you can directly manage your day to day to do list on teamchat using the following keywords:</p><br></br>"
				+ "<font color =#159CEB>Type:</font><br></br>"
				+ " <b>todoist</b>      : <font color =#159CEB> Authorize To Todoist</font><br></br>"
				+ " <b> addproject</b>  : <font color =#159CEB>Add Project In Todoist</font><br></br>"
				+ " <b>updateproject</b>: <font color =#159CEB>Update Project In Todoist</font><br></br>"
				+ " <b>deleteproject</b>: <font color =#159CEB>Delete Project In Todoist</font><br></br>"
				+ " <b>addtask</b>      : <font color =#159CEB>Add Task In Todoist</font><br></br>"
				+ " <b>updatetask</b>   : <font color =#159CEB>Update Task In Todoist</font><br></br>"
				+ " <b>deletetask</b>   : <font color =#159CEB>Delete Task In Todoist</font><br></br>"
				+ " <b>viewprojects</b> : <font color =#159CEB> View All Projects In Todoist</font><br></br>"
				+ " <b>viewtasks</b>    : <font color =#159CEB> View All Tasks In Todoist</font><br></br>"
				+"</body></html>")
		));
	}
	
	
	
	@SuppressWarnings("static-access")
	@OnKeyword("todoist")
	public void onTODOIST(TeamchatAPI api) {		
		
		this.api = api;
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml(
	                        "<a target='_blank' href='https://todoist.com/oauth/authorize?client_id=71be67ab414940e9866a8e09afa03a16&redirect_uri=http://interns.teamchat.com:8084/Todoist/Authentication&response_type=code&scope=data:read,data:delete,data:read_write,task:add,project:delete&state="+api.context().currentSender().getEmail()+"'>Click here to connect your Teamchat Account To ToDoIst</a>")));
		
		
	}
	
	public static void TODOIST(DatabaseHandler dbHandler){
		
		try {

			db = dbHandler;
			token = db.getAccessToken();
			
			try {
				
				db.CleanUpAllProjectsByEmailId();
				
				db.CleanUpAllTasksByEmailId();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			SuccessTODOISTALIAS();
			
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public final static void SuccessTODOISTALIAS() throws IOException,JSONException {

		SyncProjectsFromTODOISTServer(api);
		SyncTasksFromTODOISTServer(api);

		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestion("Successfully Login...")));
		
	}
  	public static int SyncProjectsFromTODOISTServer(TeamchatAPI api) {

		objAuthentication = new Authentication();

		// token = objAuthentication.getAccessToken();

		System.out.print("Token in Todoist: " + token);


		String syncProjectUrl = "https://todoist.com/API/v6/sync?token="
				+ token
				+ "&seq_no=0&seq_no_global=0&resource_types=[%22projects%22]";

		@SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
		HttpPost httppost = new HttpPost(syncProjectUrl);

		httppost.setHeader("Content-type", "application/json");

		InputStream inputStream = null;
		String result = null;
		try {
			HttpResponse response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();

			inputStream = entity.getContent();
			// json is UTF-8 by default
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			result = sb.toString();
			reader.close();

			JSONObject thedata = new JSONObject(result);

			JSONArray jsonArray = (JSONArray) thedata.get("Projects");

			int count = jsonArray.length(); // get totalCount of all jsonObjects

			if (0 != count) {

				try {

					for (int i = 0; i < count; i++) { // iterate through
														// jsonArray
						JSONObject jsonObject = jsonArray.getJSONObject(i); 

						String projectName = (String) jsonObject.get("name");
						Integer projectId = new Integer(
								(int) jsonObject.get("id"));
						String emailid = api.context().currentSender().getEmail();
			
						//ProjectNameToProjectIdMap.put(projectName, projectId);
						
						db.insertProjectsIntoDB(emailid, projectId, projectName);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet()
								.setQuestion("No Projects Found!!")));
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (Exception squish) {
			}
		}


		return 0;
	}

	
	public static int SyncTasksFromTODOISTServer(TeamchatAPI api) {


		String syncProjectUrl = "https://todoist.com/API/v6/sync?token="
				+ token
				+ "&seq_no=0&seq_no_global=0&resource_types=[%22items%22]";

		@SuppressWarnings("resource")
		HttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
		HttpPost httppost = new HttpPost(syncProjectUrl);

		httppost.setHeader("Content-type", "application/json");

		InputStream inputStream = null;
		String result = null;
		try {
			HttpResponse response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();

			inputStream = entity.getContent();
			// json is UTF-8 by default
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			result = sb.toString();
			reader.close();

			JSONObject thedata = new JSONObject(result);

			JSONArray jsonArray = (JSONArray) thedata.get("Items");

			int count = jsonArray.length(); // get totalCount of all jsonObjects

			if (0 != count) {

				try {

					for (int i = 0; i < count; i++) { 
						JSONObject jsonObject = jsonArray.getJSONObject(i); 

						String taskName = (String) jsonObject.get("content");
						Integer taskId = new Integer((int) jsonObject.get("id"));
						Integer projectId = new Integer(
								(int) jsonObject.get("project_id"));

//						TaskDetails objTaskDetails = new TaskDetails();
//						objTaskDetails.setTaskId(taskId);
//						objTaskDetails.setProjectId(projectId);
//						objTaskDetails.setTaskName(taskName);

						//TaskNameToTaskDetailsMap.put(taskName, objTaskDetails);
						String emailid = api.context().currentSender().getEmail();
						db.insertTasksIntoDB(emailid, projectId, taskId, taskName);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			} else {
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet()
								.setQuestion("No Task Found!!")));
			}
		} catch (Exception e) {
			// Oops
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (Exception squish) {
			}
		}


		return 0;
	}

	@OnKeyword("addproject")
	public int AddNewProjectToTODOISTServer(TeamchatAPI api) {
		
		api.perform(

		api.context()
				.currentRoom()
				.post(

				new PrimaryChatlet()
						.setQuestion("Enter Project Details.")
						.setReplyScreen(
								api.objects()
										.form()

										.addField(
												api.objects().input()
														.label("Project Name")
														.name("projectname")))
						.alias("addprojectalias")

				));
		return 0;
	}

	@OnAlias("addprojectalias")
	public void AddNewProjectToTODOISTServerAlias(TeamchatAPI api)
			throws IOException, JSONException {

		
		
		
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
			
			//ProjectNameToProjectIdMap.put(projectNameFromUI, projectId);
			String emailid = api.context().currentSender().getEmail();
			db.insertProjectsIntoDB(emailid, projectId, projectNameFromUI);
			
			api.perform(api.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml("<html><body><b><font color = #159CEB>Project Added SuccessFully.</font></b><br></br>"
						+ "<b> Project Id:&nbsp</b>" 
						+ "<font color = #159CEB>"
						+projectId
						+"</font><br></br>"
						+"<b>Project Name:&nbsp</b>" 
						+ "<font color = #159CEB>"
						+ projectNameFromUI
						+"</font></body></html>")));

		}
		else
		{
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml("<html><body><b><font color = #159CEB>Add Project Failed.</font></b><br></br>"
							+ "<b> Project Name:&nbsp</b>" 
							+ "<font color = #159CEB>"
							+ projectNameFromUI
							+"</font></body></html>")));
		}
		


	}

	@OnKeyword("updateproject")
	public int UpdateProjectToTODOISTServer(TeamchatAPI api) {
		
		Form form = api.objects().form();
		Field field = api.objects().select().label("Project Name").name("projectname");
		
		List<ProjectDetails> list = new ArrayList<ProjectDetails>();
		
		try {
			
			list = db.getAllProjectsByEmailId();
			for(ProjectDetails objProject: list)
			{
				field.addOption(objProject.getProjectName());
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		form.addField(field);
				
		api.perform(

		api.context()
				.currentRoom()
				.post(

				new PrimaryChatlet()
						.setQuestion("Enter Project Name.")
						.setQuestion("Updated Project Name Is.")
						.setReplyScreen(
								form.addField(api.objects().input()
										.label("UpdateProjectName")
										.name("updateprojectname")))
						.alias("updateprojectalias")

				));

		return 0;
	}

	@OnAlias("updateprojectalias")
	public void UpdateProjectToTODOISTServerAlias(TeamchatAPI api)
			throws IOException, JSONException, InstantiationException, IllegalAccessException {

		String projectNameFromUI = api.context().currentReply()
				.getField("projectname");
		
		String updateProjectNameFromUI = api.context().currentReply()
				.getField("updateprojectname");
		
		int projectId = 0;
		
		
		try {
			String emailid = api.context().currentSender().getEmail();
			
			projectId = db.getProjectIdByProjectName(projectNameFromUI,emailid);
			System.out.println("projectId Is:"+projectId);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		UUID uuid = UUID.randomUUID();
		System.out.println("UUID Is:" + uuid);

		String url = "https://todoist.com/API/v6/sync?token="
				+ token
				+ "&commands=[{%22type%22:%20%22project_update%22,%20%22uuid%22:%20%22"
				+ uuid + "%22,%20%22args%22:%20{%22id%22:%20" + projectId
				+ ",%20%22name%22:%22" + updateProjectNameFromUI
				+ "%22,%22indent%22:%202}}]";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();

//		JSONObject thedata = new JSONObject(response.toString());
//
//		JSONObject jsonObjectForSyncStatus = (JSONObject) thedata
//				.get("SyncStatus");
//
//		String OkStatus = (String) jsonObjectForSyncStatus.get(uuid.toString());
//
//		System.out.println("Project update status is:" + OkStatus);

		if (200== responseCode) {

			//ProjectNameToProjectIdMap.remove(projectNameFromUI);
			//ProjectNameToProjectIdMap.put(updateProjectNameFromUI, projectId);
			String emailid = api.context().currentSender().getEmail();
			try {
				
				db.UpdateProjectByEmailId(emailid, updateProjectNameFromUI, projectNameFromUI);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<html><body><b><font color = #159CEB>Project Updated SuccessFully.</font></b><br></br>"
									+ "<b>Project Id:&nbsp</b>"
									+ "<font color = #159CEB>"
									+ projectId
									+"</font><br></br>"
									+ "<b>Project Name:&nbsp</b>"
									+ "<font color = #159CEB>"
									+ updateProjectNameFromUI
									+"</font></body></html>")));

		} else {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml("<html><body><b><font color = #159CEB>Update Project Failed.</font></b><br></br>"
							+ "<b> Project Name:&nbsp</b>" 
							+ "<font color = #159CEB>"
							+ projectNameFromUI
							+"</font></body></html>")));
		
		}

	}

	@OnKeyword("deleteproject")
	public int DeleteProjectToTODOISTServer(TeamchatAPI api) {
		Form form = api.objects().form();
		Field field = api.objects().select().label("Project Name").name("projectname");
	List<ProjectDetails> list = new ArrayList<ProjectDetails>();
		
		try {
			
			list = db.getAllProjectsByEmailId();
			for(ProjectDetails objProject: list)
			{
				field.addOption(objProject.getProjectName());
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		form.addField(field);
		api.perform(

		api.context().currentRoom().post(

		new PrimaryChatlet().setQuestion("Enter Project Name.")

		.setReplyScreen(form)

		.alias("deleteprojectalias")

		));
		return 0;
	}

	@OnAlias("deleteprojectalias")
	public void DeleteProjectInProjectToTODOISTServerAlias(TeamchatAPI api)
			throws IOException, JSONException, InstantiationException, IllegalAccessException {
		
		String projectNameFromUI = api.context().currentReply()
				.getField("projectname");
		
		//Integer projectId = ProjectNameToProjectIdMap.get(projectNameFromUI);
		int projectId =0;
		try {
			String emailid = api.context().currentSender().getEmail();
			projectId = db.getProjectIdByProjectName(projectNameFromUI,emailid);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Project Id Is:"+projectId);

		//System.out.println("TaskId Is:" + ProjectId);

		UUID uuid = UUID.randomUUID();
		System.out.println("uuid is:"+uuid);

		
		String url = "https://todoist.com/API/v6/sync?token="
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

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();
		

		
//		JSONObject thedata = new JSONObject(response.toString());
//		
//				JSONObject jsonObjectForSyncStatus = (JSONObject) thedata
//					.get("SyncStatus");
//				JSONObject jsonObjectForUUID = (JSONObject) jsonObjectForSyncStatus
//					.get(uuid.toString());
//				String OkStatus = (String) jsonObjectForUUID.get(projectId.toString());
//				
//
//		System.out.println("Project delete status is:" + OkStatus);

		if (200 == responseCode) {
			
//			System.out.println("Before removing from map 1");
//			ProjectNameToProjectIdMap.remove(projectNameFromUI);
//			System.out.println("Before removing from map 2");
			
			String emailid= api.context().currentSender().getEmail();
			try {
				db.DeleteProjectByEmailId(emailid, projectNameFromUI);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<html><body><b><font color = #159CEB>Project Delete SuccessFully.</font></b><br></br>"
									+ "<b>Project Name:&nbsp" 
									+ "<font color = #159CEB>"
									+ projectNameFromUI 
									+"</font><br></br>"
									+ "<b>Project Id:</b>&nbsp" 
									+ "<font color = #159CEB>"
									+ projectId
									+"</font></body></html>"
									)));

		} else {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestion("<html><body><b><font color = #159CEB>Delete Project Failed.</font></b><br></br>"
									+ "<b>Project Name:</b>&nbsp"
									+ "<font color = #159CEB>"
									+projectId
									+"</font></body></html>"
									)));

		}

	}


	@OnKeyword("addtask")
	public int AddTaskInProjectToTODOISTServer(TeamchatAPI api) {
		
		Form form = api.objects().form();
		Field field = api.objects().select().label("Project Name").name("projectname");
		
		List<ProjectDetails> list = new ArrayList<ProjectDetails>();
		
		try {
			
			list = db.getAllProjectsByEmailId();
			for(ProjectDetails objProject: list)
			{
				field.addOption(objProject.getProjectName());
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		form.addField(field);
		
//		Form form = api.objects().form();
//		Field field = api.objects().select().label("Project Name")
//				.name("projectname");
//		for (Map.Entry<String, Integer> entry : ProjectNameToProjectIdMap
//				.entrySet()) {
//			String key = entry.getKey();
//			field.addOption(key);
//		}
//		form.addField(field);
		api.perform(

		api.context()
				.currentRoom()
				.post(

				new PrimaryChatlet()
						.setQuestion("Enter Project Name.")
						.setQuestion("Enter Task Details.")
						.setReplyScreen(
								form.addField(api.objects().input()
										.label("TaskName").name("taskname")))
						.alias("addtaskalias")

				));
		return 0;
	}

	@OnAlias("addtaskalias")
	public void AddTaskInProjectToTODOISTServerAlias(TeamchatAPI api)
			throws IOException, JSONException {

		String ProjectNameFromUI = api.context().currentReply()
				.getField("projectname");
		String taskNameFromUI = api.context().currentReply()
				.getField("taskname");

		int projectId = 0;
		String emailid = api.context().currentSender().getEmail();
		try {
			projectId = db.getProjectIdByProjectName(ProjectNameFromUI, emailid);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		UUID uuid = UUID.randomUUID();

		String url = "https://todoist.com/API/v6/sync?token="
				+ token
				+ "&commands=[{%22type%22:%20%22item_add%22,%20%22temp_id%22:%20%2243f7ed23-a038-46b5-b2c9-4abda9097ffa%22,%20%22uuid%22:%20%22"
				+ uuid + "%22,%20%22args%22:%20{%22content%22:%20%22"
				+ taskNameFromUI + "%22,%20%22project_id%22:%20" + projectId
				+ "}}]";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();

		JSONObject thedata = new JSONObject(response.toString());

		JSONObject jsonObjectForSyncStatus = (JSONObject) thedata
				.get("SyncStatus");

		String OkStatus = (String) jsonObjectForSyncStatus.get(uuid.toString());

		System.out.println("Task add status is:" + OkStatus);

		JSONObject jsonObjectForTempIdMapping = (JSONObject) thedata
				.get("TempIdMapping");

		int TaskId = (int) jsonObjectForTempIdMapping
				.get("43f7ed23-a038-46b5-b2c9-4abda9097ffa");

		System.out.println("TaskId is:" + TaskId);

		if (true == OkStatus.equals("ok")) {
			
			TaskDetails objTaskDetails = new TaskDetails();
			objTaskDetails.setTaskId(TaskId);
			objTaskDetails.setProjectId(projectId);
			objTaskDetails.setTaskName(taskNameFromUI);

		//	TaskNameToTaskDetailsMap.put(taskNameFromUI, objTaskDetails);
			
			//String emailid = api.context().currentSender().getEmail();
			db.insertTasksIntoDB(emailid, projectId, TaskId, taskNameFromUI);
			
			
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<html><body><b><font color = #159CEB>Task Added SuccessFully.</font></b><br></br>"
									+ "<b>Project Id:&nbsp</b>"
									+ "<font color = #159CEB>"
									+ projectId 
									+"</font><br></br>"
									+ "<b>Task Name:&nbsp</b>"
									+ "<font color = #159CEB>"
									+ taskNameFromUI 
									+ "</font><br></br>"
									+ "<b>Task Id:&nbsp</b>"
									+ "<font color = #159CEB>"
									+ TaskId
									+"</font></body></html>"
									 )));

		} else {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<html><body><b><font color = #159CEB>Add Task Failed.</font></b><br></br>"
									+ "<b>Project Name:&nbsp</b>"
									+ "<font color = #159CEB>"
									+ taskNameFromUI
									+"</font></body></html>"
									)));

		}

	}

	@OnKeyword("deletetask")
	public int DeleteTaskInProjectToTODOISTServer(TeamchatAPI api) {
		Form form = api.objects().form();
		Field field = api.objects().select().label("Task Name").name("taskname");
	List<TaskDetails> list = new ArrayList<TaskDetails>();
		
		try {
			
			list = db.getAllTasksByEmailId();
			for(TaskDetails objtask: list)
			{
				field.addOption(objtask.getTaskName());
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		form.addField(field);

		api.perform(

		api.context()
				.currentRoom()
				.post(

				new PrimaryChatlet()

				.setQuestion("Enter Task Details.").setReplyScreen(form)
						.alias("deletetaskalias")

				));
		return 0;
	}

	@OnAlias("deletetaskalias")
	public void DeleteTaskInProjectToTODOISTServerAlias(TeamchatAPI api)
			throws IOException, JSONException {
		String taskNameFromUI = api.context().currentReply()
				.getField("taskname");
		Integer taskid = 0;
		String emailid = api.context().currentSender().getEmail();
		try {
			taskid= db.getTaskIdByTaskName(taskNameFromUI, emailid);
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("TaskId Is:" + taskid);
		System.out.println("TaskId Is:" + taskid);

		UUID uuid = UUID.randomUUID();

		String url = "https://todoist.com/API/v6/sync?token="
				+ token
				+ "&commands=[{%22type%22:%20%22item_delete%22,%20%22uuid%22:%20%22"
				+ uuid + "%22,%20%22args%22:%20{%22ids%22:%20[" + taskid
				+ "]}}]";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();

		JSONObject thedata = new JSONObject(response.toString());

		JSONObject jsonObjectForSyncStatus = (JSONObject) thedata
				.get("SyncStatus");
		JSONObject jsonObjectForUUID = (JSONObject) jsonObjectForSyncStatus
				.get(uuid.toString());

		String OkStatus = (String) jsonObjectForUUID.get(taskid.toString());

		System.out.println("Task delete status is:" + OkStatus);

		if (true == OkStatus.equals("ok")) {
			
			try {
				db.DeleteTakByEmailId(emailid, taskNameFromUI);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<html><body><b><font color = #159CEB>Task Delete SuccessFully.</font></b><br></br>"
									+ "<b>Task Name:&nbsp" 
									+ "<font color = #159CEB>"
									+ taskNameFromUI 
									+"</font><br></br>"
									+ "<b>Task Id:</b>&nbsp" 
									+ "<font color = #159CEB>"
									+ taskid
									+"</font></body></html>"
									)));

		} else {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestion("<html><body><b><font color = #159CEB>Delete Task Failed.</font></b><br></br>"
									+ "<b> Task Name:</b>&nbsp"
									+ "<font color = #159CEB>"
									+ taskNameFromUI
									+"</font></body></html>"
									)));

		}

	}



	@OnKeyword("updatetask")
	public int UpdateTaskInProjectToTODOISTServer(TeamchatAPI api) {
		
		Form form = api.objects().form();
		Field field = api.objects().select().label("Task Name").name("taskname");
	List<TaskDetails> list = new ArrayList<TaskDetails>();
		
		try {
			
			list = db.getAllTasksByEmailId();
			for(TaskDetails objtask: list)
			{
				field.addOption(objtask.getTaskName());
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		form.addField(field);
		api.perform(

		api.context()
				.currentRoom()
				.post(

				new PrimaryChatlet()
						.setQuestion("Update Task.")
						// .setQuestion("Enter New Task Name.")
						.setReplyScreen(
								form

								.addField(api.objects().input()
										.label("NewTaskName")
										.name("newtaskname")))
						.alias("updatetaskalias")

				));
		return 0;
	}

	@OnAlias("updatetaskalias")
	public void UpdateTaskInProjectToTODOISTServerAlias(TeamchatAPI api)
			throws IOException, JSONException {
		String oldTaskNameFromUI = api.context().currentReply()
				.getField("taskname");
		String newTaskNameFromUI = api.context().currentReply()
				.getField("newtaskname");

		
		Integer taskid = 0;
		String emailid = api.context().currentSender().getEmail();
		try {
			taskid= db.getTaskIdByTaskName(oldTaskNameFromUI, emailid);
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("TaskId Is:" + taskid);

		UUID uuid = UUID.randomUUID();

		String url = "https://todoist.com/API/v6/sync?token="
				+ token
				+ "&commands=[{%22type%22:%20%22item_update%22,%20%22uuid%22:%20%22"
				+ uuid + "%22,%20%22args%22:%20{%22id%22:%20" + taskid
				+ ",%20%22content%22:%22" + newTaskNameFromUI
				+ "%22,%22priority%22:%202}}]";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}

		in.close();

		JSONObject thedata = new JSONObject(response.toString());

		JSONObject jsonObjectForSyncStatus = (JSONObject) thedata
				.get("SyncStatus");

		String OkStatus = (String) jsonObjectForSyncStatus.get(uuid.toString());

		System.out.println("Task Update status is:" + OkStatus);

		if (0 == OkStatus.compareTo("ok")) {
			// update task detail memory
			try {
				db.UpdateTaskByEmailId(emailid, newTaskNameFromUI, oldTaskNameFromUI);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<html><body><b><font color = #159CEB>Task Updated SuccessFully.</font></b><br></br>"
									+ "<b>Task Name:</b>&nbsp"
									+ "<font color = #159CEB>"
									+ newTaskNameFromUI 
									+"</font><br></br>"
									+ "<b>Task Id:</b>&nbsp" 
									+ "<font color = #159CEB>"
									+ taskid
									+"</font></body></html>")));

		} else {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestion("<html><body><b><font color = #159CEB>Update Task Failed.</font></b><br></br>"
									+ "<b>Task Name:</b>&nbsp"
									+ "<font color = #159CEB>"
									+ newTaskNameFromUI
									+"</font></body></html>"
									)));

		}

	}

	
	@OnKeyword("viewprojects")
	public int viewProjectToTODOISTServer(TeamchatAPI api) {
		
		try {
			
			List<ProjectDetails> list = db.getAllProjectsByEmailId();
			int length = list.size();
			
			String arr[] = new String[length];
			
			int i = 0;
			for (ProjectDetails objprojectDetails : list) 
			{
				arr[i] = objprojectDetails.getProjectName();
				i++;
			}
			
			
			String output="";
			for(int j=0;j<arr.length;j++)
			{
				  output=output+arr[j]+"<br>"; 
			}
			
			//output+="";
			System.out.println("Your Output Is:"+output);
			
			api.perform(api.context().currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml(output)));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}

	
	@OnKeyword("viewtasks")
	public int viewTasksToTODOISTServer(TeamchatAPI api) {
				
		try {
			
			List<TaskDetails> list = db.getAllTasksByEmailId();
			int length = list.size();
			
			String arr[] = new String[length];
			
			int i = 0;
			for (TaskDetails objTaskDetails : list) 
			{
				arr[i] = objTaskDetails.getTaskName();
				i++;
			}
			
			
			String output="";
			for(int j=0;j<arr.length;j++)
			{
				  output=output+arr[j]+"<br>"; 
			}
			
			//output+="";
			System.out.println("Your Output Is:"+output);
			
			api.perform(api.context().currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml(output)));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}
	
}