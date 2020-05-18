/**
 * 
 */
package com.church.util;

/**
 * @author pradheep
 *
 */
public enum EventTypeEnum {

	birthday_event("Birthday event"), wedding_anniversary("Wedding anniversary");

	public final String label;

	private EventTypeEnum(String label) {
		this.label = label;
	}
}
