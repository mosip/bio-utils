package io.mosip.biometrics.util.finger;

/** Segmentation quality assessment algorithm owner ID IBIA Assigned; ISO/IEC 19794-4-2011. 
*/
//https://www.ibia.org/cbeff/iso/biometric-organizations
public enum FingerSegmentationAlgorithmVendorIdentifier {

	ALGORITHM_VENDOR_IDENTIFIER(0x0000),
	GREEN_BIT_AMERICAS_INC(0x0040);

	private final int value;
	FingerSegmentationAlgorithmVendorIdentifier(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FingerSegmentationAlgorithmVendorIdentifier fromValue(int value) {
		for (FingerSegmentationAlgorithmVendorIdentifier c : FingerSegmentationAlgorithmVendorIdentifier.values()) {
			if (c.value == value) {
				return c;
			}
		}
		return ALGORITHM_VENDOR_IDENTIFIER;
		//throw new IllegalArgumentException(value + "");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString (value) + ")";
	}
}
