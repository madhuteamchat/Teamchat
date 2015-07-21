package com.teamchat.integration.glassdoor.bot;
/*
 * *@author : Anuj Arora
 * 
 */
import java.io.IOException;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class glassdoorintegrator {
// getting company details from glassdoor server.
	public String getcompanies(String Keyword,String Location,int pn) throws IOException{
   	  
	   	  String err = "Error";
				OkHttpClient client = new OkHttpClient();
				//t.p = partnerid
				//t.k = partner key
				//q = paraphrase
				//l = location
				//ps = page size
				//pn =page number
				//t.p = 39226 (partner id)
				//t.k = hl6voUXhoGA	(API key or partner key)
				Request request = new Request.Builder()
				  .url("http://api.glassdoor.com/api/api.htm?t.p=39226&t.k=hl6voUXhoGA&format=json&v=1&action=employers&q="+Keyword+"&ps=1&pn="+pn+"&l="+Location)
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
