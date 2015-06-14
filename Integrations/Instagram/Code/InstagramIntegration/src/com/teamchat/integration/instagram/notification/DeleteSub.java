package com.teamchat.integration.instagram.notification;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import com.teamchat.integration.instagram.properties.InstagramProperty;


public class DeleteSub {
	
	String callbackurl="null";
	String client_id="null";
	String client_secret="null";
	String result="";

	public DeleteSub()
	{
		InstagramProperty ip=new InstagramProperty();
		ip.loadParams();
		client_id=ip.getClientId();
		client_secret=ip.getClientSecret();
		callbackurl=ip.getWebhookUrl();
	}
	
	public void unSub(String sid)
	{
		String geturl="https://api.instagram.com/v1/subscriptions?client_secret="+client_secret+"&id="+sid+"&client_id="+client_id;
		HttpDelete httpdel=new HttpDelete(geturl);

	    HttpClient httpclient=new DefaultHttpClient();
		try {
			HttpResponse response=httpclient.execute(httpdel);
			System.out.println(response.getStatusLine());
			
			
			BufferedReader in = new BufferedReader(new
					InputStreamReader(response.getEntity().getContent()));
					                StringBuffer sb = new StringBuffer("");
					                String line = "";
					                while ((line = in.readLine()) != null) {
					                    sb.append(line +"\n");
					                    result+=line+"<br>";
					                }
					                in.close();
					                System.out.println(sb);
					                
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}
