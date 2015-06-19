package com.teamchat.youtube_integration;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class Bot {
public void mainfun() {
		
		// TODO Auto-generated method stub
		 TeamchatAPI api = TeamchatAPI.fromFile("teamchat2.data")
				    .setEmail("teambott2@gmail.com") //change to your teamchat registered email id
				    .setPassword("gupshup") //change to your teamchat password
				    .startReceivingEvents(new Bot()); //Wait for other user to send message
	}

//Uploaded video

@OnKeyword("myuploads")
public void myUploads(TeamchatAPI api) {
	MyUploads mu=new MyUploads();
	try {
		api.perform(api.context().currentRoom().post( new TextChatlet(mu.myUpload())));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}	


// Help
	@OnKeyword("youtubehelp")
	public void youtubeHelp(TeamchatAPI api) {
		
		api.perform(api.context().currentRoom().post( 
				new TextChatlet("<br><div align=\"center\">Youtube Help</div>"
						+"<br>searchbykeyword &nbsp; - &nbsp; To search videos related to the keyword"
						+"<br>searchbytopic &nbsp; - &nbsp; To search videos under a category"
						+"<br>searchchannel &nbsp; - &nbsp; To search channel related to the keyword"
						+"<br>subscribechannel &nbsp; - &nbsp; To subscribe a channel using channel id"
						+"<br>channelbulletin &nbsp; - &nbsp; To post a video in your channel by providing video id"
						+"<br>createplaylist &nbsp; - &nbsp; To ceate a playlist by providing video id"
						+"<br>uploadvideo &nbsp; - &nbsp; To upload a video into your channel by providing the location"
						+"<br>myuploads &nbsp; - &nbsp; Reterive the details of the video uploaded by you"
						)));
		
	}
}
