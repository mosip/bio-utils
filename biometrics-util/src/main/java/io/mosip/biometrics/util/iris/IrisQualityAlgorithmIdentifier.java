package io.mosip.biometrics.util.iris;

/** Table 4 of ISO/IEC 19794-6-2011. 
*/
public enum IrisQualityAlgorithmIdentifier {

	ALGORITHM_IDENTIFIER(0x0000);

	private final int value;
	IrisQualityAlgorithmIdentifier(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static IrisQualityAlgorithmIdentifier fromValue(int value) {
		for (IrisQualityAlgorithmIdentifier c : IrisQualityAlgorithmIdentifier.values()) {
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
