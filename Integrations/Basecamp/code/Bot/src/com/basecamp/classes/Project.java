/**
 * 
 */
package com.basecamp.classes;

/**
 * @author Puranjay Jain
 *
 */
public class Project {
	private String name, description, updated_at, url, app_url, color;
	private Boolean template, archived, starred, trashed, draft,
			is_client_project;
	private int id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getApp_url() {
		return app_url;
	}

	public void setApp_url(String app_url) {
		this.app_url = app_url;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Boolean getTemplate() {
		return template;
	}

	public void setTemplate(Boolean template) {
		this.template = template;
	}

	public Boolean getArchived() {
		return archived;
	}

	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	public Boolean getStarred() {
		return starred;
	}

	public void setStarred(Boolean starred) {
		this.starred = starred;
	}

	public Boolean getTrashed() {
		return trashed;
	}

	public void setTrashed(Boolean trashed) {
		this.trashed = trashed;
	}

	public Boolean getDraft() {
		return draft;
	}

	public void setDraft(Boolean draft) {
		this.draft = draft;
	}

	public Boolean getIs_client_project() {
		return is_client_project;
	}

	public void setIs_client_project(Boolean is_client_project) {
		this.is_client_project = is_client_project;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Project(String name, String description, String updated_at,
			String url, String app_url, String color, Boolean template,
			Boolean archived, Boolean starred, Boolean trashed, Boolean draft,
			Boolean is_client_project, int id) {
		super();
		this.name = name;
		this.description = description;
		this.updated_at = updated_at;
		this.url = url;
		this.app_url = app_url;
		this.color = color;
		this.template = template;
		this.archived = archived;
		this.starred = starred;
		this.trashed = trashed;
		this.draft = draft;
		this.is_client_project = is_client_project;
		this.id = id;
	}
}
