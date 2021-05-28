package io.mosip.biometrics.util.face;

/** Hair Color Table 8 of ISO/IEC 19794-5-2011. */
public enum HairColour {

	UNSPECIFIED(0x0000), 
	BALD(0x0001), 
	BLACK(0x0002), 
	BLONDE(0x0003), 
	BROWN(0x0004), 
	GRAY(0x0005), 
	WHITE(0x0006), 
	RED(0x0007),
	GREEN(0x0008),
	BLUE(0x0009),
	UNKNOWN(0x00FF);

	private final int value;
	HairColour(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static HairColour fromValue(int value) {
		for (HairColour c : HairColour.values()) {
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
