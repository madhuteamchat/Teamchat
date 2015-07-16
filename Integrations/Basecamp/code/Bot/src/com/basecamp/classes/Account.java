package com.basecamp.classes;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Account {

	@Expose
	private String product;
	@Expose
	private String name;
	@Expose
	private Integer id;
	@Expose
	private String href;

	/**
	 * 
	 * @return The product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * 
	 * @param product
	 *            The product
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	public Account withProduct(String product) {
		this.product = product;
		return this;
	}

	/**
	 * 
	 * @return The name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Account withName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * 
	 * @return The id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	public Account withId(Integer id) {
		this.id = id;
		return this;
	}

	/**
	 * 
	 * @return The href
	 */
	public String getHref() {
		return href;
	}

	/**
	 * 
	 * @param href
	 *            The href
	 */
	public void setHref(String href) {
		this.href = href;
	}

	public Account withHref(String href) {
		this.href = href;
		return this;
	}

}