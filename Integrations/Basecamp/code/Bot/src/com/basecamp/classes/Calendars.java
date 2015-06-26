/**
 * 
 */
package com.basecamp.classes;

/**
 * @author Puranjay Jain
 * Generated from GET /calendars.json
 */
public class Calendars {
	public int id;

	public Calendars(int id, String name, String updated_at, String color,
			String url, String app_url) {
		super();
		this.id = id;
		this.name = name;
		this.updated_at = updated_at;
		this.color = color;
		this.url = url;
		this.app_url = app_url;
	}
	//for calendar class

	public Calendars(int id, String name, String updated_at, String color) {
		super();
		this.id = id;
		this.name = name;
		this.updated_at = updated_at;
		this.color = color;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the updated_at
	 */
	public String getUpdated_at() {
		return updated_at;
	}

	/**
	 * @param updated_at
	 *            the updated_at to set
	 */
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color;
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

	public String name, updated_at, color, url, app_url;
}
