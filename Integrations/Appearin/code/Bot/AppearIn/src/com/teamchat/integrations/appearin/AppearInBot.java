package com.teamchat.integrations.appearin;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class AppearInBot {

	@OnKeyword("help")
	public void start(TeamchatAPI api) {
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion("Enter room name")
						.setReplyScreen(
								api.objects()
										.form()
										.addField(
												api.objects().input()
														.label("String")
														.name("str")))
						.alias("start")));
	}

	@OnAlias("start")
	public void connect(TeamchatAPI api) {

		String name = (api.context().currentReply().getField("str"));
		String print = "<a href=\"https://appear.in/" + name
				+ "\" target=\"_blank\">start conversation in browser</a>";
		String url = "https://appear.in/" + name;
		String print2 = createEmbeddedLink(url, name, "https");
		String print1 = "<a href=" + print2 + ">start conversation here</a>";
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(print1
						+ "<br><br><div align=\"center\">or</div><br>" + print)));
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
		//System.out.println(object.toString());
		byte[] byteArray = Base64.encodeBase64(object.toString().getBytes());
		String encodedString = new String(byteArray);
		String fUrl = protocol + "://teamchat:data=" + encodedString;

		return fUrl;

	}
}