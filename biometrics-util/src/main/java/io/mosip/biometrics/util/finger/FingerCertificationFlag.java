package io.mosip.biometrics.util.finger;

/**
 * certification Flag '0', '0', 0x00. Table 1 General record header ISO/IEC
 * 19794-4-2011. Indicates the presence of any device certification blocks
 * within the representation headers
 */
public class FingerCertificationFlag {
	public static final int UNSPECIFIED = 0x00;
	public static final int ONE = 0x01;

	private int value;

	public FingerCertificationFlag(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value == UNSPECIFIED || value == ONE)
			return value;
		throw new IllegalArgumentException(
				"FingerCertificationFlag value can be between (0x00 and 0x01), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
