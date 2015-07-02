package com.teamchat.integrations.wunderlist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class WebhookServlet
 */
public class WebhookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WebhookServlet() {
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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String inputline = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		StringBuffer sb = new StringBuffer();
		while ((inputline = in.readLine()) != null)
			sb.append(inputline);
		in.close();
//		System.out.println("**************************************");
//		System.out.println("JSON response in Webhook servlet is: "
//				+ sb.toString());
//		System.out.println("**************************************");
		new SendResponseToUsers().send(sb.toString());
	}
}
