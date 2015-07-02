package com.teamchat.integration.pingdom.bot;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.tomcat.util.codec.binary.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.teamchat.integration.pingdom.classes.Check;



public class pingdomintegrator {
	
	
	
	 public Check[] parse(String json) {
		 
		//parsing a json like this
		 
//		 {
//			  "checks": [
//			    {
//			      "id": 1650784,
//			      "created": 1435769579,
//			      "name": "google",
//			      "hostname": "www.google.com",
//			      "use_legacy_notifications": true,
//			      "resolution": 1,
//			      "type": "http",
//			      "lasttesttime": 1435806884,
//			      "lastresponsetime": 167,
//			      "status": "up",
//			      "probe_filters": []
//			    },
//			    {
//			      "id": 1650785,
//			      "created": 1435769756,
//			      "name": "Teamchat",
//			      "hostname": "www.teamchat.com",
//			      "use_legacy_notifications": true,
//			      "resolution": 1,
//			      "type": "http",
//			      "lasttesttime": 1435806881,
//			      "lastresponsetime": 725,
//			      "status": "up",
//			      "probe_filters": []
//			    },
//			    {
//			      "id": 1650787,
//			      "created": 1435769804,
//			      "name": "Interns Teamchat",
//			      "hostname": "interns.teamchat.com",
//			      "use_legacy_notifications": true,
//			      "resolution": 1,
//			      "type": "http",
//			      "lasterrortime": 1435806882,
//			      "lasttesttime": 1435806882,
//			      "lastresponsetime": 0,
//			      "status": "down",
//			      "probe_filters": []
//			    }
//			  ],
//			  "counts": {
//			    "total": 3,
//			    "limited": 3,
//			    "filtered": 3
//			  }
//			}
		 	Gson gson = new Gson();
		  return gson.fromJson(json, Check[].class);
		}
		
      public Response getChecks(String username,String password,String App_key,String url){
		OkHttpClient client = new OkHttpClient();
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		Request request = new Request.Builder()
		  .url(url)
		  .get()
		  .addHeader("authorization", authHeader )
		  .addHeader("app-key", App_key)
		  .build();

		try {
			return (Response) client.newCall(request).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
//	private String username, password, appkey;
		

//		protected void send_auth(String username,String password,String App_key,String url) throws IOException {
////			HttpResponse response.setContentType("text/html");
////			PrintWriter out = response.getWriter();
////			out.println("Pingdom");
//			
//			HttpGet request = new HttpGet(url);
//			String auth = username + ":" + password;
//			byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
//			String authHeader = "Basic " + new String(encodedAuth);
//			request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
//
//			HttpClient client = HttpClientBuilder.create().build();
//			HttpResponse response = client.execute(request);
//
//			int statusCode = response.getStatusLine().getStatusCode();
//			
//			System.out.println(statusCode);
//	
//		}
		
}

	

