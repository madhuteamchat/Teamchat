package com.integration.teamchat.onenote.page;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Value {

@Expose
private String title;
@Expose
private String createdByAppId;
@Expose
private Links links;
@Expose
private String contentUrl;
@Expose
private String thumbnailUrl;
@Expose
private String lastModifiedTime;
@Expose
private String id;
@Expose
private String self;
@Expose
private String createdTime;

/**
* 
* @return
* The title
*/
public String getTitle() {
return title;
}

/**
* 
* @param title
* The title
*/
public void setTitle(String title) {
this.title = title;
}

/**
* 
* @return
* The createdByAppId
*/
public String getCreatedByAppId() {
return createdByAppId;
}

/**
* 
* @param createdByAppId
* The createdByAppId
*/
public void setCreatedByAppId(String createdByAppId) {
this.createdByAppId = createdByAppId;
}

/**
* 
* @return
* The links
*/
public Links getLinks() {
return links;
}

/**
* 
* @param links
* The links
*/
public void setLinks(Links links) {
this.links = links;
}

/**
* 
* @return
* The contentUrl
*/
public String getContentUrl() {
return contentUrl;
}

/**
* 
* @param contentUrl
* The contentUrl
*/
public void setContentUrl(String contentUrl) {
this.contentUrl = contentUrl;
}

/**
* 
* @return
* The thumbnailUrl
*/
public String getThumbnailUrl() {
return thumbnailUrl;
}

/**
* 
* @param thumbnailUrl
* The thumbnailUrl
*/
public void setThumbnailUrl(String thumbnailUrl) {
this.thumbnailUrl = thumbnailUrl;
}

/**
* 
* @return
* The lastModifiedTime
*/
public String getLastModifiedTime() {
return lastModifiedTime;
}

/**
* 
* @param lastModifiedTime
* The lastModifiedTime
*/
public void setLastModifiedTime(String lastModifiedTime) {
this.lastModifiedTime = lastModifiedTime;
}

/**
* 
* @return
* The id
*/
public String getId() {
return id;
}

/**
* 
* @param id
* The id
*/
public void setId(String id) {
this.id = id;
}

/**
* 
* @return
* The self
*/
public String getSelf() {
return self;
}

/**
* 
* @param self
* The self
*/
public void setSelf(String self) {
this.self = self;
}

/**
* 
* @return
* The createdTime
*/
public String getCreatedTime() {
return createdTime;
}

/**
* 
* @param createdTime
* The createdTime
*/
public void setCreatedTime(String createdTime) {
this.createdTime = createdTime;
}

}