package com.teamchat.integration.instagram.notification;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;


public class DeleteSub {
	
	String callbackurl="http://interns.teamchat.com:8086/InstagramIntegration/CallBack";
	String client_id="858afa5bc35e419c82d02ad8c58d037e";
	String client_secret="f4dde757b8074f7893ce7fb4a5010ccc";
	String result="";

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
