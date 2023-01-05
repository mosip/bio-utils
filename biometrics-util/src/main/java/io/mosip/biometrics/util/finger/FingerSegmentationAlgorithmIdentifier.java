package io.mosip.biometrics.util.finger;

/**
 * Segmentation quality assessment algorithm ID IBIA Assigned; of ISO/IEC
 * 19794-4-2011.
 */
//https://www.ibia.org/cbeff/iso/biometric-quality-algorithm-identifiers
public class FingerSegmentationAlgorithmIdentifier {
	public static final int UNSPECIFIED = 0x0000;
	public static final int VENDOR_0001 = 0x0001;
	public static final int VENDOR_FFFF = 0xFFFF;// Maximum

	private int value;

	public FingerSegmentationAlgorithmIdentifier(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNSPECIFIED || value <= VENDOR_FFFF)
			return value;
		throw new IllegalArgumentException(
				"FingerSegmentationAlgorithmIdentifier value can be between (0x0000 and 0xFFFF), set value is wrong ["
						+ value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
