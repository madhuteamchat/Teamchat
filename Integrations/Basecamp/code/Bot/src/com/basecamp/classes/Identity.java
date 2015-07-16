package com.basecamp.classes;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Identity {

	@SerializedName("email_address")
	@Expose
	private String emailAddress;
	@SerializedName("last_name")
	@Expose
	private String lastName;
	@SerializedName("first_name")
	@Expose
	private String firstName;
	@Expose
	private Integer id;

	/**
	 * 
	 * @return The emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * 
	 * @param emailAddress
	 *            The email_address
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Identity withEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
		return this;
	}

	/**
	 * 
	 * @return The lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * 
	 * @param lastName
	 *            The last_name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Identity withLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	/**
	 * 
	 * @return The firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * 
	 * @param firstName
	 *            The first_name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Identity withFirstName(String firstName) {
		this.firstName = firstName;
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

	public Identity withId(Integer id) {
		this.id = id;
		return this;
	}

}