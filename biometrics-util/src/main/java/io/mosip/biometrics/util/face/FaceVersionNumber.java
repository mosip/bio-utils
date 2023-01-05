package io.mosip.biometrics.util.face;

/**
 * Version number '0', '3', '0', 0x00. Table 2 Section 5.3.1 of ISO/IEC
 * 19794-5-2011.
 */
public class FaceVersionNumber {
	public static final long VERSION_030 = 0x30333000;

	private final long value;

	FaceVersionNumber(int value) {
		this.value = value;
	}

	public long value() {
		return this.value;
	}

	public static long fromValue(int value) {
		if (value == VERSION_030)
			return value;
		throw new IllegalArgumentException(
				"FaceVersionNumber value can be 0x30333000, set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Long.toHexString(value) + ")";
	}
}
