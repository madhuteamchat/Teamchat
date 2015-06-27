package com.teamchat.integration.zendesk.servlet;

public class updatestatus {

	String ticketId, status;

	public updatestatus(String ticketId, String status) throws Exception {
		this.ticketId = ticketId;
		this.status = status;

	}

	public void update()throws Exception
	{
		 DBHandler datacng = new DBHandler();
		 
		datacng.stmt.executeUpdate("UPDATE " + datacng.configProps.getProperty("dbname").trim() +"."+datacng.configProps.getProperty("tablename").trim() + " SET TktStatus ='"+ status +"'"+" where ticketid='" + ticketId + "'");
	   	
		Feedback ob1 = new Feedback(ticketId);
		ob1.formfeed();
		
	}
}
