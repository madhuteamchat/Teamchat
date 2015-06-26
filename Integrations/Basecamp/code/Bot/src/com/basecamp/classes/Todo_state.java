/**
 * 
 */
package com.basecamp.classes;

import com.google.gson.annotations.SerializedName;

/**
 * @author Puranjay Jain this defines the remaining, trashed and completed
 *         states in "todos" returned from GET /projects/1/todolists/1.json as
 *         seen on
 *         https://github.com/basecamp/bcx-api/blob/master/sections/todolists
 *         .md#get-to-do-list
 */
public class Todo_state {
	public Todo_state(int id, int position, int comments_count,
			String todolist_id, String content, String created_at,
			String updated_at, String due_on, String due_at, String url,
			String app_url, Boolean trashed, Boolean completed,
			Boolean _private, Creator creator) {
		super();
		this.id = id;
		this.position = position;
		this.comments_count = comments_count;
		this.todolist_id = todolist_id;
		this.content = content;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.due_on = due_on;
		this.due_at = due_at;
		this.url = url;
		this.app_url = app_url;
		this.trashed = trashed;
		this.completed = completed;
		this._private = _private;
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
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	/**
	 * @return the comments_count
	 */
	public int getComments_count() {
		return comments_count;
	}
	/**
	 * @param comments_count the comments_count to set
	 */
	public void setComments_count(int comments_count) {
		this.comments_count = comments_count;
	}
	/**
	 * @return the todolist_id
	 */
	public String getTodolist_id() {
		return todolist_id;
	}
	/**
	 * @param todolist_id the todolist_id to set
	 */
	public void setTodolist_id(String todolist_id) {
		this.todolist_id = todolist_id;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
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
	 * @return the due_on
	 */
	public String getDue_on() {
		return due_on;
	}
	/**
	 * @param due_on the due_on to set
	 */
	public void setDue_on(String due_on) {
		this.due_on = due_on;
	}
	/**
	 * @return the due_at
	 */
	public String getDue_at() {
		return due_at;
	}
	/**
	 * @param due_at the due_at to set
	 */
	public void setDue_at(String due_at) {
		this.due_at = due_at;
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
	 * @return the trashed
	 */
	public Boolean getTrashed() {
		return trashed;
	}
	/**
	 * @param trashed the trashed to set
	 */
	public void setTrashed(Boolean trashed) {
		this.trashed = trashed;
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
	 * @return the _private
	 */
	public Boolean get_private() {
		return _private;
	}
	/**
	 * @param _private the _private to set
	 */
	public void set_private(Boolean _private) {
		this._private = _private;
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
	private int id, position, comments_count;
	private String todolist_id, content, created_at, updated_at, due_on,
			due_at, url, app_url;
	private Boolean trashed, completed;
	@SerializedName("private")
	private Boolean _private;
	private Creator creator;
}
