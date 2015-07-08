package chatletcreator;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;
import com.teamchat.client.utils.TeamchatContextListener;

public class PrintChatlet
{
	public static TeamchatAPI api;
	
	@OnKeyword("create")
	public void onCreate(TeamchatAPI api){
		this.api=api;
		api.performPostInCurrentRoom(new PrimaryChatlet().setQuestionHtml("<html><body><button onclick='myFunction()'>Click here</button><script>function myFunction(){window.open('http://interns.teamchat.com:8086/chatletcreator/Chatlet-main.html');} </script></body></html>"));
	}
	
	public void set(String output,String email)throws Exception
	{
		Room r=api.context().create().setName("p2p").add(email);
		api.perform(r.post(new TextChatlet(output)));

		}

}
