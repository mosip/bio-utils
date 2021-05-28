package io.mosip.biometrics.util.iris;

/** Constant for horizontal and veritical orientation, based on Table 2 in Section 5.5 in ISO 19794-6. 
*/
public enum Orientation {

	UNDEFINED(0x0000), 
	BASE(0x0001),
	FLIPPED(0x0002);

	private final int value;
	Orientation(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static Orientation fromValue(int value) {
		for (Orientation c : Orientation.values()) {
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
