package chatletcreator;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

public class SendChatlet
{
	public void p2pMessage(String email,String message, String authEmail, String authPass) throws ClientProtocolException, IOException{
		
		HttpPost httppost = new HttpPost("http://enterprise.teamchat.com/GatewayAPI2/rest/pmessage");
		HttpClient httpclient=HttpClientBuilder.create().build();
		ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("email", email));
		nvps.add(new BasicNameValuePair("message",message));
		UrlEncodedFormEntity sd=new UrlEncodedFormEntity(nvps,"utf-8");
		httppost.addHeader("authorization", "Simple "+ authEmail+":"+authPass);
		httppost.setEntity(sd);
		HttpResponse response = httpclient.execute(httppost);
		System.out.println(response.getStatusLine());
	}
	
	public String getRooms(String userId,String token) throws ClientProtocolException, IOException{
		HttpGet httpget = new HttpGet("https://enterprise.teamchat.com/api/rooms/lite?version=v1&appType=echat&mode=dev&method=GET&_=1437377430217");
		HttpClient httpclient=HttpClientBuilder.create().build();
		httpget.addHeader("Authorization","Gupshup "+ userId+":"+token+":"+"echat");
		httpget.addHeader("Accept","application/vnd.gupshup.privatechat-v1+json");
		HttpResponse response = httpclient.execute(httpget);
		System.out.println(response.getStatusLine());
		
		BufferedReader in = new BufferedReader(new
				InputStreamReader(response.getEntity().getContent()));
				                StringBuffer sb = new StringBuffer("");
				                String line = "";
				                while ((line = in.readLine()) != null) {
				                    sb.append(line +"\n");
				                }
				                
				                return sb.toString();
	}
	
	public void roomMessage(String roomId, String message) throws ClientProtocolException, IOException{
		HttpPost httppost = new HttpPost("http://enterprise.teamchat.com/GatewayAPI2/rest/message");
		HttpClient httpclient=HttpClientBuilder.create().build();
		ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("groupid", roomId));
		nvps.add(new BasicNameValuePair("message",message));
		UrlEncodedFormEntity sd=new UrlEncodedFormEntity(nvps,"utf-8");
		httppost.addHeader("authorization", "Simple "+ Token.getAuthEmail()+":"+Token.getAuthPass());
		httppost.setEntity(sd);
		HttpResponse response = httpclient.execute(httppost);
		
		BufferedReader in = new BufferedReader(new
				InputStreamReader(response.getEntity().getContent()));
				                StringBuffer sb = new StringBuffer("");
				                String line = "";
				                while ((line = in.readLine()) != null) {
				                    sb.append(line +"\n");
				                }
	System.out.println(sb.toString());
				                
		
	}
	
}
