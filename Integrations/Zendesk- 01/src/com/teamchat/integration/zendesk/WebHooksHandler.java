package com.teamchat.integration.zendesk;

import java.io.IOException;

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
import com.teamchat.client.utils.WebAppTeamChatAPI;

/**
 * Servlet implementation class Integrator
 */
@WebServlet("/WebHooksHandler")
public class WebHooksHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WebHooksHandler() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String ticketDataRecd = request.getParameter("datajson").trim();
			ticketDataRecd = ticketDataRecd.replace(System.getProperty("line.separator"), "");
			
			JSONObject ticketData = new JSONObject(ticketDataRecd);
			String ticketId = String.valueOf(ticketData.getInt("ticketId"));
			String comment = ticketData.getString("pubComment");
			comment = comment.substring(comment.lastIndexOf(" pm") + 3);

			DBHandler checkData = new DBHandler();
			String[] roomForm = checkData.getRoomForm(ticketId);
			
			TeamchatAPI api = WebAppTeamChatAPI.getTeamchatAPIInstance(getServletConfig());
			Room r = api.context().byId(roomForm[0]);
			Chatlet c = api.context().chatletById(roomForm[1]);
			Reply rep = c.createReply().addField("upCom", comment);
			
			api.perform(r.reply(rep));
		} catch (Exception e) {
			System.out.println(e.getCause());
		}
	}
}