package io.mosip.biometrics.util;

public enum Modality {
	UnSpecified(0x0000), 
	Finger(0x0001), 
	Face(0x0002), 
	Iris(0x0003);

	private final int value;
	Modality(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static Modality fromValue(int value) {
		for (Modality c : Modality.values()) {
			if (c.value == value) {
				return c;
			}
		}
		throw new IllegalArgumentException(value + "");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString (value) + ")";
	}
}
