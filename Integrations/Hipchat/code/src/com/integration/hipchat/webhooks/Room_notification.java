/**
 * 
 */
package com.integration.hipchat.webhooks;

import com.github.hipchat.api.Room;
import com.github.hipchat.api.messages.Message;

/**
 * @author Himanshu Rathee
 *
 */
public class Room_notification
{
	String event,oauth_client_id;
	Object item;
	int webhook_id;
	Message message;
	Room room;
	public Room_notification()
	{
		super();
	}
	public Room_notification(String event, String oauth_client_id, Object item, int webhook_id, Message message, Room room)
	{
		super();
		this.event = event;
		this.oauth_client_id = oauth_client_id;
		this.item = item;
		this.webhook_id = webhook_id;
		this.message = message;
		this.room = room;
	}
	public String getEvent()
	{
		return event;
	}
	public void setEvent(String event)
	{
		this.event = event;
	}
	public String getOauth_client_id()
	{
		return oauth_client_id;
	}
	public void setOauth_client_id(String oauth_client_id)
	{
		this.oauth_client_id = oauth_client_id;
	}
	public Object getItem()
	{
		return item;
	}
	public void setItem(Object item)
	{
		this.item = item;
	}
	public int getWebhook_id()
	{
		return webhook_id;
	}
	public void setWebhook_id(int webhook_id)
	{
		this.webhook_id = webhook_id;
	}
	public Message getMessage()
	{
		return message;
	}
	public void setMessage(Message message)
	{
		this.message = message;
	}
	public Room getRoom()
	{
		return room;
	}
	public void setRoom(Room room)
	{
		this.room = room;
	}
}
