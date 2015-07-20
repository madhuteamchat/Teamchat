package com.teamchat.integration.glassdoor.classes;
/*
 * *@author : Anuj Arora
 * 
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class LashedLocation {

@Expose
private Integer id;
@Expose
private String shortName;
@Expose
private String longName;
@Expose
private String locationType;

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
* The shortName
*/
public String getShortName() {
return shortName;
}

/**
*
* @param shortName
* The shortName
*/
public void setShortName(String shortName) {
this.shortName = shortName;
}

/**
*
* @return
* The longName
*/
public String getLongName() {
return longName;
}

/**
*
* @param longName
* The longName
*/
public void setLongName(String longName) {
this.longName = longName;
}

/**
*
* @return
* The locationType
*/
public String getLocationType() {
return locationType;
}

/**
*
* @param locationType
* The locationType
*/
public void setLocationType(String locationType) {
this.locationType = locationType;
}

}