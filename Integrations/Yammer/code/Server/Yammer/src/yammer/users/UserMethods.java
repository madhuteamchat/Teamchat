package yammer.users;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import yammer.YammerConnection;

public class UserMethods {
   private CloseableHttpClient httpclient=null;
   private String authtoken="";
   private YammerConnection yc=null;
   
   public UserMethods(String email) {
	   try {
	   yc=new YammerConnection();
       httpclient=yc.getHttpClient();
       authtoken=yc.getAuthToken(email);
	   }
	   catch(Exception e) {
		   
	   }
	   
   }
   
	public List<User> getUsers() throws IOException {
        List<User> users=new ArrayList<User>();
       
        HttpGet httpget = new HttpGet("https://www.yammer.com/api/v1/users.json");
      //  httpget.addHeader("Authorization", "Bearer " +"2fdpiJV5o98k3OI3X2TpAg");
        httpget.addHeader("Authorization", "Bearer " +authtoken);
        HttpResponse response = httpclient.execute(httpget);
        BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer sb = new StringBuffer("");
        String line = "";
        String NL = System.getProperty("line.separator");
        while ((line = in.readLine()) != null) {
            sb.append(line +"\n");
        }
        in.close();
        yc.close();
        try {
        	JSONArray json = new JSONArray(sb.toString());
       for (int i = 0; i < json.length(); i++) {  // **line 2**
    	     JSONObject childJSONObject = json.getJSONObject(i);
    	     String email = childJSONObject.getString("email");
    	     String username = childJSONObject.getString("full_name");
    	     int id=childJSONObject.getInt("id");
    	     users.add((new User(id,username,email)));
    	}
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        return users;
	}
	
	 public static void main(String argv[]) {
	    //	try{
	    //	UserMethods usr=new UserMethods();
	    	//yc.getUsers();
	    //	yc.search("yammer");
	    //	usr.getUsers();
	    //	}
	   // 	catch(IOException e) {
	    //		e.printStackTrace();
	    //	}
	    }
	
	
}
