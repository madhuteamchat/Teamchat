package com.integration.teamchat.onenote.page;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Pages {

@SerializedName("@odata.context")
@Expose
private String OdataContext;
@Expose
private List<Value> value = new ArrayList<Value>();

/**
* 
* @return
* The OdataContext
*/
public String getOdataContext() {
return OdataContext;
}

/**
* 
* @param OdataContext
* The @odata.context
*/
public void setOdataContext(String OdataContext) {
this.OdataContext = OdataContext;
}

/**
* 
* @return
* The value
*/
public List<Value> getValue() {
return value;
}

/**
* 
* @param value
* The value
*/
public void setValue(List<Value> value) {
this.value = value;
}

}