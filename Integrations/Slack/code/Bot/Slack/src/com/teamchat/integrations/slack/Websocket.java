//INTEGRATION: SLACK

//STATUS:UNUSED

package com.teamchat.integrations.slack;

import java.io.IOException;
import java.net.URI;

import javax.websocket.*;

@ClientEndpoint
public class Websocket {

	public static String tempurl = null;

	private static Object waitLock = new Object();
	
	public static int flag = 0;

	@OnMessage
	public void onMessage(String message) {
		System.out.println(message); // Convert each message to JSON Object and
										// set conditions for sending teamchat
										// notifications //Print the message
	}

	private static void wait4TerminateSignal() {
		synchronized (waitLock) {
			try {
			waitLock.wait();
			} catch (InterruptedException e) {
			}
		}
	}

	public static void connectEventStream() {
		WebSocketContainer container = null;//
		Session session = null;
		try {
			// Tyrus is plugged via ServiceLoader API. See notes above
			container = ContainerProvider.getWebSocketContainer();

			// WS1 is the context-root of my web.app
			// ratesrv is the path given in the ServerEndPoint annotation on
			// server implementation
			
			System.err.println(tempurl);
			session = container.connectToServer(Websocket.class,
					URI.create(tempurl));

			System.out.println("Waiting for terminate signal.");
			wait4TerminateSignal();
			session.close();
			System.out.println("Recieved terminate signal.");

		} catch (Exception e) {
			System.err.println("Exception!");
			e.printStackTrace();
		} finally {
			System.out.println("Into finally. Checking if session is already closed or not.");
			if (session != null) {
				try {
					System.out.println("Ending websocket notification session.");
					session.close();
				} catch (Exception e) {
					System.err.println("Exception in finally!");
					e.printStackTrace();
				}
			}
		}
	}
	
}