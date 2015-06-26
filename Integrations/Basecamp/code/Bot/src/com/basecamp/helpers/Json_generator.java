/**
 * 
 */
package com.basecamp.helpers;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author intern19
 *
 */
public class Json_generator {
	public Json_generator() {
		// TODO Auto-generated constructor stub
	}

	//convert to json string and return
	//for String value
	public String toJsonString(Map<String, String> data){
		 Gson gson = new Gson();
		 Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
		 return gson.toJson(data, mapType);
	}
	//for int value
	public String toJsonString(Map<String, Integer> data,Boolean putanytrueorfalsedontmatter){
		 Gson gson = new Gson();
		 Type mapType = new TypeToken<HashMap<String, Integer>>() {}.getType();
		 return gson.toJson(data, mapType);
	}
	//for bool value
	public String toJsonString(Map<String, Boolean> data,int putanyintegerdontmatter){
		 Gson gson = new Gson();
		 Type mapType = new TypeToken<HashMap<String, String>>() {}.getType();
		 return gson.toJson(data, mapType);
	}
}
