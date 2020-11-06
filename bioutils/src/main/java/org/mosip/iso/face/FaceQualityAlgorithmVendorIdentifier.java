package org.mosip.iso.face;

/** Table 17 of ISO/IEC 19794-5-2011. 
*/
public enum FaceQualityAlgorithmVendorIdentifier {

	ALGORITHM_VENDOR_IDENTIFIER(0x0000),
	ALGORITHM_VENDOR_IDENTIFIER_0103(0x0103);

	private final int value;
	FaceQualityAlgorithmVendorIdentifier(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FaceQualityAlgorithmVendorIdentifier fromValue(int value) {
		for (FaceQualityAlgorithmVendorIdentifier c : FaceQualityAlgorithmVendorIdentifier.values()) {
			if (c.value == value) {
				return c;
			}
		}
		throw new IllegalArgumentException(value + "");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString (value) + ")";
	}
}
