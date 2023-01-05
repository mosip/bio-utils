package io.mosip.biometrics.util.face;

/** Eye Color Table 7 of ISO/IEC 19794-5-2011. */
public class EyeColour {
	public static final int UNSPECIFIED = 0x00;
	public static final int BLACK = 0x01;
	public static final int BLUE = 0x02;
	public static final int BROWN = 0x03;
	public static final int GRAY = 0x04;
	public static final int GREEN = 0x05;
	public static final int MULTI_COLOUR = 0x06;
	public static final int PINK = 0x07;
	public static final int RESERVED_008 = 0x08;
	public static final int RESERVED_254 = 0xFE;
	public static final int OTHER_OR_UNKNOWN = 0xFF;

	private int value;

	public EyeColour(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if ((value >= UNSPECIFIED && value <= PINK) || value == OTHER_OR_UNKNOWN)
			return value;
		throw new IllegalArgumentException(
				"EyeColour value can be between (0x00 and 0x07) or (0xFF), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
