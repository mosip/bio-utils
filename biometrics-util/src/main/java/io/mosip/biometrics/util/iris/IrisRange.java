package io.mosip.biometrics.util.iris;

/**
 * IrisRange, Table 4 of ISO/IEC 19794-6-2011.
 */
public class IrisRange {
	public static final int UNASSIGNED = 0x0000;
	public static final int FAILED = 0x0001;
	public static final int OVERFLOW_0002 = 0x0002;
	public static final int OVERFLOW_FFFF = 0xFFFF;

	private int value;

	public IrisRange(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNASSIGNED && value <= OVERFLOW_FFFF)
			return value;
		throw new IllegalArgumentException(
				"IrisRange value can be between (0x0000 and 0xFFFF), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
