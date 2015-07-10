package com.teamchat.integration.onedrive.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teamchat.integration.onedrive.bot.TeamchatPost;

/**
 * Servlet implementation class GetFileList
 */
@WebServlet("/GetFileList")
public class GetFileList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetFileList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.err.println(request.getParameter("fname"));
//		String filenames[]=request.getParameter("filenames").split(",");
////		String filelinks[]=request.getParameter("filelinks").split(",");
//		String msg="";
//		for(int i=0;i<filenames.length;i++)
//		{
//			msg+=filenames[i]+"<br>";
//		}
//		msg+="<a href=\""+request.getParameter("link")+"\" target=\"_blank\"> View all </a>";
//		TeamchatPost.postMsg(msg);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String link="null";
		String rid=req.getParameter("rid");
		String filenames[] = null;
		String filelinks[] =null;
//		res.setContentType("text/plain");
//	    PrintWriter out = res.getWriter();

//	    out.println("Query String:");
//	    out.println(req.getQueryString());
//	    out.println();

//	    out.println("Request Parameters:");
	    Enumeration enumm = req.getParameterNames();
	    while (enumm.hasMoreElements()) {
	      String name = (String) enumm.nextElement();
	      String values[] = req.getParameterValues(name);
	      if (values != null) {
	        for (int i = 0; i < values.length; i++) {
//	          out.println(name + " (" + i + "): " + values[i]);
	          if(name.trim().equals("link"))
	        	  link=values[i];
	          else if(name.trim().equals("filenames"))
	        	  filenames=values[i].split(",");
	          else if(name.trim().equals("filelinks"))
	        	  filelinks=values[i].split(",");
	        }
	      }
	    }
	    String msg="";
	    for(int i=0;i<filenames.length;i++)
	    {
	    	msg+="<a href=\""+filelinks[i]+"\" target=\"_blank\"> "+filenames[i]+" </a><br>";
	    }
	    if(!link.equals("null"))
		msg+="<a href=\""+link+"\" target=\"_blank\"> View all </a>";
	    else
	    	msg="Download links:<br>"+msg;
	    System.out.println("Rid="+rid);
		new TeamchatPost().postMsg(msg,rid);
//		res.sendRedirect(res.encodeRedirectURL("http://interns.teamchat.com:8085/OneDriveIntegration/filepicker.html?rid="+rid));
		PrintWriter out=res.getWriter();
		out.println("<script>window.close();</script>");
	}

}
