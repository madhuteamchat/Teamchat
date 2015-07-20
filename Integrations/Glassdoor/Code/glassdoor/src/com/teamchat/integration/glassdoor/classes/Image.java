package com.teamchat.integration.glassdoor.classes;
/*
 * *@author : Anuj Arora
 * 
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Image {

@Expose
private String src;
@Expose
private Integer height;
@Expose
private Integer width;

/**
*
* @return
* The src
*/
public String getSrc() {
return src;
}

/**
*
* @param src
* The src
*/
public void setSrc(String src) {
this.src = src;
}

/**
*
* @return
* The height
*/
public Integer getHeight() {
return height;
}

/**
*
* @param height
* The height
*/
public void setHeight(Integer height) {
this.height = height;
}

/**
*
* @return
* The width
*/
public Integer getWidth() {
return width;
}

/**
*
* @param width
* The width
*/
public void setWidth(Integer width) {
this.width = width;
}

}