/**
 * 
 */
package com.church.util;

/**
 * @author pradheep
 *
 */
public enum EventTypeEnum {

	BirthdayEvent("BirthdayEvent"), WeddingAnniversary("WeddingAnniversary");

	public final String label;

	private EventTypeEnum(String label) {
		this.label = label;
	}
}
