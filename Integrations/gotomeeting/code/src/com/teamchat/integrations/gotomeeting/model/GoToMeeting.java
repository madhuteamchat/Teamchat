package com.teamchat.integrations.gotomeeting.model;

public class GoToMeeting
{

	private String email;
	private String password;

	public GoToMeeting(String email, String password)
	{
		super();
		this.email = email;
		this.password = password;
	}

	@Override
	public String toString()
	{
		return "GoToMeeting [email=" + email + ", password=" + password + "]";
	}

	public String getEmail()
	{
		return email;
	}

	public String getPassword()
	{
		return password;
	}

}
