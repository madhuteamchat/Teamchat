package com.teamchat.integration.glassdoor.classes;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Ceo {

@Expose
private String name;
@Expose
private String title;
@Expose
private Integer numberOfRatings;
@Expose
private Integer pctApprove;
@Expose
private Integer pctDisapprove;
@Expose
private Image image;

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
* The numberOfRatings
*/
public Integer getNumberOfRatings() {
return numberOfRatings;
}

/**
*
* @param numberOfRatings
* The numberOfRatings
*/
public void setNumberOfRatings(Integer numberOfRatings) {
this.numberOfRatings = numberOfRatings;
}

/**
*
* @return
* The pctApprove
*/
public Integer getPctApprove() {
return pctApprove;
}

/**
*
* @param pctApprove
* The pctApprove
*/
public void setPctApprove(Integer pctApprove) {
this.pctApprove = pctApprove;
}

/**
*
* @return
* The pctDisapprove
*/
public Integer getPctDisapprove() {
return pctDisapprove;
}

/**
*
* @param pctDisapprove
* The pctDisapprove
*/
public void setPctDisapprove(Integer pctDisapprove) {
this.pctDisapprove = pctDisapprove;
}

/**
*
* @return
* The image
*/
public Image getImage() {
return image;
}

/**
*
* @param image
* The image
*/
public void setImage(Image image) {
this.image = image;
}

}