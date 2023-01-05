package io.mosip.biometrics.util.face;

/**
 * Feature/Property flags meaning based on Table 10 of ISO 19794-5. 3 bytes data
 */
public class Features {
	public static final int FEATURES_ARE_SPECIFIED = 0x000000;
	public static final int GLASSES = 0x000001;
	public static final int MOUSTACHE = 0x000002;
	public static final int BEARD = 0x000003;
	public static final int TEETH_VISIBLE = 0x000004;
	public static final int BLINK = 0x000005;
	public static final int MOUTH_OPEN = 0x000006;
	public static final int LEFT_EYE_PATCH = 0x000007;
	public static final int RIGHT_EYE_PATCH = 0x000008;
	public static final int DARK_GLASSES = 0x000009;
	public static final int HEAD_COVERING_PRESENT = 0x000010;
	public static final int DISTORTING_MEDICAL_CONDITION = 0x000011;
	public static final int RESERVED_00012 = 0x000012;
	public static final int RESERVED_00013 = 0x000023;

	private int value;

	public Features(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= FEATURES_ARE_SPECIFIED && value <= DISTORTING_MEDICAL_CONDITION)
			return value;
		throw new IllegalArgumentException(
				"Features value can be between 0x000000 and 0x000011, set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
