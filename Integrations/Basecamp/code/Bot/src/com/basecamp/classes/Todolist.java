/**
 * 
 */
package com.basecamp.classes;

import com.google.gson.annotations.SerializedName;

/**
 * @author Puranjay Jain
 *
 */
public class Todolist {
	public Todolist(int id, int remaining_count, int completed_count,
			String name, String created_at, String updated_at, String position,
			String url, String app_url, Boolean description, Boolean completed,
			Boolean _private, Boolean trashed, Creator creator) {
		super();
		this.id = id;
		this.remaining_count = remaining_count;
		this.completed_count = completed_count;
		this.name = name;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.position = position;
		this.url = url;
		this.app_url = app_url;
		this.description = description;
		this.completed = completed;
		this._private = _private;
		this.trashed = trashed;
		this.creator = creator;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the remaining_count
	 */
	public int getRemaining_count() {
		return remaining_count;
	}
	/**
	 * @param remaining_count the remaining_count to set
	 */
	public void setRemaining_count(int remaining_count) {
		this.remaining_count = remaining_count;
	}
	/**
	 * @return the completed_count
	 */
	public int getCompleted_count() {
		return completed_count;
	}
	/**
	 * @param completed_count the completed_count to set
	 */
	public void setCompleted_count(int completed_count) {
		this.completed_count = completed_count;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the created_at
	 */
	public String getCreated_at() {
		return created_at;
	}
	/**
	 * @param created_at the created_at to set
	 */
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	/**
	 * @return the updated_at
	 */
	public String getUpdated_at() {
		return updated_at;
	}
	/**
	 * @param updated_at the updated_at to set
	 */
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the app_url
	 */
	public String getApp_url() {
		return app_url;
	}
	/**
	 * @param app_url the app_url to set
	 */
	public void setApp_url(String app_url) {
		this.app_url = app_url;
	}
	/**
	 * @return the description
	 */
	public Boolean getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(Boolean description) {
		this.description = description;
	}
	/**
	 * @return the completed
	 */
	public Boolean getCompleted() {
		return completed;
	}
	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}
	/**
	 * @return the private
	 */
	public Boolean getPrivate() {
		return _private;
	}
	/**
	 * @param _private the private to set
	 */
	public void setPrivate(Boolean _private) {
		this._private = _private;
	}
	/**
	 * @return the trashed
	 */
	public Boolean getTrashed() {
		return trashed;
	}
	/**
	 * @return the creator
	 */
	public Creator getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(Creator creator) {
		this.creator = creator;
	}
	/**
	 * @param trashed the trashed to set
	 */
	public void setTrashed(Boolean trashed) {
		this.trashed = trashed;
	}
	private int id, remaining_count, completed_count;
	private String name, created_at, updated_at, position, url, app_url;
	private Boolean description, completed, trashed;
	@SerializedName("private")private Boolean _private;
	private Creator creator;
}
