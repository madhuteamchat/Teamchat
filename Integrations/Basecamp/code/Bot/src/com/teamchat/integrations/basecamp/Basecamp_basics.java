/**
 * 
 */
package com.teamchat.integrations.basecamp;

/**
 * @author Puranjay Jain
 *
 */
public class Basecamp_basics {
	private String email, basecamp_email, href, expires_in, refresh_token, access_token;

	public Basecamp_basics(String email, String basecamp_email, String href,
			String expires_in, String refresh_token, String access_token) {
		super();
		this.email = email;
		this.basecamp_email = basecamp_email;
		this.href = href;
		this.expires_in = expires_in;
		this.refresh_token = refresh_token;
		this.access_token = access_token;
	}

	/**
	 * @return the basecamp_email
	 */
	public String getBasecamp_email() {
		return basecamp_email;
	}

	/**
	 * @param basecamp_email the basecamp_email to set
	 */
	public void setBasecamp_email(String basecamp_email) {
		this.basecamp_email = basecamp_email;
	}

	public Basecamp_basics(){
		
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
}
