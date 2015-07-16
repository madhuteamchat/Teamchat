package com.teamchat.integration.giphy.classes;
/*
 * *
 * @author:Anuj Arora
 */
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
//main class for getters and setters
@Generated("org.jsonschema2pojo")
public class Giphyd {

@Expose
private List<Datum> data = new ArrayList<Datum>();
@Expose
private Meta meta;
@Expose
private Pagination pagination;

/**
* 
* @return
* The data
*/
public List<Datum> getData() {
return data;
}

/**
* 
* @param data
* The data
*/
public void setData(List<Datum> data) {
this.data = data;
}

/**
* 
* @return
* The meta
*/
public Meta getMeta() {
return meta;
}

/**
* 
* @param meta
* The meta
*/
public void setMeta(Meta meta) {
this.meta = meta;
}

/**
* 
* @return
* The pagination
*/
public Pagination getPagination() {
return pagination;
}

/**
* 
* @param pagination
* The pagination
*/
public void setPagination(Pagination pagination) {
this.pagination = pagination;
}

}