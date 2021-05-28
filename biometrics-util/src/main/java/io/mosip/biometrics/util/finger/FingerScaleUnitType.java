package io.mosip.biometrics.util.finger;

/** ImpressionType Table 10 � Finger and palm impression codes of ISO/IEC 19794-4-2011. 
*/
public enum FingerScaleUnitType {
	
	PIXELS_PER_INCH(0x0001), 
	PIXELS_PER_CM(0x0002); 
	
	private final int value;
	FingerScaleUnitType(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FingerScaleUnitType fromValue(int value) {
		for (FingerScaleUnitType c : FingerScaleUnitType.values()) {
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
