package com.teamchat.integration.office365.webhook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Office365Webhook
 */
@WebServlet("/Office365Webhook")
public class Office365Webhook extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Office365Webhook() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("############Office365 Notification###################");
		String res="";
		Enumeration enumm = request.getParameterNames();
	    while (enumm.hasMoreElements()) {
	      String name = (String) enumm.nextElement();
	      String values[] = request.getParameterValues(name);
	      if (values != null) {
	        for (int i = 0; i < values.length; i++) {
	        	res=values[i];
	          System.out.println(name + " (" + i + "): " + values[i]);
	        }
	      }
	    }
	    
	 new TeamchatPost().postjsonMsg(res);
	}

}
