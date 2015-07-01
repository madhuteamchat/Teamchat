package com.teamchat.integration.yammer;

import java.io.Closeable;
import java.io.IOException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.teamchat.integration.database.Authentication;
import com.teamchat.integration.factory.PropertyFile;

public class YammerConnection implements Closeable {
    private final CloseableHttpClient httpclient;
    private String auth;
    
    public YammerConnection() throws IOException {
   	 httpclient = HttpClientBuilder.create().useSystemProperties().build();
   }
    
    @Override
    public void close() throws IOException {
        httpclient.close();
    }
    
    public CloseableHttpClient getHttpClient() {
    	return httpclient;
    }
    
    public String getAuthToken(String email) {
      auth=Authentication.getToken(email);
      try {
    
      }
      catch(Exception e) {
    	  e.printStackTrace();
      }
      return auth;
    }
}
