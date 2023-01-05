package io.mosip.biometrics.util.face;

/**
 * Image Color Space, 0x00. Table 21 of ISO/IEC 19794-5-2011. The (1 int) Image
 * Colour Space field indicates the colour space used in the encoded Image
 * Information block according to the values in Table 21. The values of
 * 80HEX-FFHEX are vendor specific. Application developers may obtain the values
 * for these codes from the vendor.
 */
public class ImageColourSpace {
	public static final int UNSPECIFIED = 0x00;
	public static final int BIT_24_RGB = 0x01;
	public static final int YUV_422 = 0x02;
	public static final int BIT_8_GREYSCALE = 0x03;
	public static final int BIT_48_RGB = 0x04;
	public static final int BIT_16_GREYSCALE = 0x05;
	public static final int OTHER = 0x06;
	public static final int RESERVED_07 = 0x07;
	public static final int RESERVED_7F = 0x7F;
	public static final int VENDOR_80 = 0x80;
	public static final int VENDOR_FF = 0xFF;

	private int value;

	public ImageColourSpace(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static int fromValue(int value) {
		if ((value >= UNSPECIFIED && value <= OTHER) || (value >= VENDOR_80 && value <= VENDOR_FF))
			return value;
		throw new IllegalArgumentException(
				"ImageColourSpace value can be between (0x00 and 0x06) or (0x80 and 0xFF), set value is wrong [" + value
						+ "]");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString(value) + ")";
	}
}
