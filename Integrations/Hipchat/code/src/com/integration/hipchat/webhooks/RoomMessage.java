package com.integration.hipchat.webhooks;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class RoomMessage
{

	@Expose
	private String event;
	@Expose
	private Item item;
	@SerializedName("oauth_client_id")
	@Expose
	private String oauthClientId;
	@SerializedName("webhook_id")
	@Expose
	private long webhookId;

	/**
	 * 
	 * @return The webhookId
	 */
	public long getWebhookId()
	{
		return webhookId;
	}

	/**
	 * 
	 * @param webhookId
	 *            The webhook_id
	 */
	public void setWebhookId(long webhookId)
	{
		this.webhookId = webhookId;
	}

	public RoomMessage withWebhookId(long webhookId)
	{
		this.webhookId = webhookId;
		return this;
	}

	/**
	 * 
	 * @return The oauthClientId
	 */
	public String getOauthClientId()
	{
		return oauthClientId;
	}

	/**
	 * 
	 * @param oauthClientId
	 *            The oauth_client_id
	 */
	public void setOauthClientId(String oauthClientId)
	{
		this.oauthClientId = oauthClientId;
	}

	public RoomMessage withOauthClientId(String oauthClientId)
	{
		this.oauthClientId = oauthClientId;
		return this;
	}

	/**
	 * 
	 * @return The item
	 */
	public Item getItem()
	{
		return item;
	}

	/**
	 * 
	 * @param item
	 *            The item
	 */
	public void setItem(Item item)
	{
		this.item = item;
	}

	public RoomMessage withItem(Item item)
	{
		this.item = item;
		return this;
	}

	/**
	 * 
	 * @return The event
	 */
	public String getEvent()
	{
		return event;
	}

	/**
	 * 
	 * @param event
	 *            The event
	 */
	public void setEvent(String event)
	{
		this.event = event;
	}

	public RoomMessage withEvent(String event)
	{
		this.event = event;
		return this;
	}

}