/**
 * 
 */
package com.teamchat.asana;

/**
 * @author Aniruddha varshney
 *
 */
public class Token {
	private String expires_in,refresh_token,access_token;
	
	//getters
	public String access_token(){
		return access_token;
	}
	public String refresh_token(){
		return refresh_token;
	}
	public String expires_in(){
		return expires_in;
	}
}