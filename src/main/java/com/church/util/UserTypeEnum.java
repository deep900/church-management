/**
 * 
 */
package com.church.util;

/**
 * @author pradheep
 *
 */
public enum UserTypeEnum {

	engineer("engineer"), administrator("administrator"),reviewer("reviewer");

	public final String label;

	private UserTypeEnum(String label) {
		this.label = label;
	}
}
