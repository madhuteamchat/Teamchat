/**
 * 
 */
package com.basecamp.classes;

/**
 * @author Puranjay Jain
 *
 */
public class Attachment {
	private String key, name, byte_size, content_type, created_at, url,
			app_url,updated_at;
	private Creator creator;

	public Attachment(String key, String name, String byte_size,
			String content_type, String created_at, String url, String app_url,
			String updated_at, Creator creator) {
		super();
		this.key = key;
		this.name = name;
		this.byte_size = byte_size;
		this.content_type = content_type;
		this.created_at = created_at;
		this.url = url;
		this.app_url = app_url;
		this.updated_at = updated_at;
		this.creator = creator;
	}

	public Attachment(String key, String name, String byte_size,
			String content_type, String created_at, String url, String app_url,
			Creator creator) {
		super();
		this.key = key;
		this.name = name;
		this.byte_size = byte_size;
		this.content_type = content_type;
		this.created_at = created_at;
		this.url = url;
		this.app_url = app_url;
		this.creator = creator;
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
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the byte_size
	 */
	public String getByte_size() {
		return byte_size;
	}

	/**
	 * @param byte_size
	 *            the byte_size to set
	 */
	public void setByte_size(String byte_size) {
		this.byte_size = byte_size;
	}

	/**
	 * @return the content_type
	 */
	public String getContent_type() {
		return content_type;
	}

	/**
	 * @param content_type
	 *            the content_type to set
	 */
	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	/**
	 * @return the created_at
	 */
	public String getCreated_at() {
		return created_at;
	}

	/**
	 * @param created_at
	 *            the created_at to set
	 */
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
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
	 * @param app_url
	 *            the app_url to set
	 */
	public void setApp_url(String app_url) {
		this.app_url = app_url;
	}

	/**
	 * @return the creator
	 */
	public Creator getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(Creator creator) {
		this.creator = creator;
	}
}
