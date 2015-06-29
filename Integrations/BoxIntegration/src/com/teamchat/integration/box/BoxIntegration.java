package com.teamchat.integration.box;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

@WebServlet("/BoxIntegration")
public class BoxIntegration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public BoxIntegration() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONArray data = new JSONArray(request.getParameter("data"));
		String roomId = request.getParameter("roomid");

		String path = "/home/intern3/Documents/data/solutions-config/workflow-data/box/box-config.properties";
		Properties configProps = new Properties();
		try {
			configProps = loadPropertyFileFromDisk(path);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		TeamchatAPI api = TeamchatAPIImpl.fromFile("data1.json")
				.setEmail(configProps.getProperty("botemail").trim())
				.setPassword(configProps.getProperty("botpass").trim());
		
		for (int i = 0; i < data.length(); i++) {
			JSONObject temp = data.getJSONObject(i);

			if (temp.getString("type").equals("folder")) {
				String s = "<a class=\"file\" href='"
						+ temp.getString("url")
						+ "' target=_blank>"
						+ "<table align=center width=342px>"
						+ "<tr><td rowspan=2 width=20%><img src='http://png-5.findicons.com/files/icons/1029/vista_folder_colors/256/red_folder.png' width=50px height=60px/></td>"
						+ "<td align=left><h5>"
						+ temp.getString("name")
						+ "</h5></td></tr>"
						+ "<tr><td colspan=2 align=center style=\"background:#CDB99C;\">Open in Box</td></tr></table>"
						+ "</a>";

				api.perform(api.context().byId(roomId)
						.post(new PrimaryChatlet().setQuestionHtml(s)));
			} else if (temp.getString("type").equals("file")) {
				String s = "<a class=\"file\" href='"
						+ temp.getString("url")
						+ "' target=_blank>"
						+ "<table align=center width=342px>"
						+ "<tr><td rowspan=2 width=20%><img src='http://icons.iconarchive.com/icons/double-j-design/origami/256/file-icon.png' width=50px height=60px/></td>"
						+ "<td align=left><h5>"
						+ temp.getString("name")
						+ "</h5></td></tr>"
						+ "<tr><td colspan=2 align=center style=\"background:#CDB99C;\">Open in Box</td></tr></table>"
						+ "</a>";
				
				api.perform(api.context().byId(roomId)
						.post(new PrimaryChatlet().setQuestionHtml(s)));
			}
		}
	}

	public static Properties loadPropertyFileFromDisk(String filePath)
			throws Exception {
		String os = System.getProperty("os.name");
		if (os.toLowerCase().contains("windows")) {
			filePath = "d:" + filePath;
		}
		Properties configProp = new Properties();
		FileInputStream fIS = new FileInputStream(filePath);
		configProp.load(fIS);
		fIS.close();
		return configProp;
	}
}