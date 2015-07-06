package com.teamchat.integration.pingdom.classes;

/*
 * *
 * @author:Anuj Arora
 */
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Check {

@Expose
private Integer id;
@Expose
private Integer created;
@Expose
private String name;
@Expose
private String hostname;
@SerializedName("use_legacy_notifications")
@Expose
private Boolean useLegacyNotifications;
@Expose
private Integer resolution;
@Expose
private String type;
@Expose
private Integer lasttesttime;
@Expose
private Integer lastresponsetime;
@Expose
private String status;
@SerializedName("probe_filters")
@Expose
private List<Object> probeFilters = new ArrayList<Object>();
@Expose
private Integer lasterrortime;

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
* The created
*/
public Integer getCreated() {
return created;
}

/**
* 
* @param created
* The created
*/
public void setCreated(Integer created) {
this.created = created;
}

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
* The hostname
*/
public String getHostname() {
return hostname;
}

/**
* 
* @param hostname
* The hostname
*/
public void setHostname(String hostname) {
this.hostname = hostname;
}

/**
* 
* @return
* The useLegacyNotifications
*/
public Boolean getUseLegacyNotifications() {
return useLegacyNotifications;
}

/**
* 
* @param useLegacyNotifications
* The use_legacy_notifications
*/
public void setUseLegacyNotifications(Boolean useLegacyNotifications) {
this.useLegacyNotifications = useLegacyNotifications;
}

/**
* 
* @return
* The resolution
*/
public Integer getResolution() {
return resolution;
}

/**
* 
* @param resolution
* The resolution
*/
public void setResolution(Integer resolution) {
this.resolution = resolution;
}

/**
* 
* @return
* The type
*/
public String getType() {
return type;
}

/**
* 
* @param type
* The type
*/
public void setType(String type) {
this.type = type;
}

/**
* 
* @return
* The lasttesttime
*/
public Integer getLasttesttime() {
return lasttesttime;
}

/**
* 
* @param lasttesttime
* The lasttesttime
*/
public void setLasttesttime(Integer lasttesttime) {
this.lasttesttime = lasttesttime;
}

/**
* 
* @return
* The lastresponsetime
*/
public Integer getLastresponsetime() {
return lastresponsetime;
}

/**
* 
* @param lastresponsetime
* The lastresponsetime
*/
public void setLastresponsetime(Integer lastresponsetime) {
this.lastresponsetime = lastresponsetime;
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
* The probeFilters
*/
public List<Object> getProbeFilters() {
return probeFilters;
}

/**
* 
* @param probeFilters
* The probe_filters
*/
public void setProbeFilters(List<Object> probeFilters) {
this.probeFilters = probeFilters;
}

/**
* 
* @return
* The lasterrortime
*/
public Integer getLasterrortime() {
return lasterrortime;
}

/**
* 
* @param lasterrortime
* The lasterrortime
*/
public void setLasterrortime(Integer lasterrortime) {
this.lasterrortime = lasterrortime;
}

}