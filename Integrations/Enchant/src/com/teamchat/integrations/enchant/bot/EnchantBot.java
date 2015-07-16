package com.teamchat.integrations.enchant.bot;

import java.util.ArrayList;
import java.util.List;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;
import com.teamchat.integrations.enchant.database.Authentication;

import enchantapi.Customer;
import enchantapi.CustomerAPI;
import enchantapi.EnchantCredentials;
import enchantapi.Message;
import enchantapi.ParseJSON;
import enchantapi.Ticket;
import enchantapi.TicketAPI;
import enchantapi.User;
import enchantapi.UserAPI;

public class EnchantBot {
	private HTMLBuilder html;
	
	public EnchantBot() {
		html=new HTMLBuilder();
	}
	
	
	@OnKeyword("help")
	public void onhelpEntry(TeamchatAPI api) {
		System.out.println("help");
		api.perform(
				api.context().currentRoom().registerForEvents().post(
				new TextChatlet("Type 'ticket' to view all tickets and to add notes to tickets<br/>"
						 +      "Type 'create' to create a new ticket<br/>"
						 +      "Type 'msg' to view messages associated with a ticket<br/>"
						 +      "Type 'login' to login<br/>"
						         )));
			
	}
	
	
	@OnKeyword("msg")
	public void msgEntry(TeamchatAPI api) {
		EnchantCredentials ec=Authentication.getCredentials(api.context().currentSender().getEmail());
		List<Ticket> tickets=new ArrayList<Ticket>();
		if(ec==null) {
			onloginEntry(api,"Please login first and then type 'msg' again.");
		}
		else {
			 TicketAPI ticketinfo=new TicketAPI(ec.username,ec.password,ec.site);
			 int statuscode=ticketinfo.getTickets();
			 if(statuscode==200) {
				 tickets=ParseJSON.getTickets(ticketinfo.getResponse());
				 for(int j=0;j<tickets.size();j++) {
					  String result=html.displayTickets(tickets.get(j));
					  result+="Click on submit to get messages associated with this ticket";
					  
			          api.perform(
						api.context().currentRoom().registerForEvents().post(
						new PrimaryChatlet()
						.setQuestionHtml(result)
						.setReplyScreen(api.objects().form()
						.addField(api.objects().hidden().label("").name("id").value(tickets.get(j).id))
						.addField(api.objects().hidden().label("").name("number").value(tickets.get(j).number))
						)
						.alias("Get messages")
						)
						);
				 }
			 }
			 else if(statuscode==401) {
				 onloginEntry(api,"Invalid Enchant credentials.Could not connect to Enchant. Please login and type 'msg' again."); 
			 }
			 else {
				 api.perform(
							api.context().currentRoom().registerForEvents().post(
							new TextChatlet("There seems to be a problem. Http status is"+statuscode+". Try logging in again")));
			 }
		}
	}
	
	
	@OnKeyword("ticket")
    public void ticketentry(TeamchatAPI api) {
	  String result="";
	  List<Ticket> tickets=new ArrayList<Ticket>();
	  List<User> users;
	  EnchantCredentials ec=Authentication.getCredentials(api.context().currentSender().getEmail());
	  if(ec==null)
		  onloginEntry(api,"Please first login with your Enchant credentials and type 'ticket' again.");
	  else {
	    try {
		    TicketAPI ticketinfo=new TicketAPI(ec.username,ec.password,ec.site);
		    UserAPI userinfo=new UserAPI(ec.username,ec.password,ec.site);
		     int statuscode=ticketinfo.getTickets();
		 	 int userapistatus=userinfo.getUsers();
		 	 
	        if(statuscode==200 && userapistatus==200) {
	        	users=ParseJSON.getUsers(userinfo.getResponse());
	        	System.out.println(userinfo.getResponse());
		        tickets=ParseJSON.getTickets(ticketinfo.getResponse());
		      for(int j=0;j<tickets.size();j++) {
		    	  result=html.displayTickets(tickets.get(j));
		          api.perform(
					api.context().currentRoom().registerForEvents().post(
					new PrimaryChatlet()
					.setQuestionHtml(result)
					.setReplyScreen(api.objects().form()
					.addField(api.objects().hidden().label("").name("id").value(tickets.get(j).id))
					.addField(api.objects().input().label("Body").name("body"))
					.addField(getUserOptions(api,users)))
					.alias("Create note")
					)
					);
		      }
	        }
	        else if(statuscode==401){
	        	 System.err.println("status code is"+statuscode);
	             onloginEntry(api,"Invalid credentials. Please login and type 'tickets' again.");
	        }
	        else {
	        	api.perform(
						api.context().currentRoom().registerForEvents().post(
						new TextChatlet("There seems to be a problem. Http status returned is "+statuscode+". Try logging in again")));
	        }
	    }
	        catch(Exception e) {
	            e.getMessage();
	        }
	  }
  }
	
