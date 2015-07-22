package com.teamchat.integration.onedrive.bot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class UploadFile {
	
	TeamchatAPI api;
	
	public UploadFile(TeamchatAPI api)
	{
		this.api=api;
	}

	public void uploadfile()
	{

		String client_id="000000004015A50A";
		String client_secret="0aFb0tPHwwjO5FkJU3cHgHp1ALdLQSQU";
		
		Properties props = new Properties();
	    InputStream is = null;
	 

	    try {    // First try loading from the current directory
	   
	    File f = new File("/home/intern11/uid.properties");
	        is = new FileInputStream( f );  	  }
catch ( Exception e ) { is = null; }

try { 
	        if(is==null)
	        	System.out.println( "You have to login");
//	 is=getClass().getResourceAsStream(uid+".properties");
	  
	        // Try loading properties from the file (if found)
	        props.load( is );
	   String at=props.getProperty("access_token");
	   System.out.println("AT="+at);
	   String rt=props.getProperty("refresh_token");
	   String floc="/home/intern11/abc.jpg";
	   File f=new File(floc);
	   String fname=f.getName();
	    HttpClient httpclient=new DefaultHttpClient();
	    System.out.println(fname);
//	    HttpPut hput=new HttpPut("https://apis.live.net/v5.0/me/skydrive/files/"+fname+"?access_token="+at);
////	    hput.addHeader("Content-Type", "multipart/form-data; boundary=A300x");
//	    FileBody bin = new FileBody(f);
//        
//        HttpEntity reqEntity = MultipartEntityBuilder.create()
//                .addPart("bin", bin)
//                .build();
//
//
//        hput.setEntity(reqEntity);
//
//		HttpResponse response = httpclient.execute(hput);
	    HttpPost hput=new HttpPost("https://apis.live.net/v5.0/me/skydrive/files?access_token="+at);
	    hput.addHeader("Content-Type", "multipart/form-data; boundary=A300x");
	    
	    
//	    String body = "--A300x\r\n";
//	    body += "Content-Disposition: form-data; name=\"file\"; filename=\"hello.txt\"\r\n";
//	    body += "Content-Type: application/octet-stream\r\n\r\n";
//	    body += "Hello Friend \r\n";
//	    body += "--A300x--";
	    
//	    FileBody bin = new FileBody(f);
	    
        
//        HttpEntity reqEntity = MultipartEntityBuilder.create()
//                .setBoundary("A300x")
//                .addTextBody("Content-Disposition", "form-data; name=\"file\"; filename=\"hello.txt\"")
//                .addTextBody("Content-Type", "application/octet-stream")
//                .build();
//
//
//        hput.setEntity(reqEntity);
//
//		HttpResponse response = httpclient.execute(hput);
//		System.out.println(response.getStatusLine());
//		System.out.println(response.getEntity().getContentType());
//		System.out.println(response.getEntity().getContent());
	    
	    URL url = new URL("https://apis.live.net/v5.0/me/skydrive/files/"+fname+"?access_token="+at);
	    HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
	    httpCon.setDoOutput(true);
	    httpCon.setRequestMethod("PUT");
	    OutputStreamWriter out = new OutputStreamWriter(
	        httpCon.getOutputStream());
	    FileReader fr=new FileReader(f);
	    int c;
	    while( (c=fr.read())!=-1)
	    {
	    	out.write(c);
	    }
	    out.close();
	    System.out.println(httpCon.getResponseCode());
	    if(httpCon.getResponseCode()==401)
	    	api.perform(api.context().currentRoom().post(new TextChatlet("You have to login")));
	    else if(httpCon.getResponseCode()==200)
	    	api.perform(api.context().currentRoom().post(new TextChatlet("Uploaded Successfully")));
	    else
	    	api.perform(api.context().currentRoom().post(new TextChatlet("Try Again")));
		
}
catch(Exception e)
{
	e.printStackTrace();
}
	}
	
}
