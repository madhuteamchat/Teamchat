/**
 * 
 */
package com.basecamp.classes;

import com.basecamp.classes.Calendars;
/**
 * @author Puranjay Jain
 *
 */
public class Calendar extends Calendars {
	private int id;
	private String name, updated_at, color, url, app_url;
	private Creator creator;
	private Accesses accesses;
	private Calendar_events calendar_events;
	/**
	 * 
	 */
	public Calendar(int id, String name, String updated_at, String color,Creator creator,Accesses accesses,Calendar_events calendar_events) {
		// TODO Auto-generated constructor stub
		super(id, name, updated_at, color);
		this.creator = creator;
		this.accesses = accesses;
		this.calendar_events = calendar_events; 
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
	 * @return the accesses
	 */
	public Accesses getAccesses() {
		return accesses;
	}
	/**
	 * @param accesses the accesses to set
	 */
	public void setAccesses(Accesses accesses) {
		this.accesses = accesses;
	}
	/**
	 * @return the calendar_events
	 */
	public Calendar_events getCalendar_events() {
		return calendar_events;
	}
	/**
	 * @param calendar_events the calendar_events to set
	 */
	public void setCalendar_events(Calendar_events calendar_events) {
		this.calendar_events = calendar_events;
	}

}
