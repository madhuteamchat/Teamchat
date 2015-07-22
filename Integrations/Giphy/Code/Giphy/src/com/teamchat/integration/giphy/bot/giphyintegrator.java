package com.teamchat.integration.giphy.bot;

/*
 * *
 * @author:Anuj Arora
 */

import java.io.IOException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class giphyintegrator {
	//authenticating and receiving images.
 public String getimages(String keyword,int limit,int offset) throws IOException{
   	  
   	  String err = "Error";

			OkHttpClient client = new OkHttpClient();
//api key = dc6zaTOxFJmzC
			Request request = new Request.Builder()
			  .url("http://api.giphy.com/v1/gifs/search?api_key=dc6zaTOxFJmzC&q="+keyword+"&offset="+offset+"&limit="+limit)
			  .get()
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
