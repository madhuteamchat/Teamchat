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
