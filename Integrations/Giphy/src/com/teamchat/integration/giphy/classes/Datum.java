package com.teamchat.integration.giphy.classes;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Datum {

@Expose
private String type;
@Expose
private String id;
@Expose
private String url;
@SerializedName("bitly_gif_url")
@Expose
private String bitlyGifUrl;
@SerializedName("bitly_url")
@Expose
private String bitlyUrl;
@SerializedName("embed_url")
@Expose
private String embedUrl;
@Expose
private String username;
@Expose
private String source;
@Expose
private String rating;
@Expose
private String caption;
@SerializedName("content_url")
@Expose
private String contentUrl;
@SerializedName("import_datetime")
@Expose
private String importDatetime;
@SerializedName("trending_datetime")
@Expose
private String trendingDatetime;
@Expose
private Images images;

/**
* 
* @return
* The type
*/
public String getType() {
return type;
}

/**
* 
* @param type
* The type
*/
public void setType(String type) {
this.type = type;
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
* The bitlyGifUrl
*/
public String getBitlyGifUrl() {
return bitlyGifUrl;
}

/**
* 
* @param bitlyGifUrl
* The bitly_gif_url
*/
public void setBitlyGifUrl(String bitlyGifUrl) {
this.bitlyGifUrl = bitlyGifUrl;
}

/**
* 
* @return
* The bitlyUrl
*/
public String getBitlyUrl() {
return bitlyUrl;
}

/**
* 
* @param bitlyUrl
* The bitly_url
*/
public void setBitlyUrl(String bitlyUrl) {
this.bitlyUrl = bitlyUrl;
}

/**
* 
* @return
* The embedUrl
*/
public String getEmbedUrl() {
return embedUrl;
}

/**
* 
* @param embedUrl
* The embed_url
*/
public void setEmbedUrl(String embedUrl) {
this.embedUrl = embedUrl;
}

/**
* 
* @return
* The username
*/
public String getUsername() {
return username;
}

/**
* 
* @param username
* The username
*/
public void setUsername(String username) {
this.username = username;
}

/**
* 
* @return
* The source
*/
public String getSource() {
return source;
}

/**
* 
* @param source
* The source
*/
public void setSource(String source) {
this.source = source;
}

/**
* 
* @return
* The rating
*/
public String getRating() {
return rating;
}

/**
* 
* @param rating
* The rating
*/
public void setRating(String rating) {
this.rating = rating;
}

/**
* 
* @return
* The caption
*/
public String getCaption() {
return caption;
}

/**
* 
* @param caption
* The caption
*/
public void setCaption(String caption) {
this.caption = caption;
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
* The content_url
*/
public void setContentUrl(String contentUrl) {
this.contentUrl = contentUrl;
}

/**
* 
* @return
* The importDatetime
*/
public String getImportDatetime() {
return importDatetime;
}

/**
* 
* @param importDatetime
* The import_datetime
*/
public void setImportDatetime(String importDatetime) {
this.importDatetime = importDatetime;
}

/**
* 
* @return
* The trendingDatetime
*/
public String getTrendingDatetime() {
return trendingDatetime;
}

/**
* 
* @param trendingDatetime
* The trending_datetime
*/
public void setTrendingDatetime(String trendingDatetime) {
this.trendingDatetime = trendingDatetime;
}

/**
* 
* @return
* The images
*/
public Images getImages() {
return images;
}

/**
* 
* @param images
* The images
*/
public void setImages(Images images) {
this.images = images;
}

}