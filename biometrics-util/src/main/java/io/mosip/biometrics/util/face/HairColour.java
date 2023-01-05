package io.mosip.biometrics.util.face;

/** Hair Color Table 8 of ISO/IEC 19794-5-2011. */
public class HairColour {
	public static final int UNSPECIFIED = 0x00;
	public static final int BALD = 0x01;
	public static final int BLACK = 0x02;
	public static final int BLONDE = 0x03;
	public static final int BROWN = 0x04;
	public static final int GRAY = 0x05;
	public static final int WHITE = 0x06;
	public static final int RED = 0x07;
	public static final int RESERVED_008 = 0x08;
	public static final int RESERVED_254 = 0xFE;
	public static final int UNKNOWN = 0xFF;

	private int value;

	public HairColour(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if ((value >= UNSPECIFIED && value <= RED) || (value == UNKNOWN))
			return value;
		throw new IllegalArgumentException(
				"HairColour value can be between (0x00 and 0x09) or (0xFF), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
