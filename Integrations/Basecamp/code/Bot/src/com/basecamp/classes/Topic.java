/**
 * 
 */
package com.basecamp.classes;

import com.google.gson.annotations.SerializedName;

/**
 * @author Puranjay Jain
 *
 */
public class Topic {
	private int id,attachments;
	private String title,excerpt,created_at,updated_at;
	private Boolean trashed;
	@SerializedName("private")private Boolean _private;
	private Topicable topicable;
	private Last_updater last_updater;
	private Bucket bucket;
	//for topics returned using
	//GET /projects/1/topics.json
	//as seen on https://github.com/basecamp/bcx-api/blob/master/sections/topics.md#get-topics
	public Topic(int id, int attachments, String title, String excerpt,
			String created_at, String updated_at, Boolean trashed,
			Boolean _private, Topicable topicable, Last_updater last_updater) {
		super();
		this.id = id;
		this.attachments = attachments;
		this.title = title;
		this.excerpt = excerpt;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.trashed = trashed;
		this._private = _private;
		this.topicable = topicable;
		this.last_updater = last_updater;
	}
	//GET /topics.json
	//as seen on https://github.com/basecamp/bcx-api/blob/master/sections/topics.md#get-topics
	public Topic(int id, int attachments, String title, String excerpt,
			String created_at, String updated_at, Boolean trashed,
			Boolean _private, Topicable topicable, Last_updater last_updater,
			Bucket bucket) {
		super();
		this.id = id;
		this.attachments = attachments;
		this.title = title;
		this.excerpt = excerpt;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.trashed = trashed;
		this._private = _private;
		this.topicable = topicable;
		this.last_updater = last_updater;
		this.bucket = bucket;
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
	 * @return the attachments
	 */
	public int getAttachments() {
		return attachments;
	}
	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(int attachments) {
		this.attachments = attachments;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the excerpt
	 */
	public String getExcerpt() {
		return excerpt;
	}
	/**
	 * @param excerpt the excerpt to set
	 */
	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
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
	 * @return the topicable
	 */
	public Topicable getTopicable() {
		return topicable;
	}
	/**
	 * @param topicable the topicable to set
	 */
	public void setTopicable(Topicable topicable) {
		this.topicable = topicable;
	}
	/**
	 * @return the last_updater
	 */
	public Last_updater getLast_updater() {
		return last_updater;
	}
	/**
	 * @param last_updater the last_updater to set
	 */
	public void setLast_updater(Last_updater last_updater) {
		this.last_updater = last_updater;
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
