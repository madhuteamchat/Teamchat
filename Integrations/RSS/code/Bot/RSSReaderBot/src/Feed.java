
public class Feed{
	String title;
	String link;
	String description;
	String guid;
	String pubdate;
	Feed Next=null;
	
	Feed(String Title, String Link, String Description, String Guid, String pubDate)
	{
		title=Title;
		link=Link;
		description=Description;
		guid=Guid;
		pubdate=pubDate;
		System.out.println("title:"+title+" description: "+description+" link: "+link);
		   
	}
	
	public String getFeed()
	{  String html="";
	if(pubdate!=null)
		html+=pubdate+"<br/>";
	else html+="<br/>";
		if(description!=null)
	   html="<a href='"+link+"'><h4>"+title+"</h4></a>"+description;
		else  html="<a href='"+link+"'><h5>"+title+"</h5></a>";
		
	System.out.println("Link of this is :"+link);
	   return html;
	}
}
