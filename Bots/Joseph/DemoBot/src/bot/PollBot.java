/*package bot;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PollChatlet;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.impl.TeamchatAPIImpl;

public class PollBot {
	
	@OnKeyword("demo")
	public void livestatus(TeamchatAPI api) {
		String ht="<!DOCTYPE html>"
                  +"<html>"
                  +"<body>";

		api.perform(
				api.context().currentRoom().post(
				new PollChatlet()
			    .setQuestionHtml(ht+
			    		"<img src=\"http://images.mitre.com/images/mitre-polarize-t-shirt-p177-1103_zoom.jpg\" alt=\"tshirt\" width=\"250px\" /><br/>"
			    		+"Do you like this t-shirt?"
			    		+"</body>"
			    		+"</html>"
			    		)
			    .alias("response")
				)
				);	
	}
	
	@OnAlias("response")
	public void productLiked(TeamchatAPI api)
	{
	  String respond=api.context().currentReply().getField("resp");
	  if(respond.equals("yes")) {
		  api.perform(
			api.context().currentRoom().post(
					new PollChatlet()
				    .setQuestion("Do you want to purchase this product?")
				    .alias("purchase")));
		  
	  }	
	}
	
	@OnAlias("purchase")
	public void purchase(TeamchatAPI api)
	{
	  String respond=api.context().currentReply().getField("resp");
	  if(respond.equals("yes")) {
		  api.perform(
			api.context().currentRoom().post(
					new PrimaryChatlet()
				   .setQuestion("Place your order")
				   .setReplyScreen(api.objects().form()
				   .addField(api.objects().input().label("Name").name("name"))
				   .addField(api.objects().input().label("Location").name("location"))
				   .addField(api.objects().input().label("Quantity").name("quantity"))
				   .addField(api.objects().select().label("Mode of payment").name("payment")
				   .addOption("Cash on delivery")
				   .addOption("By credit/debit card")
				   )
				    )
				    .alias("Submit")
				    ));
	  }	
	}
	

}
*/