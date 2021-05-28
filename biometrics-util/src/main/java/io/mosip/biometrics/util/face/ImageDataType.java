package io.mosip.biometrics.util.face;

/** Image Data Type BASIC, 0x02. Table 17 of ISO/IEC 19794-5-2011. 
 * For lossless compression PNG or JPEG2000 lossless shall be used. For lossless representation of images
 * using more than 8 bits per channel PNG or JPEG2000 lossless shall be used. For lossy representation of
 * images using more than eight bit per channel JPEG2000 shall be used. Note that an �Unspecified� Value
 * cannot be encoded
*/
public enum ImageDataType {

	JPEG(0x0000), 
	JPEG2000_LOSSY(0x0001), 
	JPEG2000_LOSS_LESS(0x0002), 
	PNG(0x0003);

	private final int value;
	ImageDataType(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static ImageDataType fromValue(int value) {
		for (ImageDataType c : ImageDataType.values()) {
			if (c.value == value) {
				return c;
			}
		}
		throw new IllegalArgumentException(value + "");
	}

	@Override
	public String toString() {
		return super.toString() + "(" + Integer.toHexString (value) + ")";
	}
}
