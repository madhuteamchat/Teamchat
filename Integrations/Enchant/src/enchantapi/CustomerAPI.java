package enchantapi;

import java.net.HttpURLConnection;
import java.net.URL;

public class CustomerAPI {

	int statuscode;
	String result;
	String enchant_username;
	String enchant_password;
	String enchant_site;
	
	public CustomerAPI(String username,String password,String site) {
		enchant_username=username;
		enchant_password=password;
		enchant_site=site;
	}
	
	public int getCustomers(){
		    try {
		        URL urldemo = new URL("https://"+enchant_site+".enchant.com/api/v1/customers");
		        HttpURLConnection yc = (HttpURLConnection)urldemo.openConnection();
		        String userpass = enchant_username + ":" +enchant_password;
		        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
		        yc.setRequestProperty ("Authorization", basicAuth);
		        statuscode=yc.getResponseCode();
		        if(statuscode==200)
		          result=HttpConnection.getBodyOfResponse(yc);
		        System.out.println(result);
		    }
		     catch(Exception e) {
		            System.out.println(e);
		        }
		    return statuscode;
	  }
	
	public String getResponse(){
		    return result;
	  }

}
