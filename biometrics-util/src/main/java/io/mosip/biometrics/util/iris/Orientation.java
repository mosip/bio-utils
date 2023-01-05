package io.mosip.biometrics.util.iris;

/**
 * Constant for horizontal and veritical orientation, based on Table 2 in
 * Section 5.5 in ISO 19794-6.
 */
public class Orientation {
	public static final int UNDEFINED = 0x00;
	public static final int BASE = 0x01;
	public static final int FLIPPED = 0x02;

	private int value;

	public Orientation(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNDEFINED && value <= FLIPPED)
			return value;
		throw new IllegalArgumentException(
				"Orientation value can be between (0x00 and 0x02), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
