package com.basecamp.classes;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Authorization {

	@Expose
	private List<Account> accounts = new ArrayList<Account>();
	@Expose
	private Identity identity;
	@SerializedName("expires_at")
	@Expose
	private String expiresAt;

	public Authorization() {
		super();
	}

	public Authorization(List<Account> accounts, Identity identity,
			String expiresAt) {
		super();
		this.accounts = accounts;
		this.identity = identity;
		this.expiresAt = expiresAt;
	}

	/**
	 * 
	 * @return The accounts
	 */
	public List<Account> getAccounts() {
		return accounts;
	}

	/**
	 * 
	 * @param accounts
	 *            The accounts
	 */
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public Authorization withAccounts(List<Account> accounts) {
		this.accounts = accounts;
		return this;
	}

	/**
	 * 
	 * @return The identity
	 */
	public Identity getIdentity() {
		return identity;
	}

	/**
	 * 
	 * @param identity
	 *            The identity
	 */
	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	public Authorization withIdentity(Identity identity) {
		this.identity = identity;
		return this;
	}

	/**
	 * 
	 * @return The expiresAt
	 */
	public String getExpiresAt() {
		return expiresAt;
	}

	/**
	 * 
	 * @param expiresAt
	 *            The expires_at
	 */
	public void setExpiresAt(String expiresAt) {
		this.expiresAt = expiresAt;
	}

	public Authorization withExpiresAt(String expiresAt) {
		this.expiresAt = expiresAt;
		return this;
	}

}