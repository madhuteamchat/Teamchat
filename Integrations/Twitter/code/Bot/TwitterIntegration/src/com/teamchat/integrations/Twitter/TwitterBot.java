package com.teamchat.integrations.Twitter;

import java.util.List;

import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.*;
import com.teamchat.client.sdk.Form;
//import com.teamchat.client.utils.TeamchatContextListener;

import twitter4j.DirectMessage;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.MediaEntity;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.auth.AccessToken;

public class TwitterBot {
	static Twitter twitter;
	static TwitterFactory tf, tf1;
	static ConfigurationBuilder cb = new ConfigurationBuilder();
	static ConfigurationBuilder cb1 = new ConfigurationBuilder();
	static ConfigurationBuilder cb2 = new ConfigurationBuilder();
	static RequestToken requestToken;
	static AccessToken accessToken;
	static String consumerkey = "StVbhY7J8qc6dGsERO30cpffM";
	static String consumersecret = "xAja5GM79qSpPN6yHbyEBYS1yxcqRcLyg7xYuiF5cJia3nalbW";

	static {
		cb.setDebugEnabled(true).setOAuthConsumerKey(consumerkey)
				.setOAuthConsumerSecret(consumersecret);
		tf = new TwitterFactory(cb.build());
		twitter = tf.getInstance();
	}

