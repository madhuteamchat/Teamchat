package com.teamchat.integrations.pipedrive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.Field;




public class Pipedrivebot {
	static ArrayList<Integer> a = new ArrayList<Integer>();
	static	ArrayList<Integer> b = new ArrayList<Integer>();
	static ArrayList<Integer> c = new ArrayList<Integer>();
	static ArrayList<String> d = new ArrayList<String>();
	static ArrayList<String> e = new ArrayList<String>();
	static ArrayList<String> f = new ArrayList<String>();
	static ArrayList<String> g = new ArrayList<String>();
	static ArrayList<String> h = new ArrayList<String>();
	
	
	static TeamchatAPI apig;
	public static String email;
	
	
//	String apikey;
	int userid;
//	public static void main(String[] args)
//	{
//		TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data")
//				 .setEmail("pipedrive37@gmail.com") //change to your teamchatregistered email id
//				 .setPassword("teamchat") //change to your teamchat password
//				 .startReceivingEvents(new Pipedrivebot()); //Wait for other user tosend message
//				}
 
	
	@OnKeyword("help")
	public void help(TeamchatAPI api) {
	apig=api;
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
				.setQuestionHtml("<h3><center>Hey!!, This is Pipedrive Bot<center> </h3>"
								+ "<h4>You can track your deals,<br> view the stage of the deal and the basic information about the deal . "
								+ " Use me to perform following functions:</h4>"
								+ "<ul><li><b>configure</b> to configure your Pipedrive account  </li> "
								+ "<li><b>listdeal</b> to view any deal  </li>  "
								+ "<li><b>logout</b> to logout  </li>  "
								

				)

				));
		
		
	}

	@OnKeyword("configure")
	public void onCreate(TeamchatAPI api) {
		
		apig=api;
		String email1 = api.context().currentSender().getEmail();

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml(
								"please enter your emailid and pasword of your pipedrive account")
						.setReplyScreen(
								api.objects()
										.form()

										.addField(
												api.objects().input()
														.label("EmailID")
														.name("email"))
										.addField(
												api.objects().input()
														.label("Password")
														.name("password"))

						).alias("answer1")));
}
	
	@OnAlias("answer1")
	public void onData(TeamchatAPI api) throws IOException {
		String email = api.context().currentReply().getField("email");
		String password = api.context().currentReply().getField("password");
		@SuppressWarnings({ "deprecation", "resource" })
		HttpClient httpclient = new DefaultHttpClient();
		 
	        
	 
	            HttpPost httpPost = new HttpPost("https://api.pipedrive.com/v1/authorizations");
	 
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	
	            nameValuePairs.add(new BasicNameValuePair("email",email));
	            nameValuePairs.add(new BasicNameValuePair("password",password));
	           
	              
	            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
	 
	            System.out.println("executing request " + httpPost.getRequestLine());
	            HttpResponse response = httpclient.execute(httpPost);
	           BufferedReader br=new BufferedReader(new 
	        		InputStreamReader (response.getEntity().getContent())   );
	           
	           StringBuffer sb=new StringBuffer("");
	           String line ="";
	           while((line= br.readLine()) !=null){
	        	   
	        	   sb.append(line);
	        	   
	           }
	           br.close();
	           
	           
	           System.out.println(sb);
	           JSONObject jobj = new JSONObject(sb.toString());
          boolean abc = jobj.getBoolean("success");
	           JSONArray data=jobj.getJSONArray("data");
	         JSONObject jobj1=data.getJSONObject(0);
	         userid=jobj1.getInt("user_id");
	        String apikey =jobj1.getString("api_token");
	         System.out.println(abc);
	         String smail = api.context().currentReply().senderEmail();
	         ManageDB db=new ManageDB();
	         db.insert(smail, apikey);
	         
	
	}
	
	@OnKeyword("listdeal")
	public void ondeal(TeamchatAPI api) throws ClientProtocolException, IOException {
		String smail = api.context().currentSender().getEmail();
		
		apig=api;
   Deals de= new Deals();

ManageDB db=new ManageDB();
String apikey=db.retrieve(smail);
de.deal(apikey);


	for (int i=0;i<f.size();i++)
	{
		System.out.println(f.get(i));
	}
	
	String label1="Name of deal",name1="nlist";
	api.perform(
			api.context().currentRoom().post(
			new PrimaryChatlet()
			.setQuestionHtml("Select the  deals")
			.setReplyLabel("select")
			.setReplyScreen
			(
					api.objects().form()
					.addField(getOptions(api,label1,name1,f))
			)
			.alias("dealid")
			)
		);
	

}


