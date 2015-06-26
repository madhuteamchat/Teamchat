/**
 * 
 */
package com.basecamp.classes;

/**
 * @author Puranjay Jain
 *
 */
public class Urls {
	private String upcoming,past;

	public Urls(String upcoming, String past) {
		super();
		this.upcoming = upcoming;
		this.past = past;
	}

	/**
	 * @return the upcoming
	 */
	public String getUpcoming() {
		return upcoming;
	}

	/**
	 * @param upcoming the upcoming to set
	 */
	public void setUpcoming(String upcoming) {
		this.upcoming = upcoming;
	}

	/**
	 * @return the past
	 */
	public String getPast() {
		return past;
	}

	/**
	 * @param past the past to set
	 */
	public void setPast(String past) {
		this.past = past;
	}
}
