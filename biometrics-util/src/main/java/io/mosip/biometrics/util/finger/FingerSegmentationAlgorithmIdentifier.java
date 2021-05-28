package io.mosip.biometrics.util.finger;

/** Segmentation quality assessment algorithm ID IBIA Assigned;  of ISO/IEC 19794-4-2011. 
*/
//https://www.ibia.org/cbeff/iso/biometric-quality-algorithm-identifiers
public enum FingerSegmentationAlgorithmIdentifier {

	ALGORITHM_IDENTIFIER(0x0000),
	VENDOR_UNKNOWN(0x0001);

	private final int value;
	FingerSegmentationAlgorithmIdentifier(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FingerSegmentationAlgorithmIdentifier fromValue(int value) {
		for (FingerSegmentationAlgorithmIdentifier c : FingerSegmentationAlgorithmIdentifier.values()) {
			if (c.value == value) {
				return c;
			}
		}
		return ALGORITHM_IDENTIFIER;
		//throw new IllegalArgumentException(value + "");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString (value) + ")";
	}
}
