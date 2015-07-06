
package com.teamchat.integration.pingdom.classes;

/*
 * *
 * @author:Anuj Arora
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Counts {

@Expose
private Integer total;
@Expose
private Integer limited;
@Expose
private Integer filtered;

/**
* 
* @return
* The total
*/
public Integer getTotal() {
return total;
}

/**
* 
* @param total
* The total
*/
public void setTotal(Integer total) {
this.total = total;
}

/**
* 
* @return
* The limited
*/
public Integer getLimited() {
return limited;
}

/**
* 
* @param limited
* The limited
*/
public void setLimited(Integer limited) {
this.limited = limited;
}

/**
* 
* @return
* The filtered
*/
public Integer getFiltered() {
return filtered;
}

/**
* 
* @param filtered
* The filtered
*/
public void setFiltered(Integer filtered) {
this.filtered = filtered;
}

}