package com.teamchat.integration.giphy.classes;
/*
 * *
 * @author:Anuj Arora
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Pagination {

@SerializedName("total_count")
@Expose
private Integer totalCount;
@Expose
private Integer count;
@Expose
private Integer offset;

/**
* 
* @return
* The totalCount
*/
public Integer getTotalCount() {
return totalCount;
}

/**
* 
* @param totalCount
* The total_count
*/
public void setTotalCount(Integer totalCount) {
this.totalCount = totalCount;
}

/**
* 
* @return
* The count
*/
public Integer getCount() {
return count;
}

/**
* 
* @param count
* The count
*/
public void setCount(Integer count) {
this.count = count;
}

/**
* 
* @return
* The offset
*/
public Integer getOffset() {
return offset;
}

/**
* 
* @param offset
* The offset
*/
public void setOffset(Integer offset) {
this.offset = offset;
}

}