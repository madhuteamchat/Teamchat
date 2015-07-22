package com.teamchat.integration.onedrive.bot;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class FileFolderProps {
	
	static ArrayList<String> flist=new ArrayList<String>(0);
	static ArrayList<String> fid=new ArrayList<String>(0);

	public static void main(String args[])
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
	   String floc="/home/intern11/hello.txt";
	   search(at);
//	   HttpGet hget=new HttpGet("https://apis.live.net/v5.0/file.b71f744a08ba70ef.B71F744A08BA70EF!108/content/?"
//    +"access_token="+at);
//	   HttpGet hget=new HttpGet("https://apis.live.net/v5.0/me/skydrive/search?access_token="+at+"&q=youtube");
//	    HttpClient httpclient=new DefaultHttpClient();
//	    HttpResponse response =httpclient.execute(hget);
//	    System.out.println(response.getStatusLine());
//	    BufferedReader in = new BufferedReader(new
//				InputStreamReader(response.getEntity().getContent()));
//				                StringBuffer sb = new StringBuffer("");
//				                String line = "";
//				                while ((line = in.readLine()) != null) {
//				                    sb.append(line +"\n");
//				                }
//				                in.close();
//				                System.out.println(sb);
		
}
catch(Exception e)
{
	e.printStackTrace();
}
	}
	
	
	public static void search(String at)
	{
		try
		{
	   HttpGet hget=new HttpGet("https://apis.live.net/v5.0/me/skydrive/search?access_token="+at+"&q=youtube");
	    HttpClient httpclient=new DefaultHttpClient();
	    HttpResponse response =httpclient.execute(hget);
	    System.out.println(response.getStatusLine());
	    BufferedReader in = new BufferedReader(new
				InputStreamReader(response.getEntity().getContent()));
				                StringBuffer sb = new StringBuffer("");
				                String line = "",js="";
				                while ((line = in.readLine()) != null) {
				                	js+=line;
				                    sb.append(line +"\n");
				                }
				                in.close();
				                System.out.println(sb);
				                JSONObject jobj=new JSONObject(js);
				                JSONArray jdata=jobj.getJSONArray("data");
				                for(int i=0;i<jdata.length();i++)
				                {
				                	flist.add(jdata.getJSONObject(i).getString("name"));
				                	fid.add(jdata.getJSONObject(i).getString("id"));
				                	System.out.println(flist.get(i));
				                }
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
}
