package com.integration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.integration.hipchat.webhooks.RoomMessage;
import com.integration.Mainbot;
import com.teamchat.client.sdk.TeamchatAPI;

/**
 * Servlet implementation class Hipchat_webhuk
 */
@WebServlet(description = "Webhook servlet", urlPatterns =
{ "/Hipchat_webhooks" })
public class Hipchat_webhooks extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Hipchat_webhooks()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		out.println("Hello");
		System.err.println(request.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try
		{
			InputStream inputStream = request.getInputStream();
			if (inputStream != null)
			{
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0)
				{
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else
			{
				stringBuilder.append("");
			}
		} catch (IOException ex)
		{
			throw ex;
		} finally
		{
			if (bufferedReader != null)
			{
				try
				{
					bufferedReader.close();
				} catch (IOException ex)
				{
					throw ex;
				}
			}
		}

		body = stringBuilder.toString();
		Gson gson = new Gson();
		RoomMessage rn = gson.fromJson(body, RoomMessage.class);
		String message = rn.getItem().getMessage().getMessage();
		Notifier n = new Notifier();
		n.Postify(message);
	}

}
