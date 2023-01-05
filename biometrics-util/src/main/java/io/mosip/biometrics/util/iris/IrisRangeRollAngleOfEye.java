package io.mosip.biometrics.util.iris;

/**
 * IrisRangeRollAngleOfEye, Table 4 of ISO/IEC 19794-6-2011.
 */
public class IrisRangeRollAngleOfEye {
	public static final int ROLL_ANGLE_0000 = 0x0000;
	public static final int ROLL_ANGLE_FFFE = 0xFFFE;
	public static final int ROLL_ANGLE_UNDEFINIED = 0xFFFF;

	private int value;

	public IrisRangeRollAngleOfEye(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= ROLL_ANGLE_0000 && value <= ROLL_ANGLE_UNDEFINIED)
			return value;
		throw new IllegalArgumentException(
				"IrisRangeRollAngleOfEye value can be between (0x0000 and 0xFFFF), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
