package com.teamchat.integrations.RSS;

public class Feed{
	private String title;
	private String link;
	private String description;
	private String guid;
	private String pubdate;
	Feed Next=null;
	
	Feed(String Title, String Link, String Description, String Guid, String pubDate)
	{
		title=Title;
		link=Link;
		description=Description;
		guid=Guid;
		pubdate=pubDate;
		   }
	
	public String getFeed()
	{ 
		String html="";
	if(pubdate!=null)
		html+=pubdate.substring(0, 16)+"<br/>";
	else html+="<br/>";
		if(description!=null)
	   html+="<a href='"+link+"'><h4>"+title+"</h4></a>"+description;
		else  html+="<a href='"+link+"'><h5>"+title+"</h5></a>";
	  return html;
	}
}