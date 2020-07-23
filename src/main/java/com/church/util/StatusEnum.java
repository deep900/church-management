/**
 * 
 */
package com.church.util;

/**
 * @author pradheep
 *
 */
public enum StatusEnum {

	scheduled("scheduled"), scheduledAndAssigned("scheduledAndAssigned"), inprogress("inprogress"), completed(
			"completed"), onhold("onhold"), cancelled("cancelled");

	public final String label;

	private StatusEnum(String label) {
		this.label = label;
	}
}
