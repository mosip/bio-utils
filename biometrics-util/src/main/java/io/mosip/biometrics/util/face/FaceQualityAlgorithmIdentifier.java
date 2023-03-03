package io.mosip.biometrics.util.face;

/**
 * Table 17 of ISO/IEC 19794-4-2011.
 */
public class FaceQualityAlgorithmIdentifier {
	public static final int UNSPECIFIED = 0x0000;
	public static final int ALGORITHM_IDENTIFIER_0001 = 0x0001;
	public static final int ALGORITHM_IDENTIFIER_0103 = 0x0103;
	public static final int VENDOR_FFFF = 0xFFFF;

	private int value;

	public FaceQualityAlgorithmIdentifier(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNSPECIFIED && value <= VENDOR_FFFF)
			return value;
		throw new IllegalArgumentException(
				"FaceQualityAlgorithmIdentifier value can be between 0x0000 and 0xFFFF, set value is wrong [" + value
						+ "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
