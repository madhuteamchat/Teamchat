package enchantapi;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserAPI {
	String username;
	String password;
	String result="";
	int statuscode;
	String enchant_site;
	
	public UserAPI(String user,String password,String site) {
		this.username=user;
		this.password=password;
		this.enchant_site=site;
	}
	
    public int getUsers() {  
	    try {
	        URL urldemo = new URL("https://"+enchant_site+".enchant.com/api/v1/users");
	        HttpURLConnection yc = (HttpURLConnection)urldemo.openConnection();
	        String userpass = username + ":" +password;
	        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
	        yc.setRequestProperty ("Authorization", basicAuth);
	        result=HttpConnection.getBodyOfResponse(yc);
	        System.out.println(result);
	        statuscode=yc.getResponseCode();
	        }catch(Exception e) {
	            System.out.println(e);
	        }
	    return statuscode;
  }
    
    public String getResponse() {
    	return result;
    }
}
