/**
 * 
 */
package com.basecamp.helpers;

/**
 * @author Puranjay Jain
 *
 */
public class HTTP_Response {
	private String response;
	private int responseCode;
	public HTTP_Response(String response, int responseCode) {
		super();
		this.response = response;
		this.responseCode = responseCode;
	}
	/**
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}
	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}
	/**
	 * @return the responseCode
	 */
	public int getResponseCode() {
		return responseCode;
	}
	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
}
