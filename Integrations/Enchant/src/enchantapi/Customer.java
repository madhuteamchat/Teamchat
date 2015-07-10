package enchantapi;

public class Customer {
  public String id;
  public String first_name="";
  public String last_name="";
  public String email="";
  
  public Customer(String id,String first_name,String last_name,String email){
	  this.id=id;
	  this.first_name=first_name;
	  this.last_name=last_name;
	  this.email=email;
  }
}
