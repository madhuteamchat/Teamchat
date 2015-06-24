/**
 * 
 */
package com.basecamp.classes;

/**
 * @author Puranjay Jain
 *
 */
public class Event extends Events {

	private String excerpt, raw_excerpt;
	private Object Attachment;

	// basic event
	public Event(int id, Boolean _private, String created_at,
			String updated_at, String summary, String action, String target,
			String url, String html_url, Eventable eventable, Creator creator) {
		super(id, _private, created_at, updated_at, summary, action, target,
				url, html_url, eventable, creator);
	}
	// advanced event
	public Event(int id, Boolean _private, String created_at,
			String updated_at, String summary, String action, String target,
			String url, String html_url, Eventable eventable, Creator creator, String excerpt, String raw_excerpt, Object Attachment) {
		super(id, _private, created_at, updated_at, summary, action, target,
				url, html_url, eventable, creator);
		this.raw_excerpt = raw_excerpt;
		this.excerpt = excerpt;
		this.Attachment = Attachment;
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
	 * @return the raw_excerpt
	 */
	public String getRaw_excerpt() {
		return raw_excerpt;
	}
	/**
	 * @param raw_excerpt the raw_excerpt to set
	 */
	public void setRaw_excerpt(String raw_excerpt) {
		this.raw_excerpt = raw_excerpt;
	}
	/**
	 * @return the attachment
	 */
	public Object getAttachment() {
		return Attachment;
	}
	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(Object attachment) {
		Attachment = attachment;
	}
}
