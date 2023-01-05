package io.mosip.biometrics.util.iris;

/**
 * ImageType, Table 4 of ISO/IEC 19794-6-2011.
 */
public class ImageType {
	public static final int UNCROPPED = 0x01;
	public static final int VGA = 0x02;
	public static final int CROPPED = 0x03;
	public static final int CROPPED_AND_MASKED = 0x07;

	private int value;

	public ImageType(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= UNCROPPED && value <= CROPPED_AND_MASKED)
			return value;
		throw new IllegalArgumentException(
				"ImageType value can be between (0x01 and 0x07), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
