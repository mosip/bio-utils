package io.mosip.biometrics.util.face;

/** Eye Color Table 7 of ISO/IEC 19794-5-2011. */
public enum EyeColour {

	UNSPECIFIED(0x0000), 
	BLACK(0x0001), 
	BLUE(0x0002), 
	BROWN(0x0003), 
	GRAY(0x0004), 
	GREEN(0x0005), 
	MULTI_COLOUR(0x0006), 
	PINK(0x0007),
	UNKNOWN(0x00FF);

	private final int value;
	EyeColour(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static EyeColour fromValue(int value) {
		for (EyeColour c : EyeColour.values()) {
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
