package io.mosip.biometrics.util.face;

/** Image Color Space, 0x00. Table 21 of ISO/IEC 19794-5-2011. 
 * The (1 byte) Image Colour Space field indicates the colour space used in the encoded Image Information
 * block according to the values in Table 21. The values of 80HEX-FFHEX are vendor specific. Application
 * developers may obtain the values for these codes from the vendor. 
*/
public enum ImageColourSpace {

	UNSPECIFIED(0x0000), 
	BIT_24_RGB(0x0001),
	YUV_422(0x0002), 
	BIT_8_GREYSCALE(0x0003),
	BIT_48_RGB(0x0004),
	BIT_16_GREYSCALE(0x0005),
	OTHER(0x0006);

	private final int value;
	ImageColourSpace(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static ImageColourSpace fromValue(int value) {
		for (ImageColourSpace c : ImageColourSpace.values()) {
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
