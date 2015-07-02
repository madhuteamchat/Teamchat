package com.teamchat.integration.pingdom.classes;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Checks {

@Expose
private List<Check> checks = new ArrayList<Check>();
@Expose
private Counts counts;

/**
* 
* @return
* The checks
*/
public List<Check> getChecks() {
return checks;
}

/**
* 
* @param checks
* The checks
*/
public void setChecks(List<Check> checks) {
this.checks = checks;
}

/**
* 
* @return
* The counts
*/
public Counts getCounts() {
return counts;
}

/**
* 
* @param counts
* The counts
*/
public void setCounts(Counts counts) {
this.counts = counts;
}

}