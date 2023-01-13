package io.mosip.biometrics.util.iris;

/**
 * HorizontalOrientation, Table 4 of ISO/IEC 19794-6-2011.
 */
public class HorizontalOrientation {
	public static final int ORIENTATION_UNDEFINIED = 0x00;
	public static final int ORIENTATION_BASE = 0x01;
	public static final int ORIENTATION_FLIPPED = 0x02;

	private int value;

	public HorizontalOrientation(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= ORIENTATION_UNDEFINIED && value <= ORIENTATION_FLIPPED)
			return value;
		throw new IllegalArgumentException(
				"HorizontalOrientation value can be between (0x00 and 0x02), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
