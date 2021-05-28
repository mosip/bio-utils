package io.mosip.biometrics.util.finger;

/** Table 17 of ISO/IEC 19794-4-2011. 
*/
//https://www.ibia.org/cbeff/iso/biometric-organizations

public enum FingerQualityAlgorithmVendorIdentifier {

	ALGORITHM_VENDOR_IDENTIFIER(0x0000),
	NIST(0x000F),
	GREEN_BIT_AMERICAS_INC(0x0040);

	private final int value;
	FingerQualityAlgorithmVendorIdentifier(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FingerQualityAlgorithmVendorIdentifier fromValue(int value) {
		for (FingerQualityAlgorithmVendorIdentifier c : FingerQualityAlgorithmVendorIdentifier.values()) {
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
