package edu.asu.lerna.iolaus.domain.queryobject;

public enum PropertyOf {
	SOURCE("source"), TARGET("target"), RELATION("r");

	private String value;

	private PropertyOf(String value) {
		this.value = value;
	}

	@Override
	public String toString() {

		return value;
	}
}
