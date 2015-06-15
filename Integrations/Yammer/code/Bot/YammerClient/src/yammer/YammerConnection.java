package yammer;

import java.io.Closeable;
import java.io.IOException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class YammerConnection implements Closeable {
	private static final String authorizationtoken = "2fdpiJV5o98k3OI3X2TpAg";
    private final CloseableHttpClient httpclient;
    
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
    
    public String getAuthToken() {
    	return authorizationtoken;
    }
}
