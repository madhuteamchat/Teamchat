package com.vso.classes;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;

@Generated("org.jsonschema2pojo")
public class Accounts {

	@Expose
	private Integer count;
	@Expose
	private List<Value> value = new ArrayList<Value>();

	/**
	 * 
	 * @return The count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * 
	 * @param count
	 *            The count
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	public Accounts withCount(Integer count) {
		this.count = count;
		return this;
	}

	/**
	 * 
	 * @return The value
	 */
	public List<Value> getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 *            The value
	 */
	public void setValue(List<Value> value) {
		this.value = value;
	}

	public Accounts withValue(List<Value> value) {
		this.value = value;
		return this;
	}

}