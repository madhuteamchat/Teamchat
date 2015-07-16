package com.teamchat.integrations.Jenkins;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UID
 */
@WebServlet("/UID")
public class UID extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UID() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Called ");
		String email=request.getParameter("email");
		String userid=request.getParameter("userid");
		String name=request.getParameter("name").replace(' ', '-');
		Database db=new Database();
		String uid=db.generateUID(email,userid,name);
		
		response.setContentType("text/html");
		System.out.println(email);
		        PrintWriter out = response.getWriter();
		  	    String docType =
		        "<!doctype html public \"-//w3c//dtd html 4.0 " +
		        "transitional//en\">\n";
		        out.println(docType +
		                  "<html>"
		                  + "<body>"
		                  +"<h3 align='center'>"
		                  + "<img src='logoteamchat.png'><br/>"
		               	  + "<font color='#159CEB'><u>Jenkins UID Generation</u></font><br/><br/><br/>"
	                	  +"<font color='#159CEB'>Your Jenkins-CI Name:  <"+name+" <br/><br/> <font color='#159CEB'>UID:  "+uid+"</font>"
		                 + "</h3>"
		                 + "<div align='center' style='color:blue;margin:auto;'>Download Teamchat-Notification Plugin from Jenkins<br/>"
		                 + "<br/>Enter this UID in Jenkins>Configure>Global Teamchat Settings>UID<br/>"
		                 + "<img src='img2.png' width='600' height='60'><br/><img src='img1.png'width='600' height='60'>"
		                 + "<br/><br/>Go to any room, Add Jenkins-Bot<br/>"
		                 + "<br/>Type Start to start getting Notifications<br/>"
		                 + "</div></body>"
		                 + "</html>");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
}
