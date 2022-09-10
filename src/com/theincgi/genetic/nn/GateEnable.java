package com.theincgi.genetic.nn;

public enum GateEnable {
	ENABLED,
	DISABLED;

	public boolean isEnabled() {
		return this.equals(ENABLED);
	}
	public boolean isDisabled() {
		return this.equals(DISABLED);
	}
	
}
