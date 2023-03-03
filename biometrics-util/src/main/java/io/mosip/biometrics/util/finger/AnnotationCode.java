package io.mosip.biometrics.util.finger;

/**
 * Table 13 Annotation data ISO/IEC 19794-4-2011.
 */
public class AnnotationCode {
	public static final int AMPUTATED_FINGER = 0x01;
	public static final int UNUSABLE_IMAGE = 0x02;

	private int value;

	public AnnotationCode(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value == AMPUTATED_FINGER || value == UNUSABLE_IMAGE)
			return value;
		throw new IllegalArgumentException(
				"AnnotationCode value can be between (0x01 and 0x02), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
