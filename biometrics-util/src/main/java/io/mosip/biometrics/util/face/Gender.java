package io.mosip.biometrics.util.face;

/** Gender Table 6 of ISO/IEC 19794-5-2011. */
public enum Gender {

	UNSPECIFIED(0x0000), 
	MALE(0x0001), 
	FEMALE(0x0002), 
	UNKNOWN(0x00FF);

	private final int value;
	Gender(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static Gender fromValue(int value) {
		for (Gender c : Gender.values()) {
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
