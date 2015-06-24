package com.bot;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;
import com.teamchat.client.utils.TeamchatContextListener;

/**
 * Servlet implementation class Bot
 */
public class Bot 
{

	
	@OnKeyword(value = "hello")
	public void start(TeamchatAPI api)
	{
		System.out.println("Inside Hello....");
	}
	
	
	
	
}
