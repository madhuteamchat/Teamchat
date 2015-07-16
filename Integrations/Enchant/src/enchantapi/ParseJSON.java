package enchantapi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ParseJSON {
    public Boolean isAuthenticated(String response) {
    	try {
    		  JSONObject json = new JSONObject(response);
    		  if(json.has("message"));
    	//	  JSONObject auth=json.getJSONObject("message");
    	//	  token=auth.getString(token);
    		  }
    		  catch(Exception e) {
    			  e.printStackTrace();
    		  }
    	return false;
    }
    
    public static List<Ticket> getTickets(String response) {
    	List<Ticket> tickets=new ArrayList<Ticket>();
    	try {
    		  JSONArray ticketarrayjson = new JSONArray(response);
    		  for(int i=0;i<ticketarrayjson.length();i++) {
    			  JSONObject obj=ticketarrayjson.getJSONObject(i);
    			  int num=obj.getInt("number");
    			  String id=obj.getString("id");
    			  String state=obj.getString("state");
    			  String subject=obj.getString("subject");
    			  String customer=obj.getString("reply_to");
    			  String updated=obj.getString("updated_at");
    			  String created=obj.getString("created_at");
    			  String summary="";
    			  if(!obj.isNull("summary"))
    			    summary=obj.getString("summary");
    			  tickets.add(new Ticket(id,num+"",subject,state,customer,summary,created,updated));
    		  }
    		  }
    		  catch(Exception e) {
    			  e.printStackTrace();
    		  }
    	return tickets;
    }
    
    public static List<User> getUsers(String response) {
    	List<User> users=new ArrayList<User>();
    	try {
    		  JSONArray userarrayjson = new JSONArray(response);
    		  for(int i=0;i<userarrayjson.length();i++) {
    			  JSONObject obj=userarrayjson.getJSONObject(i);
    			  String id=obj.getString("id");
    			  String firstname=obj.getString("first_name");
    			  String lastname=obj.getString("last_name");
    			  String email=obj.getString("email");
    			  users.add(new User(id,firstname,lastname,email));
    		  }
    		  }
    		  catch(Exception e) {
    			  e.printStackTrace();
    		  }
    	return users;
    }
    
    public static List<Customer> getCustomers(String response) {
    	List<Customer> customers=new ArrayList<Customer>();
    	System.out.println("PARSING "+response);
    	try {
    		  JSONArray ticketarrayjson = new JSONArray(response);
    		  for(int i=0;i<ticketarrayjson.length();i++) {
    			  JSONObject obj=ticketarrayjson.getJSONObject(i);
    			  String customerid=obj.getString("id");
    			  String first_name=obj.getString("first_name");
    			  String last_name=obj.getString("last_name");
    			  String email="";
    			  JSONArray contactarray=obj.getJSONArray("contacts");
    			  for(int j=0;j<contactarray.length();j++) {
    				  JSONObject contactobj=contactarray.getJSONObject(j);
    				  if(contactobj.has("type")) {
    					  if(contactobj.getString("type").equals("email"))
    						  email=contactobj.getString("value");
    						  
    				  }
    			  }

    			  customers.add(new Customer(customerid,first_name,last_name,email));
    		  }
    		  }
    		  catch(Exception e) {
    			  e.printStackTrace();
    		  }
    	return customers;
    }
    
    public static List<Message> getMessages(String response) {
    	System.out.println("parsing messages from JSON "+response);
    	List<Message> msgs=new ArrayList<Message>();
    	try {
    		  JSONObject messagejson = new JSONObject(response);
    		  JSONArray messagearray=messagejson.getJSONArray("messages");
    		  for(int i=0;i<messagearray.length();i++) {
    			  JSONObject obj=messagearray.getJSONObject(i);
    			  String id=obj.getString("id");
    			  String type=obj.getString("type");
    			  String name=obj.getString("from_name");
    			  String body=obj.getString("body");
    			  msgs.add(new Message(id,type,body,name));
    		  }
    		  }
    		  catch(Exception e) {
    			  e.printStackTrace();
    		  }
    	return msgs;
    }
}
