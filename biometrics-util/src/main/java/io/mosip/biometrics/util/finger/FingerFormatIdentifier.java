package io.mosip.biometrics.util.finger;

/**
 * Facial Record Header 'F', 'I', 'R', General record header of ISO/IEC
 * 19794-4-2011.
 */
public class FingerFormatIdentifier {
	public static final int FORMAT_FIR = 0x46495200;

	private final int value;

	FingerFormatIdentifier(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value == FORMAT_FIR)
			return value;
		throw new IllegalArgumentException(
				"FingerFormatIdentifier value can be 0x46495200, set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
