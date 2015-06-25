/**
 * 
 */
package com.basecamp.helpers;

/**
 * @author Puranjay Jain
 *
 */
public class Bool_converter {
	//converts true or false 
	//to 
	//checked or 
	public String toBoolHTML(Boolean value){
		if (value) {
			return "checked";
		}
		return "";
	}
}
