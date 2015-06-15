import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.*;


public class Channel{
	String title;
	String link;
	String description;
	String guid;
	String lastUpdated;
	String lastFeedId;
	String lastFeedTime;
	String channelName;
	URL url;
	private TeamchatAPI api;
	
	boolean subscribed;
	
	Feed feeds;
	
	
	Channel(String URL)
	{
	 try{
	  url=new URL(URL);
	 }
	 catch(MalformedURLException e)
	 {
	  System.out.println(e);	 
	 }
	}
//	private URL generateURL(String URL)
	{
		
	}
	
	public void setTeamchatAPI(TeamchatAPI API)
	{
		api=API;
	}
	
	public void subscribe()
	{
	  subscribed=true;
	}
	
	public void unsubscribe()
	{
	  subscribed=false;
	  feeds=null;
	}
	public Boolean getSubscription()
	{
		if(subscribed)
			return true;
		return false;
	}
	
	public String getName()
	{
		return channelName;
	}
	
	public void setName(String s)
	{
		channelName=s;
	}
	
	public void getFeeds()
	{    
		 String TITLE  = null,
		        DESCRIPTION=null,
		        GUID=null,
		        AUTHOR=null,
		        LINK=null,
		        LANGUAGE=null,
		        COPYRIGHT=null,
		        PUB_DATE=null;
		 try{
			 File file=new File("fb.rss");
			 System.out.println("File Imported");
      //FileInputStream in=new FileInputStream(file);
      InputStream in=url.openStream();
      
      Boolean getChannelDetails=false;
      Boolean firstItem=true;
      Boolean IsUpdated=true;
      
      XMLInputFactory factory = XMLInputFactory.newInstance();
      XMLEventReader eventReader = factory.createXMLEventReader(in);
      
      
      while(eventReader.hasNext()&&IsUpdated)
      {   
     	 XMLEvent event=eventReader.nextEvent();
         Feed f; 
     	 if(event.isStartElement())
     	 { 	
         String localpart=event.asStartElement().getName().getLocalPart();
     	// System.out.println("localpart: "+localpart);
     	    switch(localpart)
     	    {
     	    case "logo":
     	    case "image":                      //RSS Feed
     	    case "item":                       //RSS Feed
     	    case "entry":                      //ATOM Feed
     	    	if(!getChannelDetails)
     	    	{
     	    	title=TITLE;
     	    	link=LINK;
     	    	description=DESCRIPTION;
     	    	if(PUB_DATE!=null
     	    			&&lastUpdated!=null
     	    			&&PUB_DATE.compareTo(lastUpdated)==0)
     	    	{IsUpdated=false;
     	    	break ;}
     	    	
     	    	lastUpdated=PUB_DATE;
     	    	 System.out.println("Last Feed Id set to:"+lastFeedId
     	     			   +"\n Title set to "+title+TITLE
     	     			   +"\n last Updated set to: "+lastUpdated);
     	    	 PUB_DATE=null;
     	    	getChannelDetails=true;
     	    	}
     	    	break;
     	     
     	      case "title":
               TITLE= getCharacterData(event, eventReader);
               break;
             
     	      case "url":                // RSS Feed Image element
     	      case "description":        // RSS Feed
     	      case "subtitle":           // ATOM Feed
     	      case "summary":            // ATOM Feed Item element
               DESCRIPTION = getCharacterData(event, eventReader);
                break;
             
              case "link":
               LINK = getCharacterData(event, eventReader);
                break;
              
              case "guid":
               GUID = getCharacterData(event, eventReader);
                break;
              
              case "language":
               LANGUAGE = getCharacterData(event, eventReader);
                break;
              
              case "author":
               AUTHOR = getCharacterData(event, eventReader);
                break;
             
              case "pubDate":                      // RSS feed Item element
              case "lastBuildDate":                // RSS feed
              case "updated":                      // ATOM feed
               PUB_DATE = getCharacterData(event, eventReader);
                break;
              
              case "copyright":
               COPYRIGHT = getCharacterData(event, eventReader);
                break;
             
              default:
            	//System.out.println("This is a local part:"+localpart);
           }
     	 }
     	    else if(event.isEndElement())
     	    {
     	    	String localpart=event.asEndElement().getName().getLocalPart();
     	    	if(localpart.compareToIgnoreCase("item")==0
     	    	|| localpart.compareToIgnoreCase("entry")==0
     	    	// || localpart.compareToIgnoreCase("image")==0
     	    	// || localpart.compareToIgnoreCase("logo")==0
     	    	  )
     	    	{   
     	    		if(lastFeedId!=null&&
     	    				(GUID!=null&&GUID.compareTo(lastFeedId)==0
     	    			||TITLE.compareTo(lastFeedId)==0))
     	    		{System.out.println("broke");
     	    			break;}
     	    		Feed fd=new Feed(TITLE,LINK,DESCRIPTION,GUID,PUB_DATE);
                    if(firstItem)
                    {
                    	feeds=fd;
                    	firstItem=false;
                    	if(GUID!=null)
                    	lastFeedId=GUID;
                    	else lastFeedId=TITLE;
                    		System.out.println("First Item Added");
                    }
                    else{
                    	fd.Next=feeds;
                    	feeds=fd;
                    }
     	    	}
     	    }//else if(event.isCharacters())
     	    	//System.out.println(event.asCharacters().getData());
     	 
      }
      in.close();
    //  System.out.println("Last Feed Id set to:"+lastFeedId+"\n Title set to "+title+TITLE);
	  }catch(XMLStreamException e){System.out.println(e);}
		 catch(IOException e){e.printStackTrace();}
	
	}
	 private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
		      throws XMLStreamException {
		    String result = "";
		  
			   if(event.isStartElement()&&event.asStartElement().getName().getLocalPart().compareToIgnoreCase("link")==0)
		    {try{
		       result=event.asStartElement().getAttributeByName(new QName("href")).getValue();
		    System.out.println("Link part is:"+result);    
		    }catch(Exception e){
		    	System.out.println("error "+e);
		    	result=null;
		    }
		       if(result==null)
		       {event = eventReader.nextEvent();
			    if (event instanceof Characters) {
				      result = event.asCharacters().getData();
				    }   
		        }
		       }
		    else{System.out.print(event.asStartElement().getName().getLocalPart()+":");
		    	event=eventReader.nextEvent();
		    	 while(event.isCharacters())
		    { if (event instanceof Characters) {
		      result += event.asCharacters().getData();
		      event = eventReader.nextEvent();
			       }
		    }
		   }
			//   System.out.println("result:"+result);
		    return result;
		  }
	 
	 public void postFeeds(TeamchatAPI api, String roomID)
	 {   Feed feed=feeds;
	 	while(feed!=null)
	 	{
	 	  String blog=feed.getFeed();
	      String html="<b><u><h4>"+title+"</h4></b></u>";
	      html+=blog;
	      api.perform(api.context().byId(roomID).post(new PrimaryChatlet().setQuestionHtml(html)));

	 	feed=feed.Next;
	 	System.out.println("CHatlet Psted \n\n");
	 	}
	 }
	
}
