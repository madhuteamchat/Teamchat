package com.teamchat.integration.onedrive.bot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class EditFileFolder {
	public static void main(String args[])
	{

		String client_id="000000004015A50A";
		String client_secret="0aFb0tPHwwjO5FkJU3cHgHp1ALdLQSQU";
		String fid="file.b71f744a08ba70ef.B71F744A08BA70EF!111";
		
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
	   String floc="/home/intern11/hello.txt";
	   traverse(at);
	  
		
}
catch(Exception e)
{
	e.printStackTrace();
}
	}
	
	
	public static void create(String at)
	{
		
		 try{
			   
			  HttpPost httppost = new
			    		HttpPost("https://apis.live.net/v5.0/me/skydrive");
			    HttpClient httpclient=new DefaultHttpClient();
			    httppost.addHeader("Authorization", "Bearer "+at);
				httppost.addHeader("Content-Type", "application/json");	 
				JSONObject jobj=new JSONObject();
				jobj.put("name", "New Folder");
				StringEntity se=new StringEntity(jobj.toString());
				httppost.setEntity(se);
				HttpResponse response = httpclient.execute(httppost);
				System.out.println(httppost.getEntity().getContentType());
				System.out.println(response.getStatusLine());
			 }

			 catch(Exception e)
			 {
				 e.printStackTrace();
			 }
		
	}
	
	public static void delete(String fid,String at)
	{
		
		 try{
			   
			  HttpDelete httpdel = new
			    		HttpDelete("https://apis.live.net/v5.0/"+fid+"?access_token="+at);
			    HttpClient httpclient=new DefaultHttpClient();
//			    httppost.addHeader("Authorization", "Bearer "+at);
			    HttpResponse response =httpclient.execute(httpdel);
			    System.out.println(response.getStatusLine());
			 }

			 catch(Exception e)
			 {
				 e.printStackTrace();
			 }
		
	}
	
	
	public static void copy(String fid,String at)
	{
		 try{
		   
		   URL url = new URL("https://apis.live.net/v5.0/"+fid+"?access_token="+at);
		   HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		   httpCon.setDoOutput(true);
		   httpCon.setRequestProperty("Content-Type", "application/json" );
		   JSONObject jobj=new JSONObject();
		   jobj.append("destination", "folder.b71f744a08ba70ef.B71F744A08BA70EF!109");
		   httpCon.setRequestMethod("COPY");
		   OutputStreamWriter osw = new OutputStreamWriter(httpCon.getOutputStream());
	       osw.write(jobj.toString());
	       osw.flush();
	       osw.close();
		   httpCon.connect();
		   System.err.println(httpCon.getResponseCode());
		 }

		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
	}

	
	public static void list(String at)
	{
		try
		{
		  HttpGet hget=new HttpGet("https://apis.live.net/v5.0/me/skydrive/recent_docs?"
				    +"access_token="+at);
					    HttpClient httpclient=new DefaultHttpClient();
					    HttpResponse response =httpclient.execute(hget);
					    System.out.println(response.getStatusLine());
					    BufferedReader in = new BufferedReader(new
								InputStreamReader(response.getEntity().getContent()));
								                StringBuffer sb = new StringBuffer("");
								                String line = "";
								                while ((line = in.readLine()) != null) {
								                    sb.append(line +"\n");
								                }
								                in.close();
								                System.out.println(sb);
		}

						 catch(Exception e)
						 {
							 e.printStackTrace();
						 }
	}
	
	
	public static void traverse(String at)
	{
		try
		{
		  HttpGet hget=new HttpGet("https://apis.live.net/v5.0/me/skydrive?"
				    +"access_token="+at);
					    HttpClient httpclient=new DefaultHttpClient();
					    HttpResponse response =httpclient.execute(hget);
					    System.out.println(response.getStatusLine());
					    BufferedReader in = new BufferedReader(new
								InputStreamReader(response.getEntity().getContent()));
								                StringBuffer sb = new StringBuffer("");
								                String line = "";
								                while ((line = in.readLine()) != null) {
								                    sb.append(line +"\n");
								                }
								                in.close();
								                System.out.println(sb);
		}

						 catch(Exception e)
						 {
							 e.printStackTrace();
						 }
	}
	
	
}


