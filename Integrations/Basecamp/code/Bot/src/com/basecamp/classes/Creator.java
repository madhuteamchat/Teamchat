/**
 * 
 */
package com.basecamp.classes;

/**
 * @author Puranjay Jain
 *
 */
public class Creator {
	private int id;
	private String name,avatar_url,fullsize_avatar_url;
	public Creator(int id, String name, String avatar_url,
			String fullsize_avatar_url) {
		super();
		this.id = id;
		this.name = name;
		this.avatar_url = avatar_url;
		this.fullsize_avatar_url = fullsize_avatar_url;
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
	 * @return the avatar_url
	 */
	public String getAvatar_url() {
		return avatar_url;
	}
	/**
	 * @param avatar_url the avatar_url to set
	 */
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	/**
	 * @return the fullsize_avatar_url
	 */
	public String getFullsize_avatar_url() {
		return fullsize_avatar_url;
	}
	/**
	 * @param fullsize_avatar_url the fullsize_avatar_url to set
	 */
	public void setFullsize_avatar_url(String fullsize_avatar_url) {
		this.fullsize_avatar_url = fullsize_avatar_url;
	}
}
