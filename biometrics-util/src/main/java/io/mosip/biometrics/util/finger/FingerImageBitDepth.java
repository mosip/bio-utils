package io.mosip.biometrics.util.finger;

/**
 * FingerImageBitDepth, Table 2 Finger image representation header record of
 * ISO/IEC 19794-6-2011. The grayscale precision of the pixel data shall be
 * specified in terms of the bit-depth or the number of bits used to represent
 * the grayscale value of a pixel. A bit-depth of 3 provides 8 levels of
 * grayscale; a depth of 8 provides 256 levels of gray. For grayscale data, the
 * minimum value that can be assigned to a "black" pixel shall be zero. The
 * maximum value that can be assigned to a "white" pixel shall be the grayscale
 * value with all of its bits of precision set to "1". However, the “blackest"
 * pixel in an image may have a value greater than "0" and the "whitest" pixel
 * may have a value less than its maximum value. For example, the range of
 * values for a "white" pixel with 5 bits of precision shall be 31 or less. The
 * range of values for a “white” pixel using 8 bits of precision shall be 255 or
 * less. The bit-depth may range from 1 to 16 bits.
 */
public class FingerImageBitDepth {
	public static final int BPP_01 = 0x01;
	public static final int BPP_02 = 0x02;
	public static final int BPP_03 = 0x03;
	public static final int BPP_04 = 0x04;
	public static final int BPP_05 = 0x05;
	public static final int BPP_06 = 0x06;
	public static final int BPP_07 = 0x07;
	public static final int BPP_08 = 0x08;
	public static final int BPP_09 = 0x09;
	public static final int BPP_0A = 0x0A;
	public static final int BPP_0B = 0x0B;
	public static final int BPP_0C = 0x0C;
	public static final int BPP_0D = 0x0D;
	public static final int BPP_0E = 0x0E;
	public static final int BPP_0F = 0x0F;

	private int value;

	public FingerImageBitDepth(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= BPP_08 && value <= BPP_0F)
			return value;
		throw new IllegalArgumentException(
				"FingerImageBitDepth value can be between (0x01 and 0x0F), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
