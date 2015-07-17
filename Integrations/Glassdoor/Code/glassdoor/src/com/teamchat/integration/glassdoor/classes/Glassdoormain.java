package com.teamchat.integration.glassdoor.classes;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Glassdoormain {

@Expose
private Boolean success;
@Expose
private String status;
@Expose
private String jsessionid;
@Expose
private Response response;

/**
*
* @return
* The success
*/
public Boolean getSuccess() {
return success;
}

/**
*
* @param success
* The success
*/
public void setSuccess(Boolean success) {
this.success = success;
}

/**
*
* @return
* The status
*/
public String getStatus() {
return status;
}

/**
*
* @param status
* The status
*/
public void setStatus(String status) {
this.status = status;
}

/**
*
* @return
* The jsessionid
*/
public String getJsessionid() {
return jsessionid;
}

/**
*
* @param jsessionid
* The jsessionid
*/
public void setJsessionid(String jsessionid) {
this.jsessionid = jsessionid;
}

/**
*
* @return
* The response
*/
public Response getResponse() {
return response;
}

/**
*
* @param response
* The response
*/
public void setResponse(Response response) {
this.response = response;
}

}