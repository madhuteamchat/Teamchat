package com.teamchat.integration.glassdoor.classes;
/*
 * *@author : Anuj Arora
 * 
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class FeaturedReview {

@Expose
private Integer id;
@Expose
private Boolean currentJob;
@Expose
private String reviewDateTime;
@Expose
private String jobTitle;
@Expose
private String location;
@Expose
private String jobTitleFromDb;
@Expose
private String headline;
@Expose
private String pros;
@Expose
private String cons;
@Expose
private Integer overall;
@Expose
private Integer overallNumeric;

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
* The currentJob
*/
public Boolean getCurrentJob() {
return currentJob;
}

/**
*
* @param currentJob
* The currentJob
*/
public void setCurrentJob(Boolean currentJob) {
this.currentJob = currentJob;
}

/**
*
* @return
* The reviewDateTime
*/
public String getReviewDateTime() {
return reviewDateTime;
}

/**
*
* @param reviewDateTime
* The reviewDateTime
*/
public void setReviewDateTime(String reviewDateTime) {
this.reviewDateTime = reviewDateTime;
}

/**
*
* @return
* The jobTitle
*/
public String getJobTitle() {
return jobTitle;
}

/**
*
* @param jobTitle
* The jobTitle
*/
public void setJobTitle(String jobTitle) {
this.jobTitle = jobTitle;
}

/**
*
* @return
* The location
*/
public String getLocation() {
return location;
}

/**
*
* @param location
* The location
*/
public void setLocation(String location) {
this.location = location;
}

/**
*
* @return
* The jobTitleFromDb
*/
public String getJobTitleFromDb() {
return jobTitleFromDb;
}

/**
*
* @param jobTitleFromDb
* The jobTitleFromDb
*/
public void setJobTitleFromDb(String jobTitleFromDb) {
this.jobTitleFromDb = jobTitleFromDb;
}

/**
*
* @return
* The headline
*/
public String getHeadline() {
return headline;
}

/**
*
* @param headline
* The headline
*/
public void setHeadline(String headline) {
this.headline = headline;
}

/**
*
* @return
* The pros
*/
public String getPros() {
return pros;
}

/**
*
* @param pros
* The pros
*/
public void setPros(String pros) {
this.pros = pros;
}

/**
*
* @return
* The cons
*/
public String getCons() {
return cons;
}

/**
*
* @param cons
* The cons
*/
public void setCons(String cons) {
this.cons = cons;
}

/**
*
* @return
* The overall
*/
public Integer getOverall() {
return overall;
}

/**
*
* @param overall
* The overall
*/
public void setOverall(Integer overall) {
this.overall = overall;
}

/**
*
* @return
* The overallNumeric
*/
public Integer getOverallNumeric() {
return overallNumeric;
}

/**
*
* @param overallNumeric
* The overallNumeric
*/
public void setOverallNumeric(Integer overallNumeric) {
this.overallNumeric = overallNumeric;
}

}