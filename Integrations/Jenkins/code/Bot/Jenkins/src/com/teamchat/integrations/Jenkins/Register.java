package com.teamchat.integrations.Jenkins;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Register
 */
@WebServlet(description = "Servelet to enable Register", urlPatterns = { "/Register" })
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
    throws ServletException, IOException
{
    	String email=request.getParameter("email");
    	String id=request.getParameter("id");
    	response.setContentType("text/html");
System.out.println("id is "+id);
        PrintWriter out = response.getWriter();
  	    String docType =   "<!doctype html public \"-//w3c//dtd html 4.0 " +
                           "transitional//en\">\n";
        out.println(docType +
                  "<html>"
                  + "<body>"
                  + "<h3 align='center'>"
                 + "<img src='logoteamchat.png' align='middle'>"

                  +"<form action='UID' method='GET'>"
                  		+ "<h2><font color='#159CEB'><u>Jenkins UID Generation</u></font><br/></h2><br/><br/>"
                  		+ "<input type='hidden' name='email' value='"+email+"'><br/>"
                  		+ "<input type='hidden' name='userid' value='"+id+"'><br/>"
                  		+ "<font color='#159CEB'>Jenkins Name</font> <br/>"
                  		+ "<input type='text' name='name'><br/><br/>"
                  	    + "<font color='#159CEB'>Click Below to Generate a New UID</font><br/>"
                	    + "<input type='submit' name='generate' value='Generate'  />"
                  + "</form>"
                 + "</h3>"
                 + "</body>"
                 + "</html>");
}

}
