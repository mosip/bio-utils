package io.mosip.biometrics.util.finger;

/**
 * Version number '0', '2', '0', 0x020. Table 1 General record header of
 * ISO/IEC 19794-4-2011.
 */
public class FingerVersionNumber {
	public static final int VERSION_020 = 0x30323000;

	private final int value;

	FingerVersionNumber(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value == VERSION_020)
			return value;
		throw new IllegalArgumentException(
				"FingerVersionNumber value can be 0x30323000, set value is wrong [" + value + "]");
	}
	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
