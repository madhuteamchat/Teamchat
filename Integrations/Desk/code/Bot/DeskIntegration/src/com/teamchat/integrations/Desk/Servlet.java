package com.teamchat.integrations.Desk;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.teamchat.client.sdk.TeamchatAPI;

@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static TeamchatAPI api;

	public Servlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("In doGet()");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("In doPost()");
		request.getInputStream();
		String email = request.getParameter("email");
		@SuppressWarnings("resource")
		Scanner s = new Scanner(request.getInputStream(), "UTF-8")
				.useDelimiter("\\A");
		String l = s.hasNext() ? s.next() : "";
		String l1 = URLDecoder.decode(l, "UTF-8");
		JSONObject j = new JSONObject(l1);
		String message = j.getString("message");
		DeskBot.PostCase(api, message, email);
	}

}