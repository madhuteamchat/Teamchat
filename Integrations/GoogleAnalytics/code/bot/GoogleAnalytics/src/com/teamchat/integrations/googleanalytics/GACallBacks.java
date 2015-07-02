package com.teamchat.integrations.googleanalytics;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GACallback
 */
@WebServlet("/GACallBacks")
public class GACallBacks extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GACallBacks() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("CallBack==" + request.getParameter("state"));
		GAConnect gac = new GAConnect();
		int resp_code = gac.getaccesstoken(request.getParameter("code"),
				request.getParameter("state"));
		response.setContentType("text/html");
		// New location to be redirected
		String site = "";
		if (resp_code <= 200) {
			site = "success.html";
		} else {
			site = "error.html";
		}
		response.sendRedirect(site);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
