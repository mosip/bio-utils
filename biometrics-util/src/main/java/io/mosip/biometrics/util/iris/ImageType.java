package io.mosip.biometrics.util.iris;

/** Eye Label, Table 4 of ISO/IEC 19794-6-2011. 
*/
public enum ImageType {

	UNCROPPED(0x0001), 
	VGA(0x0002), 
	CROPPED(0x0003), 
	CROPPED_AND_MASKED(0x0007);

	private final int value;
	ImageType(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static ImageType fromValue(int value) {
		for (ImageType c : ImageType.values()) {
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
