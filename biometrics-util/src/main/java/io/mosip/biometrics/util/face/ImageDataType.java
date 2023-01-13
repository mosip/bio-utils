package io.mosip.biometrics.util.face;

/**
 * Image Data Type BASIC, 0x02. Table 17 of ISO/IEC 19794-5-2011. For lossless
 * compression PNG or JPEG2000 lossless shall be used. For lossless
 * representation of images using more than 8 bits per channel PNG or JPEG2000
 * lossless shall be used. For lossy representation of images using more than
 * eight bit per channel JPEG2000 shall be used. Note that an Unspecified Value
 * cannot be encoded
 */
public class ImageDataType {
	public static final int JPEG = 0x00;
	public static final int JPEG2000_LOSSY = 0x01;
	public static final int JPEG2000_LOSS_LESS = 0x02;
	public static final int PNG = 0x03;
	public static final int RESERVED_04 = 0x04;
	public static final int RESERVED_FF = 0xFF;

	private int value;

	public ImageDataType(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if (value >= JPEG && value <= PNG)
			return value;
		throw new IllegalArgumentException(
				"ImageDataType value can be between (0x00 and 0x03), set value is wrong [" + value + "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
