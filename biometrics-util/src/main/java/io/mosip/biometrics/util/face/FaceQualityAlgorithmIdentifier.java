package io.mosip.biometrics.util.face;

/** Table 17 of ISO/IEC 19794-4-2011. 
*/
public enum FaceQualityAlgorithmIdentifier {

	ALGORITHM_IDENTIFIER(0x0001),
	ALGORITHM_IDENTIFIER_0103(0x0103);

	private final int value;
	FaceQualityAlgorithmIdentifier(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FaceQualityAlgorithmIdentifier fromValue(int value) {
		for (FaceQualityAlgorithmIdentifier c : FaceQualityAlgorithmIdentifier.values()) {
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
