package io.mosip.biometrics.util.finger;

/** Table 13 � Annotation data  ISO/IEC 19794-4-2011. 
*/
public enum AnnotationCode {

	AMPUTATED_FINGER(0x0001),
	UNUSABLE_IMAGE(0x0002);

	private final int value;
	AnnotationCode(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static AnnotationCode fromValue(int value) {
		for (AnnotationCode c : AnnotationCode.values()) {
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
