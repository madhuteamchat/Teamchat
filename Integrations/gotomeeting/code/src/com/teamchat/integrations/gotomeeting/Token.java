package com.teamchat.integrations.gotomeeting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONException;
import org.json.JSONObject;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class Token
{
	public void getCredentials(TeamchatAPI api,String teamchatUserEmail)
	{
		api.perform(api.context().create().setName("p2p").add(teamchatUserEmail)
				.post((new PrimaryChatlet().setQuestion("Fill in your GoToMeeting Credentials.").setReplyScreen(api.objects().form().addField(api.objects().input().label("Email").name("email")).addField(api.objects().input().label("Password").name("pwd"))).alias("oncreds"))));
	}

	public String getAccessToken(String email, String pwd) throws IOException, JSONException
	{
		URL url = new URL("https://api.citrixonline.com/oauth/access_token?grant_type=password&user_id=" + email + "&password=" + pwd + "&client_id=l3t1BHRqzL4tuUcdCHPjUULRzCVHGlUr");
		URLConnection yc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		StringBuilder response = new StringBuilder();
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		
		String out = response.toString();
		in.close();

		JSONObject jobject = new JSONObject(out);
		String accTok = (String) jobject.get("access_token");

		return (accTok);
	}

}
