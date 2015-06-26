/**
 * 
 */
package com.basecamp.classes;

/**
 * @author Puranjay Jain
 *
 */
public class Calendar_events {
	private int count;
	private String updated_at;
	private Urls urls;
	public Calendar_events(int count, String updated_at, Urls urls) {
		super();
		this.count = count;
		this.updated_at = updated_at;
		this.urls = urls;
	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
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
	 * @return the urls
	 */
	public Urls getUrls() {
		return urls;
	}
	/**
	 * @param urls the urls to set
	 */
	public void setUrls(Urls urls) {
		this.urls = urls;
	}
}
