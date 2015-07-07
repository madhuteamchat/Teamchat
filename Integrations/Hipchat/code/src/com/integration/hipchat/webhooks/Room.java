package com.integration.hipchat.webhooks;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Room
{

	@Expose
	private String name;
	@Expose
	private Links links;
	@Expose
	private Integer id;

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

	public Room withName(String name)
	{
		this.name = name;
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

	public Room withLinks(Links links)
	{
		this.links = links;
		return this;
	}

	/**
	 * 
	 * @return The id
	 */
	public Integer getId()
	{
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	public void setId(Integer id)
	{
		this.id = id;
	}

	public Room withId(Integer id)
	{
		this.id = id;
		return this;
	}

}