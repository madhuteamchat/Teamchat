package com.hipchat.getwebhook;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Deletewebhook {

@Expose
private List<Item> items = new ArrayList<Item>();
@Expose
private Links_ links;
@Expose
private Integer maxResults;
@Expose
private Integer startIndex;

/**
* 
* @return
* The items
*/
public List<Item> getItems() {
return items;
}

/**
* 
* @param items
* The items
*/
public void setItems(List<Item> items) {
this.items = items;
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
* The maxResults
*/
public Integer getMaxResults() {
return maxResults;
}

/**
* 
* @param maxResults
* The maxResults
*/
public void setMaxResults(Integer maxResults) {
this.maxResults = maxResults;
}

/**
* 
* @return
* The startIndex
*/
public Integer getStartIndex() {
return startIndex;
}

/**
* 
* @param startIndex
* The startIndex
*/
public void setStartIndex(Integer startIndex) {
this.startIndex = startIndex;
}

}
