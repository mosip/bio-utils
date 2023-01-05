package io.mosip.biometrics.util.iris;

/**
 * Eye Label, Table 4 of ISO/IEC 19794-6-2011.
 */
public class EyeLabel {
	public static final int UNSPECIFIED = 0x00;
	public static final int RIGHT = 0x01;
	public static final int LEFT = 0x02;

	private int value;

	public EyeLabel(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNSPECIFIED && value <= LEFT)
			return value;
		throw new IllegalArgumentException(
				"EyeColour value can be between (0x00 and 0x02), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
