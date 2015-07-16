package com.teamchat.integration.giphy.classes;
/*
 * *
 * @author:Anuj Arora
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Meta {

@Expose
private Integer status;
@Expose
private String msg;

/**
* 
* @return
* The status
*/
public Integer getStatus() {
return status;
}

/**
* 
* @param status
* The status
*/
public void setStatus(Integer status) {
this.status = status;
}

/**
* 
* @return
* The msg
*/
public String getMsg() {
return msg;
}

/**
* 
* @param msg
* The msg
*/
public void setMsg(String msg) {
this.msg = msg;
}

}