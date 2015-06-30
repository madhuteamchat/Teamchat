package com.teamchat.integration.onedrive.bot;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class OneDriveBot {


	@OnKeyword("help")
	public void help(TeamchatAPI api) {

		TeamchatPost.tpapi=api;
		String rid=api.context().currentRoom().getId();
		api.perform(api.context().currentRoom().post(new PrimaryChatlet()
						.setQuestionHtml("Hey! I am OneDrive bot. You can upload files, get download links for your files and also view and edit your files on OneDrive."
								        +"<br>Type <a href=\"http://interns.teamchat.com:8080/OneDriveIntegration/filepicker.html?rid="+rid+"\" target=\"_blank\">onedrive</a> "
								        + "to connect to your OneDrive account." )));
										}
	
	
	
	@OnKeyword("onedrive")
	public void openfile(TeamchatAPI api) {
		TeamchatPost.tpapi=api;
		String rid=api.context().currentRoom().getId();
		api.perform(api.context().currentRoom().post(new PrimaryChatlet()
						.setQuestionHtml("<a href=\"http://interns.teamchat.com:8080/OneDriveIntegration/filepicker.html?rid="+rid+"\" target=\"_blank\">Connect to OneDrive</a>")
						));
	}
	
	
}
