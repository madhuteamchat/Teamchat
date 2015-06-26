package com.teamchat.integrations.slack;


import java.net.URI;
import javax.websocket.*;

@ClientEndpoint
public class Websocket  {
	
	public static String tempurl = null;
	
    private static Object waitLock = new Object();

    @OnMessage
    public void onMessage(String message) {
       System.out.println(message); //Convert each message to JSON Object and set conditions for sending teamchat notifications   	   //Print the message
    }
   
 private static void  wait4TerminateSignal()
 {
  synchronized(waitLock)
  {try {
    waitLock.wait();
   } catch (InterruptedException e) {    
   }}}
 
public static void connectEventStream() {
     WebSocketContainer container=null;//
     Session session=null;
  try{
   //Tyrus is plugged via ServiceLoader API. See notes above
   container = ContainerProvider.getWebSocketContainer(); 

//WS1 is the context-root of my web.app 
//ratesrv is the  path given in the ServerEndPoint annotation on server implementation
   System.err.println(tempurl);
   session=container.connectToServer(Websocket.class, URI.create(tempurl)); 

   wait4TerminateSignal();
  } catch (Exception e) {
   e.printStackTrace();
  }
  finally{
   if(session!=null){
    try {
     session.close();
    } catch (Exception e) {     
     e.printStackTrace();
    }
   }         
  } 
 }
}