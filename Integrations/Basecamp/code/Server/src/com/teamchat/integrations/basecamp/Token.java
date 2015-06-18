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
	
	// getters
	public String access_token() {
		return access_token;
	}

	// getters
	public String refresh_token() {
		return refresh_token;
	}

	// getters
	public String expires_in() {
		return expires_in;
	}
}
