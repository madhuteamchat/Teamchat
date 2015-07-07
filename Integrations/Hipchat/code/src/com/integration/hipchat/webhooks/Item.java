package com.integration.hipchat.webhooks;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Item
{

	@Expose
	private Room room;
	@Expose
	private Message message;

	/**
	 * 
	 * @return The room
	 */
	public Room getRoom()
	{
		return room;
	}

	/**
	 * 
	 * @param room
	 *            The room
	 */
	public void setRoom(Room room)
	{
		this.room = room;
	}

	public Item withRoom(Room room)
	{
		this.room = room;
		return this;
	}

	/**
	 * 
	 * @return The message
	 */
	public Message getMessage()
	{
		return message;
	}

	/**
	 * 
	 * @param message
	 *            The message
	 */
	public void setMessage(Message message)
	{
		this.message = message;
	}

	public Item withMessage(Message message)
	{
		this.message = message;
		return this;
	}

}