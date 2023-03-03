package io.mosip.biometrics.util.iris;

/**
 * Bit Depth, Table 4 of ISO/IEC 19794-6-2011.
 */
public class IrisImageBitDepth {
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

	public IrisImageBitDepth(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= BPP_08 && value <= BPP_0F)
			return value;
		throw new IllegalArgumentException(
				"IrisImageBitDepth value can be between (0x01 and 0x0F), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
