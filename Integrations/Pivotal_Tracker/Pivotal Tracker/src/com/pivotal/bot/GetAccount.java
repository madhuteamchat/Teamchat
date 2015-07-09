package com.pivotal.bot;
import com.google.gson.Gson;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.TeamchatAPI;

public class GetAccount {
	public Field getAccount(String token, TeamchatAPI api) {
		final String USER_AGENT = "Mozilla/5.0";
		String URL = "https://www.pivotaltracker.com/services/v5/accounts";
		String URL_parameter = "";
		SendGet sg = new SendGet();
		Field f = null;
		try {
			String jsonData = sg.sendGet(URL, USER_AGENT, URL_parameter, token);
			//debug
			System.out.println("OUTPUT IS !!! : " +jsonData);
			Gson gson = new Gson();
			Data[] Accounts = gson.fromJson(jsonData,
					Data[].class);

			f = api.objects().select().name("account_name")
					.label("Accounts");
			for (Data account : Accounts) {
				f.addOption(account.getName() + "-" + account.getId());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return f;
	}
}
