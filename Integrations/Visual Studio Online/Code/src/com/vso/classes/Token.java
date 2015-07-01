package com.vso.classes;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
/**
 * @author Puranjay Jain
 *
 */
public class Token {

	@SerializedName("access_token")
	@Expose
	private String accessToken;
	public Token() {
		super();
	}

	public Token(String accessToken, String tokenType, String expiresIn,
			String refreshToken, String scope) {
		super();
		this.accessToken = accessToken;
		this.tokenType = tokenType;
		this.expiresIn = expiresIn;
		this.refreshToken = refreshToken;
		this.scope = scope;
	}

	@SerializedName("token_type")
	@Expose
	private String tokenType;
	@SerializedName("expires_in")
	@Expose
	private String expiresIn;
	@SerializedName("refresh_token")
	@Expose
	private String refreshToken;
	@Expose
	private String scope;

	/**
	 * 
	 * @return The accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * 
	 * @param accessToken
	 *            The access_token
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Token withAccessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}

	/**
	 * 
	 * @return The tokenType
	 */
	public String getTokenType() {
		return tokenType;
	}

	/**
	 * 
	 * @param tokenType
	 *            The token_type
	 */
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public Token withTokenType(String tokenType) {
		this.tokenType = tokenType;
		return this;
	}

	/**
	 * 
	 * @return The expiresIn
	 */
	public String getExpiresIn() {
		return expiresIn;
	}

	/**
	 * 
	 * @param expiresIn
	 *            The expires_in
	 */
	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public Token withExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
		return this;
	}

	/**
	 * 
	 * @return The refreshToken
	 */
	public String getRefreshToken() {
		return refreshToken;
	}

	/**
	 * 
	 * @param refreshToken
	 *            The refresh_token
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Token withRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		return this;
	}

	/**
	 * 
	 * @return The scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * 
	 * @param scope
	 *            The scope
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	public Token withScope(String scope) {
		this.scope = scope;
		return this;
	}

}