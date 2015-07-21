package com.teamchat.integration.dropbox;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.utils.WebAppTeamChatAPI;
import com.teamchat.integration.dropbox.utils.Utility;

/**
 * Servlet implementation class ChooserResponse
 */
@WebServlet("/ChooserResponse")
public class ChooserResponseHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChooserResponseHandler() {
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
		JSONArray data = new JSONArray(request.getParameter("data"));
		String roomId = request.getParameter("roomid");

		System.out.println(data.toString());

		TeamchatAPI api = WebAppTeamChatAPI
				.getTeamchatAPIInstance(getServletConfig());
		Room r = api.context().byId(roomId);

		for (int i = 0; i < data.length(); i++) {
			String name = data.getJSONObject(i).getString("name");
			String type = name.substring(name.lastIndexOf(".") + 1);
			name = name.substring(0, name.lastIndexOf("."));
			String link = data.getJSONObject(i).getString("link");
			String size = null;
			String icon = null;
			//String desc = null;

			double temp = (double) (data.getJSONObject(i).getLong("bytes")) / 1024;
			System.out.println(temp);
			if (temp > 1024)
				size = new DecimalFormat("#.#").format((((double) (data
						.getJSONObject(i).getLong("bytes")) / 1024 / 1024)))
						+ "MB";
			else
				size = new DecimalFormat("#.##").format((((double) (data
						.getJSONObject(i).getLong("bytes")) / 1024))) + "KB";

			String html = Utility.shareDocHTML;
			if (type.equals("png") || type.equals("jpg") || type.equals("jpeg") || type.equals("mp4")) {
				icon = data.getJSONObject(i).getString("thumbnailLink");
			} else {
				icon = data.getJSONObject(i).getString("icon");
			}
			
			html = html.replace("__link", link);
			html = html.replace("__imgUrl", icon);
			html = html.replace("__name", name);
			html = html.replace("__size", size);
			html = html.replace("__type", type + " file");
			html = html.replace("__desc", "");
			html = html.replace("__openDesc", "Open in Dropbox");
			
			api.perform(r.post(new PrimaryChatlet().setQuestionHtml(html)));
		}
	}

	public static String createEmbeddedLink(String url, String title,
			String protocol) {
		JSONObject object = new JSONObject();
		JSONObject web = new JSONObject();
		web.put("title", title);
		web.put("cancelBtnName", "Back");
		web.put("minWidth", "1000");
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
