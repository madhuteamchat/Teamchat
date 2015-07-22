package com.teamchat.integration.giphy.classes;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Downsized {

@Expose
private String url;
@Expose
private String width;
@Expose
private String height;
@Expose
private String size;

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

/**
* 
* @return
* The width
*/
public String getWidth() {
return width;
}

/**
* 
* @param width
* The width
*/
public void setWidth(String width) {
this.width = width;
}

/**
* 
* @return
* The height
*/
public String getHeight() {
return height;
}

/**
* 
* @param height
* The height
*/
public void setHeight(String height) {
this.height = height;
}

/**
* 
* @return
* The size
*/
public String getSize() {
return size;
}

/**
* 
* @param size
* The size
*/
public void setSize(String size) {
this.size = size;
}

}
