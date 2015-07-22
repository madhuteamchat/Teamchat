package com.teamchat.integration.box;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.utils.WebAppTeamChatAPI;

@WebServlet("/AuthHandler")
public class AuthHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public AuthHandler() {
		super();
	}
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Request");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray data = new JSONArray(request.getParameter("data"));
		String roomId = request.getParameter("roomid");
		System.out.println(roomId);
		
		for (int i = 0; i < data.length(); i++) {
			JSONObject temp = data.getJSONObject(i);
			System.out.println(temp.get("name"));

			if (temp.getString("type").equals("folder")) {
				String s = "<a href='" + createEmbeddedLink(temp.getString("url"), "Folder", "http") + "' target=_blank>"
						+ "<table align=center width=100%>"
						+ "<tr><td align=center><img src='http://icons.iconarchive.com/icons/tpdkdesign.net/refresh-cl/256/folder-blue-mydocuments-icon.png' width=50px height=60px/></td></tr>"
						+ "<tr><td align=center><h5>" + temp.getString("name") + "</h5></td></tr></table>"
						+ "<div align=center style=\"border-radius: 5px;border: 2px solid #0000FF;\">Open in Box</div></a>";
				
				TeamchatAPI api = WebAppTeamChatAPI.getTeamchatAPIInstance(getServletConfig());
				api.perform(api.context().byId(roomId)
						.post(new PrimaryChatlet().setQuestionHtml(s)));
			} else if (temp.getString("type").equals("file")) {
				System.out.println(temp.get("name"));
				String s = "<a href='" + createEmbeddedLink(temp.getString("url"), "File", "http") + "' target=_blank>"
						+ "<table align=center width=100%>"
						+ "<tr><td align=center><img src='http://autoclicklil.noponfix.com/gallery/file.png' width=50px height=60px/></td></tr>"
						+ "<tr><td align=center><h5>" + temp.getString("name") + "</h5></td></tr></table>"
						+ "<div align=center style=\"border-radius: 5px;border: 2px solid #0000FF;\">Open in Box</div></a>";
				
				TeamchatAPI api = WebAppTeamChatAPI.getTeamchatAPIInstance(getServletConfig());
				api.perform(api.context().byId(roomId)
						.post(new PrimaryChatlet().setQuestionHtml(s)));
			}
		}
	}
	
	public static String createEmbeddedLink(String url, String title, String protocol)
	{
		JSONObject object = new JSONObject();
		JSONObject web = new JSONObject();
		web.put("title", title);
		web.put("cancelBtnName", "Back");
		web.put("minWidth", "200");
		web.put("draggable", "true");
		web.put("newWindow", "true");
		web.put("url", url);
		object.put("web", web);
		System.out.println(object.toString());
		byte[] byteArray = Base64.encodeBase64(object.toString().getBytes());
		String encodedString = new String(byteArray);
		String fUrl = protocol + "://teamchat:data=" + encodedString;

		return fUrl;

	}
}