private Field getOptions(TeamchatAPI api,String label1,String name1,ArrayList<String> f2) {
	// TODO Auto-generated method stub
	Field f=api.objects().select().label(label1).name(name1);
	for(int i=0;i<f2.size();i++){
		f.addOption(f2.get(i));
	}
	return f;
}
	@OnAlias("dealid")
	public void onDeal(TeamchatAPI api)
	{
		String title =  api.context().currentReply().getField("nlist");
		for ( int i=0;i<f.size();i++)
		{
			if (title.equals(f.get(i)))
			{
				if(b.get(i)==1)
				{
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet()
								.setQuestionHtml(
										" The details of<b> "+title+" </b>are as follows<br>"
												+ "The deal id :"+a.get(i)+"<br> Client Name : "+d.get(i)
												+"<br> Organisation Name :"+e.get(i)
												+"<br> Stage  : Idea"
												+"<br> The Deal value: "+h.get(i)
										)
										
								
										));
			}
				else if(b.get(i)==2)
				{
					api.perform(api
							.context()
							.currentRoom()
							.post(new PrimaryChatlet()
									.setQuestionHtml(
											" The details of<b> "+title+" </b>are as follows<br>"
													+ "The deal id :"+a.get(i)+"<br> Client Name : "+d.get(i)
													+"<br> Organisation Name :"+e.get(i)
													+"<br> Stage  : Contact Made"
													+"<br> The Deal value: "+h.get(i)
											)
											
									
											));
				}
				else if(b.get(i)==3)
				{
					api.perform(api
							.context()
							.currentRoom()
							.post(new PrimaryChatlet()
									.setQuestionHtml(
											" The details of<b> "+title+" </b>are as follows<br>"
													+ "The deal id :"+a.get(i)+"<br> Client Name : "+d.get(i)
													+"<br> Organisation Name :"+e.get(i)
													+"<br> Stage  : Needs Discovered"
													+"<br> The Deal value: "+h.get(i)
											)
											
									
											));
				}
				else if(b.get(i)==4)
				{
					api.perform(api
							.context()
							.currentRoom()
							.post(new PrimaryChatlet()
									.setQuestionHtml(
											" The details of<b> "+title+" </b>are as follows<br>"
													+ "The deal id :"+a.get(i)+"<br> Client Name : "+d.get(i)
													+"<br> Organisation Name :"+e.get(i)
													+"<br> Stage  : Proposal Presented"
													+"<br> The Deal value: "+h.get(i)
											)
											
									
											));
				}
				else
				{
					api.perform(api
							.context()
							.currentRoom()
							.post(new PrimaryChatlet()
									.setQuestionHtml(
											" The details of<b> "+title+" </b>are as follows<br>"
													+ "The deal id :"+a.get(i)+"<br> Client Name : "+d.get(i)
													+"<br> Organisation Name :"+e.get(i)
													+"<br> Stage  : In Negotiation"
													+"<br> The Deal value: "+h.get(i)
											)
											
									
											));
				}
				}
		}
		
	}
	
	
	@OnKeyword("logout")
	
	public void logout(TeamchatAPI api)
	{String smail = api.context().currentSender().getEmail();
	
	apig=api;
		ManageDB db  =new ManageDB();
		db.delete(smail);
	}
	
	@OnKeyword("notify")
	public void  notification (TeamchatAPI api) throws ClientProtocolException, IOException
	{
		apig=api;
	String	 smail = api.context().currentSender().getEmail();
		email=api.context().currentSender().getEmail();
		ManageDB db=new ManageDB();
	String apikey=db.retrieve(smail);
		Deals de =new Deals();
	boolean flag=	de.notifyme(apikey,userid);
	
	  if(flag==true)
	  {
		  
		  api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("You have successfuly added this account in push notification")));
		  
		 
	  }
	  
	  else
		  api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("Your  account not added in push notification")));
		  
		

	}
	
	}
	
	


