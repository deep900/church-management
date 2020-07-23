package com.church.util;

public enum FrequencyTypeEnum {
	
	daily("daily"), weekly("weekly"), fortnight("fortnight"), monthly("monthly"), yearly("yearly"), none("none");

	public final String label;

	private FrequencyTypeEnum(String label) {
		this.label = label;
	}
}
