package com.integration;
//package com.integration;
//
////basic classes for json parsing help
//import java.util.ArrayList;
//import java.util.Iterator;
//
//import com.github.hipchat.api.*;
////json parsing
//import com.google.gson.Gson;
//import com.hipchat.classes.Items;
//
//
//
//public class Hipchat_apihandlerbot
//{
//	private Request_handlerbot rh = new Request_handlerbot();
//	private Hipchat_basiccheckbot bb;
//	private Gson gson = new Gson();
// private String baseurl="http://api.hipchat.com//v2/room";
//	// user agent string
////	private String ua = "Teamchat (http://www.teamchat.com/en/)";
//
//	// init constructor
//	public void Hipchat_api_handlerbot(Hipchat_basiccheckbot bb) {
//		// TODO Auto-generated constructor stub
//		this.bb = bb;
//	}
//
//	// public methods for api access
//	// return active project list (names)
//	public String[] getActiveProjects() {
//		ArrayList<String> data = new ArrayList<String>();
//		try {
//			String response = rh.sendGet_auth(baseurl + "","auth_token=", bb.getAccess_token());
//			// put response in token class
//			Items[] items = gson.fromJson(response, Items[].class);
//			for (Items item : items) {
//				data.add(Integer.toString(item.getId()));
//			}
//			String[] info = data.toArray(new String[data.size()]);
//			return info;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}
//}