	@OnKeyword("create")
    public void createEntry(TeamchatAPI api) {
		  EnchantCredentials ec=Authentication.getCredentials(api.context().currentSender().getEmail());
		  if(ec==null)
			  onloginEntry(api,"Please first login with your Enchant credentials and type 'create' again.");
		  else {
			try{
			String result="";
			List<Customer> customers;
            CustomerAPI customerinfo=new CustomerAPI(ec.username,ec.password,ec.site);
            int statuscode=customerinfo.getCustomers();
	        if(statuscode==200) {
	          result=customerinfo.getResponse();
	    	  customers=ParseJSON.getCustomers(result);
	    	  System.out.println("Number of customers "+customers.size());
			  api.perform(
						api.context().currentRoom().registerForEvents().post(
						new PrimaryChatlet()
						.setQuestion("Create a ticket")
						.setReplyScreen(api.objects().form()
						.addField(api.objects().input().label("Subject").name("subject"))
						.addField(getCustomerOptions(api,customers))
						.addField(api.objects().input().label("Reply to").name("replymail"))
						)
						.alias("Create ticket"))
						);
	        }
	        else if(statuscode==401){
	        	 System.err.println("status code is"+statuscode);
	             onloginEntry(api,"Invalid credentials. Please login and type 'create' again.");
	        }
	        else {
	        	api.perform(
						api.context().currentRoom().registerForEvents().post(
						new TextChatlet("There seems to be a problem. Http status returned is "+statuscode+". Try logging in again")));
	        }
			}
	        catch(Exception e) {
	            e.getMessage();
	        }
		}
		
  }
	
	public Field getCustomerOptions(TeamchatAPI api,List<Customer> customers) {
		Field f=api.objects().select().label("Choose customer").name("customer");
		for(int i=0;i<customers.size();i++) {
			System.out.println("CUSTOMER EMAIL: "+customers.get(i).email);
			f.addOption(customers.get(i).email);
		}
		return f;
	}
	
	public Field getUserOptions(TeamchatAPI api,List<User> users) {
		Field f=api.objects().select().label("Choose user to associate with the message").name("user");
		for(int i=0;i<users.size();i++) {
			System.out.println(users.get(i).id);
			f.addOption(users.get(i).email);
		}
		return f;
	}
	
	public Field getTicketOptions(TeamchatAPI api,List<Ticket> tickets) {
		Field f=api.objects().select().label("Choose the ticket to which a note must be added").name("user");
		for(int i=0;i<tickets.size();i++){
			f.addOption(tickets.get(i).id);
		}
		return f;
	}
	
	@OnKeyword("login")
	 public void loginEntry(TeamchatAPI api) {
		 api.perform(
					api.context().currentRoom().registerForEvents().post(
					new PrimaryChatlet()
					.setQuestion("Enter your enchant credentials")
					.setReplyScreen(api.objects().form()
					.addField(api.objects().input().label("Email").name("email"))
					.addField(api.objects().input().label("Password").name("password"))
					.addField(api.objects().input().label("Enchant site name (For example 'teamchat' is the site name for the enchant url 'https://teamchat.enchant.com')").name("site"))
					)
					.alias("Login"))
					);
	 }
	
	
	
	 public void onloginEntry(TeamchatAPI api,String message) {
		 api.perform(
					api.context().currentRoom().registerForEvents().post(
					new PrimaryChatlet()
					.setQuestion(message)
					.setReplyScreen(api.objects().form()
					.addField(api.objects().input().label("Email").name("email"))
					.addField(api.objects().input().label("Password").name("password"))
					.addField(api.objects().input().label("Enchant site name (For example 'teamchat' is the site name for the enchant url 'https://teamchat.enchant.com')").name("site"))
					)
					.alias("Login"))
					);
	 }
	
	@OnAlias("Get messages")
	public void getMessages(TeamchatAPI api){
		List<Message> msges=new ArrayList<Message>();
	  String ticketid=api.context().currentReply().getField("id");
	  String number=api.context().currentReply().getField("number");
	  EnchantCredentials ec=Authentication.getCredentials(api.context().currentReply().senderEmail());
	  TicketAPI ticketinfo=new TicketAPI(ec.username,ec.password,ec.site);
	  int status=ticketinfo.getMessages(ticketid);
	  
	   if(status==401){
      	 System.err.println("status code is"+status);
           onloginEntry(api,"Invalid credentials.Could not connect to Enchant. Please login and type 'tickets' again.");
      }
      else if(status != 200){
      	api.perform(
					api.context().currentRoom().registerForEvents().post(
					new TextChatlet("There seems to be a problem. Http status returned is "+status+". Try logging in again")));
      }
	  else {
		  msges=ParseJSON.getMessages(ticketinfo.getResponse());
		  String result=html.displayMessages(msges,number);
		   api.perform(
					api.context().currentRoom().registerForEvents().post(
					new PrimaryChatlet()
					.setQuestionHtml(result)));
	  }
	}
	
