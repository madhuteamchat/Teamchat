package com.teamchat.integration.glassdoor.classes;
/*
 * *@author : Anuj Arora
 * 
 */
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Response {

@Expose
private String attributionURL;
@Expose
private Integer currentPageNumber;
@Expose
private Integer totalNumberOfPages;
@Expose
private Integer totalRecordCount;
@Expose
private LashedLocation lashedLocation;
@Expose
private List<Employer> employers = new ArrayList<Employer>();

/**
*
* @return
* The attributionURL
*/
public String getAttributionURL() {
return attributionURL;
}

/**
*
* @param attributionURL
* The attributionURL
*/
public void setAttributionURL(String attributionURL) {
this.attributionURL = attributionURL;
}

/**
*
* @return
* The currentPageNumber
*/
public Integer getCurrentPageNumber() {
return currentPageNumber;
}

/**
*
* @param currentPageNumber
* The currentPageNumber
*/
public void setCurrentPageNumber(Integer currentPageNumber) {
this.currentPageNumber = currentPageNumber;
}

/**
*
* @return
* The totalNumberOfPages
*/
public Integer getTotalNumberOfPages() {
return totalNumberOfPages;
}

/**
*
* @param totalNumberOfPages
* The totalNumberOfPages
*/
public void setTotalNumberOfPages(Integer totalNumberOfPages) {
this.totalNumberOfPages = totalNumberOfPages;
}

/**
*
* @return
* The totalRecordCount
*/
public Integer getTotalRecordCount() {
return totalRecordCount;
}

/**
*
* @param totalRecordCount
* The totalRecordCount
*/
public void setTotalRecordCount(Integer totalRecordCount) {
this.totalRecordCount = totalRecordCount;
}

/**
*
* @return
* The lashedLocation
*/
public LashedLocation getLashedLocation() {
return lashedLocation;
}

/**
*
* @param lashedLocation
* The lashedLocation
*/
public void setLashedLocation(LashedLocation lashedLocation) {
this.lashedLocation = lashedLocation;
}

/**
*
* @return
* The employers
*/
public List<Employer> getEmployers() {
return employers;
}

/**
*
* @param employers
* The employers
*/
public void setEmployers(List<Employer> employers) {
this.employers = employers;
}

}