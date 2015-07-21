package com.integration.teamchat.main.onenote;

import java.net.URLEncoder;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class Mainonenote
{
	Config_handler c = new Config_handler();

	@OnKeyword("onenote")
	public void ononenote(TeamchatAPI api) throws Exception
	{
		System.out.println(c.configProps.getProperty("client_id"));
		Token.api1 = api;
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml("<h4 style=\"display:block;margin:0 auto;\">Login with OneNote</h4>"
						+ "<hr/>"
						+ "<a class=\"login-button\" target=\"_blank\" href=\""
						+ "https://login.live.com/oauth20_authorize.srf?client_id="
						+ c.configProps.getProperty("client_id")
						+ "&scope=office.onenote_update&response_type=token&redirect_uri="
						+ URLEncoder.encode(c.configProps.getProperty("redirect_url"), "UTF-8")
						+ "\" style=\"display: block;box-sizing: border-box; margin: 0 auto;font-size: 21px;color: #fff;padding: 15px;border-radius: 6px;background-color: rgb(128, 57, 123);\">Login</a>")));
	}

	@OnKeyword("page")
	public void onpage(TeamchatAPI api) throws Exception
	{

		Token t = new Token();
		t.display();

	}

}
