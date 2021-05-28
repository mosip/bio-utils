package io.mosip.biometrics.util.iris;

/** Table 4 of ISO/IEC 19794-6-2011. 
*/
public enum IrisQualityAlgorithmVendorIdentifier {

	ALGORITHM_VENDOR_IDENTIFIER(0x0000);

	private final int value;
	IrisQualityAlgorithmVendorIdentifier(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static IrisQualityAlgorithmVendorIdentifier fromValue(int value) {
		for (IrisQualityAlgorithmVendorIdentifier c : IrisQualityAlgorithmVendorIdentifier.values()) {
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
