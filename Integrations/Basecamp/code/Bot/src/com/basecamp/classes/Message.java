/**
 * 
 */
package com.basecamp.classes;

import com.google.gson.annotations.SerializedName;

/**
 * @author Puranjay Jain
 *
 */
public class Message {
	private int id;
	@SerializedName("private")
	private Boolean _private;
	private Boolean trashed;
	private String subject, content, created_at, updated_at;
	private Creator creator;
	private Comment[] comments;
	public Message(int id, Boolean _private, Boolean trashed, String subject,
			String content, String created_at, String updated_at,
			Creator creator, Comment[] comments, Attachment[] attachments,
			Subscriber[] subscribers) {
		super();
		this.id = id;
		this._private = _private;
		this.trashed = trashed;
		this.subject = subject;
		this.content = content;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.creator = creator;
		this.comments = comments;
		this.attachments = attachments;
		this.subscribers = subscribers;
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
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
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
	 * @return the comments
	 */
	public Comment[] getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(Comment[] comments) {
		this.comments = comments;
	}
	/**
	 * @return the attachments
	 */
	public Attachment[] getAttachments() {
		return attachments;
	}
	/**
	 * @param attachments the attachments to set
	 */
	public void setAttachments(Attachment[] attachments) {
		this.attachments = attachments;
	}
	/**
	 * @return the subscribers
	 */
	public Subscriber[] getSubscribers() {
		return subscribers;
	}
	/**
	 * @param subscribers the subscribers to set
	 */
	public void setSubscribers(Subscriber[] subscribers) {
		this.subscribers = subscribers;
	}
	private Attachment[] attachments;
	private Subscriber[] subscribers;	
}
