package enchantapi;

public class Ticket {
      public String number;
      public String id;
      public String customermail;
      public String summary;
      public String subject;
      public String state;
      public String createdat;
      public String updated;
       
       public Ticket(String id,String number,String subject,String state,String customer,String summary,String createdat,String updated) {
    	   this.number=number;
    	   this.id=id;
    	   this.subject=subject;
    	   this.state=state;
    	   this.customermail=customer;
    	   this.summary=summary;
    	   this.createdat=createdat;
    	   this.updated=updated;
    	   
       }
}
