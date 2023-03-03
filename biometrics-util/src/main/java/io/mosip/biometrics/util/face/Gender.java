package io.mosip.biometrics.util.face;

/** Gender Table 6 of ISO/IEC 19794-5-2011. */
public class Gender {
	public static final int UNSPECIFIED = 0x00;
	public static final int MALE = 0x01;
	public static final int FEMALE = 0x02;
	public static final int UNKNOWN = 0xFF;

	private int value;

	public Gender(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value == UNSPECIFIED || value == MALE || value == FEMALE || value == UNKNOWN)
			return value;
		throw new IllegalArgumentException(
				"Gender value can be between 0x00,0x01, 0x02 and 0xFF, set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
