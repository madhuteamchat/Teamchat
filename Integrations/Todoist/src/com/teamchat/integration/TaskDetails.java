package com.teamchat.integration;

public class TaskDetails {
	
	private Integer taskId;
	private Integer projectId;
	private String  taskName;
	

	
	public Integer getTaskId() {
		return taskId;
	}
	
	
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	
	public Integer getProjectId() {
		return projectId;
	}
	
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	
	public String getTaskName() {
		return taskName;
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
		
}
