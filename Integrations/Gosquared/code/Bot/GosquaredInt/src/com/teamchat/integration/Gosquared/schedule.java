package com.teamchat.integration.Gosquared;

import java.io.IOException;
import java.util.TimerTask;

import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;

public class schedule extends TimerTask {
	TeamchatAPI api;
	String msg, smail, schedule,apikey1,stoken;

	public schedule(TeamchatAPI api, String smail) throws IOException {
		this.api = api;
     this.smail = smail;
		
		 apikey1 = ManageDB.retrieve(smail);
       stoken = ManageDB.retrieve1(smail);
		test ts = new test();
		schedule = ts.onCreate(api, apikey1, stoken);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("hiii in schedule");
		Room r = api.context().create().setName("rtrt").add(smail);

		api.perform(r.post(new PrimaryChatlet()
		.setQuestionHtml(schedule)));
	}

}
