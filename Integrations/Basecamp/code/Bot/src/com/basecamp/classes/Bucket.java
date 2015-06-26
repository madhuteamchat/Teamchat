/**
 * 
 */
package com.basecamp.classes;

/**
 * @author Puranjay Jain
 *
 */
public class Bucket {
	private String type, name, color, url, app_url;
	private int id;
	public Bucket(String type, String name, String color, String url,
			String app_url, int id) {
		super();
		this.type = type;
		this.name = name;
		this.color = color;
		this.url = url;
		this.app_url = app_url;
		this.id = id;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param color the color to set
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
}
