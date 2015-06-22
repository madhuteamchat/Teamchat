package yammer;

import java.awt.Desktop;
import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import factory.PropertyFile;

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
      auth=PropertyFile.getProperty(email);
      try {
    
      }
      catch(Exception e) {
    	  e.printStackTrace();
      }
      return auth;
    }
}
