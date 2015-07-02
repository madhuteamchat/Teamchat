package com.teamchat.integration.pingdom.bot;

import java.io.IOException;
//import java.io.PrintWriter;
import java.nio.charset.Charset;

import org.apache.tomcat.util.codec.binary.Base64;

//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
//import com.teamchat.integration.pingdom.classes.Check;



public class pingdomintegrator {
	
	
	public String addChecks(String username,String password,String App_key,String host, String name,String type) throws IOException{
  	  
  	  String err = "Error";
  	  String success ="Check successfully added!!";
  	OkHttpClient client = new OkHttpClient();

	String auth = username + ":" + password;
	byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
	String authHeader = "Basic " + new String(encodedAuth);
	System.out.println("auth key: " + authHeader);
	
  	Request request = new Request.Builder()
  	  .url("https://api.pingdom.com/api/2.0/checks?name="+name+"&host="+host+"&type="+type)
  	  .post(null)
  	  .addHeader("authorization", authHeader)
  	  .addHeader("app-key", App_key)
  	  .build();

  	Response response = client.newCall(request).execute();
  	
	
		
		 if (!response.isSuccessful())
		 {
			 return err;
			 
		 }
			 
		 else
		return success;
				
			
	
	}

		
	public String delChecks(String username,String password,String App_key,String[] del) throws IOException{
	  	  
	  	  String err = "Error";
	  	  String success ="Deletion successfull!!";
	  	OkHttpClient client = new OkHttpClient();

		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		System.out.println("auth key: " + authHeader);
		int length,i,len;
		length= del.length;
		String delids="";
		for(i=0;i<length;i++)
		{
			if(!del[i].equals("none"))
			{
				delids=delids+del[i];
				delids=delids+",";
			}
			
		}
		len=delids.length();
		delids=delids.substring(0, (len-2));
		Request request = new Request.Builder()
		  .url("https://api.pingdom.com/api/2.0/checks/?delcheckids="+delids)
		  .delete(null)
		  .addHeader("authorization", authHeader)
		  .addHeader("app-key", App_key)
		  .build();

	  	Response response = client.newCall(request).execute();
	  	
		
			
			 if (!response.isSuccessful())
			 {
				 return err;
				 
			 }
				 
			 else
			return success;
					
				
		
		}
	
      public String getChecks(String username,String password,String App_key,String url) throws IOException{
    	  
    	  String err = "Error";
          
		OkHttpClient client = new OkHttpClient();
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		System.out.println("auth key: " + authHeader);
		Request request = new Request.Builder()
		  .url(url)
		  .get()
		  .addHeader("authorization", authHeader )
		  .addHeader("app-key", App_key)
		  .build();
		
		Response response = client.newCall(request).execute();
		
		 if (!response.isSuccessful())
		 {
			 return err;
			 
		 }
			 
		 else
		return response.body().string();
				
			
	
	}

		
}

	

