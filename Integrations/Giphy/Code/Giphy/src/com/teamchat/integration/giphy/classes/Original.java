package com.teamchat.integration.giphy.classes;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Original {

@Expose
private String url;
@Expose
private String width;
@Expose
private String height;
@Expose
private String size;
@Expose
private String frames;
@Expose
private String mp4;
@SerializedName("mp4_size")
@Expose
private String mp4Size;
@Expose
private String webp;
@SerializedName("webp_size")
@Expose
private String webpSize;

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

/**
* 
* @return
* The frames
*/
public String getFrames() {
return frames;
}

/**
* 
* @param frames
* The frames
*/
public void setFrames(String frames) {
this.frames = frames;
}

/**
* 
* @return
* The mp4
*/
public String getMp4() {
return mp4;
}

/**
* 
* @param mp4
* The mp4
*/
public void setMp4(String mp4) {
this.mp4 = mp4;
}

/**
* 
* @return
* The mp4Size
*/
public String getMp4Size() {
return mp4Size;
}

/**
* 
* @param mp4Size
* The mp4_size
*/
public void setMp4Size(String mp4Size) {
this.mp4Size = mp4Size;
}

/**
* 
* @return
* The webp
*/
public String getWebp() {
return webp;
}

/**
* 
* @param webp
* The webp
*/
public void setWebp(String webp) {
this.webp = webp;
}

/**
* 
* @return
* The webpSize
*/
public String getWebpSize() {
return webpSize;
}

/**
* 
* @param webpSize
* The webp_size
*/
public void setWebpSize(String webpSize) {
this.webpSize = webpSize;
}

}