	@OnKeyword("help")
	public void Help(TeamchatAPI api) {
		requestToken=null;
		accessToken=null;
		String intro = "<b>Hey, this is Twitter Bot!</b><br><img src=http://www.socialmediatoday.com/sites/default/files/BenKerryPreciseEnglish/files/twitter%202.png alt=twitter.png style=width:230px;height:90px;><br><i>You can use me to tweet, send messages and get tweets on hashtags from twitter.<br>Use following keywords.</i>";
		String msg = intro
				+ "<table style=width:100%><tr><th>Keywords</th><th>Function</th></tr><tr><td>tweet</td><td>Post a tweet</td></tr><tr><td>gettweet</td><td>Get tweets from your home timeline</td></tr><tr><td>directmsg</td><td>Send message to a user</td></tr><tr><td>search</td><td>Search a keyword</td></tr><tr><td>logout</td><td>Log out of twitter</td></tr></table>";
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(msg)));
		twitter = tf.getInstance();
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Pin").name("pin"));
		try {
			requestToken = twitter.getOAuthRequestToken();
			String msg1 = "<html><b>Log in to twitter and enter the pin.</b> <a href="
					+ requestToken.getAuthorizationURL()
					+ ">"
					+ requestToken.getAuthorizationURL() + "></a>";
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml(msg1)
							.setReplyScreen(f).setReplyLabel("Enter")
							.showDetails(true).alias("chatlet1")));
		} catch (TwitterException te) {
			te.printStackTrace();
		}
	}

	@OnKeyword("logout")
	public void Logout(TeamchatAPI api) {
		cb2.setDebugEnabled(true).setOAuthConsumerKey(consumerkey)
				.setOAuthConsumerSecret(consumersecret).setOAuthAccessToken("")
				.setOAuthAccessTokenSecret("");
		tf = new TwitterFactory(cb2.build());
		twitter = tf.getInstance();
		api.perform(api.context().currentRoom()
				.post(new TextChatlet("Logout Successful!")));
	}

	@OnAlias("chatlet1")
	public void Chatlet2(TeamchatAPI api) {
		String pin = api.context().currentReply().getField("pin");

		try {
			accessToken = twitter.getOAuthAccessToken(requestToken, pin);
		} catch (TwitterException te) {
			if (401 == te.getStatusCode()) {
				api.perform(api
						.context()
						.currentRoom()
						.post(new TextChatlet(
								"Login Unsuccessful! Please try again.")));
			} else {
				te.printStackTrace();
			}
		}

		cb1.setDebugEnabled(true).setOAuthConsumerKey(consumerkey)
				.setOAuthConsumerSecret(consumersecret)
				.setOAuthAccessToken(accessToken.getToken())
				.setOAuthAccessTokenSecret(accessToken.getTokenSecret());
		tf = new TwitterFactory(cb1.build());
		twitter = tf.getInstance();

		if (twitter.getAuthorization().isEnabled()) {
			api.perform(api.context().currentRoom()
					.post(new TextChatlet("Successful Login!")));
		}

	}

	@OnKeyword("tweet")
	public void Chatlet3(TeamchatAPI api) {
		String msg = "What's happening?";
		Form f = api.objects().form();
		f.addField(api.objects().input().label("").name("tweet"));
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestion(msg).setReplyScreen(f)
						.setReplyLabel("Tweet").showDetails(true)
						.alias("chatlet3")));
	}

	@OnAlias("chatlet3")
	public void Chatlet4(TeamchatAPI api) {
		String tweet = api.context().currentReply().getField("tweet");
		System.out.println(tweet);
		if (tweet.length() <= 140) {
			try {
				Status status = twitter.updateStatus(tweet);
				api.perform(api
						.context()
						.currentRoom()
						.post(new TextChatlet(
								"Successfully updated the status to ["
										+ status.getText() + "].")));
			} catch (TwitterException te) {
				te.printStackTrace();
			}
		} else
			api.perform(api.context().currentRoom()
					.post(new TextChatlet("Maximum 140 characters allowed!")));
	}

	@OnKeyword("gettweet")
	public void Chatlet5(TeamchatAPI api) {
		try {
			twitter = tf.getInstance();
			User user = twitter.verifyCredentials();
			List<Status> tweets = twitter.getHomeTimeline();
			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet("Showing @" + user.getScreenName()
							+ "'s home timeline.")));
			int i = 0;
			for (Status tweet : tweets) {
				i++;
				api.perform(api
						.context()
						.currentRoom()
						.post(new TextChatlet(tweet.getUser().getScreenName()
								+ " - " + tweet.getText())));
				if (i == 10)
					break;
			}

		} catch (TwitterException te) {
			te.printStackTrace();
			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet("Failed to get timeline: "
							+ te.getMessage())));
		}
	}

	@OnKeyword("directmsg")
	public void Chatlet6(TeamchatAPI api) {
		String msg = "Enter the username of the recipient and the message to be sent.";
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Message Recipient")
				.name("rcpt"));
		f.addField(api.objects().input().label("Message").name("msg"));
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestion(msg).setReplyScreen(f)
						.setReplyLabel("Enter").showDetails(true)
						.alias("chatlet6")));
	}

	@OnAlias("chatlet6")
	public void Chatlet7(TeamchatAPI api) {
		String rcpt = api.context().currentReply().getField("rcpt");
		String msg = api.context().currentReply().getField("msg");
		twitter = tf.getInstance();
		try {
			DirectMessage message = twitter.sendDirectMessage(rcpt, msg);
			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet(
							"Direct message successfully sent to "
									+ message.getRecipientScreenName())));
		} catch (TwitterException te) {
			te.printStackTrace();
			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet("Failed to send a direct message: "
							+ te.getMessage())));
		}
	}

	@OnKeyword("search")
	public void Chatlet8(TeamchatAPI api) {
		String msg = "Enter the keyword to be searched.";
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Search Keyword").name("kywd"));
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestion(msg).setReplyScreen(f)
						.setReplyLabel("Enter").showDetails(true)
						.alias("chatlet8")));
	}

	@OnAlias("chatlet8")
	public void Chatlet9(TeamchatAPI api) {
		String kywd = api.context().currentReply().getField("kywd");
		twitter = tf.getInstance();
		try {
			Query query = new Query(kywd);
			QueryResult result;
			int i = 0;
			do {
				result = twitter.search(query);
				List<Status> tweets = result.getTweets();
				for (Status tweet : tweets) {
					for (MediaEntity mediaEntity : tweet.getMediaEntities()) {
						i++;
						api.perform(api
								.context()
								.currentRoom()
								.post(new TextChatlet(tweet.getUser()
										.getScreenName()
										+ " - "
										+ tweet.getText()
										+ " "
										+ mediaEntity.getType()
										+ ": "
										+ mediaEntity.getMediaURL())));
						if (i == 10)
							break;
					}
					if (i == 10)
						break;
				}
				if (i == 10)
					break;
			} while ((query = result.nextQuery()) != null);
		} catch (TwitterException te) {
			te.printStackTrace();
			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet("Failed to search tweets: "
							+ te.getMessage())));
		}
	}

}