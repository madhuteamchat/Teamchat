package yammer;

import java.awt.Desktop;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

public class YammerAuthenticate {
	private static final String OAUTH_ACCESS_TOKEN_URL = "https://www.yammer.com/superrit/oauth2/authorize?client_id=%s&response_type=code&redirect_uri=https:localhost:8080/Yammer";
	private static final String OAUTH_GET_ACCESS_TOKEN_URL = "https://www.yammer.com/oauth2/authorize?client_id=%s";
	HttpClient httpclient;
	public YammerAuthenticate() {
		 httpclient = HttpClientBuilder.create().useSystemProperties().build();
	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String code="code";
		try{
		  HttpClient httpclient = new DefaultHttpClient();
		  HttpPost httpost = new HttpPost(String.format(OAUTH_GET_ACCESS_TOKEN_URL, "VxmwJQvzJg8wR3nVO49nTA"));
		  HttpResponse response=httpclient.execute(httpost);
		//  System.out.println(response.getLastHeader("Location").getValue());
		//  HttpUnitOptions.setScriptingEnabled(false);
        //  WebConversation wc = new WebConversation();
        //  WebResponse resp = wc.getResponse(String.format(OAUTH_GET_ACCESS_TOKEN_URL, "VxmwJQvzJg8wR3nVO49nTA"));
          if(!response.toString().contains(code)){
          	 Desktop d=Desktop.getDesktop();
          	d.browse(new URI(String.format(OAUTH_GET_ACCESS_TOKEN_URL, "VxmwJQvzJg8wR3nVO49nTA")));
            while(!response.toString().contains(code)){
            	System.out.println(response);
            	response = httpclient.execute(httpost);
            }
          }
     //     System.out.println(resp.getURL().toString().split("=")[1]);
    //      resp = wc.getResponse("https://www.yammer.com/oauth2/authorize?client_id=VxmwJQvzJg8wR3nVO49nTA&response_type=code&redirect_uri=http:localhost:8080/Yammer/");
          
    //    URI url=new URI("https://www.yammer.com/oauth2/authorize?client_id=VxmwJQvzJg8wR3nVO49nTA&response_type=code&redirect_uri=http:localhost:8080/Yammer/");
		// Browse a URL, say google.com
	//	d.browse(new URI("https://www.yammer.com/oauth2/authorize?client_id=VxmwJQvzJg8wR3nVO49nTA&response_type=code&redirect_uri=http:localhost:8080/Yammer/"));
       
      }
      catch(Exception e) {
    	  e.printStackTrace();
      }
	}

}
