package io.mosip.biometrics.util.iris;

/** Version number '0', '2', '0', 0x00. Table 3 of ISO/IEC 19794-6-2011. */
public class IrisVersionNumber {
	public static final int VERSION_020 = 0x30323000;

	private final int value;

	IrisVersionNumber(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value == VERSION_020)
			return value;
		throw new IllegalArgumentException(
				"IrisVersionNumber value can be 0x30323000, set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
