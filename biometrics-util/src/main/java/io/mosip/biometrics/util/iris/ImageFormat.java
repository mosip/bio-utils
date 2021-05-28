package io.mosip.biometrics.util.iris;

/** ImageFormat Table 4 of ISO/IEC 19794-6-2011. 
*/
public enum ImageFormat {

	MONO_RAW(0x0002), 
	RGB_RAW(0x0004),
	MONO_JPEG(0x0006), 
	RGB_JPEG(0x0008), 
	MONO_JPEG_LOSS_LESS(0x000A), 
	RGB_JPEG_LOSS_LESS(0x000C), 
	MONO_JPEG2000(0x000E), 
	RGB_JPEG2000(0x0010);

	private final int value;
	ImageFormat(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static ImageFormat fromValue(int value) {
		for (ImageFormat c : ImageFormat.values()) {
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
