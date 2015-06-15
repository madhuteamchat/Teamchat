package users;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import yammer.YammerConnection;

public class User {

	   public int id;
	   public String username="";
	   public String email="";
	   public String department="";
	   public String designation="";
	   
	   private CloseableHttpClient httpclient=null;
	   private static final Charset UTF8 = Charset.forName("UTF-8");
	   
	   public User(int id,String name,String email) {
		   this.id=id;
		   this.username=name;
		   this.email=email;
	   }
	   
	   public User(String name,String email,String department,String designation) {
		   this.username=name;
		   this.email=email;
		   this.department=department;
		   this.designation=designation;
	   }
	   
	   public int sendMessage(String message) {
		   HttpResponse response=null;
		      try {
		       YammerConnection yc=new YammerConnection();
	           httpclient=yc.getHttpClient();
		      HttpPost httppost = new HttpPost("https://www.yammer.com/api/v1/messages.json");
		        httppost.addHeader("Authorization", "Bearer " +yc.getAuthToken());
		        List<NameValuePair> nvps = new ArrayList<>();
		        nvps.add(new BasicNameValuePair("body",message));
		        nvps.add(new BasicNameValuePair("direct_to_id",this.id+""));
		        httppost.setEntity(new UrlEncodedFormEntity(nvps, UTF8));
		        
		         response = httpclient.execute(httppost);  
		        if(response.getStatusLine().getStatusCode()==201)
		        	System.out.println("Message sent!!");
		        else
		        	System.out.println(response.toString());
		        yc.close();
		      }
		      catch(Exception e) {
		    	  e.printStackTrace();
		      }
		        return response.getStatusLine().getStatusCode();
	   }
}
