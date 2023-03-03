package io.mosip.biometrics.util.finger;

/**
 * ImpressionType Table 10 Finger and palm impression codes of ISO/IEC
 * 19794-4-2011.
 */
public class FingerScaleUnitType {
	public static final int PIXELS_PER_INCH = 0x01;
	public static final int PIXELS_PER_CM = 0x02;

	private int value;

	public FingerScaleUnitType(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value == PIXELS_PER_INCH || value == PIXELS_PER_CM)
			return value;
		throw new IllegalArgumentException(
				"FingerScaleUnitType value can be between (0x01 and 0x02), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
