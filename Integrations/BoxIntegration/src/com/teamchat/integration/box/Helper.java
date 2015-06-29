package com.teamchat.integration.box;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

public class Helper {
	public static String getBase64Data(String data) {
		return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
	}

	public static String getTeamchatEmbeddUrl(String url, String title, String protocol) throws JSONException {
		JSONObject object = new JSONObject();
		JSONObject web = new JSONObject();
		web.put("title", title);
		web.put("cancelBtnName", "Back");
		web.put("minWidth", "500");
		web.put("draggable", "true");
		web.put("newWindow", "true");
		web.put("url", url);
		object.put("web", web);
		System.out.println(object.toString());
		String fUrl = protocol + "://teamchat:data=" + getBase64Data(object.toString());
		return fUrl;
	}
}
