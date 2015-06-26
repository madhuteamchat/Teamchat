/**
 * 
 */
package com.basecamp.classes;

import com.google.gson.annotations.SerializedName;

/**
 * @author Puranjay Jain
 * Generated from GET/events.json
 */
public class Events {
	private int id;
	@SerializedName("private")private Boolean _private;
	private String created_at,updated_at,summary,action,target,url,html_url;
	private Eventable eventable;
	private Creator creator;
	private Bucket bucket;
	public Events(int id, Boolean _private, String created_at,
			String updated_at, String summary, String action, String target,
			String url, String html_url, Eventable eventable, Creator creator,
			Bucket bucket) {
		super();
		this.id = id;
		this._private = _private;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.summary = summary;
		this.action = action;
		this.target = target;
		this.url = url;
		this.html_url = html_url;
		this.eventable = eventable;
		this.creator = creator;
		this.bucket = bucket;
	}
	//for event class
	public Events(int id, Boolean _private, String created_at,
			String updated_at, String summary, String action, String target,
			String url, String html_url, Eventable eventable, Creator creator) {
		super();
		this.id = id;
		this._private = _private;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.summary = summary;
		this.action = action;
		this.target = target;
		this.url = url;
		this.html_url = html_url;
		this.eventable = eventable;
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
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}
	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}
	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
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
	 * @return the html_url
	 */
	public String getHtml_url() {
		return html_url;
	}
	/**
	 * @param html_url the html_url to set
	 */
	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}
	/**
	 * @return the eventable
	 */
	public Eventable getEventable() {
		return eventable;
	}
	/**
	 * @param eventable the eventable to set
	 */
	public void setEventable(Eventable eventable) {
		this.eventable = eventable;
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
	 * @return the bucket
	 */
	public Bucket getBucket() {
		return bucket;
	}
	/**
	 * @param bucket the bucket to set
	 */
	public void setBucket(Bucket bucket) {
		this.bucket = bucket;
	}
}
