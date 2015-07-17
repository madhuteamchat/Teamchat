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
	
	@OnKeyword("help")
	public void onCreate(TeamchatAPI api){
		System.out.println("on create");
		this.api=api;
		System.out.println("Checkpoint");
		//api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestion("Check")));
		api.performPostInCurrentRoom(new PrimaryChatlet().setQuestionHtml("<html><body><button onclick='myFunction()'>Click here to create your own Chatlet</button><script>function myFunction(){window.open('http://localhost:8080/chatletcreator/index.html');} </script></body></html>"));
	}
	
	@OnKeyword("hello")
	public void onHello(TeamchatAPI api){
		System.out.println("on create");
		this.api=api;
		System.out.println("Checkpoint");
		//api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestion("Check")));
		//api.performPostInCurrentRoom(new PrimaryChatlet().setQuestionHtml("<html><body><button onclick='myFunction()'>Click here to create your own Chatlet</button><script>function myFunction(){window.location.href='/Chatlet-main.html'} </script></body></html>"));
	}
	
	public void set(String output,String email)throws Exception
	{
		System.out.println("posting in room");
		Room r=api.context().create().setName("p2p").add(email);
		api.perform(r.post(new TextChatlet(output)));

		}

}
