package com.integration;

public class Hipchat_basiccheckbot
{
	private String email,access_token,notify_token;

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
	public String getnotify_token() {
		return notify_token;
	}

	public void setnotify_token(String notify_token) {
		this.notify_token = notify_token;
	}
	public Hipchat_basiccheckbot(){
		
	}
	
	public Hipchat_basiccheckbot(String email,String access_token,String notify_token) {
		super();
		this.email = email;
		this.access_token = access_token;
		this.notify_token=notify_token;
	}
}
