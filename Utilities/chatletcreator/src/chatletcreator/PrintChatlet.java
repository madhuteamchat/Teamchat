package chatletcreator;


import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

public class PrintChatlet
{

	public void set(String output,String email)throws Exception
	{
		TeamchatAPI api = TeamchatAPIImpl.fromFile("teamchat1.data")
				.setEmail("bot888@outlook.com")
				.setPassword("bazingacooper04");

		Room r=api.context().create().setName("p2p").add(email);
		api.perform(r.post(new TextChatlet(output)));

		}

}