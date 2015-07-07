package com.integration.hipchat.webhooks;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Message
{

	@Expose
	private String type;
	@Expose
	private String message;
	@Expose
	private List<Object> mentions = new ArrayList<Object>();
	@Expose
	private String id;
	@Expose
	private From from;
	@Expose
	private String date;

	/**
	 * 
	 * @return The type
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * 
	 * @param type
	 *            The type
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	public Message withType(String type)
	{
		this.type = type;
		return this;
	}

	/**
	 * 
	 * @return The message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * 
	 * @param message
	 *            The message
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	public Message withMessage(String message)
	{
		this.message = message;
		return this;
	}

	/**
	 * 
	 * @return The mentions
	 */
	public List<Object> getMentions()
	{
		return mentions;
	}

	/**
	 * 
	 * @param mentions
	 *            The mentions
	 */
	public void setMentions(List<Object> mentions)
	{
		this.mentions = mentions;
	}

	public Message withMentions(List<Object> mentions)
	{
		this.mentions = mentions;
		return this;
	}

	/**
	 * 
	 * @return The id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	public Message withId(String id)
	{
		this.id = id;
		return this;
	}

	/**
	 * 
	 * @return The from
	 */
	public From getFrom()
	{
		return from;
	}

	/**
	 * 
	 * @param from
	 *            The from
	 */
	public void setFrom(From from)
	{
		this.from = from;
	}

	public Message withFrom(From from)
	{
		this.from = from;
		return this;
	}

	/**
	 * 
	 * @return The date
	 */
	public String getDate()
	{
		return date;
	}

	/**
	 * 
	 * @param date
	 *            The date
	 */
	public void setDate(String date)
	{
		this.date = date;
	}

	public Message withDate(String date)
	{
		this.date = date;
		return this;
	}

}