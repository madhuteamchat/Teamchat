package yammer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class Notifications {
	  private CloseableHttpClient httpclient=null;
	   private String authtoken="";
	   private HttpGet httpget;
	   private HttpResponse response;
	   private JSONObject countobject;
	   private JSONObject json; 
	  private YammerConnection yc;
	  private StringBuffer sb;
	  List<String> notifications=new ArrayList<String>();
	public Notifications(String email) {
		
		String result="";
		 try{
			    yc=new YammerConnection();
		        httpclient=yc.getHttpClient();
		        authtoken=yc.getAuthToken(email);
		        httpget = new HttpGet("https://www.yammer.com/api/v1/streams/notifications.json");
		        httpget.addHeader("Authorization", "Bearer " +authtoken);
		        response = httpclient.execute(httpget);
		        BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		        sb = new StringBuffer("");
		        String line = "";
		        while ((line = in.readLine()) != null) {
		            sb.append(line +"\n");
		        }
		        in.close();
		        System.out.println(sb);
		        json = new JSONObject(sb.toString());
		    	JSONArray childJSONarray = json.getJSONArray("items");
		    	for (int i = 0; i < childJSONarray.length(); i++) { 
             	    JSONObject childobject = childJSONarray.getJSONObject(i);
             	    if(!childobject.getString("category").equals("likes-message")) {
             	      result=childobject.getString("message");//stores the search results
             	      JSONObject objectJSON=json.getJSONObject("objects");
             	      JSONArray userarray=objectJSON.getJSONArray("user");
             	      result+=" "+userarray.getJSONObject(0).getString("full_name");
             	      notifications.add(result);
             	    }
             	}
		    	System.out.println(result);
		        
	}
		 catch(Exception e) {
			 e.printStackTrace();
		 }
}
	public List<String> getLikedBy() {
		List<String> likers=new ArrayList<String>();
		try{
		yc=new YammerConnection();
        httpclient=yc.getHttpClient();
        httpget = new HttpGet("https://www.yammer.com/api/v1/messages.json");
        httpget.addHeader("Authorization", "Bearer " +authtoken);
        response = httpclient.execute(httpget);
        BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        sb = new StringBuffer("");
        String line = "";
        while ((line = in.readLine()) != null) {
            sb.append(line +"\n");
        }
        in.close();
        System.out.println(sb);
        json = new JSONObject(sb.toString());
        JSONArray childJSONarray = json.getJSONArray("messages");
       
        String message="";
        for(int i=0;i<childJSONarray.length();i++) {
        	JSONObject likedby=childJSONarray.getJSONObject(i).getJSONObject("liked_by");
        	System.out.println("hey"+childJSONarray.getJSONObject(i).getJSONObject("body").getString("plain"));
        	int count=likedby.getInt("count");
        	System.out.println(count);
        	if(count>0) {
        		JSONObject body=childJSONarray.getJSONObject(i).getJSONObject("body");
        		message="Your post <b>"+body.getString("plain")+"</b> is liked by ";
        		System.out.println(message);
        		JSONArray likersarray=likedby.getJSONArray("names");
        		for(int k=0;k<likersarray.length();k++) {
        			likers.add(likersarray.getJSONObject(k).getString("full_name"));
        		}
        		for(int j=0;j<likers.size();j++) {
        	        	if(j != likers.size()-1)
        	        	  message+="<b>"+likers.get(j)+"</b>,";
        	        	else
        	        	  message+="<b>"+likers.get(j)+"</b>";
        	        }
        		System.out.println(message);
        		notifications.add(message);
        	}
        }
		}
		catch(Exception e){
			
		}
        
		return notifications;
	}
	
	public static void main(String argv[]) {
	//	Notifications nf=new Notifications();
	//	nf.getLikedBy();
	}
}
