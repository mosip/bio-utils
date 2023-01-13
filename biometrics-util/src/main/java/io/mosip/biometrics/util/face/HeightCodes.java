package io.mosip.biometrics.util.face;

/**
 * Subject Height Table 9 of ISO/IEC 19794-5-2011. Subject Height in cm 0x01 to
 * 0xFF
 */
public class HeightCodes {
	public static final int UNSPECIFIED = 0x00;
	public static final int HEIGHT_FF = 0xFF;

	private int value;

	public HeightCodes(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNSPECIFIED && value <= HEIGHT_FF)
			return value;
		throw new IllegalArgumentException(
				"EyeColour value can be between (0x00 and 0xFF), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
