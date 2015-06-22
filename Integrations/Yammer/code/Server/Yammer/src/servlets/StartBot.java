package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import teamchat.YammerBot;

public class StartBot extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		      throws ServletException, java.io.IOException {
		YammerBot bot=new YammerBot();
		bot.initialize();
		
	}
    
}
