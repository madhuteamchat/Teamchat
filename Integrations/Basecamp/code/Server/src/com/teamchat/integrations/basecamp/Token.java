/**
 * 
 */
package com.teamchat.integrations.basecamp;

/**
 * @author Puranjay Jain
 *
 */
public class Token {
	private String expires_in, refresh_token, access_token;
	
	Token(String expires_in,String refresh_token,String access_token){
		this.expires_in = expires_in;
		this.refresh_token = refresh_token;
		this.access_token = access_token;
	}

	/**
	 * @return the expires_in
	 */
	public String getExpires_in() {
		return expires_in;
	}

	/**
	 * @param expires_in the expires_in to set
	 */
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	/**
	 * @return the refresh_token
	 */
	public String getRefresh_token() {
		return refresh_token;
	}

	/**
	 * @param refresh_token the refresh_token to set
	 */
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	/**
	 * @return the access_token
	 */
	public String getAccess_token() {
		return access_token;
	}

	/**
	 * @param access_token the access_token to set
	 */
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
}
