package com.hipchat.getwebhook;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Item {

@Expose
private String event;
@Expose
private Integer id;
@Expose
private Links_ links;
@Expose
private String name;
@Expose
private Object pattern;
@Expose
private String url;

/**
* 
* @return
* The event
*/
public String getEvent() {
return event;
}

/**
* 
* @param event
* The event
*/
public void setEvent(String event) {
this.event = event;
}

/**
* 
* @return
* The id
*/
public Integer getId() {
return id;
}

/**
* 
* @param id
* The id
*/
public void setId(Integer id) {
this.id = id;
}

/**
* 
* @return
* The links
*/
public Links_ getLinks() {
return links;
}

/**
* 
* @param links
* The links
*/
public void setLinks(Links_ links) {
this.links = links;
}

/**
* 
* @return
* The name
*/
public String getName() {
return name;
}

/**
* 
* @param name
* The name
*/
public void setName(String name) {
this.name = name;
}

/**
* 
* @return
* The pattern
*/
public Object getPattern() {
return pattern;
}

/**
* 
* @param pattern
* The pattern
*/
public void setPattern(Object pattern) {
this.pattern = pattern;
}

/**
* 
* @return
* The url
*/
public String getUrl() {
return url;
}

/**
* 
* @param url
* The url
*/
public void setUrl(String url) {
this.url = url;
}

}