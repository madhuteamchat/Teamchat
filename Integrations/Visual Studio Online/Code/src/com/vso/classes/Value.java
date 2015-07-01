package com.vso.classes;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Value {

	@Expose
	private String accountId;
	@Expose
	private String accountUri;
	@Expose
	private String accountName;
	@Expose
	private String organizationName;
	@Expose
	private String accountType;
	@Expose
	private String accountOwner;
	@Expose
	private String createdBy;
	@Expose
	private String createdDate;
	@Expose
	private String accountStatus;
	@Expose
	private String lastUpdatedBy;
	@Expose
	private Properties properties;
	@Expose
	private String lastUpdatedDate;

	/**
	 * 
	 * @return The accountId
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * 
	 * @param accountId
	 *            The accountId
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Value withAccountId(String accountId) {
		this.accountId = accountId;
		return this;
	}

	/**
	 * 
	 * @return The accountUri
	 */
	public String getAccountUri() {
		return accountUri;
	}

	/**
	 * 
	 * @param accountUri
	 *            The accountUri
	 */
	public void setAccountUri(String accountUri) {
		this.accountUri = accountUri;
	}

	public Value withAccountUri(String accountUri) {
		this.accountUri = accountUri;
		return this;
	}

	/**
	 * 
	 * @return The accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * 
	 * @param accountName
	 *            The accountName
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Value withAccountName(String accountName) {
		this.accountName = accountName;
		return this;
	}

	/**
	 * 
	 * @return The organizationName
	 */
	public String getOrganizationName() {
		return organizationName;
	}

	/**
	 * 
	 * @param organizationName
	 *            The organizationName
	 */
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public Value withOrganizationName(String organizationName) {
		this.organizationName = organizationName;
		return this;
	}

	/**
	 * 
	 * @return The accountType
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * 
	 * @param accountType
	 *            The accountType
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Value withAccountType(String accountType) {
		this.accountType = accountType;
		return this;
	}

	/**
	 * 
	 * @return The accountOwner
	 */
	public String getAccountOwner() {
		return accountOwner;
	}

	/**
	 * 
	 * @param accountOwner
	 *            The accountOwner
	 */
	public void setAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
	}

	public Value withAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
		return this;
	}

	/**
	 * 
	 * @return The createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * 
	 * @param createdBy
	 *            The createdBy
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Value withCreatedBy(String createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	/**
	 * 
	 * @return The createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * 
	 * @param createdDate
	 *            The createdDate
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Value withCreatedDate(String createdDate) {
		this.createdDate = createdDate;
		return this;
	}

	/**
	 * 
	 * @return The accountStatus
	 */
	public String getAccountStatus() {
		return accountStatus;
	}

	/**
	 * 
	 * @param accountStatus
	 *            The accountStatus
	 */
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Value withAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
		return this;
	}

	/**
	 * 
	 * @return The lastUpdatedBy
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	/**
	 * 
	 * @param lastUpdatedBy
	 *            The lastUpdatedBy
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Value withLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
		return this;
	}

	/**
	 * 
	 * @return The properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * 
	 * @param properties
	 *            The properties
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Value withProperties(Properties properties) {
		this.properties = properties;
		return this;
	}

	/**
	 * 
	 * @return The lastUpdatedDate
	 */
	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	/**
	 * 
	 * @param lastUpdatedDate
	 *            The lastUpdatedDate
	 */
	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Value withLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
		return this;
	}

}