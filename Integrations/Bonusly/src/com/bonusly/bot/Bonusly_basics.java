package com.bonusly.bot;

public class Bonusly_basics {
	private String email, access_token, teamchat_email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public void setTeamchatEmail(String teamchat_email) {
		this.teamchat_email = teamchat_email;
	}

	public String getTeamchatEmail(String teamchat_email) {
		return teamchat_email;
	}

	public Bonusly_basics() {

	}

	public Bonusly_basics(String email, String access_token) {
		super();
		this.email = email;
		this.access_token = access_token;
	}
}