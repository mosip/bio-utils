package io.mosip.biometrics.util.iris;

/**
 * NoOfEyesRepresented, Table 4 of ISO/IEC 19794-6-2011.
 */
public class NoOfEyesRepresented {
	public static final int UNKNOWN = 0x00;
	public static final int LEFT_OR_RIGHT_EYE_PRESENT = 0x01;
	public static final int LEFT_AND_RIGHT_EYE_PRESENT = 0x02;

	private int value;

	public NoOfEyesRepresented(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNKNOWN && value <= LEFT_AND_RIGHT_EYE_PRESENT)
			return value;
		throw new IllegalArgumentException(
				"NoOfEyesRepresented value can be between (0x00 and 0x02), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
