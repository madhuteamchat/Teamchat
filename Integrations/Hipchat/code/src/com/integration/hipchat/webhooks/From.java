package com.integration.hipchat.webhooks;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class From
{

	@Expose
	private String name;
	@SerializedName("mention_name")
	@Expose
	private String mentionName;
	@Expose
	private Links links;
	@Expose
	private long id;

	/**
	 * 
	 * @return The name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	public From withName(String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * 
	 * @return The mentionName
	 */
	public String getMentionName()
	{
		return mentionName;
	}

	/**
	 * 
	 * @param mentionName
	 *            The mention_name
	 */
	public void setMentionName(String mentionName)
	{
		this.mentionName = mentionName;
	}

	public From withMentionName(String mentionName)
	{
		this.mentionName = mentionName;
		return this;
	}

	/**
	 * 
	 * @return The links
	 */
	public Links getLinks()
	{
		return links;
	}

	/**
	 * 
	 * @param links
	 *            The links
	 */
	public void setLinks(Links links)
	{
		this.links = links;
	}

	public From withLinks(Links links)
	{
		this.links = links;
		return this;
	}

	/**
	 * 
	 * @return The id
	 */
	public long getId()
	{
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	public void setId(long id)
	{
		this.id = id;
	}

	public From withId(long id)
	{
		this.id = id;
		return this;
	}

}