/**
 * 
 */
package com.Maintenance;

/**
 * @author Puranjay Jain
 *
 */
public class Customer {
	String name, problem;
	Boolean status;
	int id;

	Customer() {

	}

	Customer(int id, String name, String problem, Boolean status) {
		this.id = id;
		this.name = name;
		this.problem = problem;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
}
