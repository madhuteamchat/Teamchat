package enchantapi;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;


public class TicketAPI {

	int statuscode;
	String result;
	String enchant_username;
	String enchant_password;
	String enchant_site;
	
	public TicketAPI(String username,String password,String site) {
		enchant_username=username;
		enchant_password=password;
		enchant_site=site;
	}
	
			public int createTicket(String customerid,String subject,String reply) throws IOException {
		        URL urldemo = new URL("https://"+enchant_site+".enchant.com/api/v1/tickets");
		        String userpass = enchant_username + ":" +enchant_password;
		        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
		        JSONObject json=new JSONObject();
		        json.put("type","email");
		        json.put("subject", subject);
		        json.put("customer_id", customerid);
		        json.put("reply_to", reply);
		        byte[] postDataBytes = json.toString().getBytes("UTF-8");
		        HttpURLConnection yc = (HttpURLConnection)urldemo.openConnection();
		        yc.setRequestProperty ("Authorization", basicAuth);
		        yc.setRequestProperty( "Content-Type", "application/json");     
		        yc.setRequestMethod("POST");
		        yc.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		        yc.setDoOutput(true);
		        yc.getOutputStream().write(postDataBytes);
		        statuscode=yc.getResponseCode();
		        System.out.println("Status code "+statuscode);  
		       	try {
					result=HttpConnection.getBodyOfResponse(yc);
				} catch (Exception e) {
					e.printStackTrace();
				}
		        return statuscode;
			}
			
			public int getTickets(){
				    try {
				    	String path="https://"+enchant_site+".enchant.com/api/v1/tickets";
				        URL urldemo = new URL(path);
				        System.err.println(path);
				        HttpURLConnection yc = (HttpURLConnection)urldemo.openConnection();
				        String userpass = enchant_username + ":" +enchant_password;
				        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
				        yc.setRequestProperty ("Authorization", basicAuth);
				        statuscode=yc.getResponseCode();
				        if(statuscode==200)
				          result=HttpConnection.getBodyOfResponse(yc);
				        System.out.println("JSON RESPONSE IS "+result);
				    }
				     catch(Exception e) {
				            e.printStackTrace();
				        }
				    return statuscode;
			  }
			
			
            public int addNotetoTicket(String ticketid,String userid,String body){
            	try{
            	System.out.println("user id "+userid);
            	 URL urldemo = new URL("https://"+enchant_site+".enchant.com/api/v1/tickets/"+ticketid+"/messages");
 		        String userpass = enchant_username + ":" +enchant_password;
 		        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
 		        JSONObject json=new JSONObject();
 		        json.put("type","note");
 		        json.put("user_id", userid);
 		        json.put("body", body);
 		        json.put("htmlized",false);
 		        byte[] postDataBytes = json.toString().getBytes("UTF-8");
 		        HttpURLConnection yc = (HttpURLConnection)urldemo.openConnection();
 		        yc.setRequestProperty ("Authorization", basicAuth);
 		        yc.setRequestProperty( "Content-Type", "application/json");     
 		        yc.setRequestMethod("POST");
 		        yc.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
 		        yc.setDoOutput(true);
 		        yc.getOutputStream().write(postDataBytes);
 		        statuscode=yc.getResponseCode();
 		        System.out.println("Status code "+statuscode+" "+HttpConnection.getBodyOfResponse(yc));  
 		        System.out.println(yc.getResponseMessage());
 				result=HttpConnection.getBodyOfResponse(yc);
 			
 				} catch (Exception e) {
 					
 					e.printStackTrace();
 				
 				}
 		        return statuscode;
			}
			
			public String getResponse(){
				return result;
			}
			
			public int getMessages(String ticketid){
			    try {
			        URL urldemo = new URL("https://"+enchant_site+".enchant.com/api/v1/tickets/"+ticketid+"?embed=messages");
			        HttpURLConnection yc = (HttpURLConnection)urldemo.openConnection();
			        String userpass = enchant_username + ":" +enchant_password;
			        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
			        yc.setRequestProperty ("Authorization", basicAuth);
			        statuscode=yc.getResponseCode();
			        if(statuscode == 200)
			          result=HttpConnection.getBodyOfResponse(yc);
			        System.out.println("JSON RESPONSE IS "+result);
			    }
			     catch(Exception e) {
			            e.printStackTrace();
			        }
			    return statuscode;
		  }
			
			
		public static void main(String args[]) {
			try{
		//	createTicket("xjoseph.arnold@gmail.com","12345","559a1c8e69702d7bd100580b","TESTING","vinay@enchant.com");
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

}
