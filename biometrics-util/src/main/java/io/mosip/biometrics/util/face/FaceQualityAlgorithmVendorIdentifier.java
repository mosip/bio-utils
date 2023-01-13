package io.mosip.biometrics.util.face;

/**
 * Table 17 of ISO/IEC 19794-5-2011.
 */
public class FaceQualityAlgorithmVendorIdentifier {
	public static final int UNSPECIFIED = 0x0000;
	public static final int ALGORITHM_VENDOR_IDENTIFIER_0001 = 0x0001;
	public static final int ALGORITHM_VENDOR_IDENTIFIER_0103 = 0x0103;
	public static final int VENDOR_FFFF = 0xFFFF;

	private int value;

	public FaceQualityAlgorithmVendorIdentifier(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNSPECIFIED && value <= VENDOR_FFFF)
			return value;
		throw new IllegalArgumentException(
				"FaceQualityAlgorithmVendorIdentifier value can be between 0x0000 and 0xFFFF, set value is wrong ["
						+ value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
