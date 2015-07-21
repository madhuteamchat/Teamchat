package com.teamchat.integration;
public class LoginData {
	private String password;
	private String  emailId;

	public LoginData (String email,String password) {
		
		this.emailId = email;
		this.password = password;
	}

	public String getEmail() {
		return emailId;
	}

	public void setEmail(String email) {
		this.emailId = email;
	}
	
	public String getPassword() {
		return emailId;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
