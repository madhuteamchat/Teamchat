package com.teamchat.integrations.slack;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

public class websocket {

	public void startClient() throws URISyntaxException {
		
	//	String s = "wss:\/\/ms161.slack-msgs.com\/websocket\/uLkqZ6bYUmDwDgAS9BQ94BbE3QNqjm3VtyByzrslS_MIZ9p2cLEEFYJGmBrkmHLk393FIozoYUfK5upa5krlVA8Dpp8PMnln8gs_3W9ZZ0A=";
		
		WebSocketClient mWs = new WebSocketClient(new URI(
				"ws://socket.example.com:1234"), new Draft_10()) {
			@Override
			public void onMessage(String message) {
				JSONObject obj = new JSONObject(message);
				// String channel = obj.getString("channel");
			}
			
			//ws://socket.example.com:1234

			@Override
			public void onOpen(ServerHandshake handshake) {
				System.out.println("opened connection");
			}

			@Override
			public void onClose(int code, String reason, boolean remote) {
				System.out.println("closed connection");
			}

			@Override
			public void onError(Exception ex) {
				ex.printStackTrace();
			}
		};

		mWs.connect();
		JSONObject obj = new JSONObject();
		// obj.put("event", "addChannel");
		// obj.put("channel", "ok_btccny_ticker");
		// String message = obj.toString();
	}

}
