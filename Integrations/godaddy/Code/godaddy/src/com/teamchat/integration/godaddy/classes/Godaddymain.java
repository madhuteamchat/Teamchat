package com.teamchat.integration.godaddy.classes;
/*
 * *
 * @author:Anuj Arora
 */
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Godaddymain {

@Expose
private Integer price;
@Expose
private String currency;
@Expose
private Integer period;
@Expose
private Boolean available;
@Expose
private String domain;

/**
* 
* @return
* The price
*/
public Integer getPrice() {
return price;
}

/**
* 
* @param price
* The price
*/
public void setPrice(Integer price) {
this.price = price;
}

/**
* 
* @return
* The currency
*/
public String getCurrency() {
return currency;
}

/**
* 
* @param currency
* The currency
*/
public void setCurrency(String currency) {
this.currency = currency;
}

/**
* 
* @return
* The period
*/
public Integer getPeriod() {
return period;
}

/**
* 
* @param period
* The period
*/
public void setPeriod(Integer period) {
this.period = period;
}

/**
* 
* @return
* The available
*/
public Boolean getAvailable() {
return available;
}

/**
* 
* @param available
* The available
*/
public void setAvailable(Boolean available) {
this.available = available;
}

/**
* 
* @return
* The domain
*/
public String getDomain() {
return domain;
}

/**
* 
* @param domain
* The domain
*/
public void setDomain(String domain) {
this.domain = domain;
}

}