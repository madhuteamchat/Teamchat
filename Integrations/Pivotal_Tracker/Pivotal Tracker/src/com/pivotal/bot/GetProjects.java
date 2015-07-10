package com.pivotal.bot;


import com.google.gson.Gson;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.TeamchatAPI;

public class GetProjects {
	public Field getProject(String token, TeamchatAPI api) {
		final String USER_AGENT = "Mozilla/5.0";
		String URL = "https://www.pivotaltracker.com/services/v5/projects";
		String URL_parameter = "";
		SendGet sg = new SendGet();
		Field f = null;
		try {
			String jsonData = sg.sendGet(URL, USER_AGENT, URL_parameter, token);
			Gson gson = new Gson();
			Data[] Projects = gson.fromJson(jsonData,
					Data[].class);

			f = api.objects().select().name("project_name")
					.label("Projects");
			for (Data project : Projects) {
				f.addOption(project.getName() + "-" + project.getId());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return f;
	}
}