	@OnAlias("Login")
	public void storeCredentials(TeamchatAPI api){
		String teamchatid=api.context().currentReply().senderEmail();
		String user = api.context().currentReply().getField("email");
		String pwd = api.context().currentReply().getField("password");
		String site= api.context().currentReply().getField("site");
		Authentication.setCredentials(teamchatid, user, pwd,site);
		api.perform(
		api.context().currentRoom().registerForEvents().post(
				new TextChatlet("Type your keywords again. Type help for options")));
		
	}
	
	public String getUserId(UserAPI userinfo,String useremail) {
		String uid="";
	     List<User> users=ParseJSON.getUsers(userinfo.getResponse());
	     for(User u:users){
	    	 if(u.email.equals(useremail))
	    		 return u.id;
	     }
	     return uid;
	}
	
	public String getCustomerId(CustomerAPI custinfo,String customeremail) {
		String uid="";
	     List<Customer> customers=ParseJSON.getCustomers(custinfo.getResponse());
	     for(Customer c:customers){
	    	 if(c.email.equals(customeremail))
	    		 return c.id;
	     }
	     return uid;
	}
	
	@OnAlias("Create note")
	public void createNote(TeamchatAPI api){
		String body= api.context().currentReply().getField("body");
		String ticketid = api.context().currentReply().getField("id");
		String useremail = api.context().currentReply().getField("user");
		
		try {
		 EnchantCredentials ec=Authentication.getCredentials(api.context().currentReply().senderEmail());
		 UserAPI userinfo=new UserAPI(ec.username,ec.password,ec.site);
		 if(userinfo.getUsers()==401) {
			 onloginEntry(api,"Invalid credentials.Could not connect to Enchant. Please login and type 'ticket' again."); 
		 }
		 else {
	      String userid= getUserId(userinfo,useremail);
	      TicketAPI ticketinfo=new TicketAPI(ec.username,ec.password,ec.site); 
	      int statuscode=ticketinfo.addNotetoTicket(ticketid, userid, body);
	      System.out.println("Status code after adding not is "+statuscode);
		  if(statuscode==201) {
			 api.perform(
						api.context().currentRoom().registerForEvents().post(
						new TextChatlet("Note added to ticket "+ticketid)));			
		 }
		 else if(statuscode==401) {
			 onloginEntry(api,"Invalid credentials.Could not connect to Enchant.Please login and type 'ticket' again.");
		 }
		 else {
			 api.perform(
						api.context().currentRoom().registerForEvents().post(
						new TextChatlet("There seems to be a problem. Http status is"+statuscode+". Try logging in again")));
		 }
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	@OnAlias("Create ticket")
	public void createTicket(TeamchatAPI api){
		EnchantCredentials ec=Authentication.getCredentials(api.context().currentReply().senderEmail());
		if(ec==null) {
			onloginEntry(api,"Please login first and then type 'create ticket' again.");
		}
		else {
		String subject= api.context().currentReply().getField("subject");
		String customermail = api.context().currentReply().getField("customer");
		String replyemail = api.context().currentReply().getField("replymail");
		CustomerAPI c=new CustomerAPI(ec.username,ec.password,ec.site);
		TicketAPI ticketinfo=new TicketAPI(ec.username,ec.password,ec.site);
		if(c.getCustomers()==401) {
			onloginEntry(api,"Invalid credentials.Could not connect to Enchant. Please login and type 'ticket' again.");
		}
		else {
			String customerid=getCustomerId(c,customermail);
		try {
	     
	     int statuscode=ticketinfo.createTicket(customerid, subject,replyemail);
		 if(statuscode==201) {
			 api.perform(
						api.context().currentRoom().registerForEvents().post(
						new TextChatlet("Ticket created successfully")));			
		 }
		 else if(statuscode==401) {
			 onloginEntry(api,"Invalid credentials. Please login and type 'ticket' again.");
		 }
		 else {
			 api.perform(
						api.context().currentRoom().registerForEvents().post(
						new TextChatlet("There seems to be a problem. Http status is"+statuscode+". Try logging in again")));
		 }
		}
		catch(Exception e){
			e.printStackTrace();
		}
		}
		}
	}
	
  
	
}
