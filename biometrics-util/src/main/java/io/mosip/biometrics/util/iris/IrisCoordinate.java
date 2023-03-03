package io.mosip.biometrics.util.iris;

/**
 * IrisCoordinate, Table 4 of ISO/IEC 19794-6-2011.
 */
public class IrisCoordinate {
	public static final int COORDINATE_UNDEFINIED = 0x0000;
	public static final int COORDINATE_0001 = 0x0001;
	public static final int COORDINATE_FFFF = 0xFFFF;

	private int value;

	public IrisCoordinate(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= COORDINATE_UNDEFINIED && value <= COORDINATE_FFFF)
			return value;
		throw new IllegalArgumentException(
				"IrisCoordinate value can be between (0x0000 and 0xFFFF), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
