package com.teamchat.integration.zendesk.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.teamchat.client.sdk.Chatlet;
import com.teamchat.client.sdk.Reply;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;

/**
 * Servlet implementation class Integrator
 */
@WebServlet("/integrator")
public class ZendeskIntegrator extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ZendeskIntegrator() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request,HttpServletResponse response) {
		String path = "/data/solutions-config/workflow-data/zendesk/zendesk-config.properties";
		Properties configProps = new Properties();
		
		try {
			configProps = loadPropertyFileFromDisk(path);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		String ticketDataRecd = request.getParameter("datajson");
		JSONObject ticketData = new JSONObject(ticketDataRecd);

		String ticketId = String.valueOf(ticketData.getInt("ticketId"));
		String comment = ticketData.getString("comment");
		//String status = ticketData.getString("status");

		DBHandler checkData = new DBHandler();
		String[] roomForm = checkData.getRoomForm(ticketId);

		TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data")
				.setEmail(configProps.getProperty("botemail").trim()).setPassword(configProps.getProperty("botpass").trim()).setEndPoint(configProps.getProperty("endpoint").trim());
		
		Room r = api.context().byId(roomForm[0]);
		Chatlet c = api.context().chatletById(roomForm[1]);
		Reply rep = c.createReply().addField("upCom", comment);
		api.perform(r.reply(rep));
	}
	
	public static Properties loadPropertyFileFromDisk(String filePath) throws Exception
	{
		String os = System.getProperty("os.name");
		if (os.toLowerCase().contains("windows"))
		{
			filePath = "d:" + filePath;

		}
		Properties configProp = new Properties();
		FileInputStream fIS = new FileInputStream(filePath);
		configProp.load(fIS);
		fIS.close();
		return configProp;
	}
}