package com.teamchat.integration.onedrive.bot;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class OneDriveBot {


	@OnKeyword("help")
	public void lowerhelp(TeamchatAPI api) {

		TeamchatPost.tpapi=api;
		String rid=api.context().currentRoom().getId();
		TeamchatURLlink urlLink = new TeamchatURLlink();
		String url="http://interns.teamchat.com:8085/OneDriveIntegration/filepicker.html?rid="+rid;
//		String urlEncoded = urlLink.createEmbeddedLink(url,"OneDrive Bot","http");
		api.perform(api.context().currentRoom().post(new PrimaryChatlet()
						.setQuestionHtml("Hey! I am <strong>OneDrive bot</strong>. You can upload files, "
										+ "get download links for your files and "
										+ "also view and edit your files on OneDrive."
								        +"<br>Type <a href=\""+url+"\" target=\"_blank\">OneDrive</a> "
								        + "to connect to your OneDrive account." )));
										}
	
	@OnKeyword("Help")
	public void upperhelp(TeamchatAPI api) 
	{
		lowerhelp(api);
	}
	
	@OnKeyword("OneDrive")
	public void OpenFile(TeamchatAPI api) {
		openfile(api);
	}
	
	@OnKeyword("Onedrive")
	public void Openfile(TeamchatAPI api) {
		openfile(api);
	}

			
	@OnKeyword("onedrive")
	public void openfile(TeamchatAPI api) {
		TeamchatPost.tpapi=api;
		String rid=api.context().currentRoom().getId();
		TeamchatURLlink urlLink = new TeamchatURLlink();
		String url="http://interns.teamchat.com:8085/OneDriveIntegration/filepicker.html?rid="+rid;
//		String urlEncoded = urlLink.createEmbeddedLink(url,"OneDrive Bot","http");
		api.perform(api.context().currentRoom().post(new PrimaryChatlet()
						.setQuestionHtml("<a href=\""+url+"\" target=\"_blank\">Connect to OneDrive</a>")
						));
	}
	
	
}
