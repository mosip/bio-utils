package io.mosip.biometrics.util.iris;

/**
 * Facial Record Header 'I', 'I', 'R', 0x02. Table 3 of ISO/IEC 19794-6-2011.
 */
public class IrisFormatIdentifier {
	public static final int FORMAT_IIR = 0x49495200;

	private final int value;

	IrisFormatIdentifier(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value == FORMAT_IIR)
			return value;
		throw new IllegalArgumentException(
				"IrisFormatIdentifier value can be 0x49495200, set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
