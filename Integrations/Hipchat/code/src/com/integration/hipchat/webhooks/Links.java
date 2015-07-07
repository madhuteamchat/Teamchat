package com.integration.hipchat.webhooks;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Links
{

	@Expose
	private String webhooks;
	@Expose
	private String self;
	@Expose
	private String participants;
	@Expose
	private String members;

	/**
	 * 
	 * @return The webhooks
	 */
	public String getWebhooks()
	{
		return webhooks;
	}

	/**
	 * 
	 * @param webhooks
	 *            The webhooks
	 */
	public void setWebhooks(String webhooks)
	{
		this.webhooks = webhooks;
	}

	public Links withWebhooks(String webhooks)
	{
		this.webhooks = webhooks;
		return this;
	}

	/**
	 * 
	 * @return The self
	 */
	public String getSelf()
	{
		return self;
	}

	/**
	 * 
	 * @param self
	 *            The self
	 */
	public void setSelf(String self)
	{
		this.self = self;
	}

	public Links withSelf(String self)
	{
		this.self = self;
		return this;
	}

	/**
	 * 
	 * @return The participants
	 */
	public String getParticipants()
	{
		return participants;
	}

	/**
	 * 
	 * @param participants
	 *            The participants
	 */
	public void setParticipants(String participants)
	{
		this.participants = participants;
	}

	public Links withParticipants(String participants)
	{
		this.participants = participants;
		return this;
	}

	/**
	 * 
	 * @return The members
	 */
	public String getMembers()
	{
		return members;
	}

	/**
	 * 
	 * @param members
	 *            The members
	 */
	public void setMembers(String members)
	{
		this.members = members;
	}

	public Links withMembers(String members)
	{
		this.members = members;
		return this;
	}

}