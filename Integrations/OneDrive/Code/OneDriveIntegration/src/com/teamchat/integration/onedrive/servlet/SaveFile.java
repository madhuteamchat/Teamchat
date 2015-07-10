package com.teamchat.integration.onedrive.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teamchat.integration.onedrive.bot.TeamchatPost;

/**
 * Servlet implementation class SaveFile
 */
@WebServlet("/SaveFile")
public class SaveFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveFile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String rid=request.getParameter("rid");
		if(request.getParameter("msg").equals("success"))
		{
			String msg="Uploaded Successfully";
			new TeamchatPost().postMsg(msg,rid);
//			res.sendRedirect(res.encodeRedirectURL("http://interns.teamchat.com:8085/OneDriveIntegration/filepicker.html?rid="+rid));
		}
		else if(request.getParameter("msg").equals("cancel"))
		{
			String msg="Cancelled";
			new TeamchatPost().postMsg(msg,rid);
//			res.sendRedirect(res.encodeRedirectURL("http://interns.teamchat.com:8085/OneDriveIntegration/filepicker.html?rid="+rid));
		}
		else if(request.getParameter("msg").equals("error"))
		{
			String msg=request.getParameter("msg");
			new TeamchatPost().postMsg(msg,rid);
//			res.sendRedirect(res.encodeRedirectURL("http://interns.teamchat.com:8085/OneDriveIntegration/filepicker.html?rid="+rid));
		}
		PrintWriter out=res.getWriter();
		out.println("<script>window.close();</script>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
