package io.mosip.biometrics.util.finger;

/**
 * FingerPosition, Table 6 Finger position codes and Table 7 Multiple finger
 * position codes and Table 8 Palm codes ISO/IEC 19794-4-2011.
 */
public class FingerPosition {
	public static final int UNKNOWN = 0x00;
	public static final int RIGHT_THUMB = 0x01;
	public static final int RIGHT_INDEX_FINGER = 0x02;
	public static final int RIGHT_MIDDLE_FINGER = 0x03;
	public static final int RIGHT_RING_FINGER = 0x04;
	public static final int RIGHT_LITTLE_FINGER = 0x05;
	public static final int LEFT_THUMB = 0x06;
	public static final int LEFT_INDEX_FINGER = 0x07;
	public static final int LEFT_MIDDLE_FINGER = 0x08;
	public static final int LEFT_RING_FINGER = 0x09;
	public static final int LEFT_LITTLE_FINGER = 0x0A;
	public static final int PLAIN_RIGHT_FOUR_FINGERS = 0x0D;
	public static final int PLAIN_LEFT_FOUR_FINGERS = 0x0E;
	public static final int PLAIN_TWO_THUMBS = 0x0F;
	/** Table 8 Palm codes ISO 19794-4-2011. */
	public static final int UNKNOWN_PALM = 0x14;
	public static final int RIGHT_FULL_PALM = 0x15;
	public static final int RIGHT_WRITER_S_PALM = 0x16;
	public static final int LEFT_FULL_PALM = 0x17;
	public static final int LEFT_WRITER_S_PALM = 0x18;
	public static final int RIGHT_LOWER_PALM = 0x19;
	public static final int RIGHT_UPPER_PALM = 0x1A;
	public static final int LEFT_LOWER_PALM = 0x1B;
	public static final int LEFT_UPPER_PALM = 0x1C;
	public static final int RIGHT_OTHER = 0x1D;
	public static final int LEFT_OTHER = 0x1E;
	public static final int RIGHT_INTERDIGITAL = 0x1F;
	public static final int RIGHT_THENAR = 0x20;
	public static final int RIGHT_HYPOTHENAR = 0x21;
	public static final int LEFT_INTERDIGITAL = 0x22;
	public static final int LEFT_THENAR = 0x23;
	public static final int LEFT_HYPOTHENAR = 0x24;
	// 2-Finger Combinations
	public static final int RIGHT_INDEX_AND_MIDDLE_FINGERS = 0x28;
	public static final int RIGHT_MIDDLE_AND_RING_FINGERS = 0x29;
	public static final int RIGHT_RING_AND_LITTLE_FINGERS = 0x2A;
	public static final int LEFT_INDEX_AND_MIDDLE_FINGERS = 0x2B;
	public static final int LEFT_MIDDLE_AND_RING_FINGERS = 0x2C;
	public static final int LEFT_RING_AND_LITTLE_FINGERS = 0x2D;
	public static final int RIGHT_INDEX_AND_LEFT_INDEX_FINGERS = 0x2E;
	// 3-Finger Combinations
	public static final int RIGHT_INDEX_AND_MIDDLE_AND_RING_FINGERS = 0x2F;
	public static final int RIGHT_MIDDLE_AND_RING_AND_LITTLE_FINGERS = 0x30;
	public static final int LEFT_INDEX_AND_MIDDLE_AND_RING_FINGERS = 0x31;
	public static final int LEFT_MIDDLE_AND_RING_AND_LITTLE_FINGERS = 0x32;

	private final int value;

	public FingerPosition(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNKNOWN && value <= LEFT_MIDDLE_AND_RING_AND_LITTLE_FINGERS)
			return value;
		throw new IllegalArgumentException(
				"FingerPosition value can be between (0x00 and 0x32), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
