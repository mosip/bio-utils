package io.mosip.biometrics.util.face;

/** Version number '0', '3', '0', 0x00. Table 2 Section 5.3.1 of ISO/IEC 19794-5-2011. */
public enum FaceVersionNumber {

	VERSION_030(0x30333000);

	private final int value;
	FaceVersionNumber(int value) {
		this.value = value;
	}	
	
	public int value() {
		return this.value;
	}

	public static FaceVersionNumber fromValue(int value) {
		for (FaceVersionNumber c : FaceVersionNumber.values()) {
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
