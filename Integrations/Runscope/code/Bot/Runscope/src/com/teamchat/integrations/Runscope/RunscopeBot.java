package com.teamchat.integrations.Runscope;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class RunscopeBot {
	public static final String client_id = "2dae3556-1220-49e5-b67d-57fb0dad7c70";
	public static final String client_secret = "be856147-2dc7-441f-8aa0-6122febaca92";
	public static final String callback_url = "http://localhost:8080/Runscope/Servlet";
	public static String access_token;
	public static String[] name;
	public static String[] key;
	public static String[] desc;
	public static String[] rslt;

	@OnKeyword("help")
	public void Login(TeamchatAPI api) {
		Servlet.api = api;
		String intro = "Hey this is Runscope Bot. You can use me to view the results of API tests currently assigned to any of your buckets.<table style=width:100%><tr><th>Keywords</th><th>Function</th></tr><tr><td>select</td><td>Select from a list of buckets</td></tr><tr><td>help</td><td>Log in to Runscope</td></tr>You have to log in first to select your buckets.<br>";
		String msg = "<a href=https://www.runscope.com/signin/oauth/authorize?response_type=code&client_id="
				+ client_id
				+ "&redirect_uri="
				+ callback_url
				+ "&scope=api:read%20account:email%20message:write%20bucket:auth_token%20bucket:write&state=PLACEHOLDER target=_blank>Click here to Login</a>";
		api.performPostInCurrentRoom(new PrimaryChatlet().setQuestionHtml(intro
				+ msg));
	}

	@OnKeyword("select")
	public static void selectBucket(TeamchatAPI api) {
		JSONArray j = RunscopeFetch.getBuckets();
		name = new String[j.length()];
		key = new String[j.length()];
		for (int i = 0; i < j.length(); i++) {
			JSONObject j1 = j.getJSONObject(i);
			name[i] = j1.getString("name");
			key[i] = j1.getString("key");
		}
		Form f = api.objects().form();
		f.addField(getOptions(api, name));
		api.performPostInCurrentRoom(new PrimaryChatlet()
				.setQuestionHtml(
						"Select the bucket for which you want results.")
				.setReplyScreen(f).setReplyLabel("Select").alias("onSelect"));
	}

	public static Field getOptions(TeamchatAPI api, String[] name) {
		Field f = api.objects().select().label("Buckets").name("bucket");
		for (int i = 0; i < name.length; i++) {
			f.addOption(name[i]);
		}
		return f;
	}

	@OnAlias("onSelect")
	public void Display(TeamchatAPI api) {
		String bucket = api.context().currentReply().getField("bucket");
		int i;
		for (i = 0; i < name.length; i++)
			if (name.equals(bucket))
				break;
		String id = key[i - 1];
		JSONArray j = RunscopeFetch.getTest(id);
		desc = new String[j.length()];
		rslt = new String[j.length()];
		for (int k = 0; k < j.length(); k++) {
			JSONObject j1 = j.getJSONObject(k);
			desc[k] = j1.getString("name");
			rslt[k] = j1.getString("result");
			api.performPostInCurrentRoom(new PrimaryChatlet()
					.setQuestionHtml("<b>" + desc[k] + ":</b><br>"
							+ RunscopeFetch.getResutls(rslt[k])));
		}
	}
}