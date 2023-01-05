package io.mosip.biometrics.util.iris;

/**
 * Table 4 of ISO/IEC 19794-6-2011.
 */
public class IrisQualityAlgorithmIdentifier {
	public static final int UNSPECIFIED = 0x0000;
	// jonny 20/12/2022 Previous Value was 0 it should be 1 to ffff
	public static final int ALGORITHM_IDENTIFIER_0001 = 0x0001;
	public static final int VENDOR_FFFF = 0xFFFF;

	private int value;

	public IrisQualityAlgorithmIdentifier(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNSPECIFIED && value <= VENDOR_FFFF)
			return value;
		throw new IllegalArgumentException(
				"IrisQualityAlgorithmIdentifier value can be between 0x0000 and 0xFFFF, set value is wrong [" + value
						+ "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
