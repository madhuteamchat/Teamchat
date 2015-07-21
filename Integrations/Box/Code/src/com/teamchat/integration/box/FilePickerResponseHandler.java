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
import com.teamchat.integration.box.utils.Utility;

@WebServlet("/FilePickerResponseHandler")
public class FilePickerResponseHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public FilePickerResponseHandler() {
		super();
	}
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Request");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray data = new JSONArray(request.getParameter("data"));
		String roomId = request.getParameter("roomid");
		
		for (int i = 0; i < data.length(); i++) {
			JSONObject temp = data.getJSONObject(i);
			String link = temp.getString("url");
			String name = temp.getString("name");
			String type = name.substring(name.lastIndexOf(".") + 1);
			name = name.substring(0, name.lastIndexOf("."));
			String icon = null;
			String html = Utility.shareDocHTML;

			if (temp.getString("type").equals("folder")) {
				icon = "http://icons.iconarchive.com/icons/tpdkdesign.net/refresh-cl/256/folder-blue-mydocuments-icon.png";
			} else if (temp.getString("type").equals("file")) {
				icon = "http://autoclicklil.noponfix.com/gallery/file.png";
			}
			
			html = html.replace("__link", link);
			html = html.replace("__name", name);
			html = html.replace("__imgUrl", icon);
			html = html.replace("__size", "");
			html = html.replace("__type", type + " file");
			html = html.replace("__desc", "");
			html = html.replace("__openDesc", "Open in Box");
			TeamchatAPI api = WebAppTeamChatAPI.getTeamchatAPIInstance(getServletConfig());
			api.perform(api.context().byId(roomId)
					.post(new PrimaryChatlet().setQuestionHtml(html)));
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