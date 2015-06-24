package com.teamchat.integration.office365.subscription;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;

public class HttpConnection {
	
	
  public String getBodyOfResponse(HttpResponse yc) throws Exception{
	  String inputline;
	  String result="";
	  BufferedReader in = new BufferedReader(new InputStreamReader(yc.getEntity().getContent()));
      
        while ((inputline = in.readLine()) != null)
      	result+=inputline;
        in.close();
	    System.out.println(result);
	  return result;
  }
}
