package io.mosip.biometrics.util.iris;

/**
 * ImageFormat Table 4 of ISO/IEC 19794-6-2011.
 */
public class ImageFormat {
	public static final int MONO_RAW = 0x02;
	public static final int RGB_RAW = 0x04;
	public static final int MONO_JPEG = 0x06;
	public static final int RGB_JPEG = 0x08;
	public static final int MONO_JPEG2000 = 0x0A;
	public static final int RGB_JPEG2000 = 0x0C;
	public static final int MONO_PNG = 0x0E;
	public static final int RGB_PNG = 0x10;

	private int value;

	public ImageFormat(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= MONO_RAW && value <= RGB_PNG)
			return value;
		throw new IllegalArgumentException(
				"ImageFormat value can be between (0x02 and 0x10), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
