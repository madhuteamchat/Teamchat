package com.integration.teamchat.onenote.page;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Links {

@Expose
private OneNoteClientUrl oneNoteClientUrl;
@Expose
private OneNoteWebUrl oneNoteWebUrl;

/**
* 
* @return
* The oneNoteClientUrl
*/
public OneNoteClientUrl getOneNoteClientUrl() {
return oneNoteClientUrl;
}

/**
* 
* @param oneNoteClientUrl
* The oneNoteClientUrl
*/
public void setOneNoteClientUrl(OneNoteClientUrl oneNoteClientUrl) {
this.oneNoteClientUrl = oneNoteClientUrl;
}

/**
* 
* @return
* The oneNoteWebUrl
*/
public OneNoteWebUrl getOneNoteWebUrl() {
return oneNoteWebUrl;
}

/**
* 
* @param oneNoteWebUrl
* The oneNoteWebUrl
*/
public void setOneNoteWebUrl(OneNoteWebUrl oneNoteWebUrl) {
this.oneNoteWebUrl = oneNoteWebUrl;
}

}