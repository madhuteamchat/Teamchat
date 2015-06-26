/**
 * 
 */
package com.basecamp.classes;

import com.google.gson.annotations.SerializedName;

/**
 * @author Puranjay Jain
 *
 */
public class Comment {
	private int id;
	private String content,created_at,updated_at;
	private Boolean trashed;
	private Attachment attachment;
	@SerializedName("private")private Boolean _private;
	private Creator creator;
	public Comment(int id, String content, String created_at,
			String updated_at, Boolean trashed, Attachment attachment,
			Boolean _private, Creator creator) {
		super();
		this.id = id;
		this.content = content;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.trashed = trashed;
		this.attachment = attachment;
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
	 * @return the attachment
	 */
	public Attachment getAttachments() {
		return attachment;
	}
	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachments(Attachment attachment) {
		this.attachment = attachment;
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
}
