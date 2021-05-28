package io.mosip.biometrics.util.face;

/** Subject Height Table 9 of ISO/IEC 19794-5-2011. */
public enum HeightCodes {

	UNSPECIFIED(0x0000);

	private final int value;
	HeightCodes(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static HeightCodes fromValue(int value) {
		for (HeightCodes c : HeightCodes.values()) {
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
