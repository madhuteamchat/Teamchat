package com.vso.classes;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
/**
 * @author Puranjay Jain
 *
 */
public class Profile {

	public Profile() {
		super();
	}

	public Profile(String displayName, String publicAlias, String emailAddress,
			Integer coreRevision, String timeStamp, String id, Integer revision) {
		super();
		this.displayName = displayName;
		this.publicAlias = publicAlias;
		this.emailAddress = emailAddress;
		this.coreRevision = coreRevision;
		this.timeStamp = timeStamp;
		this.id = id;
		this.revision = revision;
	}

	@Expose
	private String displayName;
	@Expose
	private String publicAlias;
	@Expose
	private String emailAddress;
	@Expose
	private Integer coreRevision;
	@Expose
	private String timeStamp;
	@Expose
	private String id;
	@Expose
	private Integer revision;

	/**
	 * 
	 * @return The displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 
	 * @param displayName
	 *            The displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Profile withDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}

	/**
	 * 
	 * @return The publicAlias
	 */
	public String getPublicAlias() {
		return publicAlias;
	}

	/**
	 * 
	 * @param publicAlias
	 *            The publicAlias
	 */
	public void setPublicAlias(String publicAlias) {
		this.publicAlias = publicAlias;
	}

	public Profile withPublicAlias(String publicAlias) {
		this.publicAlias = publicAlias;
		return this;
	}

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
	 *            The emailAddress
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Profile withEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
		return this;
	}

	/**
	 * 
	 * @return The coreRevision
	 */
	public Integer getCoreRevision() {
		return coreRevision;
	}

	/**
	 * 
	 * @param coreRevision
	 *            The coreRevision
	 */
	public void setCoreRevision(Integer coreRevision) {
		this.coreRevision = coreRevision;
	}

	public Profile withCoreRevision(Integer coreRevision) {
		this.coreRevision = coreRevision;
		return this;
	}

	/**
	 * 
	 * @return The timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * 
	 * @param timeStamp
	 *            The timeStamp
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Profile withTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
		return this;
	}

	/**
	 * 
	 * @return The id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	public void setId(String id) {
		this.id = id;
	}

	public Profile withId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * 
	 * @return The revision
	 */
	public Integer getRevision() {
		return revision;
	}

	/**
	 * 
	 * @param revision
	 *            The revision
	 */
	public void setRevision(Integer revision) {
		this.revision = revision;
	}

	public Profile withRevision(Integer revision) {
		this.revision = revision;
		return this;
	}

}